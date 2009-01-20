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

import it.assist.jrecordbind.Marshaller;
import it.assist_si.schemas.jrb.multi_row.MultiRowRecord;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Calendar;

import junit.framework.TestCase;

public class MultiRowRecordMarshallTest extends TestCase {

  private Marshaller<MultiRowRecord> marshaller;
  private MultiRowRecord record;
  private StringWriter stringWriter;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    record = new MultiRowRecord();
    record.setName("JOHN");
    record.setSurname("SMITH");
    record.setTaxCode("ABCDEF88L99H123B");

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 1979);
    calendar.set(Calendar.MONTH, 4);
    calendar.set(Calendar.DAY_OF_MONTH, 18);
    record.setBirthday(calendar);

    record.setOneInteger(81);
    record.setOneFloat(1.97f);

    record.setFatherName("ADAM SMITH");
    record.setMotherName("DEBRA MORGAN");

    marshaller = new Marshaller<MultiRowRecord>(new InputStreamReader(MultiRowRecordMarshallTest.class
        .getResourceAsStream("/multi-row.def.xsd")));

    stringWriter = new StringWriter();
  }

  public void testMarshallALot() throws Exception {
    for (int i = 0; i < 1000; i++) {
      marshaller.marshall(record, stringWriter);
    }

    assertEquals(142000, stringWriter.toString().length());
  }

  public void testMarshallMore() throws Exception {
    marshaller.marshall(record, stringWriter);
    marshaller.marshall(record, stringWriter);

    assertEquals("JOHN                SMITH               ABCDEF88L99H123B1979051881197 \n"
        + "ADAM SMITH          DEBRA MORGAN                                      \n"
        + "JOHN                SMITH               ABCDEF88L99H123B1979051881197 \n"
        + "ADAM SMITH          DEBRA MORGAN                                      \n", stringWriter.toString());
  }

  public void testMarshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals("JOHN                SMITH               ABCDEF88L99H123B1979051881197 \n"
        + "ADAM SMITH          DEBRA MORGAN                                      \n", stringWriter.toString());
  }
}
