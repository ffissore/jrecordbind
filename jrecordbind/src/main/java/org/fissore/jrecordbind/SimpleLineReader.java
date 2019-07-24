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

package org.fissore.jrecordbind;

import java.io.IOException;
import java.io.Reader;

/**
 * The default {@link LineReader} implementation. It will read all lines from a Reader,
 * using given line separator to understand when a line ends
 */
public class SimpleLineReader implements LineReader {

  public String readLine(Reader reader, Padder defaultPadder, String propertyDelimiter, int recordLength, String lineSeparator) {
    char[] lineseparatorChars = lineSeparator.toCharArray();
    int matchedPos = 0;
    int inInt;
    char inChar;
    StringBuilder outLine = new StringBuilder();
    try {
      while ((inInt = reader.read()) > 0) {
        inChar = (char) inInt;
        outLine.append(inChar);
        if (inChar == lineseparatorChars[matchedPos]) {
          matchedPos++;
          if (matchedPos == lineseparatorChars.length) {
            // We have a line end
            return outLine.replace(outLine.length() - lineseparatorChars.length, outLine.length(), "").toString();
          }
        } else {
          if (matchedPos > 0) {
            matchedPos = 0;
          }
        }
      }
      if (outLine.length() > 0) {
        return outLine.toString();
      }
      return null;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
