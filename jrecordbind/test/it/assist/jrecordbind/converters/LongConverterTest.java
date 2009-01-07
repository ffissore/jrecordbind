package it.assist.jrecordbind.converters;

import junit.framework.TestCase;

public class LongConverterTest extends TestCase {

  private LongConverter converter;

  public LongConverterTest() {
    converter = new LongConverter();
  }

  public void testConvert() {
    assertEquals(Long.valueOf(90), converter.convert("90"));
    assertEquals(Long.valueOf(-1), converter.convert("-1"));
    assertNull(converter.convert(null));
    assertNull(converter.convert(""));
  }

  public void testToStringObject() {
    assertEquals("90", converter.toString(Long.valueOf(90)));
    assertEquals("-1", converter.toString(Long.valueOf(-1)));
    assertEquals("", converter.toString(null));
  }

}
