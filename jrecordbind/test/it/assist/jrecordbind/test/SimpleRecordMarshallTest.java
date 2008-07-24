package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Marshaller;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Calendar;

import junit.framework.TestCase;

public class SimpleRecordMarshallTest extends TestCase {

  private Marshaller<SimpleRecord> marshaller;
  private SimpleRecord record;
  private StringWriter stringWriter;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    record = new SimpleRecord();
    record.setName("FEDERICO");
    record.setSurname("FISSORE");
    record.setTaxCode("FSSFRC79E18L219V");

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 1979);
    calendar.set(Calendar.MONTH, 4);
    calendar.set(Calendar.DAY_OF_MONTH, 18);
    record.setBirthday(calendar.getTime());

    record.setOneInteger(Integer.valueOf(81));
    record.setOneFloat(Float.valueOf(1.97f));

    marshaller = new Marshaller<SimpleRecord>(new InputStreamReader(SimpleRecordMarshallTest.class
        .getResourceAsStream("/simple.def.properties")));

    stringWriter = new StringWriter();
  }

  public void testMarshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals("FEDERICO            FISSORE             FSSFRC79E18L219V1979051881197\n", stringWriter.toString());
  }

  public void testMarshallMore() throws Exception {
    marshaller.marshall(record, stringWriter);
    marshaller.marshall(record, stringWriter);

    assertEquals("FEDERICO            FISSORE             FSSFRC79E18L219V1979051881197\n"
        + "FEDERICO            FISSORE             FSSFRC79E18L219V1979051881197\n", stringWriter.toString());
  }
}
