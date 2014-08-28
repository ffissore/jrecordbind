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
import org.junit.Test;

import java.io.InputStreamReader;
import java.util.Iterator;

import it.assist.jrecordbind.Unmarshaller;
import it.assist_si.schemas.jrb.typesafeenumtest.EnumOne;
import it.assist_si.schemas.jrb.typesafeenumtest.EnumRecord;
import it.assist_si.schemas.jrb.typesafeenumtest.EnumTwo;

public class TypesafeEnumTest {

  private final Unmarshaller<EnumRecord> unmarshaller;

  public TypesafeEnumTest() throws Exception {
    unmarshaller = new Unmarshaller<EnumRecord>(new InputStreamReader(TypesafeEnumTest.class
        .getResourceAsStream("/typesafeEnumTest.def.xsd")));
  }

  @Test
  public void unmarshall() throws Exception {
    Iterator<EnumRecord> iter = unmarshaller.unmarshall(new InputStreamReader(
        EnumWithRestrictionsRecordUnmarshallTest.class.getResourceAsStream("typesafeEnumTest.txt")));

    assertTrue(iter.hasNext());
    EnumRecord record = iter.next();
    assertEquals(EnumOne.ONE, record.getEnumOne());
    assertEquals(EnumTwo.ONE, record.getEnumTwo());
    record = iter.next();
    assertEquals(EnumOne.TWO, record.getEnumOne());
    assertEquals(EnumTwo.TWO, record.getEnumTwo());
    record = iter.next();
    assertEquals(EnumOne.THREE, record.getEnumOne());
    assertEquals(EnumTwo.THREE, record.getEnumTwo());
    record = iter.next();
    assertEquals(EnumOne.NINE, record.getEnumOne());
    assertEquals(EnumTwo.FIVE, record.getEnumTwo());

    assertFalse(iter.hasNext());
    assertEquals("", unmarshaller.getCurrentJunk());
  }

}
