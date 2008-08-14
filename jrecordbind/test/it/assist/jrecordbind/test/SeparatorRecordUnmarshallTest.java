package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Unmarshaller;

import java.io.InputStreamReader;
import java.util.Iterator;

import junit.framework.TestCase;

public class SeparatorRecordUnmarshallTest extends TestCase {

  private Unmarshaller unmarshaller;

  public SeparatorRecordUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller(new InputStreamReader(SeparatorRecordUnmarshallTest.class
        .getResourceAsStream("/separator.def.properties")));
  }

  public void testUnmarshall() throws Exception {
    Iterator iter = unmarshaller.unmarshall(new InputStreamReader(SeparatorRecordUnmarshallTest.class
        .getResourceAsStream("separator_test.txt")));

    assertTrue(iter.hasNext());
    SeparatorRecord record = (SeparatorRecord) iter.next();
    assertEquals("name      ", record.getName());
    assertEquals("surname   ", record.getSurname());
    assertEquals("0123456789", record.getTaxCode());

    assertTrue(iter.hasNext());
    record = (SeparatorRecord) iter.next();
    assertEquals("other name", record.getName());
    assertEquals("other surn", record.getSurname());
    assertEquals("9876543210", record.getTaxCode());
  }
}
