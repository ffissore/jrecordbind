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

import it.assist.jrecordbind.Marshaller;
import it.assist_si.schemas.jrb.simple.SimpleRecord;

import java.io.InputStreamReader;
import java.io.StringWriter;

import junit.framework.TestCase;

public class DelimiterRecordMarshallTest extends TestCase {

  private Marshaller<SimpleRecord> marshaller;
  private SimpleRecord record;
  private StringWriter stringWriter;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    record = new SimpleRecord();
    record.setName("A NAME");
    record.setSurname("A SURNAME");
    record.setTaxCode("0123456789");

    marshaller = new Marshaller<SimpleRecord>(new InputStreamReader(DelimiterRecordMarshallTest.class
        .getResourceAsStream("/delimiter.def.xsd")));

    stringWriter = new StringWriter();
  }

  public void testMarshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals("A NAME    |A SURNAME |0123456789\n", stringWriter.toString());
  }
}
