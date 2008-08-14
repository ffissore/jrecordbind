package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Marshaller;

import java.io.InputStreamReader;
import java.io.StringWriter;

import junit.framework.TestCase;

public class SeparatorRecordMarshallTest extends TestCase {

  private Marshaller marshaller;
  private SimpleRecord record;
  private StringWriter stringWriter;

  protected void setUp() throws Exception {
    super.setUp();

    record = new SimpleRecord();
    record.setName("FEDERICO");
    record.setSurname("FISSORE");
    record.setTaxCode("0123456789");

    marshaller = new Marshaller(new InputStreamReader(SeparatorRecordMarshallTest.class
        .getResourceAsStream("/separator.def.properties")));

    stringWriter = new StringWriter();
  }

  public void testMarshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals("FEDERICO  |FISSORE   |0123456789\n", stringWriter.toString());
  }
}
