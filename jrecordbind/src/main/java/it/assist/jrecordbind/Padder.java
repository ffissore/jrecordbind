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

/**
 * Used by the {@link Marshaller}: some records require data to be left or right
 * aligned. By default the {@link Marshaller} will left align using spaces.
 * JRecordBind comes with 4 pre-build padders that will left/right pad with
 * spaces/zeros. If you need a different padding, you can specify it by
 * implementing this interface. <br/>
 * Padders can be global (specified in the "main" tag of the .xsd definition)
 * and can be custom (specified at the single element level)
 * 
 * @author Federico Fissore
 */
public interface Padder {

  /**
   * Pads a string to the given length
   * 
   * @param string
   *          the string to pad
   * @param length
   *          the length of the final string
   * @return the padded string
   */
  String pad(String string, int length);

}
