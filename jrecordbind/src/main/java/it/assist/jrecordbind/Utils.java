package it.assist.jrecordbind;

import java.io.File;
import java.io.Reader;
import java.net.MalformedURLException;

import org.xml.sax.InputSource;

class Utils {

  public static Class<?> loadClass(String fqn) {
    try {
      return Class.forName(fqn);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object newInstanceOf(String fqn) {
    try {
      return Utils.loadClass(fqn).newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

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
