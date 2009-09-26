/*
 * JRecordBind, fixed-length file (un)marshaller
 * Copyright 2009, Assist s.r.l., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package it.assist.jrecordbind.test;

import static org.junit.Assert.*;
import it.assist.jrecordbind.Marshaller;
import it.assist_si.schemas.jrb.deep_hierarchy.Child;
import it.assist_si.schemas.jrb.deep_hierarchy.Father;
import it.assist_si.schemas.jrb.deep_hierarchy.GrandChild;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DeepHierarchyRecordMarshallTest {

  private Marshaller<Father> marshaller;
  private List<Father> records;
  private StringWriter stringWriter;

  @Test
  public void marshallALot() throws Exception {
    for (int i = 0; i < 1000; i++) {
      for (Father record : records) {
        marshaller.marshall(record, stringWriter);
      }
    }

    assertEquals(48000, stringWriter.toString().length());
  }

  @Test
  public void marshallOne() throws Exception {
    for (Father record : records) {
      marshaller.marshall(record, stringWriter);
    }

    assertEquals("000|001\n" + "000|002\n" + "002|003\n" + "000|004\n" + "001|005\n" + "002|006\n", stringWriter
        .toString());
  }

  @Before
  public void setUp() throws Exception {
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
        .getResourceAsStream("/deepHierarchy.def.xsd")));

    stringWriter = new StringWriter();
  }

}
