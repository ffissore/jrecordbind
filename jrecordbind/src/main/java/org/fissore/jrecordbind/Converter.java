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

/**
 * Used to convert a String to an appropriate object and an object to its String representation. Must be stateless.<br>
 * <p>
 * See {@link org.fissore.jrecordbind.converters} for the bundled ones.
 */
public interface Converter {

  /**
   * Converts a String into an object, e.g. String 19790518 to Date May 18th, 1979
   *
   * @param value the string to convert
   * @return the converted object
   */
  Object convert(String value);

  /**
   * Converts an object into string, e.g. Date May 18th, 1979 to String 19790518
   *
   * @param value the object to convert
   * @return a string rapresentation of the object
   */
  String toString(Object value);

}
