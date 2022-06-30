/**
 * @file
 * @author Miguel Mejía Jiménez
 * @copyright MIT License
 * @brief This file contains the RadialLight class.
 */
package org.jcandle;
import java.util.ArrayList;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Vector2f;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.BlendMode;
import java.util.Comparator;
import java.util.Collections;


    /**
     * @brief LightSource that emits light from a single point
     * @details
     *
     * A RadialLight is defined, mainly, by the position, the orientation, the
   * range of the light and the beam angle. To manipulate the
     * position and the orientation of the light, you can change the position
   * and rotation of the object as you would do with any sf::Transformable.
   * The range can be manipulated as with other LightSources, with
     * @ref LightSource::setRange, and the angle of the beam with
   * @ref setBeamAngle.
     *
     * <table width="100%">
     * <tr>
     *  <td align="center"> <img src="radial_1.png" width="300px"> <br> <em>Variables schema</em> </td>
     *  <td align="center"> <img src="radial_2.png" width="300px"> <br> <em>Demo example</em> </td>
     * </tr>
     * </table>
     */

public class RadialLight extends LightSource{
  public static boolean RADIAL_LIGHT_FIX=false;
  private static int s_instanceCount=0;
  private float m_beamAngle;
  private float BASE_RADIUS = 400.0f;
  private boolean l_texturesReady=false;
  private RenderTexture l_lightTextureFade=new RenderTexture();
  private RenderTexture l_lightTexturePlain=new RenderTexture();
  
