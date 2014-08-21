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

import it.assist.jrecordbind.RecordDefinition.Property;
import it.assist.jrecordbind.padders.SpaceRightPadder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import org.xml.sax.InputSource;

/**
 * Marshalls beans according to the given .xsd definition
 *
 * @author Federico Fissore
 */
public class Marshaller<E> extends AbstractUnMarshaller {

  /**
   * Creates a new marshaller, reading the configuration specified in the .xsd
   * definition given as input. Fields without a specific padder will be left
   * aligned with spaces. <br/>
   * This is the most convenient way to init the Marshaller since it will
   * resolve local XSDs
   *
   * @see SpaceRightPadder
   * @param definition
   *          the .xsd definition
   */
  public Marshaller(File definition) {
    super(Marshaller.class.getClassLoader(), Utils.toInputSource(definition));
  }

  /**
   * Creates a new marshaller, reading the configuration specified in the .xsd
   * definition given as input. Fields without a specific padder will be left
   * aligned with spaces. <br/>
   * This is the most convenient way to init the Marshaller since it will
   * resolve local XSDs
   *
   * @see SpaceRightPadder
   * @param loader
   *          class loader
   * @param definition
   *          the .xsd definition
   */
  public Marshaller(ClassLoader loader, File definition) {
    super(loader, Utils.toInputSource(definition));
  }

  /**
   * Creates a new marshaller, reading the configuration specified in the .xsd
   * definition given as input. Fields without a specific padder will be left
   * aligned with spaces.<br/>
   * Use this constructor only if your XSD is self contained (no
   * included/imported XSDs)
   *
   * @see SpaceRightPadder
   * @param definition
   *          the .xsd definition
   */
  public Marshaller(Reader definition) {
    super(Marshaller.class.getClassLoader(), Utils.toInputSource(definition));
  }

  /**
   * Creates a new marshaller, reading the configuration specified in the .xsd
   * definition given as input. Fields without a specific padder will be left
   * aligned with spaces.<br/>
   * Use this constructor only if your XSD is self contained (no
   * included/imported XSDs)
   *
   * @see SpaceRightPadder
   * @param loader
   *          class loader
   * @param definition
   *          the .xsd definition
   */
  public Marshaller(ClassLoader loader, Reader definition) {
    super(loader, Utils.toInputSource(definition));
  }

  /**
   * Creates a new marshaller, reading the configuration specified in the .xsd
   * definition given as input. Fields without a specific padder will be left
   * aligned with spaces.<br/>
   * Use this constructor only if you know how to handle an InputSource
   *
   * @see SpaceRightPadder
   * @param definition
   *          the .xsd definition
   */
  public Marshaller(InputSource definition) {
    super(Marshaller.class.getClassLoader(), definition);
  }

  /**
   * Creates a new marshaller, reading the configuration specified in the .xsd
   * definition given as input. Fields without a specific padder will be left
   * aligned with spaces.<br/>
   * Use this constructor only if you know how to handle an InputSource
   *
   * @see SpaceRightPadder
   * @param loader
   *          class loader
   * @param definition
   *          the .xsd definition
   */
  public Marshaller(ClassLoader loader, InputSource definition) {
    super(loader, definition);
  }

  private void addFiller(final StringBuilder sb, int definitionLength, int length) {
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
   * @param record
   *          the bean to marshal
   * @param writer
   *          the target writer
   * @throws IOException
   */
  public void marshall(E record, Writer writer) throws IOException {
    marshall(record, definition, writer);
  }

  @SuppressWarnings("unchecked")
  private void marshall(Object record, RecordDefinition currentDefinition, Writer writer) throws IOException {
    StringBuilder sb = new StringBuilder(currentDefinition.getLength());
    int currentRow = 0;
    int length = 0;
    Padder currentPadder;
    boolean propertyFound = false;
    for (Iterator<Property> iter = currentDefinition.getProperties().iterator(); iter.hasNext();) {
      propertyFound = true;
      Property property = iter.next();
      if (property.getRow() != currentRow) {
        currentRow = property.getRow();
        addFiller(sb, currentDefinition.getLength(), length);
        length = 0;
        sb.append(definition.getLineSeparator());
      }
      if (property.getPadder() != null) {
        currentPadder = padders.get(property.getPadder());
      } else {
        currentPadder = padders.get(currentDefinition.getGlobalPadder());
      }
      String value = currentPadder.pad(converters.get(property.getConverter()).toString(
          propertyUtils.getProperty(record, property.getName())), property.getLength());
      sb.append(ensureCorrectLength(property.getLength(), value));
      length += property.getLength();
      if (iter.hasNext()) {
        sb.append(currentDefinition.getPropertyDelimiter());
        length += currentDefinition.getPropertyDelimiter().length();
      }
    }

    if (propertyFound) {
      addFiller(sb, currentDefinition.getLength(), length);

      sb.append(definition.getLineSeparator());
    }

    writer.append(sb.toString());

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
   * @param records
   *          the beans to marshall
   * @param writer
   *          the target writer
   * @throws IOException
   */
  public void marshallAll(Collection<E> records, Writer writer) throws IOException {
    for (E record : records) {
      marshall(record, definition, writer);
    }
  }
}
