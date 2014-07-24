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
import it.assist_si.schemas.jrb.simple.SimpleRecord;

import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Iterator;

import org.junit.Test;

public class CorrectIteratorImplementationUnmarshallTest {

  private Unmarshaller<SimpleRecord> unmarshaller;

  public CorrectIteratorImplementationUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<SimpleRecord>(Unmarshaller.class.getClassLoader(), new InputStreamReader(
        CorrectIteratorImplementationUnmarshallTest.class.getResourceAsStream("/simple.def.xsd")));
  }

  @Test
  public void unmarshall() throws Exception {
    Iterator<SimpleRecord> iter = unmarshaller.unmarshall(new InputStreamReader(
        CorrectIteratorImplementationUnmarshallTest.class.getResourceAsStream("simple.txt")));

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

    assertEquals("JOHN                ", iter.next().getName());
    assertEquals("JOHN                ", iter.next().getName());
    assertEquals("JOHN                ", iter.next().getName());
    assertEquals("JOHN                ", iter.next().getName());
    assertEquals("JOHN                ", iter.next().getName());
    assertEquals(
        "JOHN                SMITH               ABCDEF88L99H123B1979051881197Y                              \n",
        unmarshaller.getCurrentJunk());

    assertTrue(iter.hasNext());
    assertNotNull(iter.next());
    assertEquals(
        "JOHN                SMITH               ABCDEF88L99H123B1979051881197Y                              \n",
        unmarshaller.getCurrentJunk());
  }
}
