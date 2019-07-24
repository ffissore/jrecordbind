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
import org.jrecordbind.schemas.jrb.hierarchical.ChildRecord;
import org.jrecordbind.schemas.jrb.hierarchical.MasterRecord;
import org.jrecordbind.schemas.jrb.hierarchical.RowChildRecord;
import org.jrecordbind.schemas.jrb.hierarchical.RowRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class HierarchicalRecordMarshallTest {

  private Marshaller<MasterRecord> marshaller;
  private MasterRecord record;
  private StringWriter stringWriter;

  @Before
  public void setUp() {
    record = new MasterRecord();
    record.setRecordId("000");
    record.setName("NAME");
    record.setSurname("SURNAME");
    record.setTaxCode("0123456789");
    RowRecord rowRecord = new RowRecord();
    record.getRows().add(rowRecord);
    rowRecord.setRecordId("A00");
    rowRecord.setName("ROW NAME");
    rowRecord.setSurname("ROW SURNAM");
    RowChildRecord rowChildRecord = new RowChildRecord();
    rowRecord.setChild(rowChildRecord);
    rowChildRecord.setRecordId("A01");
    ChildRecord childRecord = new ChildRecord();
    record.setChild(childRecord);
    childRecord.setRecordId("B01");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.MONTH, 0);
    calendar.set(Calendar.YEAR, 2000);
    childRecord.setWhen(calendar);

    marshaller = new Marshaller<>(Utils.loadDefinition("/record_definitions/hierarchical.def.xsd"));

    stringWriter = new StringWriter();
  }

  @Test
  public void marshallALot() throws Exception {
    for (int i = 0; i < 1000; i++) {
      marshaller.marshall(record, stringWriter);
    }

    assertEquals(148000, stringWriter.toString().length());
  }

  @Test
  public void marshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals("000|NAME      |SURNAME   |0123456789\n" + "A00|ROW NAME  |ROW SURNAM           \n"
      + "A01                                 \n" + "B01|20000101                        \n", stringWriter.toString());
    assertEquals(148, stringWriter.toString().length());
  }

}
