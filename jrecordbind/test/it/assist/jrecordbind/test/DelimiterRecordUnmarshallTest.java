/*
 * JRecordBind, fixed-length file (un)marshaler
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

import it.assist.jrecordbind.Unmarshaller;
import it.assist_si.schemas.jrb.delimiter.DelimiterRecord;

import java.io.InputStreamReader;
import java.util.Iterator;

import junit.framework.TestCase;

public class DelimiterRecordUnmarshallTest extends TestCase {

  private Unmarshaller<DelimiterRecord> unmarshaller;

  public DelimiterRecordUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<DelimiterRecord>(new InputStreamReader(DelimiterRecordUnmarshallTest.class
        .getResourceAsStream("/delimiter.def.xsd")));
  }

  public void testUnmarshall() throws Exception {
    Iterator<DelimiterRecord> iter = unmarshaller.unmarshall(new InputStreamReader(DelimiterRecordUnmarshallTest.class
        .getResourceAsStream("delimiter_test.txt")));

    assertTrue(iter.hasNext());
    DelimiterRecord record = iter.next();
    assertEquals("name      ", record.getName());
    assertEquals("surname   ", record.getSurname());
    assertEquals("0123456789", record.getTaxCode());

    assertTrue(iter.hasNext());
    record = iter.next();
    assertEquals("other name", record.getName());
    assertEquals("other surn", record.getSurname());
    assertEquals("9876543210", record.getTaxCode());

    assertEquals("", unmarshaller.getCurrentJunk());
  }
}
