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
import it.assist.jrecordbind.padders.SpaceLeftPadder;
import it.assist_si.schemas.jrb.padders.SimpleRecord;

import java.io.InputStreamReader;
import java.io.StringWriter;

import junit.framework.TestCase;

public class DifferentPaddersRecordMarshallTest extends TestCase {

  private Marshaller<SimpleRecord> marshaller;
  private SimpleRecord record;
  private StringWriter stringWriter;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    record = new SimpleRecord();
    record.setName("A NAME");
    record.setOneInteger(81);
    record.setTwoInteger(1934);
    record.setOneFloat(1.97f);

    marshaller = new Marshaller<SimpleRecord>(new InputStreamReader(SimpleRecordMarshallTest.class
        .getResourceAsStream("/differentPadders.def.xsd")), new SpaceLeftPadder());

    stringWriter = new StringWriter();
  }

  public void testMarshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals(
        "              A NAME00000000811934                  197                                              \n",
        stringWriter.toString());
  }
}
