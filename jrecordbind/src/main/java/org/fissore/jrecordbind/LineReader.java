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

import java.io.Reader;

/**
 * When the {@link Unmarshaller} reads a text file, it reads it via a LineReader.<br>
 * Unless specified, the default implementation will be used, {@link SimpleLineReader}, which will read each line and pass it to the {@link Unmarshaller}<br>
 * Sometimes, text files contains lines you're not interested to, or you want to do some preprocessing before parsing them: in such cases, it's useful to provide your own implementation of LineReader
 *
 * @see SimpleLineReader
 */
public interface LineReader {

  /**
   * Reads a line from a Reader. This is where you want to add your customization
   *
   * @param reader            a reader, reading from a fixed-length file
   * @param defaultPadder     the padder to optionally use when reading a line
   * @param propertyDelimiter the delimiter used to delimit properties on a line
   * @param recordLength      the total expected length of a line
   * @param lineSeparator     the character sequence used to mark the end of a line
   * @return a string, or null if there are no more lines to read
   */
  String readLine(Reader reader, Padder defaultPadder, String propertyDelimiter, int recordLength, String lineSeparator);

}
