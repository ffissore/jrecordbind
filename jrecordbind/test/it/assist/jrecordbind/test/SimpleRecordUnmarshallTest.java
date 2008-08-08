package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Unmarshaller;

import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Iterator;

import junit.framework.TestCase;

public class SimpleRecordUnmarshallTest extends TestCase {

  private Unmarshaller unmarshaller;

  public SimpleRecordUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller(new InputStreamReader(SimpleRecordUnmarshallTest.class
        .getResourceAsStream("/simple.def.properties")));
  }

  public void testUnmarshallAll() throws Exception {
    Iterator records = unmarshaller.unmarshall(new InputStreamReader(SimpleRecordUnmarshallTest.class
        .getResourceAsStream("simple_test.txt")));

    int i = 0;
    while (records.hasNext()) {
      records.next();
      i++;
    }

    assertEquals(100000, i);
  }

  public void testUnmarshall() throws Exception {
    Iterator iter = unmarshaller.unmarshall(new InputStreamReader(SimpleRecordUnmarshallTest.class
        .getResourceAsStream("simple_test.txt")));

    assertTrue(iter.hasNext());
    SimpleRecord record = (SimpleRecord) iter.next();
    assertEquals("FEDERICO            ", record.getName());
    assertEquals("FISSORE             ", record.getSurname());
    assertEquals("ABCDEF88L99H123B", record.getTaxCode());

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(record.getBirthday());
    assertEquals(1979, calendar.get(Calendar.YEAR));
    assertEquals(4, calendar.get(Calendar.MONTH));
    assertEquals(18, calendar.get(Calendar.DAY_OF_MONTH));

    assertEquals(81, record.getOneInteger().intValue());
    assertEquals(1.97, record.getOneFloat().floatValue(), 0.001);

    assertTrue(iter.hasNext());
    assertTrue(iter.hasNext());
    assertTrue(iter.hasNext());
    assertTrue(iter.hasNext());
    assertTrue(iter.hasNext());
  }
}
