package it.assist.jrecordbind;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/***
 * The definition of the record bean. I.E. this is the object rapresentation of the record definition (.properties) file 
 * 
 * @author Federico Fissore
 */
public class RecordDefinition {

  public static class Property {

    private String converter;
    private int length;
    private final String name;
    private int row;
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

    public int getRow() {
      return row;
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

    public void setRow(int row) {
      this.row = row;
    }

    public void setType(String type) {
      this.type = type;
    }

  }

  private String className;
  private String packageName;
  private final List properties;
  private String separator;
  private Set rowNumbers;

  public RecordDefinition() {
    this.properties = new LinkedList();
    this.separator = "";
    this.rowNumbers = new HashSet();
  }

  public String getClassName() {
    return className;
  }

  public int getLength() {
    int length = 0;
    for (Iterator iter = properties.iterator(); iter.hasNext();) {
      length += ((Property) iter.next()).getLength();
    }
    return length;
  }

  public String getPackageName() {
    return packageName;
  }

  public List getProperties() {
    return properties;
  }

  public String getSeparator() {
    return separator;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public void setSeparator(String separator) {
    this.separator = separator;
  }

  public int getRows() {
    return rowNumbers.size();
  }

  public void addRowNumber(String row) {
    rowNumbers.add(row);
  }

}
