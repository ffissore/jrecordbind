package it.assist.jrecordbind.util;

import java.lang.reflect.Method;

/**
 * Given an object, looks for methods that start with "get" and return a String.
 * Then it replaces that String, trimmed, by getting and setting it
 * 
 * @author Federico Fissore
 */
public class Trimmer {

  public void trim(Object obj) throws TrimmerException {
    for (Method method : obj.getClass().getMethods()) {
      if (method.getName().startsWith("get") && String.class.isAssignableFrom(method.getReturnType())) {
        try {
          String value = (String) method.invoke(obj);
          if (value != null) {
            value = value.trim();
            Method setter = obj.getClass().getMethod("set" + method.getName().substring(3), String.class);
            setter.invoke(obj, value);
          }
        } catch (Exception e) {
          throw new TrimmerException(e);
        }
      }
    }
  }

}
