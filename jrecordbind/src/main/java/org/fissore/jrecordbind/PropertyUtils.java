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

import com.sun.xml.bind.api.impl.NameConverter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

class PropertyUtils {

  private final Logger log = Logger.getLogger(PropertyUtils.class.getName());

  private final Map<Class<?>, Map<String, Method>> getters;
  private final Map<Class<?>, Map<String, Method>> setters;

  public PropertyUtils() {
    this.getters = new HashMap<>();
    this.setters = new HashMap<>();
  }

  Object getProperty(Object record, String name) {
    log.log(Level.FINE, () -> "Getting property " + name + " from object of " + record.getClass());

    Class<?> clazz = record.getClass();
    if (!getters.containsKey(clazz)) {
      populateMethodsMap(clazz);
    }

    try {
      return getters.get(clazz).get(name).invoke(record);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  private void populateMethodsMap(Class<?> clazz) {
    Map<String, Method> getters = new HashMap<>();
    Map<String, Method> setters = new HashMap<>();

    for (Method method : clazz.getMethods()) {
      String methodName = method.getName();
      if (methodName.startsWith("is")) {
        getters.put(NameConverter.standard.toVariableName(methodName.substring(2)), method);
      } else if (methodName.startsWith("get")) {
        getters.put(NameConverter.standard.toVariableName(methodName.substring(3)), method);
      } else if (methodName.startsWith("set")) {
        setters.put(NameConverter.standard.toVariableName(methodName.substring(3)), method);
      }
    }
    this.getters.put(clazz, getters);
    this.setters.put(clazz, setters);
  }

  void setProperty(Object record, String name, Object value) {
    log.log(Level.FINE, () -> "Setting property " + name + " with value '" + value + "' to object of " + record.getClass());

    Class<?> clazz = record.getClass();
    if (!setters.containsKey(clazz)) {
      populateMethodsMap(clazz);
    }

    try {
      setters.get(clazz).get(name).invoke(record, value);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

}
