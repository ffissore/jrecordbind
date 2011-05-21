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

package it.assist.jrecordbind;

import java.io.BufferedReader;
import java.io.IOException;

public class SimpleLineReader implements LineReader {

  private char[] sepChars;
  private int sepLen;

  public String readLine(BufferedReader reader) {
    int matchedPos = 0;
    int inInt;
    char inChar;
    StringBuffer outLine = new StringBuffer();
    try {
      while ((inInt = reader.read()) > 0) {
        inChar = (char) inInt;
        outLine.append(inChar);
        if (inChar == sepChars[matchedPos]) {
          matchedPos++;
          if (matchedPos == sepLen) {
            // We have a line end
            return outLine.replace(outLine.length() - sepLen, outLine.length(), "").toString();
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

  public void setGlobalPadder(Padder globalPadder) {
  }

  public void setPropertyDelimiter(String propertyDelimiter) {
  }

  public void setRecordLength(int recordLength) {
  }

  public void setLineSeparator(String lineSeparator) {
    this.sepChars = lineSeparator.toCharArray();
    this.sepLen = sepChars.length;
  }

}
