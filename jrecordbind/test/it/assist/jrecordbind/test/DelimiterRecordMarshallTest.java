package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Marshaller;
import it.assist_si.schemas.jrb.simple.SimpleRecord;

import java.io.StringWriter;

import junit.framework.TestCase;

public class DelimiterRecordMarshallTest extends TestCase {

  private Marshaller<SimpleRecord> marshaller;
  private SimpleRecord record;
  private StringWriter stringWriter;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    record = new SimpleRecord();
    record.setName("FEDERICO");
    record.setSurname("FISSORE");
    record.setTaxCode("0123456789");

    marshaller = new Marshaller<SimpleRecord>(DelimiterRecordMarshallTest.class
        .getResourceAsStream("/delimiter.def.xsd"));

    stringWriter = new StringWriter();
  }

  public void testMarshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals("FEDERICO  |FISSORE   |0123456789\n", stringWriter.toString());
  }
}
