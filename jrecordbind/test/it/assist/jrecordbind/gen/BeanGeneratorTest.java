package it.assist.jrecordbind.gen;

import it.assist.jrecordbind.DefinitionLoader;
import it.assist.jrecordbind.gen.BeanGenerator;

import java.io.StringWriter;

import junit.framework.TestCase;

public class BeanGeneratorTest extends TestCase {

  public void testGenerate() throws Exception {

    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(BeanGeneratorTest.class.getResourceAsStream("/simple.def.properties"));
    StringWriter sw = new StringWriter();
    new BeanGenerator().generate(definitionLoader.getDefinition(), sw);

    assertTrue(sw.toString().indexOf("package it.assist.jrecordbind;") != -1);
    assertTrue(sw.toString().indexOf("public class SimpleRecord") != -1);
  }

}
