package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Marshaller;
import it.assist_si.schemas.jrb.hierarchical.ChildRecord;
import it.assist_si.schemas.jrb.hierarchical.MasterRecord;
import it.assist_si.schemas.jrb.hierarchical.RowChildRecord;
import it.assist_si.schemas.jrb.hierarchical.RowRecord;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Calendar;

import junit.framework.TestCase;

public class HierarchicalRecordMarshallTest extends TestCase {

  private Marshaller<MasterRecord> marshaller;
  private MasterRecord record;
  private StringWriter stringWriter;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    record = new MasterRecord();
    record.setRecordId("000");
    record.setName("NAME");
    record.setSurname("SURNAME");
    record.setTaxCode("0123456789");
    RowRecord rowRecord = new RowRecord();
    record.getRows().add(rowRecord);
    rowRecord.setRecordId("A00");
    rowRecord.setName("ROW NAME");
    rowRecord.setSurname("ROW SURNAM");
    RowChildRecord rowChildRecord = new RowChildRecord();
    rowRecord.setChild(rowChildRecord);
    rowChildRecord.setRecordId("A01");
    ChildRecord childRecord = new ChildRecord();
    record.setChild(childRecord);
    childRecord.setRecordId("B01");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.MONTH, 0);
    calendar.set(Calendar.YEAR, 2000);
    childRecord.setWhen(calendar);

    marshaller = new Marshaller<MasterRecord>(new InputStreamReader(HierarchicalRecordMarshallTest.class
        .getResourceAsStream("/hierarchical.def.xsd")));

    stringWriter = new StringWriter();
  }

  public void testMarshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals("000|NAME      |SURNAME   |0123456789 \n" + "A00|ROW NAME  |ROW SURNAM            \n"
        + "A01                                  \n" + "B01|20000101                         \n", stringWriter
        .toString());
  }

  public void testMarshallALot() throws Exception {
    for (int i = 0; i < 1000; i++) {
      marshaller.marshall(record, stringWriter);
    }

    assertEquals(152000, stringWriter.toString().length());
  }

}
