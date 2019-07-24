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

import org.fissore.jrecordbind.RecordDefinition.Property;

import java.lang.reflect.Method;

import static org.fissore.jrecordbind.Utils.loadClass;

class EnumPropertyHelper {

  private final Property property;

  public EnumPropertyHelper(Property property) {
    this.property = property;
  }

  public boolean isEnum() {
    Class<?> typeClass;
    try {
      typeClass = getTypeClass();
    } catch (Exception e) {
      return false;
    }

    return Enum.class.isAssignableFrom(typeClass);
  }

  public Class<?> getTypeClass() {
    return loadClass(property.getType());
  }

  @SuppressWarnings("unchecked")
  public Enum<?>[] getValues() {
    try {
      Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) getTypeClass();
      Method method = enumClass.getMethod("values");
      return (Enum<?>[]) method.invoke(enumClass);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String[] getStringValues() {
    Enum<?>[] values = getValues();
    String[] stringValues = new String[values.length];
    try {
      Method valueMethod = values[0].getClass().getMethod("value");
      for (int i = 0; i < values.length; i++) {
        stringValues[i] = (String) valueMethod.invoke(values[i]);
      }
      return stringValues;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
