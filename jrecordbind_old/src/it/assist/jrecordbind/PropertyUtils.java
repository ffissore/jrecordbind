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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class PropertyUtils {

  private static final transient Map<Class<?>, Map<String, Method>> getters = new HashMap<Class<?>, Map<String, Method>>();
  private static final transient Map<Class<?>, Map<String, Method>> setters = new HashMap<Class<?>, Map<String, Method>>();

  static Object getProperty(Object record, String name) throws IllegalArgumentException, SecurityException,
      IllegalAccessException, InvocationTargetException, IntrospectionException {
    Class<? extends Object> clazz = record.getClass();
    if (!getters.containsKey(clazz)) {
      synchronized (getters) {
        HashMap<String, Method> methods = new HashMap<String, Method>();
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(record.getClass()).getPropertyDescriptors();
        for (PropertyDescriptor p : propertyDescriptors) {
          methods.put(p.getName(), p.getReadMethod());
        }
        getters.put(clazz, methods);
      }
    }
    return getters.get(clazz).get(name).invoke(record);
  }

  static void setProperty(Object record, String name, Object value) throws IllegalAccessException,
      InvocationTargetException, IntrospectionException {
    Class<? extends Object> clazz = record.getClass();
    if (!setters.containsKey(clazz)) {
      synchronized (setters) {
        HashMap<String, Method> methods = new HashMap<String, Method>();
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(record.getClass()).getPropertyDescriptors();
        for (PropertyDescriptor p : propertyDescriptors) {
          methods.put(p.getName(), p.getWriteMethod());
        }
        setters.put(clazz, methods);
      }
    }
    setters.get(clazz).get(name).invoke(record, value);
  }

}
