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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.xml.bind.api.impl.NameConverter;

class PropertyUtils {

  private final Logger log = Logger.getLogger(PropertyUtils.class.getName());

  private final Map<Class<?>, Map<String, Method>> getters;
  private final Map<Class<?>, Map<String, Method>> setters;

  public PropertyUtils() {
    this.getters = new HashMap<Class<?>, Map<String, Method>>();
    this.setters = new HashMap<Class<?>, Map<String, Method>>();
  }

  Object getProperty(Object record, String name) {
    if (log.isLoggable(Level.FINE)) {
      log.log(Level.FINE, "Getting property " + name + " from object of " + record.getClass());
    }
    try {
      Class<? extends Object> clazz = record.getClass();
      if (!getters.containsKey(clazz)) {
        populateMethodsMap(clazz);
      }
      return getters.get(clazz).get(name).invoke(record);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  private void populateMethodsMap(Class<?> clazz) {
    Map<String, Method> classGetters = new HashMap<String, Method>();
    Map<String, Method> classSetters = new HashMap<String, Method>();

    Method[] methods = clazz.getMethods();
    for (Method m : methods) {
      if (m.getName().startsWith("is")) {
        classGetters.put(NameConverter.standard.toVariableName(m.getName().substring(2)), m);
      } else if (m.getName().startsWith("get")) {
        classGetters.put(NameConverter.standard.toVariableName(m.getName().substring(3)), m);
      } else if (m.getName().startsWith("set")) {
        classSetters.put(NameConverter.standard.toVariableName(m.getName().substring(3)), m);
      }
    }
    getters.put(clazz, classGetters);
    setters.put(clazz, classSetters);
  }

  void setProperty(Object record, String name, Object value) {
    if (log.isLoggable(Level.FINE)) {
      log.log(Level.FINE, "Setting property " + name + " with value '" + value + "' to object of " + record.getClass());
    }
    try {
      Class<? extends Object> clazz = record.getClass();
      if (!setters.containsKey(clazz)) {
        populateMethodsMap(clazz);
      }
      setters.get(clazz).get(name).invoke(record, value);
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

}
