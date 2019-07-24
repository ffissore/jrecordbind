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

package org.fissore.jrecordbind.util;

import java.lang.reflect.Method;

/**
 * Given an object, looks for methods that start with "get" or "is" and return a String.
 * Then it replaces that String, trimmed, by getting and setting it
 */
public class Trimmer {

  public void trim(Object obj) throws TrimmerException {
    try {
      for (Method readMethod : obj.getClass().getMethods()) {
        String fieldName = null;
        String readMethodName = readMethod.getName();
        if (readMethodName.startsWith("is")) {
          fieldName = readMethodName.substring(2);
        } else if (readMethodName.startsWith("get")) {
          fieldName = readMethodName.substring(3);
        }

        if (fieldName != null && String.class.isAssignableFrom(readMethod.getReturnType())) {
          Method writeMethod = obj.getClass().getMethod("set" + fieldName, String.class);
          if (writeMethod != null) {
            String value = (String) readMethod.invoke(obj);
            if (value != null) {
              value = value.trim();
              writeMethod.invoke(obj, value);
            }
          }
        }
      }
    } catch (Exception e) {
      throw new TrimmerException(e);
    }
  }
}
