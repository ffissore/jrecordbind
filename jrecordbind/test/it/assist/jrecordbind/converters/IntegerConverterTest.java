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

package it.assist.jrecordbind.converters;

import junit.framework.TestCase;

public class IntegerConverterTest extends TestCase {

  private IntegerConverter converter;

  public IntegerConverterTest() {
    converter = new IntegerConverter();
  }

  public void testConvert() {
    assertEquals(Integer.valueOf(90), converter.convert("90"));
    assertEquals(Integer.valueOf(-1), converter.convert("-1"));
    assertNull(converter.convert(null));
    assertNull(converter.convert(""));
  }

  public void testToStringObject() {
    assertEquals("90", converter.toString(Integer.valueOf(90)));
    assertEquals("-1", converter.toString(Integer.valueOf(-1)));
    assertEquals("", converter.toString(null));
  }

}
