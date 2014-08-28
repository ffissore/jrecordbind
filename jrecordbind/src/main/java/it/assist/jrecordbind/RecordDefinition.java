/*
 * JRecordBind, fixed-length file (un)marshaller
 * Copyright 2009, Assist s.r.l., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package it.assist.jrecordbind;

import it.assist.jrecordbind.padders.SpaceRightPadder;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/***
 * The definition of the record bean. I.E. this is the bean rapresentation of
 * the fixed-length file definition (.xsd)
 * 
 * @author Federico Fissore
 */
class RecordDefinition {

  /**
   * A single "element" in the fixed-length file definition
   * 
   * @author Federico Fissore
   */
  public static class Property {

    private final ClassLoader classLoader;
    private String converter;
    private String fixedValue;
    private int length;
    private final String name;
    private String padder;
    private int row;
    private String type;
    private final EnumPropertyHelper enumPropertyHelper;

    /**
     * Creates a new Property
     * 
     * @param name
     *          the name of the property
     */
    public Property(ClassLoader classLoader, String name) {
      this.classLoader = classLoader;
      this.name = name;
      enumPropertyHelper = new EnumPropertyHelper(this);
    }

    public ClassLoader getClassLoader() {
      return classLoader;
    }

    /**
     * The fully qualified class name of the converter used to marshall and
     * unmarshall this property
     * 
     * @return a fully qualified class name
     */
    public String getConverter() {
      return converter;
    }

    /**
     * The fixed value of this property, usually used to identify different
     * lines in a single file
     * 
     * @return the value
     */
    public String getFixedValue() {
      return fixedValue;
    }

    /**
     * The length of the property
     * 
     * @return the length of the property
     */
    public int getLength() {
      return length;
    }

    /**
     * The name of the property
     * 
     * @return the name of the property
     */
    public String getName() {
      return name;
    }

    /**
     * The padder used to pad this property value. If missing (<code>null</code>
     * ) the padder given to the {@link Marshaller#Marshaller(Reader, Padder)}
     * (or its default padder) will be used
     * 
     * @return a fully qualified class name or <code>null</code> if none
     *         specified
     */
    public String getPadder() {
      return padder;
    }

    /**
     * The row this property is at. Consistency (ie: if this is the first
     * property at row 2, every subsequent property must be at least at row 2)
     * is up to the definition writer (developer)
     * 
     * @return the row
     */
    public int getRow() {
      return row;
    }

    /**
     * The class name of the type of this property. Internally supported type
     * (String, Integer...) names are not fully qualified
     * 
     * @return the class name of this property type
     */
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

    public boolean isEnum() {
      return enumPropertyHelper.isEnum();
    }

  }

  private final ClassLoader classLoader;
  private boolean choice;
  private String className;
  private String globalPadder;
  private int length;
  private String lineSeparator;
  private int maxOccurs;
  private int minOccurs;
  private final RecordDefinition parent;
  private String printableLineSeparator;
  private final List<Property> properties;
  private String propertyDelimiter;
  private String propertyPattern;
  private String setterName;
  private final List<RecordDefinition> subRecords;

  /**
   * Creates a new instance, without a setter name (ie: this is the main
   * definition)
   */
  public RecordDefinition(ClassLoader classLoader) {
    this(classLoader, null);
  }

  /**
   * Creates a new instance, with the given setter name (ie: this definition is
   * contained by a main one)
   * 
   * @param parent
   *          the name of the property that will contain this kind of records
   */
  public RecordDefinition(ClassLoader classLoader, RecordDefinition parent) {
    this.classLoader = classLoader;
    this.properties = new LinkedList<Property>();
    this.propertyDelimiter = "";
    this.subRecords = new LinkedList<RecordDefinition>();
    this.parent = parent;
    this.globalPadder = SpaceRightPadder.class.getName();
  }

  public ClassLoader getClassLoader() {
    return classLoader;
  }

  /**
   * The fully qualified class name described by this definition
   * 
   * @return a fully qualified class name
   */
  public String getClassName() {
    return className;
  }

  public String getGlobalPadder() {
    if (hasParent()) {
      return parent.getGlobalPadder();
    }
    return globalPadder;
  }

