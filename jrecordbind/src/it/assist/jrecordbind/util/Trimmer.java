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

package it.assist.jrecordbind.util;

import java.lang.reflect.Method;

/**
 * Given an object, looks for methods that start with "get" and return a String.
 * Then it replaces that String, trimmed, by getting and setting it
 * 
 * @author Federico Fissore
 */
public class Trimmer {

  public void trim(Object obj) throws TrimmerException {
    for (Method method : obj.getClass().getMethods()) {
      if (method.getName().startsWith("get") && String.class.isAssignableFrom(method.getReturnType())) {
        try {
          String value = (String) method.invoke(obj);
          if (value != null) {
            value = value.trim();
            Method setter = obj.getClass().getMethod("set" + method.getName().substring(3), String.class);
            setter.invoke(obj, value);
          }
        } catch (Exception e) {
          throw new TrimmerException(e);
        }
      }
    }
  }

}
