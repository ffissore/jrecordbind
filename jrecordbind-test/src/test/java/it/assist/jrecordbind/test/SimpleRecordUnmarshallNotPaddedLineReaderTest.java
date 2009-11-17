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
import it.assist.jrecordbind.LineReader;
import it.assist.jrecordbind.Padder;
import it.assist.jrecordbind.Unmarshaller;
import it.assist_si.schemas.jrb.simple.SimpleRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class SimpleRecordUnmarshallNotPaddedLineReaderTest {

  private Unmarshaller<SimpleRecord> unmarshaller;

  @Before
  public void setUp() {
    unmarshaller = new Unmarshaller<SimpleRecord>(new InputStreamReader(
        SimpleRecordUnmarshallNotPaddedLineReaderTest.class.getResourceAsStream("/simple.def.xsd")), new LineReader() {

      private Padder globalPadder;
      private int recordLength;

      public String readLine(BufferedReader reader) {
        try {
          String line = reader.readLine();
          if (line != null) {
            if (line.length() < recordLength) {
              line = globalPadder.pad(line, recordLength);
            } else if (line.length() > recordLength) {
              line = line.substring(0, recordLength);
            }
          }
          return line;
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }

      public void setGlobalPadder(Padder globalPadder) {
        this.globalPadder = globalPadder;
      }

      public void setPropertyDelimiter(String propertyDelimiter) {
      }

      public void setRecordLength(int recordLength) {
        this.recordLength = recordLength;
      }

    });
  }

  @Test
  public void unmarshall() throws Exception {
    Iterator<SimpleRecord> iter = unmarshaller.unmarshall(new InputStreamReader(
        SimpleRecordUnmarshallNotPaddedLineReaderTest.class.getResourceAsStream("simple_test_not_padded.txt")));

    assertTrue(iter.hasNext());
    SimpleRecord record = iter.next();
    assertEquals("JOHN                ", record.getName());
    assertEquals("SMITH               ", record.getSurname());
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
        "JOHN                SMITH               ABCDEF88L99H123B1979051881197Y                              \n",
        unmarshaller.getCurrentJunk());

    assertTrue(iter.hasNext());
    assertEquals(
        "JOHN                SMITH               ABCDEF88L99H123B1979051881197Y                              \n"
            + "JOHN                SMITH               ABCDEF88L99H123B1979051881197Y                              \n",
        unmarshaller.getCurrentJunk());
  }
}
