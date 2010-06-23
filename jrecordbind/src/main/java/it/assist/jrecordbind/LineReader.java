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

/**
 * There are special situations when you need to make some preprocessing work on
 * the lines you read from the text file before JRecordBind starts unmarshalling
 * 
 * @author Federico Fissore
 */
public interface LineReader {

  /**
   * Reads a line from the BufferedReader. That's where you would like to add
   * your customization
   * 
   * @param reader
   *          a reader, pointing at the fixed length files
   * @return a string or null if there are no more lines to read
   */
  String readLine(BufferedReader reader);

  /**
   * The {@link Unmarshaller} will inject this property when instantiated
   * 
   * @param globalPadder
   */
  void setGlobalPadder(Padder globalPadder);

  /**
   * The {@link Unmarshaller} will inject this property when instantiated
   * 
   * @param propertyDelimiter
   */
  void setPropertyDelimiter(String propertyDelimiter);

  /**
   * The {@link Unmarshaller} will inject this property when instantiated
   * 
   * @param recordLength
   */
  void setRecordLength(int recordLength);
  
  /**
   * The {@link Unmarshaller} will inject this property when instantiated
   * 
   * @param lineSeparator
   */
  void setLineSeparator(String lineSeparator);

}
