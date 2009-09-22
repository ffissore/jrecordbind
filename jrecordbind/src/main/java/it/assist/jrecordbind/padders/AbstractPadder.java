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

import it.assist.jrecordbind.Padder;

abstract class AbstractPadder implements Padder {

  private final char padChar;

  public AbstractPadder(char padChar) {
    this.padChar = padChar;
  }

  protected String buildPad(String string, int totalLength) {
    int lengthOfString = lengthOf(string);
    int padSize = totalLength - lengthOfString;
    if (padSize <= 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder(padSize);
    while (padSize-- > 0) {
      sb.append(padChar);
    }
    return sb.toString();
  }

  private int lengthOf(String string) {
    if (string == null) {
      return 0;
    }
    return string.length();
  }

}
