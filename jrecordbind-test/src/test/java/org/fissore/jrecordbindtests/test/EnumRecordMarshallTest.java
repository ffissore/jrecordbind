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

import org.fissore.jrecordbind.Marshaller;
import org.fissore.jrecordbindtests.Utils;
import org.jrecordbind.schemas.jrb._enum.CarType;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class EnumRecordMarshallTest {

  private Marshaller<TestTypes.MyEnumRecord> marshaller;
  private TestTypes.MyEnumRecord record;
  private StringWriter stringWriter;

  @Before
  public void setUp() {
    record = new TestTypes.MyEnumRecord();
    record.setMyEnumConverted(TestTypes.MyEnum.ONE);
    record.setBigNumber(BigDecimal.TEN);
    record.setMyCar(CarType.TESLA);

    marshaller = new Marshaller<>(Utils.loadDefinition("/record_definitions/enum.def.xsd"));

    stringWriter = new StringWriter();
  }

  @Test
  public void marshallALot() {
    for (int i = 0; i < 100000; i++) {
      marshaller.marshall(record, stringWriter);
    }

    assertEquals(10100000, stringWriter.toString().length());
  }

  @Test
  public void marshallMore() {
    marshaller.marshall(record, stringWriter);
    marshaller.marshall(record, stringWriter);

    assertEquals(
      "ONE                 10                  TESLA                                                       \n"
        + "ONE                 10                  TESLA                                                       \n",
      stringWriter.toString());

    assertEquals(202, stringWriter.toString().length());
  }

  @Test
  public void marshallOne() {
    marshaller.marshall(record, stringWriter);

    assertEquals(
      "ONE                 10                  TESLA                                                       \n",
      stringWriter.toString());

    assertEquals(101, stringWriter.toString().length());
  }

  @Test
  public void marshallOneExceedsLength() {
    record.setBigNumber(BigDecimal.ZERO);
    marshaller.marshall(record, stringWriter);

    assertEquals(
      "ONE                 0                   TESLA                                                       \n",
      stringWriter.toString());

    assertEquals(101, stringWriter.toString().length());
  }
}
