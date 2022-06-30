package org.jcandle;

import java.util.ArrayList;
import java.util.Collections;
public abstract class CPPUtils{
  public static <T> void Vector_resize(ArrayList<T> list, int size) {
    if(list.size()==size)
      return;
    if(list.size()>size){
      ArrayList<T> l=copyArrayList(list, 0, size);
      list.clear();
      list.addAll(l);
    }
    if(list.size()<size){
      for(int i=0; i<size-list.size(); i++){
        list.add(null);
      }
    }
  }
  public static <T> ArrayList<T> copyArrayList(ArrayList<T> list, int from, int to) {
    ArrayList<T> returnList=new ArrayList<T>(to-from);
    for (int i=from; i<to; i++) {
      returnList.add(list.get(i));
    } 
    return returnList;
  }
  public static <T> T Stack_top(ArrayList<T> list){
    return list.get(list.size()-1);
  }
  public static <T> void Stack_pop(ArrayList<T> list){
    list.remove(list.size()-1);
  }
}
