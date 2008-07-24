package it.assist.jrecordbind;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RecordDefinition {

  public static class Property {

    private String converter;
    private int length;
    private final String name;
    private String type;

    public Property(String name) {
      this.name = name;
    }

    public String getConverter() {
      return converter;
    }

    public String getInMethodName() {
      return getName().substring(0, 1).toUpperCase() + getName().substring(1);
    }

    public int getLength() {
      return length;
    }

    public String getName() {
      return name;
    }

    public String getType() {
      return type;
    }

    public void setConverter(String converter) {
      this.converter = converter;
    }

    public void setLength(int end) {
      this.length = end;
    }

    public void setType(String type) {
      this.type = type;
    }

  }

  private String className;
  private String packageName;
  private final List<Property> properties;

  public RecordDefinition() {
    this.properties = new LinkedList<Property>();
  }

  public String getClassName() {
    return className;
  }

  public int getLength() {
    int length = 0;
    for (Iterator<Property> iter = properties.iterator(); iter.hasNext();) {
      length += iter.next().getLength();
    }
    return length;
  }

  public String getPackageName() {
    return packageName;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

}
