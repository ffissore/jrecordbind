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

package org.fissore.jrecordbind.padders;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PaddersTest {

  private AbstractLeftPadder spaceLeftPadder;
  private AbstractRightPadder zeroRightPadder;

  public PaddersTest() {
  }

  @Before
  public void setUp() {
    spaceLeftPadder = new AbstractLeftPadder(' ') {

    };
    zeroRightPadder = new AbstractRightPadder('0') {

    };
  }

  @Test
  public void pad() {
    assertEquals("   example", spaceLeftPadder.pad("example", 10));
    assertEquals("example", spaceLeftPadder.pad("example", "example".length()));
    assertEquals("example", spaceLeftPadder.pad("example", 5));
    assertEquals("example000", zeroRightPadder.pad("example", 10));
    assertEquals("example", zeroRightPadder.pad("example", "example".length()));
    assertEquals("example", zeroRightPadder.pad("example", 5));
    assertEquals("     ", spaceLeftPadder.pad(null, 5));
    assertEquals("00000", zeroRightPadder.pad(null, 5));
  }

  @Test
  public void unpad() {
    assertEquals("example", spaceLeftPadder.unpad("   example"));
    assertEquals("example", zeroRightPadder.unpad("example000"));
    assertEquals("", spaceLeftPadder.unpad(null));
    assertEquals("", spaceLeftPadder.unpad(""));
    assertEquals("", zeroRightPadder.unpad(null));
    assertEquals("", zeroRightPadder.unpad(""));
  }
}
