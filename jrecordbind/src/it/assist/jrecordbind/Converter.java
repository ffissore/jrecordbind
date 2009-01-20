/*
 * JRecordBind, fixed-length file (un)marshaler
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
 * Used by both the {@link Marshaller} and the {@link Unmarshaller} to convert a
 * String into an appropriate object and back. Must have a default constructor
 * and must be stateless.<br/>
 * See {@link it.assist.jrecordbind.converters} for the default ones.
 * 
 * @author Federico Fissore
 */
public interface Converter {

  /**
   * Converts a String into an object, e.g. a Date
   * 
   * @param value
   *          the string to convert
   * @return the converted object
   */
  Object convert(String value);

  /**
   * Converts an object into string
   * 
   * @param value
   *          the object to convert
   * @return a string rapresentation of the object
   */
  String toString(Object value);

}
