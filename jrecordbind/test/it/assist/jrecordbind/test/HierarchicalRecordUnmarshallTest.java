package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Unmarshaller;
import it.assist_si.schemas.jrb.hierarchical.ChildRecord;
import it.assist_si.schemas.jrb.hierarchical.MasterRecord;
import it.assist_si.schemas.jrb.hierarchical.RowRecord;

import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Iterator;

import junit.framework.TestCase;

public class HierarchicalRecordUnmarshallTest extends TestCase {

  private Unmarshaller<MasterRecord> unmarshaller;

  public HierarchicalRecordUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<MasterRecord>(new InputStreamReader(HierarchicalRecordUnmarshallTest.class
        .getResourceAsStream("/hierarchical.def.xsd")));
  }

  public void testUnmarshall() throws Exception {
    Iterator<MasterRecord> iter = unmarshaller.unmarshall(new InputStreamReader(HierarchicalRecordUnmarshallTest.class
        .getResourceAsStream("hierarchical_test.txt")));

    assertTrue(iter.hasNext());
    MasterRecord record = iter.next();
    assertEquals("000", record.getRecordId());
    assertEquals("name      ", record.getName());
    assertEquals("surname   ", record.getSurname());
    assertEquals("0123456789", record.getTaxCode());

    assertEquals(2, record.getRows().size());

    RowRecord row = record.getRows().get(0);
    assertEquals("A00", row.getRecordId());
    assertEquals("0subr name", row.getName());
    assertEquals("0subr surn", row.getSurname());
    row = record.getRows().get(1);
    assertEquals("A00", row.getRecordId());
    assertEquals("1subr name", row.getName());
    assertEquals("1subr surn", row.getSurname());

    ChildRecord child = record.getChild();
    assertEquals("B01", child.getRecordId());
    Calendar calendar = child.getWhen();
    assertEquals(2008, calendar.get(Calendar.YEAR));
    assertEquals(11, calendar.get(Calendar.MONTH));
    assertEquals(20, calendar.get(Calendar.DAY_OF_MONTH));

    assertTrue(iter.hasNext());
    iter.next();
    assertFalse(iter.hasNext());
  }
}
