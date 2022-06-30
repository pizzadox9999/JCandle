package org.jcandle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.system.Vector2f;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.VertexArray;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.BlendMode;
import org.jsfml.graphics.Vertex;
public class RadialLight extends LightSource{
  public static boolean USE_LIGHTFIX=false;
  private static int s_instanceCount=0;
  private float m_beamAngle;
  private float BASE_RADIUS = 400.0f;
  private boolean l_texturesReady=false;
  private RenderTexture l_lightTextureFade=null;
  private RenderTexture l_lightTexturePlain=null;
  
  public void initializeTextures(){
    int points = 100;
    try {
      l_lightTextureFade=new RenderTexture();
      l_lightTexturePlain=new RenderTexture();
      l_lightTextureFade.create((int)BASE_RADIUS*2 + 2, (int)BASE_RADIUS*2 + 2);
      l_lightTexturePlain.create((int)BASE_RADIUS*2 + 2, (int)BASE_RADIUS*2 + 2);
    } catch(Exception e) {
      e.printStackTrace();
    } 
    
    VertexArray lightShape=new VertexArray();
    
    lightShape.setPrimitiveType(PrimitiveType.TRIANGLE_FAN);
    
    for (int i=lightShape.size(); i<points+2; i++) {
      lightShape.add(new Vertex(Vector2f.ZERO));
    } 
    
    
    float step = (float)Math.PI*2.f/points;
    
    //lightShape.add(new Vertex(new Vector2f(BASE_RADIUS + 1, BASE_RADIUS + 1)));
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(lightShape, 0, new Vector2f(BASE_RADIUS + 1, BASE_RADIUS + 1));
    
    for(int i = 1; i < points+2; i++){
      //lightShape.add(new Vertex(new Vector2f((float)(Math.sin(step*(i)) + 1) * BASE_RADIUS + 1, (float)(Math.cos(step*(i)) + 1) * BASE_RADIUS + 1), Color.TRANSPARENT));
      JSFMLUtil.VertexArray_setIndexNewVertexPosition(lightShape, i, new Vector2f((float)(Math.sin(step*(i)) + 1) * BASE_RADIUS + 1, (float)(Math.cos(step*(i)) + 1) * BASE_RADIUS + 1));
      JSFMLUtil.VertexArray_setIndexNewVertexColor(lightShape, i, JSFMLUtil.Color_copyWithNewAlpha(lightShape.get(i).color, 0));
    }
    
    
    
    l_lightTextureFade.clear(Color.TRANSPARENT);
    l_lightTextureFade.draw(lightShape);
    l_lightTextureFade.display();
    l_lightTextureFade.setSmooth(true);
    
    
    CandleUtil.setColor(lightShape, Color.WHITE);
    
    
    l_lightTexturePlain.clear(Color.TRANSPARENT);
    l_lightTexturePlain.draw(lightShape);
    l_lightTexturePlain.display();
    l_lightTexturePlain.setSmooth(true);
  }
  
  float module360(float x){
    x = (float)CandleUtil.fmod(x,360.f);
    if(x < 0.f) x += 360.f;
    return x;
  }
  
