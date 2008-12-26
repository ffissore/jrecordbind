package it.assist.jrecordbind;

import junit.framework.TestCase;

public class RegexGeneratorTest extends TestCase {

  public void testDelimiter() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(RegexGeneratorTest.class.getResourceAsStream("/delimiter.def.xsd"));

    RegexGenerator regexGenerator = new RegexGenerator(definitionLoader.getDefinition());

    assertEquals("([a-zA-Z_0-9\\s]{10})\\|([a-zA-Z_0-9\\s]{10})\\|([a-zA-Z_0-9\\s]{10})", regexGenerator.pattern()
        .pattern());
  }

  public void testHierarchical() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(RegexGeneratorTest.class.getResourceAsStream("/hierarchical.def.xsd"));

    RegexGenerator regexGenerator = new RegexGenerator(definitionLoader.getDefinition());

    assertEquals(
        "(000)\\|([a-zA-Z_0-9\\s]{10})\\|([a-zA-Z_0-9\\s]{10})\\|([a-zA-Z_0-9\\s]{10})(\\n(A00)\\|([a-zA-Z_0-9\\s]{10})\\|([a-zA-Z_0-9\\s]{10})[\\s]{11}){0,}(\\n(B01)\\|([a-zA-Z_0-9\\s]{8})[\\s]{24}){1,1}",
        regexGenerator.pattern().pattern());
  }

  public void testMultiRow() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(RegexGeneratorTest.class.getResourceAsStream("/multi-row.def.xsd"));

    RegexGenerator regexGenerator = new RegexGenerator(definitionLoader.getDefinition());

    assertEquals(
        "([a-zA-Z_0-9\\s]{20})([a-zA-Z_0-9\\s]{20})([a-zA-Z_0-9\\s]{16})([a-zA-Z_0-9\\s]{8})([a-zA-Z_0-9\\s]{2})([a-zA-Z_0-9\\s]{3})\\n([a-zA-Z_0-9\\s]{20})([a-zA-Z_0-9\\s]{20})[\\s]{29}",
        regexGenerator.pattern().pattern());
  }

  public void testSimple() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(RegexGeneratorTest.class.getResourceAsStream("/simple.def.xsd"));

    RegexGenerator regexGenerator = new RegexGenerator(definitionLoader.getDefinition());

    assertEquals(
        "([a-zA-Z_0-9\\s]{20})([a-zA-Z_0-9\\s]{20})([a-zA-Z_0-9\\s]{16})([a-zA-Z_0-9\\s]{8})([a-zA-Z_0-9\\s]{2})([a-zA-Z_0-9\\s]{3})[\\s]{31}",
        regexGenerator.pattern().pattern());
  }

}
