package it.assist.jarb.test;

import it.assist.jarb.SimpleRecord;
import it.assist.jarb.Unmarshaller;

import java.util.Calendar;
import java.util.Iterator;

import junit.framework.TestCase;

public class SimpleRecordTest extends TestCase {

  private Unmarshaller<SimpleRecord> unmarshaller;

  public SimpleRecordTest() throws Exception {
    unmarshaller = new Unmarshaller<SimpleRecord>(SimpleRecordTest.class.getResourceAsStream("/simple.def.properties"));
  }

  public void testUnmarshallAll() throws Exception {
    Iterator<SimpleRecord> records = unmarshaller.unmarshall(SimpleRecordTest.class
        .getResourceAsStream("simple_test.txt"));

    int i = 0;
    while (records.hasNext()) {
      records.next();
      i++;
    }

    assertEquals(100000, i);
  }

  public void testUnmarshall() throws Exception {
    Iterator<SimpleRecord> iter = unmarshaller
        .unmarshall(SimpleRecordTest.class.getResourceAsStream("simple_test.txt"));

    assertTrue(iter.hasNext());
    SimpleRecord record = iter.next();
    assertEquals("FEDERICO            ", record.getNome());
    assertEquals("FISSORE             ", record.getCognome());
    assertEquals("FSSFRC79E18L219V", record.getCodiceFiscale());

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(record.getDataNascita());
    assertEquals(1979, calendar.get(Calendar.YEAR));
    assertEquals(4, calendar.get(Calendar.MONTH));
    assertEquals(18, calendar.get(Calendar.DAY_OF_MONTH));

    assertEquals(81, record.getIntero().intValue());
    assertEquals(1.97, record.getVirgola().floatValue(), 0.001);

    assertTrue(iter.hasNext());
    assertTrue(iter.hasNext());
    assertTrue(iter.hasNext());
    assertTrue(iter.hasNext());
    assertTrue(iter.hasNext());
  }
}
