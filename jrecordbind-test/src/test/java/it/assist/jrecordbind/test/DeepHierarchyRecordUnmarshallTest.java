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
import it.assist.jrecordbind.Unmarshaller;
import it.assist_si.schemas.jrb.deep_hierarchy.Child;
import it.assist_si.schemas.jrb.deep_hierarchy.Father;
import it.assist_si.schemas.jrb.deep_hierarchy.GrandChild;

import java.io.InputStreamReader;
import java.util.List;

import org.junit.Test;

public class DeepHierarchyRecordUnmarshallTest {

  private Unmarshaller<Father> unmarshaller;

  public DeepHierarchyRecordUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<Father>(Unmarshaller.class.getClassLoader(), new InputStreamReader(DeepHierarchyRecordUnmarshallTest.class
        .getResourceAsStream("/deepHierarchy.def.xsd")));
  }

  @Test
  public void unmarshall() throws Exception {
    List<Father> records = unmarshaller.unmarshallAll(new InputStreamReader(DeepHierarchyRecordUnmarshallTest.class
        .getResourceAsStream("deepHierarchy.txt")));

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

    assertEquals("", unmarshaller.getCurrentJunk());
  }
}
