package org.jcandle;

import java.util.ArrayList;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.MouseEvent;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.system.Vector2f;
public class JCandleDriver{
  
  public static void main(String[] args) {
    RenderWindow window=new RenderWindow(new VideoMode(800, 800), "app");
    
    // create a light source
    RadialLight light=new RadialLight();
    light.setRange(150);
    
    // create an edge pool
    ArrayList<Line> edges=new ArrayList<Line>();
    edges.add(new Line(new Vector2f(400.f, 200.f), new Vector2f(400.f, 600.f)));
    
    // main loop
    while(window.isOpen()){
      window.clear();
      window.draw(light);
      window.display();
      for (Event event : window.pollEvents()) {
        switch (event.type) {
          case  CLOSED: 
            window.close();
            break;
          case  MOUSE_MOVED: 
            MouseEvent mouseEvent=event.asMouseEvent();
            Vector2f mp=new Vector2f(mouseEvent.position);
            light.setPosition(mp);
            light.castLight(edges);
            break;
        } 
      } 
    }
  }
  
}
