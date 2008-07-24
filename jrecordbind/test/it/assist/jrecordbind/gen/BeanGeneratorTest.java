package it.assist.jrecordbind.gen;

import it.assist.jrecordbind.DefinitionLoader;
import it.assist.jrecordbind.gen.RecordBeanGenerator;

import java.io.InputStreamReader;
import java.io.StringWriter;

import junit.framework.TestCase;

public class BeanGeneratorTest extends TestCase {

  public void testGenerate() throws Exception {

    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(new InputStreamReader(BeanGeneratorTest.class.getResourceAsStream("/simple.def.properties")));
    StringWriter sw = new StringWriter();
    new RecordBeanGenerator().generate(definitionLoader.getDefinition(), sw);

    assertTrue(sw.toString().indexOf("package it.assist.jrecordbind.test;") != -1);
    assertTrue(sw.toString().indexOf("public class SimpleRecord") != -1);
  }

}
