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

import java.io.BufferedReader;
import java.io.Reader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.fissore.jrecordbind.Utils.newInstanceOf;

/**
 * Transforms an input reader into beans. You can consume the transformed beans
 * by calling {@link #unmarshallToIterator(Reader)} which returns an Iterator, or
 * by calling {@link #unmarshallToStream(Reader)}, which returns a Stream.
 */
public class Unmarshaller<E> extends AbstractUnMarshaller {

  private class UnmarshallerIterator<T> implements Iterator<T> {

    private final Logger log = Logger.getLogger(UnmarshallerIterator.class.getName());

    private final StringBuilder buffer;
    private final Pattern globalPattern;
    private final LineReader lineReader;
    private final Reader reader;
    private final RegexGenerator regexGenerator;
    private final PropertyUtils propertyUtils;
    private Object currentRecord;

    public UnmarshallerIterator(StringBuilder buffer, LineReader lineReader, Reader reader) {
      this.buffer = buffer;
      this.lineReader = lineReader;
      this.reader = reader;
      this.regexGenerator = new RegexGenerator();
      this.globalPattern = regexGenerator.deepPattern(definition);
      this.propertyUtils = new PropertyUtils();
    }

    private void readNext() {
      if (currentRecord != null) {
        return;
      }

      String current;
      Matcher matcher;
      boolean found;
      // Try to match a record on the current buffer.
      // As long as there is no match, or the match ends at the buffer's end,
      // and there is one more line, read a new line, and add it to the buffer.
      // Then try again
      while ((!(found = (matcher = globalPattern.matcher(buffer)).find()) || matcher.end() == (buffer.length() - 1))
        && (current = lineReader
        .readLine(reader, padders.get(definition.getDefaultPadder()), definition.getPropertyDelimiter(), definition.getLength(), definition.getLineSeparator())) != null) {
        buffer.append(current).append("\n");
      }

      if (found) {
        try {
          Object record = newInstanceOf(definition.getClassName());
          StringBuilder currentBuffer = new StringBuilder(buffer.substring(matcher.start(), matcher.end()));

          // Parse the data from currentBuffer to record according to definition
          recursive(record, definition, currentBuffer);

          buffer.delete(matcher.start(), matcher.end() + 1);

          currentRecord = record;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      log.log(Level.FINE, "A new record has been read");
    }

    public boolean hasNext() {
      readNext();
      return currentRecord != null;
    }

    @SuppressWarnings("unchecked")
    public T next() {
      readNext();
      if (currentRecord == null) {
        throw new NoSuchElementException();
      }
      T current = (T) currentRecord;
      currentRecord = null;
      return current;
    }

    @SuppressWarnings("unchecked")
    private void recursive(Object record, RecordDefinition currentDefinition, StringBuilder currentBuffer) {

      // If the current definition has direct properties, match and read them
      Matcher matcher = regexGenerator.localPattern(currentDefinition).matcher(currentBuffer);
      if (!matcher.find()) {
        return;
      }

      int groupCount = 1;

      for (Property property : currentDefinition.getProperties()) {
        String stringValue = matcher.group(groupCount);
        String unpaddedStringValue = getPadder(currentDefinition, property).unpad(stringValue);
        Object convertedValue = getConverter(property).convert(unpaddedStringValue);
        propertyUtils.setProperty(record, property.getName(), convertedValue);
        groupCount++;
      }
      // Then delete the matched section
      currentBuffer.delete(matcher.start(), matcher.end());

      // If the current definition has subrecords, handle them
      Matcher subMatcher;
      for (RecordDefinition subDefinition : currentDefinition.getSubRecords()) {
        int matchedRows = 0;
        while ((subMatcher = regexGenerator.deepPattern(subDefinition).matcher(currentBuffer)).find()
          && (subDefinition.getMaxOccurs() == -1 || matchedRows < subDefinition.getMaxOccurs())) {

          // Recurursively call this function for the found subrecord
          StringBuilder subBuffer = new StringBuilder(currentBuffer.substring(subMatcher.start(), subMatcher.end()));
          Object subRecord = newInstanceOf(subDefinition.getClassName());
          recursive(subRecord, subDefinition, subBuffer);
          currentBuffer.delete(subMatcher.start(), subMatcher.end());

          // Set, or add to the collection the property found above
          Object property = propertyUtils.getProperty(record, subDefinition.getSetterName());
          if (property instanceof Collection) {
            Collection<Object> collection = (Collection<Object>) property;
            collection.add(subRecord);
          } else {
            propertyUtils.setProperty(record, subDefinition.getSetterName(), subRecord);
          }
          matchedRows++;
        }
      }
    }

    public void remove() {
    }

  }

  private final StringBuilder buffer;
  private final LineReader lineReader;

  /**
   * Creates a new Unmarshaller, with the specified record definition and
   * using the default {@link LineReader} implementation<br>
   *
   * @param definition the record definition
   */
  public Unmarshaller(RecordDefinition definition) {
    this(definition, new SimpleLineReader());
  }

  /**
   * Creates a new Unmarshaller, with the specified record definition and
   * using the specified {@link LineReader} implementation
   *
   * @param definition the record definition
   * @param lineReader a custom implementation of the LineReader
   */
  public Unmarshaller(RecordDefinition definition, LineReader lineReader) {
    this(definition, lineReader, new HashMap<>(), new HashMap<>());
  }

  /**
   * Creates a new Unmarshaller, with the specified record definition and
   * using the specified {@link LineReader} implementation, and with user provided
   * instances of converters and padders.
   *
   * @param definition the record definition
   * @param lineReader a custom implementation of the LineReader
   * @param converters user provided instances of converters
   * @param padders    user provided instances of padders
   */
  public Unmarshaller(RecordDefinition definition, LineReader lineReader, Map<String, Converter> converters, Map<String, Padder> padders) {
    super(definition, converters, padders);
    this.lineReader = lineReader;
    this.buffer = new StringBuilder();
  }

  /**
   * Returns the current internal buffer content. If called right after a
   * {@link Iterator#next()} call, it will return what JRecordBind wasn't able
   * to unmarshall. Usually called after the {@link Iterator#hasNext()} has
   * returned <code>false</code> to report the user about the "junk" found in
   * the text file
   *
   * @return the current "junk" stored in the internal buffer
   */
  public String getCurrentJunk() {
    return buffer.toString();
  }

  /**
   * Unmarshalls the input fixed-length file, a bean at a time
   *
   * @param input the input fixed-length file
   * @return an Iterator: each next() call will give back the next bean
   */
  public Iterator<E> unmarshallToIterator(Reader input) {
    return new UnmarshallerIterator<>(buffer, lineReader, new BufferedReader(input));
  }

  /**
   * Like {@link Unmarshaller#unmarshallToIterator(Reader)}, but returns a Stream instead of an Iterator
   *
   * @param input the input fixed-length file
   * @return a stream of beans
   */
  public Stream<E> unmarshallToStream(Reader input) {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(unmarshallToIterator(input), Spliterator.ORDERED), false);
  }

}
