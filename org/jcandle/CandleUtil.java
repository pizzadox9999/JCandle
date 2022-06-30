package org.jcandle;


import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;
import org.jsfml.graphics.VertexArray;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Vertex;

public abstract class CandleUtil{
  public static Color darken(final Color c, final float r){       
    return Color.mul(c, (1.f-r));
  }                                    
  public static Color lighten(final Color c, final float r){ 
    return Color.mul(c, (1.f+r));
  }
  public static Color interpolate(final Color c1, final Color c2, final float r){
    //                              ((c2 - c1) * r)+c1
    return Color.add(c1, Color.mul(Color.add(c2, Color.mul(c1, -1)), r));
    //return new Color(c1.r + (c2.r - c1.r) * r, c1.g + (c2.g - c1.g) * r, c1.b + (c2.b - c1.b) * r, c1.a + (c2.a - c1.a) * r);
  }
  public static Color complementary(final Color c){
    return new Color(255-c.r, 255-c.g, 255-c.b, c.a);
  }
  public static double fmod(final double a, final double b) {
    int result = (int) Math.floor(a / b);
    return a - result * b;
  } 
  /**
  * Get the magnitude of a 2D vector.
  */  
  public static float magnitude(final Vector2f v){
    return (float)Math.sqrt(v.x*v.x + v.y*v.y);
  }
  /**
   * Get the squared magnitude of a 2D vector.
   */
  public static float magnitude2(final Vector2f v){
    return (float)(v.x*v.x + v.y*v.y);
  }    
  /**
   * Get the normalized version of a 2D vector.
   */
  public static Vector2f normalize(final Vector2f v){
    float m = magnitude(v);
    return new Vector2f(v.x/m, v.y/m);
  }    
  /**
   * Get the dot product of two 2D vectors.
   */
  public static float dot(final Vector2f v1, final Vector2f v2){
    return (float)(v1.x*v2.x + v1.y*v2.y);
  }  
  /**
   * Get the angle between two 2D vectors.
   */
  public static float angle(final Vector2f v1, final Vector2f v2){
    return (float)(Math.acos(dot(v1,v2)/(magnitude(v1)*magnitude(v2))) * 180/Math.PI);
  }  
  /**
   * Get the angle of a 2D vector with the X axis.
   */
  public static float angle(final Vector2f v){
    return (float)fmod(Math.atan2(v.y, v.x) * 180.f/Math.PI + 360.f, 360.f);
  }
  
  public static void setColor(VertexArray va, Color color){
    for(int i = 0; i < va.size(); i++){
      JSFMLUtil.VertexArray_setIndexNewVertexColor(va, i, color);
    }
  }
    
   public static void transform(VertexArray va, Transform t){
    for(int i = 0; i < va.size(); i++){
      JSFMLUtil.VertexArray_setIndexNewVertexPosition(va, i, t.transformPoint((va.get(i).position)));
    }
  }
  
  public static void move(VertexArray va, Vector2f d){
    for(int i = 0; i < va.size(); i++){
      JSFMLUtil.VertexArray_setIndexNewVertexPosition(va, i,      Vector2f.add(va.get(i).position, d));
    }
  }
  
  public static void darken(VertexArray va, float r){
    for(int i = 0; i < va.size(); i++){
      JSFMLUtil.VertexArray_setIndexNewVertexColor(va, i, darken(va.get(i).color, r));
    }
  }
  
  public static void lighten(VertexArray va, float r){
    for(int i = 0; i < va.size(); i++){
      JSFMLUtil.VertexArray_setIndexNewVertexColor(va, i, lighten(va.get(i).color, r));
    }
  }
  
  public static void interpolate(VertexArray va, Color c, float r){
    for(int i = 0; i < va.size(); i++){
      JSFMLUtil.VertexArray_setIndexNewVertexColor(va, i, interpolate(va.get(i).color, c, r));
    }
  }
  
  public static void complementary(VertexArray va){
    for(int i = 0; i < va.size(); i++){
      JSFMLUtil.VertexArray_setIndexNewVertexColor(va, i, complementary(va.get(i).color));
    }
  }   
}
