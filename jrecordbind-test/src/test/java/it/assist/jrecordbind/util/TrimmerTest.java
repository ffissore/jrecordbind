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

package it.assist.jrecordbind.util;

import static org.junit.Assert.*;
import it.assist.jrecordbind.Unmarshaller;
import it.assist.jrecordbind.test.SimpleRecordUnmarshallTest;
import it.assist_si.schemas.jrb.simple.SimpleRecord;

import java.io.InputStreamReader;

import org.junit.Test;

public class TrimmerTest {

  private Trimmer trimmer;
  private Unmarshaller<SimpleRecord> unmarshaller;

  public TrimmerTest() throws Exception {
    unmarshaller = new Unmarshaller<SimpleRecord>(new InputStreamReader(SimpleRecordUnmarshallTest.class
        .getResourceAsStream("/simple.def.xsd")));
    trimmer = new Trimmer();
  }

  @Test
  public void trim() throws Exception {
    SimpleRecord simpleRecord = unmarshaller.unmarshallAll(
        new InputStreamReader(TrimmerTest.class.getResourceAsStream("trimmer_simple_test.txt"))).get(0);

    simpleRecord.setSurname(null);

    assertEquals("JOHN                ", simpleRecord.getName());
    assertNull(simpleRecord.getSurname());
    assertEquals("ABCDEF88L99H123B", simpleRecord.getTaxCode());

    trimmer.trim(simpleRecord);

    assertEquals("JOHN", simpleRecord.getName());
    assertNull(simpleRecord.getSurname());
    assertEquals("ABCDEF88L99H123B", simpleRecord.getTaxCode());
  }
}
