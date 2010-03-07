package it.assist.jrecordbind;

class ClassUtils {

  public static Object newInstanceOf(String fqn) {
    try {
      return loadClass(fqn).newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Class<?> loadClass(String fqn) {
    try {
      return Class.forName(fqn);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

}
