package org.jcandle;

import java.util.ArrayList;
import org.jsfml.system.Vector2f;
import org.jsfml.graphics.FloatRect;
public class Polygon{
  ArrayList<Line> m_lines=new ArrayList<Line>();
  public Polygon(FloatRect rect){
    initialize(rect);
  }
  public Polygon(Vector2f[] points, int n){
    initialize(points, n);
  }
  public void initialize(FloatRect rect){
    Vector2f lt=new Vector2f(rect.left, rect.top);
    Vector2f rt=new Vector2f(rect.left+rect.width, rect.top);//Vector2f rt=new Vector2f(rect.right, rect.top);
    Vector2f lb=new Vector2f(rect.left, rect.top+rect.height);//Vector2f lb=new Vector2f(rect.left, rect.bottom);
    Vector2f rb=new Vector2f(rect.left+rect.width, rect.top+rect.height); //Vector2f rb=new Vector2f(rect.right, rect.bottom); 
    m_lines.add(new Line(lt, rt)); 
    m_lines.add(new Line(rt, rb));
    m_lines.add(new Line(rb, lb));
    m_lines.add(new Line(lb, lt));
  }
  public void initialize(Vector2f[] points, int n){
    m_lines.clear();
    m_lines.ensureCapacity(n);
    for(int i=1; i <= n; i++){
      m_lines.add(new Line(points[i - 1], points[i % n]));
    }
  }
}