  /**
   * The length of the fixed-length file
   * 
   * @return the length
   */
  public int getLength() {
    if (hasParent()) {
      return parent.getLength();
    }
    return length;
  }

  public String getLineSeparator() {
    if (hasParent()) {
      return parent.getLineSeparator();
    }
    return lineSeparator;
  }

  /**
   * How many times this definition can occur in the fixed-length file? Main
   * definition can occur 1 time only, subdefinitions can vary
   * 
   * @return an int
   */
  public int getMaxOccurs() {
    return maxOccurs;
  }

  /**
   * How many times this definition must occur in the fixed-length file? Main
   * definition must occur 1 time, subdefinitions can vary
   * 
   * @return an int
   */
  public int getMinOccurs() {
    return minOccurs;
  }

  public RecordDefinition getParent() {
    return parent;
  }

  public String getPrintableLineSeparator() {
    if (hasParent()) {
      return parent.getPrintableLineSeparator();
    }
    return printableLineSeparator;
  }

  /**
   * The list of {@link Property properties} contained by this definition
   * 
   * @return the list of {@link Property properties}
   */
  public List<Property> getProperties() {
    return properties;
  }

  /**
   * The delimiter used in the fixed-length file
   * 
   * @return the delimiter
   */
  public String getPropertyDelimiter() {
    if (hasParent()) {
      return parent.getPropertyDelimiter();
    }
    return propertyDelimiter;
  }
  
  /**
   * True if the record is delimited, false if it's fixed length
   * @return
   */
  public boolean isDelimited(){
	  return(!"".equals(getPropertyDelimiter()));
  }

  /**
   * The name of the property used to set records from this definition in the
   * parent (container) definition. E.G.: the "name" attribute in "element" like
   * the following<br>
   * <code>&lt;xs:element name="child" type="RowChildRecord"/&gt;</code>
   * 
   * @return a property name
   */
  public String getSetterName() {
    return setterName;
  }

  /**
   * The sub definitions contained by this definition (hierarchy based files)
   * 
   * @return the list of sub definitions
   */
  public List<RecordDefinition> getSubRecords() {
    return subRecords;
  }

  /**
   * The regex that matches a property 
   * (i.e. a string that does not contain a delimiter)
   * 
   * @return the regex that matches a delimited property
   */
  public String getPropertyPattern() {
    if (hasParent()) {
      return parent.getPropertyPattern();
    }
  	return propertyPattern;
  }

	@Override
  public int hashCode() {
    return getClassName().hashCode();
  }

  public boolean hasParent() {
    return parent != null;
  }

  public boolean isChoice() {
    return choice;
  }

  public void setChoice(boolean choice) {
    this.choice = choice;
  }

  public void setClassName(String fullyQualifiedClassName) {
    this.className = fullyQualifiedClassName;
  }

  public void setClassName(String className, String packageName) {
    this.className = packageName + "." + className;
  }

  public void setGlobalPadder(String globalPadder) {
    if (globalPadder == null) {
      return;
    }
    this.globalPadder = globalPadder;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public void setLineSeparator(String separator) {
    this.lineSeparator = separator;
    StringBuilder sb = new StringBuilder();
    for (char c : this.lineSeparator.toCharArray()) {
      if (!Character.isISOControl(c)) {
        sb.append(c);
      }
    }
    this.printableLineSeparator = sb.toString();
  }

  public void setMaxOccurs(int maxOccurs) {
    this.maxOccurs = maxOccurs;
  }

  public void setMinOccurs(int minOccurs) {
    this.minOccurs = minOccurs;
  }

  public void setPropertyDelimiter(String delimiter) {
    if (delimiter == null) {
      return;
    }
    this.propertyDelimiter = delimiter;
    this.propertyPattern = generatePropertyPattern(delimiter);
  }
  

  public void setSetterName(String setterName) {
    this.setterName = setterName;
  }
  
  private String generatePropertyPattern(String delimiter){
  	if(delimiter.equals("")){
  		return (".");
  	} else if(delimiter.length()==1){
  		return("[^\\Q"+delimiter+"\\E\\n]");
  	} else {
  		return "[^\\Q"+delimiter.substring(0, 1)+"\\E\\n]|(?:\\Q"+delimiter.substring(0, 1)+"\\E(?!\\Q"+delimiter.substring(1)+"\\E))";
  	}
  }

}
