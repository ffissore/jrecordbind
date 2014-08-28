package it.assist.jrecordbind;

import java.io.File;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.*;

import org.xml.sax.InputSource;

class Utils {

  public static Class<?> loadClass(ClassLoader loader, String fqn) {
    try {
      return Class.forName(fqn, true, loader);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object newInstanceOf(ClassLoader loader, String fqn) {
    try {
      return Utils.loadClass(loader, fqn).newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("deprecation")
  public static InputSource toInputSource(File file) {
    try {
      return new InputSource(file.toURL().toExternalForm());
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  public static InputSource toInputSource(Reader reader) {
    return new InputSource(reader);
  }

}
