package it.assist.jrecordbind.util;

import it.assist.jrecordbind.Unmarshaller;
import it.assist.jrecordbind.test.SimpleRecordUnmarshallTest;
import it.assist_si.schemas.jrb.simple.SimpleRecord;

import java.io.InputStreamReader;

import junit.framework.TestCase;

public class TrimmerTest extends TestCase {

  private Unmarshaller<SimpleRecord> unmarshaller;
  private Trimmer trimmer;

  public TrimmerTest() throws Exception {
    unmarshaller = new Unmarshaller<SimpleRecord>(new InputStreamReader(SimpleRecordUnmarshallTest.class
        .getResourceAsStream("/simple.def.xsd")));
    trimmer = new Trimmer();
  }

  public void testTrim() throws Exception {
    SimpleRecord simpleRecord = unmarshaller.unmarshallAll(
        new InputStreamReader(TrimmerTest.class.getResourceAsStream("trimmer_simple_test.txt"))).get(0);

    simpleRecord.setSurname(null);

    assertEquals("JOHN                ", simpleRecord.getName());
    assertNull(simpleRecord.getSurname());
    assertEquals("ABCDEF88L99H123B", simpleRecord.getTaxCode());

    trimmer.trim(simpleRecord);

    assertEquals("JOHN", simpleRecord.getName());
    assertNull(simpleRecord.getSurname());
    assertEquals("ABCDEF88L99H123B", simpleRecord.getTaxCode());
  }
}
