package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Unmarshaller;
import it.assist_si.schemas.jrb.deep_hierarchy.Child;
import it.assist_si.schemas.jrb.deep_hierarchy.Father;
import it.assist_si.schemas.jrb.deep_hierarchy.GrandChild;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import junit.framework.TestCase;

public class DeepHierarchyRecordUnmarshallTest extends TestCase {

  private Unmarshaller<Father> unmarshaller;
  private StringWriter junk;

  public DeepHierarchyRecordUnmarshallTest() throws Exception {
    junk = new StringWriter();
    unmarshaller = new Unmarshaller<Father>(new InputStreamReader(DeepHierarchyRecordUnmarshallTest.class
        .getResourceAsStream("/deepHierarchy.def.xsd")), junk);
  }

  public void testUnmarshall() throws Exception {
    List<Father> records = unmarshaller.unmarshallAll(new InputStreamReader(DeepHierarchyRecordUnmarshallTest.class
        .getResourceAsStream("deepHierarchy_test.txt")));

    assertEquals(3, records.size());

    Father father = records.get(0);
    assertEquals(1, father.getUniqueId());
    assertEquals(0, father.getChildren().size());
    assertEquals(0, father.getGrandChildren().size());

    father = records.get(1);
    assertEquals(2, father.getUniqueId());
    assertEquals(0, father.getChildren().size());
    assertEquals(1, father.getGrandChildren().size());
    GrandChild grandChild = father.getGrandChildren().get(0);
    assertEquals(3, grandChild.getUniqueId());

    father = records.get(2);
    assertEquals(4, father.getUniqueId());
    assertEquals(1, father.getChildren().size());
    assertEquals(0, father.getGrandChildren().size());
    Child child = father.getChildren().get(0);
    assertEquals(5, child.getUniqueId());
    assertEquals(1, child.getGrandChildren().size());
    grandChild = child.getGrandChildren().get(0);
    assertEquals(6, grandChild.getUniqueId());

  }
}
