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

import java.io.InputStreamReader;
import java.util.Iterator;

import org.junit.Test;

import com.test.batch.local.ChildRecordA;
import com.test.batch.local.ChildRecordB;
import com.test.batch.local.ChildRecordC;
import com.test.batch.local.MasterRecord;

public class Issue19UnmarshallTest {

  private Unmarshaller<MasterRecord> unmarshaller;

  public Issue19UnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<MasterRecord>(new InputStreamReader(Issue19UnmarshallTest.class
        .getResourceAsStream("/issue19.xsd")));
  }

  @Test
  public void unmarshall() throws Exception {
    Iterator<MasterRecord> iter = unmarshaller.unmarshall(new InputStreamReader(Issue19UnmarshallTest.class
        .getResourceAsStream("issue19.txt")));

    assertTrue(iter.hasNext());
    MasterRecord record = iter.next();
    assertEquals("BATCH", record.getRecId());
    assertEquals(" ", record.getFiller());
    assertEquals("20401", record.getOperatorID());
    assertEquals("002025 A          ", record.getBatchNumber());

    assertEquals(1, record.getChildA().size());

    ChildRecordA childRecordA = record.getChildA().get(0);
    assertEquals("20102740000013", childRecordA.getDcn());
    assertEquals("A", childRecordA.getRecId());
    assertEquals("0000005", childRecordA.getOther());
    assertEquals("8622   ", childRecordA.getOther2());

    assertEquals(3, childRecordA.getChildB().size());

    ChildRecordB childRecordB = childRecordA.getChildB().get(0);
    assertEquals("20102740000013", childRecordB.getDcn());
    assertEquals("B", childRecordB.getRecId());
    assertEquals("00000058622   ", childRecordB.getOther());
    childRecordB = childRecordA.getChildB().get(1);
    assertEquals("20102740000013", childRecordB.getDcn());
    assertEquals("B", childRecordB.getRecId());
    assertEquals("0 1INDIYAD/I  ", childRecordB.getOther());
    childRecordB = childRecordA.getChildB().get(2);
    assertEquals("20102740000013", childRecordB.getDcn());
    assertEquals("B", childRecordB.getRecId());
    assertEquals("0 2ICMANE TI  ", childRecordB.getOther());

    assertEquals(5, childRecordA.getChildC().size());

    ChildRecordC childRecordC = childRecordA.getChildC().get(0);
    assertEquals("20102740000013", childRecordC.getDcn());
    assertEquals("C", childRecordC.getRecId());
    assertEquals("0 2P & W TIL  ", childRecordC.getOther());
    childRecordC = childRecordA.getChildC().get(1);
    assertEquals("20102740000013", childRecordC.getDcn());
    assertEquals("C", childRecordC.getRecId());
    assertEquals("0 2P & W TIL  ", childRecordC.getOther());
    childRecordC = childRecordA.getChildC().get(2);
    assertEquals("20102740000013", childRecordC.getDcn());
    assertEquals("C", childRecordC.getRecId());
    assertEquals("0 2GIDS SLYB  ", childRecordC.getOther());
    childRecordC = childRecordA.getChildC().get(3);
    assertEquals("20102740000013", childRecordC.getDcn());
    assertEquals("C", childRecordC.getRecId());
    assertEquals("0 2DIKA TILS  ", childRecordC.getOther());
    childRecordC = childRecordA.getChildC().get(4);
    assertEquals("20102740000013", childRecordC.getDcn());
    assertEquals("C", childRecordC.getRecId());
    assertEquals("0 2DIKA TILS  ", childRecordC.getOther());

    assertEquals("", unmarshaller.getCurrentJunk());
  }
}
