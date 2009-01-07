package it.assist.jrecordbind;

import java.util.HashSet;
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
    private String fixedValue;
    private int length;
    private final String name;
    private String padder;
    private int row;
    private String type;

    public Property(String name) {
      this.name = name;
    }

    public String getConverter() {
      return converter;
    }

    public String getFixedValue() {
      return fixedValue;
    }

    public int getLength() {
      return length;
    }

    public String getName() {
      return name;
    }

    public String getPadder() {
      return padder;
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

    public void setFixedValue(String value) {
      this.fixedValue = value;
    }

    public void setLength(int end) {
      this.length = end;
    }

    public void setPadder(String padder) {
      this.padder = padder;
    }

    public void setRow(int row) {
      this.row = row;
    }

    public void setType(String type) {
      this.type = type;
    }

  }

  private String className;
  private String delimiter;
  private int length;
  private int maxOccurs;
  private int minOccurs;
  private final String name;
  private final List<Property> properties;
  private Set<String> rowNumbers;
  private final List<RecordDefinition> subRecords;

  public RecordDefinition() {
    this(null);
  }

  public RecordDefinition(String name) {
    this.name = name;
    this.properties = new LinkedList<Property>();
    this.delimiter = "";
    this.rowNumbers = new HashSet<String>();
    this.subRecords = new LinkedList<RecordDefinition>();
  }

  public void addRowNumber(String row) {
    rowNumbers.add(row);
  }

  public String getClassName() {
    return className;
  }

  public String getDelimiter() {
    return delimiter;
  }

  public int getLength() {
    return length;
  }

  public int getMaxOccurs() {
    return maxOccurs;
  }

  public int getMinOccurs() {
    return minOccurs;
  }

  public String getName() {
    return name;
  }

  public List<Property> getProperties() {
    return properties;
  }

  public int getRows() {
    return rowNumbers.size();
  }

  public List<RecordDefinition> getSubRecords() {
    return subRecords;
  }

  public void setDelimiter(String delimiter) {
    if (delimiter == null) {
      return;
    }
    this.delimiter = delimiter;
  }

  public void setFullName(String className, String packageName) {
    this.className = packageName + "." + className;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public void setMaxOccurs(int maxOccurs) {
    this.maxOccurs = maxOccurs;
  }

  public void setMinOccurs(int minOccurs) {
    this.minOccurs = minOccurs;
  }

}
