package it.assist.jrecordbind;

import it.assist.jrecordbind.DefinitionLoader;
import it.assist.jrecordbind.RecordDefinition;
import it.assist.jrecordbind.RecordDefinition.Property;
import junit.framework.TestCase;

public class DefinitionLoaderTest extends TestCase {

  public void testLoad() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(DefinitionLoaderTest.class.getResourceAsStream("/simple.def.properties"));
    RecordDefinition definition = definitionLoader.getDefinition();

    assertEquals("SimpleRecord", definition.getClassName());
    assertEquals("it.assist.jrecordbind", definition.getPackageName());

    assertEquals(6, definition.getProperties().size());

    Property property = definition.getProperties().get(0);
    assertEquals("nome", property.getName());
    assertEquals("Nome", property.getInMethodName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.StringConverter", property.getConverter());

    property = definition.getProperties().get(1);
    assertEquals("cognome", property.getName());
    assertEquals("Cognome", property.getInMethodName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.StringConverter", property.getConverter());

    property = definition.getProperties().get(2);
    assertEquals("codiceFiscale", property.getName());
    assertEquals("CodiceFiscale", property.getInMethodName());
    assertEquals("String", property.getType());
    assertEquals(16, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.StringConverter", property.getConverter());

    property = definition.getProperties().get(3);
    assertEquals("dataNascita", property.getName());
    assertEquals("DataNascita", property.getInMethodName());
    assertEquals("java.util.Date", property.getType());
    assertEquals(8, property.getLength());
    assertEquals("it.assist.jrecordbind.test.SimpleRecordDateConverter", property.getConverter());

    property = definition.getProperties().get(4);
    assertEquals("intero", property.getName());
    assertEquals("Intero", property.getInMethodName());
    assertEquals("Integer", property.getType());
    assertEquals(2, property.getLength());
    assertEquals("it.assist.jrecordbind.converters.IntegerConverter", property.getConverter());

    property = definition.getProperties().get(5);
    assertEquals("virgola", property.getName());
    assertEquals("Virgola", property.getInMethodName());
    assertEquals("Float", property.getType());
    assertEquals(3, property.getLength());
    assertEquals("it.assist.jrecordbind.test.SimpleRecordFloatConverter", property.getConverter());
  }
}