  public RadialLight(){
    super();
    if(!l_texturesReady){
      // The first time we create a RadialLight, we must create the textures
      initializeTextures();
      l_texturesReady = true;
    }
    m_polygon.setPrimitiveType(PrimitiveType.TRIANGLE_FAN);
    
    for (int i=m_polygon.size(); i<6; i++) {
      m_polygon.add(new Vertex(Vector2f.ZERO));
    } 
    
    //v1
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 0, new Vector2f(BASE_RADIUS+1, BASE_RADIUS+1));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 0, new Vector2f(BASE_RADIUS+1, BASE_RADIUS+1));
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 1, Vector2f.ZERO);
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 1, Vector2f.ZERO);
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 2, new Vector2f(BASE_RADIUS*2 + 2, 0.f));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 2, new Vector2f(BASE_RADIUS*2 + 2, 0.f));
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 3, new Vector2f(BASE_RADIUS*2 + 2, BASE_RADIUS*2 + 2));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 3, new Vector2f(BASE_RADIUS*2 + 2, BASE_RADIUS*2 + 2));
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 4, new Vector2f(0.f, BASE_RADIUS*2 + 2));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 4, new Vector2f(0.f, BASE_RADIUS*2 + 2));
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 5, Vector2f.ZERO);
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 5, Vector2f.ZERO);

    
    setOrigin(BASE_RADIUS, BASE_RADIUS);
    setRange(1.0f);
    setBeamAngle(360.f);
    // castLight(); //commented from original author
    s_instanceCount++;
    
  }
  
  /* dont use it because java is garbage collected
  public RadialLight(){
  s_instanceCount--;
  if(USE_LIGHTFIX)
  if (s_instanceCount == 0 && l_lightTextureFade!=null && l_lightTexturePlain!=null){
  l_lightTextureFade.clear(Color.WHITE);
  l_lightTexturePlain.clear(Color.WHITE);
  l_texturesReady = false;
  }
  }
  */
  public void close(){
    s_instanceCount--;
    if(USE_LIGHTFIX)
      if (s_instanceCount == 0 && l_lightTextureFade!=null && l_lightTexturePlain!=null){
        l_lightTextureFade.clear(Color.WHITE);
        l_lightTexturePlain.clear(Color.WHITE);
        l_texturesReady = false;
      }
  }
  public void draw(RenderTarget t, RenderStates s) {
    Transform trm = Transform.scale(getTransform(), m_range/BASE_RADIUS, m_range/BASE_RADIUS, BASE_RADIUS, BASE_RADIUS);

    
    s=JSFMLUtil.RenderStates_copyWithNewTransform(s, trm);
    if(m_fade)
      s=JSFMLUtil.RenderStates_copyWithNewTexture(s, (Texture)l_lightTextureFade.getTexture());
    else
      s=JSFMLUtil.RenderStates_copyWithNewTexture(s, (Texture)l_lightTexturePlain.getTexture());
    
    if(s.blendMode == BlendMode.ALPHA){
      s=JSFMLUtil.RenderStates_copyWithNewBlendMode(s, BlendMode.ADD);//s.blendMode = BlendMode.ADD;
    }
 
    
    t.draw(m_polygon, s);
  }
  public void resetColor(){
    CandleUtil.setColor(m_polygon, m_color);
  }
  
  public void setBeamAngle(float r){
    m_beamAngle = module360(r);
  }
  
  public float getBeamAngle() {
    return m_beamAngle;
  }
    
  public FloatRect getLocalBounds() {
    return new FloatRect(0.0f, 0.0f, BASE_RADIUS*2, BASE_RADIUS*2);
  }
    
  public FloatRect getGlobalBounds() {
    float scaledRange = m_range / BASE_RADIUS;
    Transform trm = getTransform();
    trm=trm.scale(trm, scaledRange, scaledRange, BASE_RADIUS, BASE_RADIUS);
    return trm.transformRect( getLocalBounds() );
  }
  private boolean angleInBeam(boolean beamAngleBigEnough, float a, float bl1, float bl2){
    return beamAngleBigEnough
    ||(bl1 < bl2 && a > bl1 && a < bl2)
    ||(bl1 > bl2 && (a > bl1 || a < bl2));
  }  
  public void castLight(ArrayList<Line> edgeVectors){
    float scaledRange = m_range / BASE_RADIUS;
    Transform trm = Transform.scale(getTransform(), scaledRange, scaledRange, BASE_RADIUS, BASE_RADIUS);
    ArrayList<Line> rays=new ArrayList<Line>();
    
    rays.ensureCapacity(2 + edgeVectors.size() * 2 * 3); // 2: beam angle, 4: corners, 2: pnts/sgmnt, 3 rays/pnt
    
    // Start casting
    float bl1 = module360(getRotation() - m_beamAngle/2);
    float bl2 = module360(getRotation() + m_beamAngle/2);
    
    boolean beamAngleBigEnough = false;
    if(m_beamAngle < 0.1f)
      beamAngleBigEnough=true;
    
    Vector2f castPoint = getPosition();
    float off = .001f;
    
    
    
    for(float a = 45.f; a < 360.f; a += 90.f){
      if(beamAngleBigEnough || angleInBeam(beamAngleBigEnough, a, bl1, bl2)){
        rays.add(new Line(castPoint, a));
      }
    }
    
    FloatRect lightBounds = getGlobalBounds();
    for(Line s : edgeVectors){
      //auto& s = *it;
      
      //Only cast a ray if the line is in range
      if( lightBounds.intersection( s.getGlobalBounds() ) !=null){
        Line r1=new Line(castPoint, s.m_origin);
        Line r2=new Line(castPoint, s.point(1.f));
        float a1 = CandleUtil.angle(r1.m_direction);
        float a2 = CandleUtil.angle(r2.m_direction);
        if(angleInBeam(beamAngleBigEnough, a1, bl1, bl2)){
          rays.add(r1);
          rays.add(new Line(castPoint, a1 - off));
          rays.add(new Line(castPoint, a1 + off));
        }
        if(angleInBeam(beamAngleBigEnough, a2, bl1, bl2)){
          rays.add(r2);
          rays.add(new Line(castPoint, a2 - off));
          rays.add(new Line(castPoint, a2 + off));
        }
      }
    }
    
    if(bl1 > bl2){
      Collections.sort(rays, new Comparator<Line>(){
        public int compare(Line r1, Line r2){
          float _bl1 = bl1-0.1f;
          float _bl2 = bl2+0.1f;
          float a1 = CandleUtil.angle(r1.m_direction);
          float a2 = CandleUtil.angle(r2.m_direction);
          if((a1 >= _bl1 && a2 <= _bl2) || (a1 < a2 && (_bl1 <= a1 || a2 <= _bl2)))
            return 1;
          else 
            return -1;
        }
      });
    }else{   
      Collections.sort(rays, new Comparator<Line>(){
        public int compare(Line r1, Line r2){
          if(CandleUtil.angle(r1.m_direction) < CandleUtil.angle(r2.m_direction))
            return 1;
          else 
            return -1;
        }
      });
    }
    if(beamAngleBigEnough){
      rays.add(0, new Line(castPoint, bl1));//rays.add(new Line(rays.get(0), castPoint, bl1)); rays.emplace(rays.begin(), castPoint, bl1);
      rays.add(new Line(castPoint, bl2));
    }
    
    Transform tr_i = trm.getInverse();
    // keep only the ones within the area
    ArrayList<Vector2f> points=new ArrayList<Vector2f>(rays.size());
    for (Line r: rays){
      points.add(tr_i.transformPoint(Line.castRay( edgeVectors, r, m_range*m_range)));
    }
    int int_beamAngleBigEnough;
    if(beamAngleBigEnough)
      int_beamAngleBigEnough=1;
    else 
      int_beamAngleBigEnough=0;
    
    
    
    
    for(int i=m_polygon.size(); i<points.size() + 1 + int_beamAngleBigEnough; i++){
      m_polygon.add(new Vertex(Vector2f.ZERO));
    }
    
    
    
    JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, 0, m_color);
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 0, tr_i.transformPoint(castPoint));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 0, tr_i.transformPoint(castPoint));
    
    for(int i=0; i < points.size()-1; i++){
      Vector2f p = points.get(i);
      JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, i+1, p);
      JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, i+1, p);
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, i+1, m_color);
    }
    if(beamAngleBigEnough){
      m_polygon.set(points.size()+1, m_polygon.get(1));//m_polygon[points.size()+1] = m_polygon[1];
    }
  }
}
