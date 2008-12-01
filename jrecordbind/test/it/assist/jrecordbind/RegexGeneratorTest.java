package it.assist.jrecordbind;

import java.io.InputStreamReader;

import junit.framework.TestCase;

public class RegexGeneratorTest extends TestCase {

  public void testSimple() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader
        .load(new InputStreamReader(RegexGeneratorTest.class.getResourceAsStream("/simple.def.properties")));

    RegexGenerator regexGenerator = new RegexGenerator(definitionLoader.getDefinition());

    assertEquals(
        "([a-zA-Z_0-9\\s]{20})([a-zA-Z_0-9\\s]{20})([a-zA-Z_0-9\\s]{16})([a-zA-Z_0-9\\s]{8})([a-zA-Z_0-9\\s]{2})([a-zA-Z_0-9\\s]{3})",
        regexGenerator.pattern().pattern());
  }

  public void testSeparator() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/separator.def.properties")));

    RegexGenerator regexGenerator = new RegexGenerator(definitionLoader.getDefinition());

    assertEquals("([a-zA-Z_0-9\\s]{10})\\|([a-zA-Z_0-9\\s]{10})\\|([a-zA-Z_0-9\\s]{10})", regexGenerator.pattern()
        .pattern());
  }

  public void testMultiRow() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/multi-row.def.properties")));

    RegexGenerator regexGenerator = new RegexGenerator(definitionLoader.getDefinition());

    assertEquals(
        "([a-zA-Z_0-9\\s]{20})([a-zA-Z_0-9\\s]{20})([a-zA-Z_0-9\\s]{16})([a-zA-Z_0-9\\s]{8})([a-zA-Z_0-9\\s]{2})([a-zA-Z_0-9\\s]{3})\\n([a-zA-Z_0-9\\s]{20})([a-zA-Z_0-9\\s]{20})",
        regexGenerator.pattern().pattern());
  }

}
