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

import org.xml.sax.InputSource;

import java.io.File;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

class Utils {

  private static ClassLoader getClassLoader() {
    return Objects.requireNonNullElse(Thread.currentThread().getContextClassLoader(), Utils.class.getClassLoader());
  }

  public static Class<?> loadClass(String fqn) {
    return loadClass(getClassLoader(), fqn);
  }

  public static Class<?> loadClass(ClassLoader loader, String fqn) {
    try {
      return Class.forName(fqn, true, loader);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static Object newInstanceOf(String fqn) {
    return newInstanceOf(getClassLoader(), fqn);
  }

  public static Object newInstanceOf(ClassLoader loader, String fqn) {
    try {
      return loadClass(loader, fqn).getConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static InputSource toInputSource(File file) {
    try {
      return new InputSource(file.toURI().toURL().toExternalForm());
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  public static InputSource toInputSource(Reader reader) {
    return new InputSource(reader);
  }

  public static List<RecordDefinition.Property> collectProperties(RecordDefinition definition) {
    List<RecordDefinition.Property> properties = new ArrayList<>(definition.getProperties());
    for (RecordDefinition subDefinition : definition.getSubRecords()) {
      properties.addAll(collectProperties(subDefinition));
    }
    return properties;
  }

  @SuppressWarnings("unchecked")
  public static <E> void createAndStoreMissingInstancesOf(List<RecordDefinition.Property> properties, Function<RecordDefinition.Property, String> mapper, Map<String, E> store) {
    properties.stream()
      .map(mapper)
      .filter(Objects::nonNull)
      .filter(classNameOrLabel -> !store.containsKey(classNameOrLabel))
      .distinct()
      .forEach(classNameOrLabel -> store.put(classNameOrLabel, (E) newInstanceOf(classNameOrLabel)));
  }
}
