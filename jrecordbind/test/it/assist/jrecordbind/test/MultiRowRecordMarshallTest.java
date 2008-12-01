package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Marshaller;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Calendar;

import junit.framework.TestCase;

public class MultiRowRecordMarshallTest extends TestCase {

  private Marshaller marshaller;
  private MultiRowRecord record;
  private StringWriter stringWriter;

  protected void setUp() throws Exception {
    super.setUp();

    record = new MultiRowRecord();
    record.setName("JOHN");
    record.setSurname("SMITH");
    record.setTaxCode("ABCDEF88L99H123B");

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 1979);
    calendar.set(Calendar.MONTH, 4);
    calendar.set(Calendar.DAY_OF_MONTH, 18);
    record.setBirthday(calendar.getTime());

    record.setOneInteger(new Integer(81));
    record.setOneFloat(new Float(1.97f));

    record.setFatherName("ADAM SMITH");
    record.setMotherName("DEBRA MORGAN");

    marshaller = new Marshaller(new InputStreamReader(MultiRowRecordMarshallTest.class
        .getResourceAsStream("/multi-row.def.properties")));

    stringWriter = new StringWriter();
  }

  public void testMarshallALot() throws Exception {
    for (int i = 0; i < 1000; i++) {
      marshaller.marshall(record, stringWriter);
    }

    assertEquals(111000, stringWriter.toString().length());
  }

  public void testMarshallMore() throws Exception {
    marshaller.marshall(record, stringWriter);
    marshaller.marshall(record, stringWriter);

    assertEquals("JOHN                SMITH               ABCDEF88L99H123B1979051881197\n"
        + "ADAM SMITH          DEBRA MORGAN        \n"
        + "JOHN                SMITH               ABCDEF88L99H123B1979051881197\n"
        + "ADAM SMITH          DEBRA MORGAN        \n", stringWriter.toString());
  }

  public void testMarshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals("JOHN                SMITH               ABCDEF88L99H123B1979051881197\n"
        + "ADAM SMITH          DEBRA MORGAN        \n", stringWriter.toString());
  }
}
