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

package it.assist.jrecordbind.padders;

import static org.junit.Assert.*;

import org.junit.Test;

public class ZeroPaddersTest {

  private ZeroLeftPadder zeroLeftPadder;
  private ZeroRightPadder zeroRightPadder;

  public ZeroPaddersTest() {
    zeroLeftPadder = new ZeroLeftPadder();
    zeroRightPadder = new ZeroRightPadder();
  }

  @Test
  public void leftPad() throws Exception {
    assertEquals("000example", zeroLeftPadder.pad("example", 10));
    assertEquals("example", zeroLeftPadder.pad("example", 5));
  }

  @Test
  public void rightPad() throws Exception {
    assertEquals("example000", zeroRightPadder.pad("example", 10));
    assertEquals("example", zeroRightPadder.pad("example", 5));
  }
}
