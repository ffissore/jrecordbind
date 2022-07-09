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
import org.jrecordbind.schemas.jrb.simple.SimpleRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class SimpleMarshallTest {

  private Marshaller<SimpleRecord> marshaller;
  private SimpleRecord record;
  private StringWriter stringWriter;

  @Before
  public void setUp() {
    record = new SimpleRecord();
    record.setName("A NAME");
    record.setSurname("A SURNAME");
    record.setTaxCode("ABCDEF88L99H123B");

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 1979);
    calendar.set(Calendar.MONTH, 4);
    calendar.set(Calendar.DAY_OF_MONTH, 18);
    record.setBirthday(calendar);

    record.setOneInteger(81);
    record.setTwoInteger(1804);
    record.setOneFloat(1.97f);

    record.setSelected(true);

    record.setThreeInteger(24);

    marshaller = new Marshaller<>(Utils.loadDefinition("/record_definitions/simple.def.xsd"));

    stringWriter = new StringWriter();
  }

  @Test
  public void marshallALot() {
    for (int i = 0; i < 100000; i++) {
      marshaller.marshall(record, stringWriter);
    }

    assertEquals(12100000, stringWriter.toString().length());
  }

  @Test
  public void marshallMore() {
    marshaller.marshall(record, stringWriter);
    marshaller.marshall(record, stringWriter);

    assertEquals(
      "A NAME              A SURNAME           ABCDEF88L99H123B1979051800000000811804                  197Y024                 \n"
        + "A NAME              A SURNAME           ABCDEF88L99H123B1979051800000000811804                  197Y024                 \n",
      stringWriter.toString());

    assertEquals(242, stringWriter.toString().length());
  }

  @Test
  public void marshallOne() {
    marshaller.marshall(record, stringWriter);

    assertEquals(
      "A NAME              A SURNAME           ABCDEF88L99H123B1979051800000000811804                  197Y024                 \n",
      stringWriter.toString());

    assertEquals(121, stringWriter.toString().length());
  }

  @Test
  public void marshallOneExceedsLength() {
    record.setName("1234567890123456789012345");
    marshaller.marshall(record, stringWriter);

    assertEquals(
      "12345678901234567890A SURNAME           ABCDEF88L99H123B1979051800000000811804                  197Y024                 \n",
      stringWriter.toString());

    assertEquals(121, stringWriter.toString().length());
  }
}
