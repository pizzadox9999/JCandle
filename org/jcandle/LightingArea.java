package org.jcandle;


import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.VertexArray;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.BlendMode;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Vertex;
import java.util.ArrayList;
enum Mode {
            /**
             * In this mode, the area behaves like a mask through which it is
             * only possible to see by drawing light on it.
             */
            FOG,
            /**
             * Use the area as an extra layer of light.
             */
            AMBIENT
        }
public class LightingArea extends BasicTransformable implements Drawable{
  private Texture m_baseTexture;
  private IntRect m_baseTextureRect;
  private VertexArray m_baseTextureQuad;
  private RenderTexture m_renderTexture;
  private VertexArray m_areaQuad;
  private Color m_color;
  private float m_opacity;
  private Vector2f m_size;
  private Mode m_mode;
  void initializeRenderTexture(Vector2f size){
    try {
      m_renderTexture.create((int)size.x, (int)size.y);
    } catch(Exception e) {
      e.printStackTrace();
    } 
    m_renderTexture.setSmooth(true);
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_baseTextureQuad, 0, Vector2f.ZERO);//m_baseTextureQuad[0].position =
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_areaQuad, 0, Vector2f.ZERO);//m_areaQuad[0].position =
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_areaQuad, 0, Vector2f.ZERO);//m_areaQuad[0].texCoords = Vector2f.ZERO;
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_baseTextureQuad, 1, new Vector2f(size.x, 0));//m_baseTextureQuad[0].position =
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_areaQuad, 1, new Vector2f(size.x, 0));//m_areaQuad[0].position =
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_areaQuad, 1, new Vector2f(size.x, 0));//m_areaQuad[0].texCoords = new Vector2f(size.x, 0);
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_baseTextureQuad, 2, new Vector2f(size.x, size.y));//m_baseTextureQuad[0].position =
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_areaQuad, 2, new Vector2f(size.x, size.y));//m_areaQuad[0].position =
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_areaQuad, 2, new Vector2f(size.x, size.y));//m_areaQuad[0].texCoords = new Vector2f(size.x, size.y);    
    
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_baseTextureQuad, 3, new Vector2f(0, size.y));//m_baseTextureQuad[0].position =
    JSFMLUtil.VertexArray_setIndexNewVertexPosition(m_areaQuad, 3, new Vector2f(0, size.y));//m_areaQuad[0].position =
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_areaQuad, 3, new Vector2f(0, size.y));//m_areaQuad[0].texCoords = new Vector2f(0, size.y);
  }
  
  public LightingArea(Mode mode, Vector2f position, Vector2f size){
    m_baseTextureQuad.setPrimitiveType(PrimitiveType.QUADS);
    m_baseTextureQuad.ensureCapacity(4);
    m_areaQuad.setPrimitiveType(PrimitiveType.QUADS);
    m_areaQuad.ensureCapacity(4);
    m_color=Color.WHITE;
    m_opacity = 1.f;
    m_mode = mode;
    m_baseTexture = null;
    initializeRenderTexture(size);
    setPosition(position);
  }
  
  public LightingArea(Mode mode, Texture t, IntRect r){ 
    m_baseTextureQuad.setPrimitiveType(PrimitiveType.QUADS);
    m_baseTextureQuad.ensureCapacity(4);
    m_areaQuad.setPrimitiveType(PrimitiveType.QUADS);
    m_areaQuad.ensureCapacity(4);
    m_color=Color.WHITE;
    m_opacity = 1.f;
    m_mode = mode;
    setAreaTexture(t, r);
  }
    
  public FloatRect getLocalBounds () {
    return m_areaQuad.getBounds();
  }
    
  public FloatRect getGlobalBounds () {
    return getTransform().transformRect(m_areaQuad.getBounds());
  }
    
  public void draw(RenderTarget t, RenderStates s) {
    if(m_opacity > 0.f){
      if(m_mode == Mode.AMBIENT){
        s=JSFMLUtil.RenderStates_copyWithNewBlendMode(s, BlendMode.ADD);//s.blendMode = sf::BlendAdd;
      }
      s=JSFMLUtil.RenderStates_copyWithNewTransform(s, getTransform());//s.transform *= Transformable::getTransform();
      s=JSFMLUtil.RenderStates_copyWithNewTexture(s, (Texture)m_renderTexture.getTexture());//s.texture = &m_renderTexture.getTexture();
      Vertex[] v=new Vertex[m_areaQuad.size()];
      v=m_areaQuad.toArray(v);
      t.draw(v, m_areaQuad.getPrimitiveType(), s);//t.draw(m_areaQuad, s);
    }
  }
    
  public void clear(){
    if(m_baseTexture != null){
      m_renderTexture.clear(Color.TRANSPARENT);
      Vertex[] v=new Vertex[m_baseTextureQuad.size()];
      v=m_baseTextureQuad.toArray(v);
      m_renderTexture.draw(v, m_baseTextureQuad.getPrimitiveType(), JSFMLUtil.RenderStates_copyWithNewTexture(RenderStates.DEFAULT, m_baseTexture)); //      m_baseTexture
    }else{
      m_renderTexture.clear(getActualColor());
    }
  }
    
  public void setAreaColor(Color c){
    m_color = c;
    CandleUtil.setColor(m_baseTextureQuad, getActualColor());
  }
    
  public Color getAreaColor() {
    return m_color;
  }
    
  public Color getActualColor() {
    return JSFMLUtil.Color_copyWithNewAlpha(m_color, (int)m_opacity);
    /*Color ret=m_color;
    ret=JSFMLUtil.Color_copyWithNewAlpha(ret, m_opacity); //ret.a = m_opacity;
    return ret;*/
  }
    
  public void setAreaOpacity(float o){
    m_opacity = o;
    CandleUtil.setColor(m_baseTextureQuad, getActualColor());// setColor(m_baseTextureQuad, getActualColor());
  }
    
  public float getAreaOpacity() {
    return m_opacity;
  }
    
  public void draw(LightSource light){
    if(m_opacity > 0.f && m_mode == Mode.FOG){
      RenderStates fogrs=RenderStates.DEFAULT;
      fogrs=JSFMLUtil.RenderStates_copyWithNewBlendMode(fogrs, BlendMode.ALPHA);//fogrs.blendMode = l_substractAlpha;
      fogrs=JSFMLUtil.RenderStates_copyWithNewTransform(fogrs, getTransform().getInverse());//fogrs.transform *= getTransform().getInverse();
      m_renderTexture.draw(light, fogrs);
    }
  }
    
  public void setAreaTexture(Texture texture, IntRect rect){
    m_baseTexture = texture;
    if(rect.width == 0 && rect.height == 0 && texture != null){
      JSFMLUtil.IntRect_copyWithNewWidth(rect, texture.getSize().x);
      JSFMLUtil.IntRect_copyWithNewHeight(rect, texture.getSize().y);
    }
    initializeRenderTexture(new Vector2f(rect.width, rect.height));
    setTextureRect(rect);
  }
    
  public Texture getAreaTexture() {
    return m_baseTexture;
  }
    
  public void setTextureRect(IntRect rect){
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_baseTextureQuad, 0, new Vector2f(rect.left, rect.top));//m_baseTextureQuad[0].texCoords = sf::Vector2f(rect.left, rect.top);
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_baseTextureQuad, 1, new Vector2f(rect.left + rect.width, rect.top));//m_baseTextureQuad[1].texCoords = sf::Vector2f(rect.left + rect.width, rect.top);
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_baseTextureQuad, 2, new Vector2f(rect.left + rect.width, rect.top + rect.height));//m_baseTextureQuad[2].texCoords = sf::Vector2f(rect.left + rect.width, rect.top + rect.height);
    JSFMLUtil.VertexArray_setIndexNewVertexTexCoords(m_baseTextureQuad, 3, new Vector2f(rect.left, rect.top + rect.height));//m_baseTextureQuad[3].texCoords = sf::Vector2f(rect.left, rect.top + rect.height);
  }
    
  public IntRect getTextureRect() {
    return m_baseTextureRect;
  }
    
  public void setMode(Mode mode){
    m_mode = mode;
  }
  public Mode getMode() {
    return m_mode;
  }
    
  public void display(){
    m_renderTexture.display();
  }
}
