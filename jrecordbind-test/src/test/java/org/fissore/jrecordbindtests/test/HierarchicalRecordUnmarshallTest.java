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
import org.jrecordbind.schemas.jrb.hierarchical.ChildRecord;
import org.jrecordbind.schemas.jrb.hierarchical.MasterRecord;
import org.jrecordbind.schemas.jrb.hierarchical.RowRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Iterator;

import static org.junit.Assert.*;

public class HierarchicalRecordUnmarshallTest {

  private Unmarshaller<MasterRecord> unmarshaller;

  @Before
  public void setUp() {
    unmarshaller = new Unmarshaller<>(Utils.loadDefinition("/record_definitions/hierarchical.def.xsd"));
  }

  @Test
  public void unmarshall() {
    Iterator<MasterRecord> iter = unmarshaller.unmarshallToIterator(Utils.openStream("/record_files/hierarchical.txt"));

    assertTrue(iter.hasNext());
    MasterRecord record = iter.next();
    assertEquals("000", record.getRecordId());
    assertEquals("name", record.getName());
    assertEquals("surname", record.getSurname());
    assertEquals("0123456789", record.getTaxCode());

    assertEquals(2, record.getRows().size());

    RowRecord row = record.getRows().get(0);
    assertEquals("A00", row.getRecordId());
    assertEquals("0subr name", row.getName());
    assertEquals("0subr surn", row.getSurname());
    assertEquals("A01", row.getChild().getRecordId());
    row = record.getRows().get(1);
    assertEquals("A00", row.getRecordId());
    assertEquals("1subr name", row.getName());
    assertEquals("1subr surn", row.getSurname());
    assertEquals("A01", row.getChild().getRecordId());

    ChildRecord child = record.getChild();
    assertEquals("B01", child.getRecordId());
    Calendar calendar = child.getWhen();
    assertEquals(2008, calendar.get(Calendar.YEAR));
    assertEquals(11, calendar.get(Calendar.MONTH));
    assertEquals(20, calendar.get(Calendar.DAY_OF_MONTH));

    assertTrue(iter.hasNext());
    iter.next();
    assertFalse(iter.hasNext());

    assertEquals("", unmarshaller.getCurrentJunk());
  }
}
