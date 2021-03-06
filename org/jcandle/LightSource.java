package org.jcandle;

import org.jsfml.graphics.Transformable;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderStates;
import java.util.ArrayList;
import org.jsfml.system.Vector2f;
import org.jsfml.graphics.VertexArray;
import org.jsfml.graphics.BasicTransformable;
// v1: public abstract class LightSource implements Transformable, Drawable{
//v2: 
public abstract class LightSource extends BasicTransformable implements Drawable{ 
  protected Color m_color;
  protected VertexArray m_polygon;
  protected float m_range;
  protected float m_intensity; // only for fog
  protected boolean m_fade;
  
  public abstract void draw(RenderTarget rt, RenderStates rs);
  public abstract void castLight(ArrayList<Line> edgeVectors); 
  

  public LightSource(){
    m_color = Color.WHITE;
    m_fade = true;
    m_polygon=new VertexArray();
  }
  
  public void resetColor(){
    m_color=new Color(0, 0, 0, 0);
  }
    
  public void setIntensity(float intensity){
    JSFMLUtil.Color_copyWithNewAlpha(m_color, (int)(255 * intensity));
  }
    
  public float getIntensity() {
    return (float)m_color.a/255.f;
  }
    
  public void setColor(Color c){
    m_color = new Color(c.r, c.g, c.b, m_color.a);
    resetColor();
  }
  public Color getColor() {
    Color c = m_color;
    return new Color(c.r, c.g, c.b, 255);
  }
    
  public void setFade(boolean fade){
    m_fade = fade;
    resetColor();
  }
    
  public boolean getFade() {
    return m_fade;
  }
    
  public void setRange(float r){
    m_range = r;
  }
    
  public float getRange() {
    return m_range;
  } 
}
