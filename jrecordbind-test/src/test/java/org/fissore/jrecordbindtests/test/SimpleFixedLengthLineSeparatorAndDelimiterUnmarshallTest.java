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
import org.jrecordbind.schemas.jrb.simple_fixed_length_line_separator_and_delimiter.SimpleRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleFixedLengthLineSeparatorAndDelimiterUnmarshallTest {

  private Unmarshaller<SimpleRecord> unmarshaller;

  @Before
  public void setUp() {
    unmarshaller = new Unmarshaller<>(Utils.loadDefinition("/record_definitions/simple_fixed_length_line_separator_and_delimiter.def.xsd"));
  }

  @Test
  public void unmarshall() {
    Iterator<SimpleRecord> iter = unmarshaller.unmarshallToIterator(Utils.openStream("/record_files/simple_fixed_length_line_separator_and_delimiter.txt"));

    assertTrue(iter.hasNext());
    SimpleRecord record = iter.next();
    assertEquals("JOHN", record.getName());
    assertEquals("SMITH", record.getSurname());
    assertEquals("ABCDEF88L99H123B", record.getTaxCode());

    Calendar calendar = record.getBirthday();
    assertEquals(1979, calendar.get(Calendar.YEAR));
    assertEquals(4, calendar.get(Calendar.MONTH));
    assertEquals(18, calendar.get(Calendar.DAY_OF_MONTH));

    assertEquals(81, record.getOneInteger());
    assertEquals(1.97, record.getOneFloat(), 0.001);

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
      "JOHN                |SMITH               |ABCDEF88L99H123B|19790518|81|197|Y                        \n",
      unmarshaller.getCurrentJunk());

    assertTrue(iter.hasNext());
    assertEquals(
      "JOHN                |SMITH               |ABCDEF88L99H123B|19790518|81|197|Y                        \n",
      unmarshaller.getCurrentJunk());
  }
}
