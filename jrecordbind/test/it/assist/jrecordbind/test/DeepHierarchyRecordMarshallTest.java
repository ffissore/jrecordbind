package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Marshaller;
import it.assist.jrecordbind.Padder;
import it.assist_si.schemas.jrb.deep_hierarchy.Child;
import it.assist_si.schemas.jrb.deep_hierarchy.Father;
import it.assist_si.schemas.jrb.deep_hierarchy.GrandChild;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class DeepHierarchyRecordMarshallTest extends TestCase {

  private Marshaller<Father> marshaller;
  private List<Father> records;
  private StringWriter stringWriter;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    records = new LinkedList<Father>();

    Father father = new Father();
    father.setRecordId("000");
    father.setUniqueId(1);
    records.add(father);

    father = new Father();
    father.setRecordId("000");
    father.setUniqueId(2);
    GrandChild grandChild = new GrandChild();
    grandChild.setRecordId("002");
    grandChild.setUniqueId(3);
    father.getGrandChildren().add(grandChild);
    records.add(father);

    father = new Father();
    father.setRecordId("000");
    father.setUniqueId(4);
    Child child = new Child();
    child.setRecordId("001");
    child.setUniqueId(5);
    father.getChildren().add(child);
    grandChild = new GrandChild();
    grandChild.setRecordId("002");
    grandChild.setUniqueId(6);
    child.getGrandChildren().add(grandChild);
    records.add(father);

    marshaller = new Marshaller<Father>(new InputStreamReader(DeepHierarchyRecordMarshallTest.class
        .getResourceAsStream("/deepHierarchy.def.xsd")), new Padder() {

      @Override
      public String pad(String string, int length) {
        StringBuilder sb = new StringBuilder(string);
        while (sb.length() < length) {
          sb.insert(0, "0");
        }
        return sb.toString();
      }

    });

    stringWriter = new StringWriter();
  }

  public void testMarshallOne() throws Exception {
    for (Father record : records) {
      marshaller.marshall(record, stringWriter);
    }

    assertEquals("000|001\n" + "000|002\n" + "002|003\n" + "000|004\n" + "001|005\n" + "002|006\n", stringWriter
        .toString());
  }

  public void testMarshallALot() throws Exception {
    for (int i = 0; i < 1000; i++) {
      for (Father record : records) {
        marshaller.marshall(record, stringWriter);
      }
    }

    assertEquals(48000, stringWriter.toString().length());
  }

}
