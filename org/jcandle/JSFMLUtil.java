package org.jcandle;

import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;
import org.jsfml.graphics.VertexArray;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.BlendMode;
import org.jsfml.graphics.Shader;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.ConstShader;

public abstract class JSFMLUtil{
  public static Vertex Vertex_copyWithNewColor(Vertex v, Color color){
    return new Vertex(v.position, color, v.texCoords);
  }
  public static Vertex Vertex_copyWithNewPosition(Vertex v, Vector2f position){
    return new Vertex(position, v.color, v.texCoords);
  }
  public static Vertex Vertex_copyWithNewTexCoords(Vertex v, Vector2f texCoords){
    return new Vertex( v.position, v.color, texCoords);
  }
  public static void VertexArray_setIndexNewVertexColor(VertexArray va, int index, Color color){
    Vertex v=va.get(index);
    v=Vertex_copyWithNewColor(v, color);
    va.set(index, v);
  }
  public static void VertexArray_setIndexNewVertexTexCoords(VertexArray va, int index, Vector2f texCoords){
    Vertex v=va.get(index);
    v=Vertex_copyWithNewTexCoords(v, texCoords);
    va.set(index, v);
  }
  public static void VertexArray_setIndexNewVertexPosition(VertexArray va, int index, Vector2f position){
    Vertex v=va.get(index);
    
    v=Vertex_copyWithNewPosition(v, position);
    va.set(index, v);
  }
  public static void VertexArray_fill(VertexArray va, int amount){
    VertexArray_fill(va, 0, amount);
  }
  public static void VertexArray_fill(VertexArray va, int from, int to){
    for (int i=from; i<to; i++) {
      va.add(new Vertex(Vector2f.ZERO));
    } 
  }
  //RenderStates(BlendMode blendMode, Transform transform, ConstTexture texture, ConstShader shader) 
  public static RenderStates RenderStates_copyWithNewBlendMode(RenderStates rs, BlendMode blendMode){
    return new RenderStates(blendMode, rs.transform, rs.texture, rs.shader);
  }
  public static RenderStates RenderStates_copyWithNewTransform(RenderStates rs, Transform transform){
    return new RenderStates(rs.blendMode, transform, rs.texture, rs.shader);
  }
  public static RenderStates RenderStates_copyWithNewTexture(RenderStates rs, Texture texture){
    return new RenderStates(rs.blendMode, rs.transform, texture, rs.shader);
  }
  public static RenderStates RenderStates_copyWithNewShader(RenderStates rs, Shader shader){
    return new RenderStates(rs.blendMode, rs.transform, rs.texture, shader);
  }
  public static RenderStates RenderStates_copyWith(RenderStates rs, BlendMode blendMode, Transform transform, ConstTexture texture, ConstShader shader){
    if(blendMode==null)
      blendMode=rs.blendMode;
    if(transform==null)
      transform=rs.transform;
    if(texture==null)
      texture=rs.texture;
    if(shader==null)
      shader=rs.shader;
    return new RenderStates(blendMode, transform, texture, shader);
  }
  //Color(int r, int g, int b, int a) 
  public static Color Color_copyWithNewRed(Color c, int r){
    return new Color(r, c.g, c.b, c.a);
  }
  public static Color Color_copyWithNewGreen(Color c, int g){
    return new Color(c.r, g, c.b, c.a);
  }
  public static Color Color_copyWithNewBlue(Color c, int b){
    return new Color(c.r, c.g, b, c.a);
  }
  public static Color Color_copyWithNewAlpha(Color c, int a){
    return new Color(c.r, c.g, c.b, a);
  }
  //FloatRect(float left, float top, float width, float height) 
  public static FloatRect FloatRect_copyWithNewLeft(FloatRect f, float left){
    return new FloatRect(left, f.top, f.width, f.height);
  }
  public static FloatRect FloatRect_copyWithNewTop(FloatRect f, float top){
    return new FloatRect(f.left, top, f.width, f.height);
  }
  public static FloatRect FloatRect_copyWithNewWidth(FloatRect f, float width){
    return new FloatRect(f.left, f.top, width, f.height);
  }
  public static FloatRect FloatRect_copyWithNewHeight(FloatRect f, float height){
    return new FloatRect(f.left, f.top, f.width, height);
  }
  //IntRect(int left, int top, int width, int height)
  public static IntRect IntRect_copyWithNewLeft(IntRect i, int left){
    return new IntRect(left, i.top, i.width, i.height);
  }
  public static IntRect IntRect_copyWithNewTop(IntRect i, int top){
    return new IntRect(i.left, top, i.width, i.height);
  }
  public static IntRect IntRect_copyWithNewWidth(IntRect i, int width){
    return new IntRect(i.left, i.top, width, i.height);
  }
  public static IntRect IntRect_copyWithNewHeight(IntRect i, int height){
    return new IntRect(i.left, i.top, i.width, height);
  } 
}
