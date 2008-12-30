package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Unmarshaller;
import it.assist_si.schemas.jrb.simple.SimpleRecord;

import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Iterator;

import junit.framework.TestCase;

public class SimpleRecordUnmarshallTest extends TestCase {

  private Unmarshaller<SimpleRecord> unmarshaller;

  public SimpleRecordUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<SimpleRecord>(new InputStreamReader(SimpleRecordUnmarshallTest.class
        .getResourceAsStream("/simple.def.xsd")));
  }

  public void testUnmarshallAll() throws Exception {
    Iterator<SimpleRecord> records = unmarshaller.unmarshall(new InputStreamReader(SimpleRecordUnmarshallTest.class
        .getResourceAsStream("simple_test.txt")));

    int i = 0;
    while (records.hasNext()) {
      records.next();
      i++;
    }

    assertEquals(100, i);
    assertFalse(records.hasNext());
  }

  public void testLoadUnmarshallAll() throws Exception {
    Iterator<SimpleRecord> records = unmarshaller.unmarshall(new InputStreamReader(SimpleRecordUnmarshallTest.class
        .getResourceAsStream("simple_load_test.txt")));

    int i = 0;
    while (records.hasNext()) {
      records.next();
      i++;
    }

    assertEquals(100000, i);
    assertFalse(records.hasNext());
  }

  public void testUnmarshall() throws Exception {
    Iterator<SimpleRecord> iter = unmarshaller.unmarshall(new InputStreamReader(SimpleRecordUnmarshallTest.class
        .getResourceAsStream("simple_test.txt")));

    assertTrue(iter.hasNext());
    SimpleRecord record = iter.next();
    assertEquals("FEDERICO            ", record.getName());
    assertEquals("FISSORE             ", record.getSurname());
    assertEquals("ABCDEF88L99H123B", record.getTaxCode());

    Calendar calendar = record.getBirthday();
    assertEquals(1979, calendar.get(Calendar.YEAR));
    assertEquals(4, calendar.get(Calendar.MONTH));
    assertEquals(18, calendar.get(Calendar.DAY_OF_MONTH));

    assertEquals(81, record.getOneInteger());
    assertEquals(1.97, record.getOneFloat(), 0.001);

    assertTrue(iter.hasNext());
    iter.next();
    assertTrue(iter.hasNext());
    iter.next();
    assertTrue(iter.hasNext());
    iter.next();
    assertTrue(iter.hasNext());
    iter.next();
    assertTrue(iter.hasNext());
    iter.next();
    assertTrue(iter.hasNext());
  }
}
