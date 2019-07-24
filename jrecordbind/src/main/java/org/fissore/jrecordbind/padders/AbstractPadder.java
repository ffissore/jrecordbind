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

import org.fissore.jrecordbind.Padder;

public abstract class AbstractPadder implements Padder {

  protected final char padChar;

  public AbstractPadder(char padChar) {
    this.padChar = padChar;
  }

  protected String buildPad(String string, int totalLength) {
    int lengthOfString = lengthOf(string);
    // PITEST note: the following conditional will generate a surviving mutation.
    // That's because it's just an optimization to avoid creating a StringBuilder
    // So the code works even if the `if` is removed/negated, it'll just be slower
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