  void initializeTextures(){
    if(CandleUtil.JCANDLE_DEBUG)
      System.out.println("RadialLight: InitializeTextures");
    
    int points = 100;
    
    try {
      l_lightTextureFade.create((int)BASE_RADIUS*2 + 2, (int)BASE_RADIUS*2 + 2);
      l_lightTexturePlain.create((int)BASE_RADIUS*2 + 2, (int)BASE_RADIUS*2 + 2);
    } catch(Exception e) {
      e.printStackTrace();
    } 
    
    VertexArray lightShape=new VertexArray();
    
    lightShape.setPrimitiveType(PrimitiveType.TRIANGLE_FAN);
    
    JSFMLUtil.VertexArray_fill(lightShape, points+2);
    
    float step = (float)Math.PI*2.f/points;
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(lightShape, 0, new Vector2f(BASE_RADIUS + 1, BASE_RADIUS + 1));
    for(int i = 1; i < points+2; i++){
      JSFMLUtil.VertexArray_setIndexNewVertexPosition(lightShape, i, new Vector2f(((float)Math.sin(step*(i)) + 1) * BASE_RADIUS + 1, ((float)Math.cos(step*(i)) + 1) * BASE_RADIUS + 1));
      JSFMLUtil.VertexArray_setIndexNewVertexColor(lightShape, i, Color.TRANSPARENT);
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
  /**
  * @brief Constructor
  */
  public RadialLight(){
    super();
    if(!l_texturesReady){
      // The first time we create a RadialLight, we must create the textures
      initializeTextures();
      l_texturesReady = true;
    }
    m_polygon.setPrimitiveType(PrimitiveType.TRIANGLE_FAN);
    JSFMLUtil.VertexArray_fill(m_polygon, 6);
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 0, new Vector2f(BASE_RADIUS+1, BASE_RADIUS+1));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 0, new Vector2f(BASE_RADIUS+1, BASE_RADIUS+1));
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 1, new Vector2f(0, 0));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 1, new Vector2f(0, 0));
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 2, new Vector2f(BASE_RADIUS*2 + 2, 0.f));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 2, new Vector2f(BASE_RADIUS*2 + 2, 0.f));
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 3, new Vector2f(BASE_RADIUS*2 + 2, BASE_RADIUS*2 + 2));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 3, new Vector2f(BASE_RADIUS*2 + 2, BASE_RADIUS*2 + 2));
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 4, new Vector2f(0.f, BASE_RADIUS*2 + 2));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 4, new Vector2f(0.f, BASE_RADIUS*2 + 2));
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 5, new Vector2f(0, 0));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 5, new Vector2f(0, 0));
    
    setOrigin(BASE_RADIUS, BASE_RADIUS);
    setRange(1.0f);
    setBeamAngle(360.f);
    // castLight();
    s_instanceCount++;
  }
  
  /**
  * @brief Destructor
  */
  public void close(){
    
    s_instanceCount--;
    if(RADIAL_LIGHT_FIX){
      if (s_instanceCount == 0 &&
      l_lightTextureFade !=null &&
      l_lightTexturePlain != null)
      {
        l_lightTextureFade=null;
        l_lightTexturePlain=null;
        l_texturesReady = false;
        if(CandleUtil.JCANDLE_DEBUG){
          System.out.println("RadialLight: Textures destroyed");
        }
      }
    }
  } 
  
  public void draw(RenderTarget t, RenderStates s) {
    Transform trm = Transform.scale(getTransform(), m_range/BASE_RADIUS, m_range/BASE_RADIUS, BASE_RADIUS, BASE_RADIUS);
    s=JSFMLUtil.RenderStates_copyWithNewTransform(s, trm);
    Texture texture = m_fade ? (Texture)l_lightTextureFade.getTexture() : (Texture)l_lightTexturePlain.getTexture();
    s=JSFMLUtil.RenderStates_copyWithNewTexture(s, texture);
    
    if(s.blendMode == BlendMode.ALPHA){
      s=JSFMLUtil.RenderStates_copyWithNewBlendMode(s, BlendMode.ADD);
    }
    
    t.draw(m_polygon, s);
    
    if(CandleUtil.JCANDLE_DEBUG){
      RenderStates deb_s=new RenderStates(s.transform);
      t.draw(m_debug, deb_s);
    } 
  }
  
  public void resetColor(){
    CandleUtil.setColor(m_polygon, m_color);
  }
  
  
  private boolean angleInBeam(boolean beamAngleBigEnough, float a, float bl1, float bl2){
    return beamAngleBigEnough
    ||(bl1 < bl2 && a > bl1 && a < bl2)
    ||(bl1 > bl2 && (a > bl1 || a < bl2));
  }
  private int booleanToInt(boolean b){
    if(b)
      return 1;
    else 
      return 0;
  }
  public void castLight(ArrayList<Line> lines){
    float scaledRange = m_range / BASE_RADIUS;
    Transform trm=Transform.scale(getTransform(), scaledRange, scaledRange, BASE_RADIUS, BASE_RADIUS);
    ArrayList<Line> rays=new ArrayList<Line>(2 + lines.size() * 2 * 3);// 2: beam angle, 4: corners, 2: pnts/sgmnt, 3 rays/pnt
    
    // Start casting
    float bl1 = module360(getRotation() - m_beamAngle/2);
    float bl2 = module360(getRotation() + m_beamAngle/2);
    boolean beamAngleBigEnough = m_beamAngle < 0.1f;
    Vector2f castPoint = getPosition();
    float off = .001f;
    
    
    
    for(float a = 45.f; a < 360.f; a += 90.f){
      if(beamAngleBigEnough || angleInBeam(beamAngleBigEnough, a, bl1, bl2)){
        rays.add(new Line(castPoint, a));
      }
    }
    
    FloatRect lightBounds = getGlobalBounds();
    for(Line s : lines){
      //auto& s = *it;
      
      //Only cast a ray if the line is in range
      if( lightBounds.intersection( s.getGlobalBounds() ) != null){
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
    
    if(!beamAngleBigEnough){
      rays.add(0, new Line(castPoint, bl1));
      rays.add(new Line(castPoint, bl2));
    }
    
    Transform tr_i = trm.getInverse();
    // keep only the ones within the area
    ArrayList<Vector2f> points=new ArrayList<Vector2f>(rays.size());
    
    for (Line r: rays){
      points.add(tr_i.transformPoint(Line.castRay(lines,    r, m_range*m_range)));
    }
    
    m_polygon.clear();
    JSFMLUtil.VertexArray_fill(m_polygon, points.size() + 1 + booleanToInt(beamAngleBigEnough));// m_polygon.resize(); //// + center and last
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, 0, tr_i.transformPoint(castPoint));
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, 0, tr_i.transformPoint(castPoint));
    JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, 0, m_color);
    
    if(CandleUtil.JCANDLE_DEBUG){
      float bl1rad = bl1 * (float)Math.PI/180.f;
      float bl2rad = bl2 * (float)Math.PI/180.f;
      Vector2f al1=new Vector2f((float)Math.cos(bl1rad), (float)Math.sin(bl1rad));
      Vector2f al2=new Vector2f((float)Math.cos(bl2rad), (float)Math.sin(bl2rad));
      int d_n = points.size()*2 + 4;
      m_debug.clear();
      JSFMLUtil.VertexArray_fill(m_debug, d_n);
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_debug, d_n-1, Color.CYAN);
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_debug, d_n-2, Color.CYAN);
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_debug, d_n-3, Color.YELLOW);
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_debug, d_n-4, Color.YELLOW);
      JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_debug, d_n-1, m_polygon.get(0).position);
      JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_debug, d_n-3, m_polygon.get(0).position);
      JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_debug, d_n-2, tr_i.transformPoint(Vector2f.add(castPoint, Vector2f.mul(al1, m_range))));
      JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_debug, d_n-4, tr_i.transformPoint(Vector2f.add(castPoint, Vector2f.mul(al2, m_range))));
    }
    for(int i=0; i < points.size()-1; i++){
      Vector2f p = points.get(i);
      JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_polygon, i+1, p);
      JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_polygon, i+1, p);
      JSFMLUtil.VertexArray_setIndexNewVertexColor(m_polygon, i+1, m_color);
      if(CandleUtil.JCANDLE_DEBUG){
        JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_debug, i*2, m_polygon.get(0).position);
        JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_debug, i*2+1, p);
        JSFMLUtil.VertexArray_setIndexNewVertexColor(m_debug, i*2, Color.MAGENTA);
        JSFMLUtil.VertexArray_setIndexNewVertexColor(m_debug, i*2+1, Color.MAGENTA);
      }
    }
    if(beamAngleBigEnough){
      m_polygon.set(points.size()+1, m_polygon.get(1)); //[points.size()+1] = m_polygon[1];
    }
  }
      
      
      /**
      * @brief Set the range for which rays may be casted.
      * @details The angle shall be specified in degrees. The angle in which the rays will be casted will be
      * [R - angle/2, R + angle/2], where R is the rotation of the object.
      * @param angle
      * @see getBeamAngle
      */
  public void setBeamAngle(float angle){
    m_beamAngle = module360(angle);
  }
      
      /**
      * @brief Get the range for which rays may be casted.
      * @details It defaults to 360º.
      * @see setBeamAngle
      */
  public float getBeamAngle() {
    return m_beamAngle;
  }
      
      /**
      * @brief Get the local bounding rectangle of the light.
      * @returns The local bounding rectangle in float.
      */
  public FloatRect getLocalBounds() {
    return new FloatRect(0.0f, 0.0f, BASE_RADIUS*2, BASE_RADIUS*2);
  }
      
      /**
      * @brief Get the global bounding rectangle of the light.
      * @returns The global bounding rectangle in float.
      */
  public FloatRect getGlobalBounds(){
    float scaledRange = m_range / BASE_RADIUS;
    Transform trm = Transform.scale(getTransform(), scaledRange, scaledRange, BASE_RADIUS, BASE_RADIUS);
    return trm.transformRect( getLocalBounds() );
  }
      
}
