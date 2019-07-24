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
import org.fissore.jrecordbind.padders.SpaceRightPadder;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Transforms beans to text according to the given record definition
 */
public class Marshaller<E> extends AbstractUnMarshaller {

  /**
   * Creates a new marshaller, with the specified record definition
   *
   * @param definition the record definition
   * @see SpaceRightPadder
   */
  public Marshaller(RecordDefinition definition) {
    this(definition, new HashMap<>(), new HashMap<>());
  }

  /**
   * Creates a new marshaller, with the specified record definition and user provided instances of converters and padders.
   *
   * @param definition the record definition
   * @param converters user provided instances of converters
   * @param padders    user provided instances of padders
   */
  public Marshaller(RecordDefinition definition, Map<String, Converter> converters, Map<String, Padder> padders) {
    super(definition, converters, padders);
  }

  private void fillWithBlanks(StringBuilder sb, int definitionLength, int length) {
    int fillerLength = definitionLength - length;
    while (fillerLength-- > 0) {
      sb.append(" ");
    }
  }

  private Object ensureCorrectLength(int length, String value) {
    if (length > 0 && value.length() > length) {
      return value.substring(0, length);
    }
    return value;
  }

  /**
   * Marshalls a bean to a writer
   *
   * @param record the bean to marshal
   * @param writer the target writer
   */
  public void marshall(E record, Writer writer) {
    marshall(record, definition, writer);
  }

  @SuppressWarnings("unchecked")
  private void marshall(Object record, RecordDefinition currentDefinition, Writer writer) {
    StringBuilder sb = new StringBuilder(currentDefinition.getLength());
    int currentRow = 0;
    int length = 0;
    boolean propertyFound = false;
    for (Iterator<Property> iter = currentDefinition.getProperties().iterator(); iter.hasNext(); ) {
      propertyFound = true;
      Property property = iter.next();
      if (property.getRow() != currentRow) {
        currentRow = property.getRow();
        fillWithBlanks(sb, currentDefinition.getLength(), length);
        length = 0;
        sb.append(definition.getLineSeparator());
      }
      Object propertyValue = propertyUtils.getProperty(record, property.getName());
      String convertedPropertyValue = getConverter(property).toString(propertyValue);
      String paddedPropertyValue = getPadder(currentDefinition, property).pad(convertedPropertyValue, property.getLength());
      sb.append(ensureCorrectLength(property.getLength(), paddedPropertyValue));
      length += property.getLength();
      if (iter.hasNext()) {
        sb.append(currentDefinition.getPropertyDelimiter());
        length += currentDefinition.getPropertyDelimiter().length();
      }
    }

    if (propertyFound) {
      fillWithBlanks(sb, currentDefinition.getLength(), length);

      sb.append(definition.getLineSeparator());
    }

    try {
      writer.append(sb.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    boolean choiceRecordDone = false;
    for (RecordDefinition subDefinition : currentDefinition.getSubRecords()) {
      if (!choiceRecordDone) {
        Object subRecord = propertyUtils.getProperty(record, subDefinition.getSetterName());
        if (subRecord == null && !subDefinition.getParent().isChoice()) {
          throw new NullPointerException("Missing object from " + subDefinition.getSetterName());
        }
        if (subRecord != null) {
          if (subRecord instanceof Collection) {
            Collection<Object> subRecords = (Collection<Object>) subRecord;
            for (Object o : subRecords) {
              marshall(o, subDefinition, writer);
            }
          } else {
            marshall(subRecord, subDefinition, writer);
          }
          choiceRecordDone = currentDefinition.isChoice();
        }
      }
    }
  }

  /**
   * Marshalls a collection of beans to a writer
   *
   * @param records the beans to marshall
   * @param writer  the target writer
   */
  public void marshallAll(Collection<E> records, Writer writer) {
    for (E record : records) {
      marshall(record, definition, writer);
    }
  }
}
