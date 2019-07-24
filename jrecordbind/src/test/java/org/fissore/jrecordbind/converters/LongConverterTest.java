/*
 * JRecordBind, fixed-length file (un)marshaller
 * Copyright 2019, Federico Fissore, and individual contributors. See
 * AUTHORS.txt in the distribution for a full listing of individual
 * contributors.
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

package org.fissore.jrecordbind.converters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LongConverterTest {

  private LongConverter converter;

  @Before
  public void setUp() {
    converter = new LongConverter();
  }

  @Test
  public void convert() {
    assertEquals(90L, converter.convert("90"));
    assertEquals((long) -1, converter.convert("-1"));
    assertNull(converter.convert(null));
    assertNull(converter.convert(""));
  }

  @Test
  public void toStringObject() {
    assertEquals("90", converter.toString(90L));
    assertEquals("-1", converter.toString((long) -1));
    assertEquals("", converter.toString(null));
  }

}
