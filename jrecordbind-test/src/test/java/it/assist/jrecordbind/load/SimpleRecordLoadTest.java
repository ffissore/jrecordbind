package it.assist.jrecordbind.load;

import it.assist.jrecordbind.Marshaller;
import it.assist.jrecordbind.Unmarshaller;
import it.assist.jrecordbind.test.SimpleRecordMarshallTest;
import it.assist_si.schemas.jrb.simple.SimpleRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Iterator;

import junit.framework.TestCase;

public class SimpleRecordLoadTest extends TestCase {

  private File file;
  private Marshaller<SimpleRecord> marshaller;
  private SimpleRecord record;
  private Unmarshaller<SimpleRecord> unmarshaller;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    record = new SimpleRecord();
    record.setName("A NAME");
    record.setSurname("A SURNAME");
    record.setTaxCode("ABCDEF88L99H123B");

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 1979);
    calendar.set(Calendar.MONTH, 4);
    calendar.set(Calendar.DAY_OF_MONTH, 18);
    record.setBirthday(calendar);

    record.setOneInteger(81);
    record.setOneFloat(1.97f);

    record.setSelected(true);

    marshaller = new Marshaller<SimpleRecord>(new InputStreamReader(SimpleRecordMarshallTest.class
        .getResourceAsStream("/simple.def.xsd")));

    unmarshaller = new Unmarshaller<SimpleRecord>(new InputStreamReader(SimpleRecordMarshallTest.class
        .getResourceAsStream("/simple.def.xsd")));

    file = File.createTempFile("jrecord_bind_simple_record", "test");
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();

    file.delete();
  }

  public void testLoadMarshall() throws Exception {
    FileWriter writer = new FileWriter(file);
    for (int i = 0; i < 1000000; i++) {
      marshaller.marshall(record, writer);
    }
    writer.close();
  }

  public void testLoadRoundtrip() throws Exception {
    FileWriter writer = new FileWriter(file);
    for (int i = 0; i < 1000000; i++) {
      marshaller.marshall(record, writer);
    }
    writer.close();

    FileReader reader = new FileReader(file);
    Iterator<SimpleRecord> iterator = unmarshaller.unmarshall(reader);
    while (iterator.hasNext()) {
      iterator.next();
    }
    reader.close();

    assertEquals("", unmarshaller.getCurrentJunk());
  }

}
