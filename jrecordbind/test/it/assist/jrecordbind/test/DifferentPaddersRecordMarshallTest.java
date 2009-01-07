package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Marshaller;
import it.assist.jrecordbind.padders.SpaceLeftPadder;
import it.assist_si.schemas.jrb.padders.SimpleRecord;

import java.io.InputStreamReader;
import java.io.StringWriter;

import junit.framework.TestCase;

public class DifferentPaddersRecordMarshallTest extends TestCase {

  private Marshaller<SimpleRecord> marshaller;
  private SimpleRecord record;
  private StringWriter stringWriter;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    record = new SimpleRecord();
    record.setName("A NAME");
    record.setOneInteger(81);
    record.setTwoInteger(1934);
    record.setOneFloat(1.97f);

    marshaller = new Marshaller<SimpleRecord>(new InputStreamReader(SimpleRecordMarshallTest.class
        .getResourceAsStream("/differentPadders.def.xsd")), new SpaceLeftPadder());

    stringWriter = new StringWriter();
  }

  public void testMarshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals(
        "              A NAME00000000811934                  197                                              \n",
        stringWriter.toString());
  }
}
