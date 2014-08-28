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
package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Converter;
import it.assist_si.schemas.jrb.typesafeenumtest.EnumOne;
import it.assist_si.schemas.jrb.typesafeenumtest.EnumTwo;

public class TypesafeEnumTestConverters {

  private TypesafeEnumTestConverters() {

  }

  public static class EnumOneConverter implements Converter {

    public Object convert(String value) {
      return EnumOne.fromValue(value.trim());
    }

    public String toString(Object value) {
      return ((EnumOne) value).value();
    }

  }

  public static class EnumTwoConverter implements Converter {

    public Object convert(String value) {
      return EnumTwo.fromValue(value.trim());
    }

    public String toString(Object value) {
      return ((EnumTwo) value).value();
    }
  }
}
