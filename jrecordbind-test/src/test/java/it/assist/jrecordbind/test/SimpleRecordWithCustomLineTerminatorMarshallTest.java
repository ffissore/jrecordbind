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
import it.assist_si.schemas.jrb.simplelineterminator.SimpleRecord;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class SimpleRecordWithCustomLineTerminatorMarshallTest {

  private Marshaller<SimpleRecord> marshaller;
  private SimpleRecord record;
  private StringWriter stringWriter;

  @Test
  public void marshallALot() throws Exception {
    for (int i = 0; i < 100000; i++) {
      marshaller.marshall(record, stringWriter);
    }

    assertEquals(10200000, stringWriter.toString().length());
  }

  @Test
  public void marshallMore() throws Exception {
    marshaller.marshall(record, stringWriter);
    marshaller.marshall(record, stringWriter);

    assertEquals(
        "A NAME              A SURNAME           ABCDEF88L99H123B1979051881197Y                              \r\n"
            + "A NAME              A SURNAME           ABCDEF88L99H123B1979051881197Y                              \r\n",
        stringWriter.toString());

    assertEquals(204, stringWriter.toString().length());
  }

  @Test
  public void marshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals(
        "A NAME              A SURNAME           ABCDEF88L99H123B1979051881197Y                              \r\n",
        stringWriter.toString());

    assertEquals(102, stringWriter.toString().length());
  }

  @Test
  public void marshallOneExceedsLength() throws Exception {
    record.setName("1234567890123456789012345");
    marshaller.marshall(record, stringWriter);

    assertEquals(
        "12345678901234567890A SURNAME           ABCDEF88L99H123B1979051881197Y                              \r\n",
        stringWriter.toString());

    assertEquals(102, stringWriter.toString().length());
  }

  @Before
  public void setUp() throws Exception {
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
    record.setOneFloat(1.97f);

    record.setSelected(true);

    marshaller = new Marshaller<SimpleRecord>(new InputStreamReader(
        SimpleRecordWithCustomLineTerminatorMarshallTest.class
            .getResourceAsStream("/simpleWithCustomLineTerminator.def.xsd")));

    stringWriter = new StringWriter();
  }
}
