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
import it.assist_si.schemas.jrb._enum.EnumRecord;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Iterator;

import org.junit.Test;

public class EnumRecordUnmarshallTest {

  private Unmarshaller<EnumRecord> unmarshaller;

  public EnumRecordUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<EnumRecord>(Unmarshaller.class.getClassLoader(), new InputStreamReader(EnumRecordUnmarshallTest.class
        .getResourceAsStream("/enum.def.xsd")));
  }

  @Test
  public void unmarshall() throws Exception {
    Iterator<EnumRecord> iter = unmarshaller.unmarshall(new InputStreamReader(EnumRecordUnmarshallTest.class
        .getResourceAsStream("enum.txt")));

    assertTrue(iter.hasNext());
    EnumRecord record = iter.next();
    assertEquals(MyEnum.ONE, record.getMyEnum());
    assertEquals(BigDecimal.TEN, record.getBigNumber());

    assertTrue(iter.hasNext());
    iter.next();
    assertTrue(iter.hasNext());
    iter.next();
    assertTrue(iter.hasNext());
    iter.next();
    assertTrue(iter.hasNext());
    iter.next();
    assertTrue(iter.hasNext());
    iter.next();
    assertEquals(
        "ONE                 10                                                                              \n",
        unmarshaller.getCurrentJunk());

    assertTrue(iter.hasNext());
    assertEquals(
        "ONE                 10                                                                              \n",
        unmarshaller.getCurrentJunk());
  }

  @Test
  public void unmarshallAll() throws Exception {
    Iterator<EnumRecord> records = unmarshaller.unmarshall(new InputStreamReader(EnumRecordUnmarshallTest.class
        .getResourceAsStream("enum.txt")));

    int i = 0;
    while (records.hasNext()) {
      records.next();
      i++;
    }

    assertEquals(9, i);
    assertFalse(records.hasNext());
    assertEquals("", unmarshaller.getCurrentJunk());
  }
}
