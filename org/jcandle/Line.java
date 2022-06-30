package org.jcandle;
import org.jsfml.system.Vector2f;
import java.util.ArrayList;
import org.jsfml.graphics.FloatRect;
public class Line{
  public Vector2f m_origin; ///< Origin point of the line.
  public Vector2f m_direction; ///< Direction vector (not necessarily  normalized)
  public Line(Line l){
    m_origin=l.m_origin;
    m_direction=l.m_direction;
  }
  public Line(Vector2f origin, Vector2f direction){
    m_origin=origin;
    m_direction=Vector2f.sub(direction, origin);
  }
  public Line(Vector2f p, float angle){
    m_origin=p;
    float PI2 = (float)Math.PI*2;
    float ang = (float)CandleUtil.fmod(angle*Math.PI/180.f + Math.PI , PI2);
    if(ang < 0) ang += PI2;
    ang = (float)(ang - Math.PI);
    m_direction = new Vector2f((float)Math.cos(ang), (float)Math.sin(ang));
  }
  public FloatRect getGlobalBounds() {
    Vector2f point1 = m_origin;
    Vector2f point2 = Vector2f.add(m_direction, m_origin);
    //Make sure that the rectangle begin from the upper left corner                                                                                 //The +1 is here to avoid having a width of zero    //(SFML doesn't like 0 in rect)
    return new FloatRect((point1.x < point2.x) ? point1.x : point2.x, (point1.y < point2.y) ? point1.y : point2.y, (float)Math.abs(m_direction.x) + 1.0f, (float)Math.abs(m_direction.y) + 1.0f);
  }

  public int relativePosition(final Vector2f point) {
    float f = (point.x-m_origin.x) / m_direction.x - (point.y-m_origin.y) / m_direction.y;
    //return (0.f < f) - (f < 0.f);
    //version 1
    if(f==0)
      return 0;
    else if(f>0)
        return 1;
      else 
        return -1;
  }
  
  public float distance(final Vector2f point) {
    float d;
    if(m_direction.x == 0){
      d = Math.abs(point.x - m_origin.x);
    }else if(m_direction.y == 0){
        d = Math.abs(point.y - m_origin.y);
      }else{
        float A = 1.f / m_direction.x;
        float B = - 1.f / m_direction.y;
        float C = - B*m_origin.y - A*m_origin.x;
        d = (float)Math.abs(A*point.x + B*point.y + C) / (float)Math.sqrt(A*A + B*B);
      }
    return d;
  }

  public boolean intersection(final Line lineB) {
    float normA=0,normB=0;
    return intersection(lineB, normA, normB);
  }
  public boolean intersection(final Line lineB, final float normA) {
    float normB=0;
    return intersection(lineB, normA,normB);
  }
  public boolean intersection(Line lineB, float normA, float normB) {
    Vector2f lineAorigin = m_origin;
    Vector2f lineAdirection = m_direction;
    Vector2f lineBorigin = lineB.m_origin;
    Vector2f lineBdirection = lineB.m_direction;
    
    //When the lines are parallel, we consider that there is not intersection.
    float lineAngle = CandleUtil.angle(lineAdirection, lineBdirection);
    if( (lineAngle < 0.001f || lineAngle > 359.999f) || ((lineAngle < 180.001f) && (lineAngle > 179.999f)) ){
      return false;
    }
    
    //Math resolving, you can find more information here : https://ncase.me/sight-and-light/
    if ( (Math.abs(lineBdirection.y) >= 0.0f) && (Math.abs(lineBdirection.x) < 0.001f) || (Math.abs(lineAdirection.y) < 0.001f) && (Math.abs(lineAdirection.x) >= 0.0f) ) {
      normB = (lineAdirection.x*(lineAorigin.y-lineBorigin.y) + lineAdirection.y*(lineBorigin.x-lineAorigin.x))/(lineBdirection.y*lineAdirection.x - lineBdirection.x*lineAdirection.y);
      normA = (lineBorigin.x+lineBdirection.x*normB-lineAorigin.x)/lineAdirection.x;
    } else {
      normA = (lineBdirection.x*(lineBorigin.y-lineAorigin.y) + lineBdirection.y*(lineAorigin.x-lineBorigin.x))/(lineAdirection.y*lineBdirection.x - lineAdirection.x*lineBdirection.y);
      normB = (lineAorigin.x+lineAdirection.x*normA-lineBorigin.x)/lineBdirection.x;
    }
    
    //Make sure that there is actually an intersection
    //woher kommt magnitude
    if ( (normB>0) && (normA>0) && (normA<CandleUtil.magnitude(m_direction)) ){
      return true;
    }
    
    return false;
  }

  public Vector2f point(float param) {
    return Vector2f.add(m_origin, Vector2f.mul(m_direction, param));
  }

  //public Vector2f castRay(Iterator begin, Iterator end, Line ray, float maxRange=std::numeric_limits<float>::infinity()){
  public static Vector2f castRay(ArrayList<Line> list, Line ray, float maxRange){
    if(maxRange<0)
      maxRange=Float.MAX_VALUE;
    
    float minRange = maxRange;
    
    ray.m_direction = CandleUtil.normalize(ray.m_direction);
    
    for(int i=0; i<list.size(); i++){
      Line l=list.get(i);
      
      float t_seg=0, t_ray=0;
      
      if(l.intersection(ray, t_seg, t_ray)
      && t_ray <= minRange
      && t_ray >= 0.f
      && t_seg <= 1.f
      && t_seg >= 0.f
      ){
        minRange = t_ray;
      }
    }
    return ray.point(minRange);
  }
}
