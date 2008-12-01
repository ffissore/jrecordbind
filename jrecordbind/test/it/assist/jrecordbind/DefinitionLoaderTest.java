package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.io.InputStreamReader;

import junit.framework.TestCase;

public class DefinitionLoaderTest extends TestCase {

  public void testLoad() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(new InputStreamReader(DefinitionLoaderTest.class
        .getResourceAsStream("/simple.def.properties")));
    RecordDefinition definition = definitionLoader.getDefinition();

    assertEquals("SimpleRecord", definition.getClassName());
    assertEquals("it.assist.jrecordbind.test", definition.getPackageName());
    assertEquals("", definition.getSeparator());

    assertEquals(6, definition.getProperties().size());

    Property property = (Property) definition.getProperties().get(0);
    assertEquals("name", property.getName());
    assertEquals("Name", property.getInMethodName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.StringConverter", property.getConverter());

    property = (Property) definition.getProperties().get(1);
    assertEquals("surname", property.getName());
    assertEquals("Surname", property.getInMethodName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.StringConverter", property.getConverter());

    property = (Property) definition.getProperties().get(2);
    assertEquals("taxCode", property.getName());
    assertEquals("TaxCode", property.getInMethodName());
    assertEquals("String", property.getType());
    assertEquals(16, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.StringConverter", property.getConverter());

    property = (Property) definition.getProperties().get(3);
    assertEquals("birthday", property.getName());
    assertEquals("Birthday", property.getInMethodName());
    assertEquals("java.util.Date", property.getType());
    assertEquals(8, property.getLength());
    assertEquals("it.assist.jrecordbind.test.SimpleRecordDateConverter", property.getConverter());

    property = (Property) definition.getProperties().get(4);
    assertEquals("oneInteger", property.getName());
    assertEquals("OneInteger", property.getInMethodName());
    assertEquals("Integer", property.getType());
    assertEquals(2, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.IntegerConverter", property.getConverter());

    property = (Property) definition.getProperties().get(5);
    assertEquals("oneFloat", property.getName());
    assertEquals("OneFloat", property.getInMethodName());
    assertEquals("Float", property.getType());
    assertEquals(3, property.getLength());
    assertEquals("it.assist.jrecordbind.test.SimpleRecordFloatConverter", property.getConverter());
  }

  public void testLoadSeparator() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(new InputStreamReader(DefinitionLoaderTest.class
        .getResourceAsStream("/separator.def.properties")));
    RecordDefinition definition = definitionLoader.getDefinition();

    assertEquals("|", definition.getSeparator());
  }

  public void testLoadMultiRow() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(new InputStreamReader(DefinitionLoaderTest.class
        .getResourceAsStream("/multi-row.def.properties")));
    RecordDefinition definition = definitionLoader.getDefinition();
    assertEquals(2, definition.getRows());

    assertEquals("MultiRowRecord", definition.getClassName());
    assertEquals("it.assist.jrecordbind.test", definition.getPackageName());
    assertEquals("", definition.getSeparator());

    assertEquals(8, definition.getProperties().size());

    Property property = (Property) definition.getProperties().get(0);
    assertEquals("name", property.getName());
    assertEquals("Name", property.getInMethodName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.StringConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = (Property) definition.getProperties().get(1);
    assertEquals("surname", property.getName());
    assertEquals("Surname", property.getInMethodName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.StringConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = (Property) definition.getProperties().get(2);
    assertEquals("taxCode", property.getName());
    assertEquals("TaxCode", property.getInMethodName());
    assertEquals("String", property.getType());
    assertEquals(16, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.StringConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = (Property) definition.getProperties().get(3);
    assertEquals("birthday", property.getName());
    assertEquals("Birthday", property.getInMethodName());
    assertEquals("java.util.Date", property.getType());
    assertEquals(8, property.getLength());
    assertEquals("it.assist.jrecordbind.test.SimpleRecordDateConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = (Property) definition.getProperties().get(4);
    assertEquals("oneInteger", property.getName());
    assertEquals("OneInteger", property.getInMethodName());
    assertEquals("Integer", property.getType());
    assertEquals(2, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.IntegerConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = (Property) definition.getProperties().get(5);
    assertEquals("oneFloat", property.getName());
    assertEquals("OneFloat", property.getInMethodName());
    assertEquals("Float", property.getType());
    assertEquals(3, property.getLength());
    assertEquals("it.assist.jrecordbind.test.SimpleRecordFloatConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = (Property) definition.getProperties().get(6);
    assertEquals("fatherName", property.getName());
    assertEquals("FatherName", property.getInMethodName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.StringConverter", property.getConverter());
    assertEquals(1, property.getRow());

    property = (Property) definition.getProperties().get(7);
    assertEquals("motherName", property.getName());
    assertEquals("MotherName", property.getInMethodName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.StringConverter", property.getConverter());
    assertEquals(1, property.getRow());

  }
}
