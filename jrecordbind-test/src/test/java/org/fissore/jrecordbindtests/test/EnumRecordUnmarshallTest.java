/*
 * JRecordBind, fixed-length file (un)marshaller
 * Copyright 2019, Federico Fissore, and individual contributors. See
 * AUTHORS.txt in the distribution for a full listing of individual
 * contributors.
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

package org.fissore.jrecordbindtests.test;

import org.fissore.jrecordbind.Unmarshaller;
import org.fissore.jrecordbindtests.Utils;
import org.jrecordbind.schemas.jrb._enum.CarType;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Iterator;

import static org.junit.Assert.*;

public class EnumRecordUnmarshallTest {

  private Unmarshaller<TestTypes.MyEnumRecord> unmarshaller;

  @Before
  public void setUp() {
    unmarshaller = new Unmarshaller<>(Utils.loadDefinition("/record_definitions/enum.def.xsd"));
  }

  @Test
  public void unmarshall() {
    Iterator<TestTypes.MyEnumRecord> iter = unmarshaller.unmarshallToIterator(Utils.openStream("/record_files/enum.txt"));

    assertTrue(iter.hasNext());
    TestTypes.MyEnumRecord record = iter.next();
    assertEquals(TestTypes.MyEnum.ONE, record.getMyEnumConverted());
    assertEquals(BigDecimal.TEN, record.getBigNumber());
    assertEquals(CarType.TESLA, record.getMyCar());
    assertEquals(TestTypes.MyOtherEnum.THAT, record.getMyOtherEnum());

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
      "ONE                 10                  Nissan    THAT                                              \n",
      unmarshaller.getCurrentJunk());

    assertTrue(iter.hasNext());
    assertEquals(
      "ONE                 10                  Tesla     THAT                                              \n",
      unmarshaller.getCurrentJunk());
  }

  @Test
  public void unmarshallAll() {
    Iterator<TestTypes.MyEnumRecord> records = unmarshaller.unmarshallToIterator(Utils.openStream("/record_files/enum.txt"));

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
