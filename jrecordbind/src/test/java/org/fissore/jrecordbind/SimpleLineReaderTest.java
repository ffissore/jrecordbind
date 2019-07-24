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

import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleLineReaderTest {

  @Test
  public void endingWithNewLine() {
    var input = new StringReader("line 1\nline 2");
    var reader = new SimpleLineReader();

    assertEquals("line 1", reader.readLine(input, null, null, -1, "\n"));
    assertEquals("line 2", reader.readLine(input, null, null, -1, "\n"));
    assertNull(reader.readLine(input, null, null, -1, "\n"));
  }

  @Test
  public void endingWithCRLF() {
    var input = new StringReader("line 1\r\nline 2");
    var reader = new SimpleLineReader();

    assertEquals("line 1", reader.readLine(input, null, null, -1, "\r\n"));
    assertEquals("line 2", reader.readLine(input, null, null, -1, "\r\n"));
    assertNull(reader.readLine(input, null, null, -1, "\n"));
  }

  @Test
  public void endingWithArbitraryChars() {
    var input = new StringReader("line 1#!'line 2");
    var reader = new SimpleLineReader();

    assertEquals("line 1", reader.readLine(input, null, null, -1, "#!'"));
    assertEquals("line 2", reader.readLine(input, null, null, -1, "#!'"));
    assertNull(reader.readLine(input, null, null, -1, "#!'"));
  }
}
