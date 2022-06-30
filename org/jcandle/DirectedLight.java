package org.jcandle;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.BlendMode;
import org.jsfml.graphics.PrimitiveType;
import java.util.ArrayList;
import org.jsfml.system.Vector2f;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderTarget;
class LineParam extends Line{
  public float param;
  public LineParam(Line l){
    this(0, l);
  }
  public LineParam(float f, Line l) {
    super(l);
    param=f;
  }
  public LineParam(Vector2f orig, Vector2f dir, float p) { 
    super(orig, Vector2f.add(orig, dir));
    param=p;
  }
}
public class DirectedLight extends LightSource{
  private float m_beamWidth;
  public void draw(RenderTarget t, RenderStates st){
    st = JSFMLUtil.RenderStates_copyWithNewTransform(st, getTransform());
    
    if(st.blendMode  ==  BlendMode.ALPHA){
      st=JSFMLUtil.RenderStates_copyWithNewBlendMode(st, BlendMode.ADD);
    }
    t.draw(m_polygon, st);
    }
  public void resetColor(){
    int quads = m_polygon.size() / 4;
    for(int i = 0; i < quads; i++){
      float p1 = i*4;
      float p2 = p1+1;
      float p3 = p1+2;
      float p4 = p1+3;
      Vector2f r1 = m_polygon.get((int)p1).position;
      Vector2f r2 = m_polygon.get((int)p2).position;
      Vector2f r3 = m_polygon.get((int)p4).position;
      Vector2f r4 = m_polygon.get((int)p3).position;

      float dr1 = 1.f - Float.valueOf(String.valueOf(m_fade)) * (CandleUtil.magnitude(Vector2f.sub(r2, r1)) / m_range);
      float dr2 = 1.f - Float.valueOf(String.valueOf(m_fade)) * (CandleUtil.magnitude(Vector2f.sub(r4, r3)) / m_range);
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p1, m_color);
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p2, m_color);
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p3, m_color);
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p4, m_color);
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p2, JSFMLUtil.Color_copyWithNewAlpha(m_polygon.get((int)p2).color, (int)(m_color.a * dr1))); //m_polygon[p2].color.a = m_color.a * dr1;
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p3, JSFMLUtil.Color_copyWithNewAlpha(m_polygon.get((int)p3).color, (int)(m_color.a * dr2))); //m_polygon[p3].color.a = m_color.a * dr2;
    }
  }
    public void DirectedLight(){
    m_polygon.setPrimitiveType(PrimitiveType.QUADS);
    
    CPPUtils.Vector_resize(m_polygon, 2); //m_polygon.resize(2);
    setBeamWidth(10.f);
    // castLight(); //orignal author commented this out
  }
  public void setBeamWidth(final float width){
    m_beamWidth = width;
  }

  public float getBeamWidth(){
    return m_beamWidth;
  }
  public boolean compareLineParam(LineParam a, LineParam b){
    if(a.param < b.param)
      return true;
    else 
      return false;
    //return a.param < b.param;
  }
    /*public bool operator < (const LineParam& a, const LineParam& b){
        return a.param < b.param;
    }
  */
  public void castLight(ArrayList<Line> list){
    Transform trm = getTransform();
    Transform trm_i = trm.getInverse();
    float widthHalf = m_beamWidth/2.f;
    FloatRect baseBeam = new FloatRect(0, -widthHalf, m_range, m_beamWidth);
    Vector2f lim1o = trm.transformPoint(0, -widthHalf);
    Vector2f lim1d = trm.transformPoint(m_range, -widthHalf);
    Vector2f lim2o = trm.transformPoint(0, widthHalf);
    Vector2f lim2d = trm.transformPoint(m_range, widthHalf);
    float off = (float)(0.01/CandleUtil.magnitude(Vector2f.sub(lim2o, lim1o)));
    Vector2f lightDir = Vector2f.sub(lim1d, lim1o);
    Line lim1=new Line(lim1o, lim1d);
    Line lim2=new Line(lim2o, lim2d);
    Line raySrc=new Line(lim1o, lim2o);
    Line rayRng=new Line(lim1d, lim2d);
    ArrayList<LineParam> rays=new ArrayList<LineParam>(); //priority_queue <LineParam> rays;
    rays.add(0, new LineParam(lim1));
    rays.add(1, new LineParam(lim2));
    for(Line seg : list){ //for(int i=0; i<list.size(); i++){ //for(auto it = begin; it != end; it++){
      //Line seg=list.get(i);//auto& seg = *it;
      float tRng=0, tSeg=0;
      
      if(
          rayRng.intersection(seg, tRng, tSeg)
          && tRng <= 1
          && tRng >= 0
          && tSeg <= 1
          && tSeg >= 0
      ){
        rays.add(new LineParam(raySrc.point(tRng), lightDir, tRng));
      }
      float t=0;
      Vector2f end = seg.m_origin;
      if(baseBeam.contains(trm_i.transformPoint(end))){
        raySrc.intersection(new Line(end, Vector2f.sub(end, lightDir)), t);
        rays.add(new LineParam(raySrc.point(t - off), lightDir, t - off));
        rays.add(new LineParam(raySrc.point(t), lightDir, t));
        rays.add(new LineParam(raySrc.point(t + off), lightDir, t + off));
      }
      end = seg.point(1.f);
      if(baseBeam.contains(trm_i.transformPoint(end))){
        raySrc.intersection(new Line(end, Vector2f.sub(end, lightDir)), t);
        rays.add(new LineParam(raySrc.point(t - off), lightDir, t - off));
        rays.add(new LineParam(raySrc.point(t), lightDir, t));
        rays.add(new LineParam(raySrc.point(t + off), lightDir, t + off));
      }
    }
    ArrayList<Vector2f> points=new ArrayList<Vector2f>();
    points.ensureCapacity(rays.size()*2);
    while(!rays.isEmpty()){
      LineParam r = CPPUtils.Stack_top(rays); //rays.top();

      Vector2f p1 = trm_i.transformPoint(r.m_origin);
      Vector2f p2 = trm_i.transformPoint(Line.castRay(list, r, m_range));
      points.add(p1);
      points.add(p2);
      CPPUtils.Stack_pop(rays);      
    }
    if(!points.isEmpty()){
      int quads = points.size()/2-1; // a quad between every two rays
      CPPUtils.Vector_resize(m_polygon, quads * 4);
      for(int i = 0; i < quads; i++){
        float p1 = i*4,  r1 = i*2;
        float p2 = p1+1, r2 = r1+1;
        float p3 = p1+2, r3 = r1+2;
        float p4 = p1+3, r4 = r1+3;
        JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, (int)p1, points.get((int)r1));
        JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, (int)p2, points.get((int)r2));
        JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, (int)p3, points.get((int)r4));
        JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, (int)p4, points.get((int)r3));

        float dr1 = 1.f - Float.valueOf(String.valueOf(m_fade)) * (CandleUtil.magnitude(Vector2f.sub(points.get((int)r2), points.get((int)r1))) / m_range);
        float dr2 = 1.f - Float.valueOf(String.valueOf(m_fade)) * (CandleUtil.magnitude(Vector2f.sub(points.get((int)r4), points.get((int)r3))) / m_range);

        JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p1, m_color);
        JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p2, m_color);
        JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p3, m_color);
        JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p4, m_color);
        
        JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p4, JSFMLUtil.Color_copyWithNewAlpha(m_polygon.get((int)p2).color, (int)(m_color.a * dr1)));//m_polygon[p2].color.a = m_color.a * dr1;
        JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, (int)p4, JSFMLUtil.Color_copyWithNewAlpha(m_polygon.get((int)p3).color, (int)(m_color.a * dr2)));//m_polygon[p3].color.a = m_color.a * dr2;
      }
    }
  }
}
