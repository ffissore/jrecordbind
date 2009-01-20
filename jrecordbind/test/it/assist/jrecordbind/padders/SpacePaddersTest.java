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

package it.assist.jrecordbind.padders;

import junit.framework.TestCase;

public class SpacePaddersTest extends TestCase {

  private SpaceLeftPadder spaceLeftPadder;
  private SpaceRightPadder spaceRightPadder;

  public SpacePaddersTest() {
    spaceLeftPadder = new SpaceLeftPadder();
    spaceRightPadder = new SpaceRightPadder();
  }

  public void testLeftPad() throws Exception {
    assertEquals("   example", spaceLeftPadder.pad("example", 10));
    assertEquals("example", spaceLeftPadder.pad("example", 5));
  }

  public void testRIghtPad() throws Exception {
    assertEquals("example   ", spaceRightPadder.pad("example", 10));
    assertEquals("example", spaceRightPadder.pad("example", 5));
  }
}
