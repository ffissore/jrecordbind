package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Unmarshaller;

import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Iterator;

import junit.framework.TestCase;

public class MultiRowRecordUnmarshallTest extends TestCase {

  private Unmarshaller unmarshaller;

  public MultiRowRecordUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller(new InputStreamReader(MultiRowRecordUnmarshallTest.class
        .getResourceAsStream("/multi-row.def.properties")));
  }

  public void testUnmarshall() throws Exception {
    Iterator iter = unmarshaller.unmarshall(new InputStreamReader(MultiRowRecordUnmarshallTest.class
        .getResourceAsStream("multi-row_test.txt")));

    assertTrue(iter.hasNext());
    MultiRowRecord record = (MultiRowRecord) iter.next();
    assertEquals("JOHN                ", record.getName());
    assertEquals("SMITH               ", record.getSurname());
    assertEquals("ABCDEF88L99H123B", record.getTaxCode());
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(record.getBirthday());
    assertEquals(1979, calendar.get(Calendar.YEAR));
    assertEquals(4, calendar.get(Calendar.MONTH));
    assertEquals(18, calendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(81, record.getOneInteger().intValue());
    assertEquals(1.97, record.getOneFloat().floatValue(), 0.001);
    assertEquals("ADAM SMITH          ", record.getFatherName());
    assertEquals("DEBRA MORGAN        ", record.getMotherName());

    assertTrue(iter.hasNext());
    record = (MultiRowRecord) iter.next();
    assertEquals("EDWARD              ", record.getName());
    assertEquals("COLEEN              ", record.getSurname());
    assertEquals("ABCDEF88L99H123B", record.getTaxCode());
    calendar = Calendar.getInstance();
    calendar.setTime(record.getBirthday());
    assertEquals(1979, calendar.get(Calendar.YEAR));
    assertEquals(4, calendar.get(Calendar.MONTH));
    assertEquals(18, calendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(81, record.getOneInteger().intValue());
    assertEquals(1.97, record.getOneFloat().floatValue(), 0.001);
    assertEquals("SAMUEL COLEEN       ", record.getFatherName());
    assertEquals("SAMANTHA PAGE       ", record.getMotherName());

    assertFalse(iter.hasNext());

  }
}
