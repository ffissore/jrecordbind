package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Unmarshaller;
import it.assist_si.schemas.jrb.delimiter.DelimiterRecord;

import java.util.Iterator;

import junit.framework.TestCase;

public class DelimiterRecordUnmarshallTest extends TestCase {

  private Unmarshaller<DelimiterRecord> unmarshaller;

  public DelimiterRecordUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<DelimiterRecord>(DelimiterRecordUnmarshallTest.class
        .getResourceAsStream("/delimiter.def.xsd"));
  }

  public void testUnmarshall() throws Exception {
    Iterator<DelimiterRecord> iter = unmarshaller.unmarshall(DelimiterRecordUnmarshallTest.class
        .getResourceAsStream("delimiter_test.txt"));

    assertTrue(iter.hasNext());
    DelimiterRecord record = iter.next();
    assertEquals("name      ", record.getName());
    assertEquals("surname   ", record.getSurname());
    assertEquals("0123456789", record.getTaxCode());

    assertTrue(iter.hasNext());
    record = iter.next();
    assertEquals("other name", record.getName());
    assertEquals("other surn", record.getSurname());
    assertEquals("9876543210", record.getTaxCode());
  }
}
