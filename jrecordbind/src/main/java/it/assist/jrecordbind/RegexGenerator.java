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

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Generates the regular expression the matches the {@link RecordDefinition}
 * 
 * @author Federico Fissore
 */
class RegexGenerator {

  private final Logger log = Logger.getLogger(RegexGenerator.class.getName());

  private final HashMap<RecordDefinition, Pattern> deepPatterns;
  private final HashMap<RecordDefinition, Pattern> localPatterns;

  public RegexGenerator() {
    deepPatterns = new HashMap<RecordDefinition, Pattern>();
    localPatterns = new HashMap<RecordDefinition, Pattern>();
  }

  private void addFiller(final StringBuilder sb, int rowLengthByDefinition, int actualRowLength) {
    int fillerLength = rowLengthByDefinition - actualRowLength;
    if (fillerLength > 0) {
      sb.append("[ ]{").append(fillerLength).append("}");
    }
  }

  public Pattern deepPattern(RecordDefinition definition) {
    if (!deepPatterns.containsKey(definition)) {
      StringBuilder sb = new StringBuilder();
      deepPattern(definition, sb);
      if (!definition.hasParent()) {
        sb.append(definition.getPrintableLineSeparator());
      }
      deepPatterns.put(definition, Pattern.compile(sb.toString()));
    }
    Pattern pattern = deepPatterns.get(definition);
    if (log.isLoggable(Level.FINE)) {
      log.log(Level.FINE, "Generated regex: " + pattern.toString());
    }
    return pattern;
  }

  private void deepPattern(RecordDefinition definition, StringBuilder sb) {
    if (definition.isChoice()) {
      sb.append("(");
    }

    localPattern(definition, sb);

    for (Iterator<RecordDefinition> iter = definition.getSubRecords().iterator(); iter.hasNext();) {
      RecordDefinition subRecord = iter.next();
      boolean firstRecord = sb.toString().replaceAll("\\(", "").length() == 0;
      sb.append("(");
      if (!firstRecord && !subRecord.isChoice() && !subRecord.getProperties().isEmpty()) {
        sb.append(definition.getPrintableLineSeparator()).append("\\n");
      }
      deepPattern(subRecord, sb);
      sb.append("){").append(subRecord.getMinOccurs()).append(",");

      if (subRecord.getMaxOccurs() != -1) {
        sb.append(subRecord.getMaxOccurs());
      }
      sb.append("}");

      if (definition.isChoice()) {
        if (iter.hasNext()) {
          sb.append(")|(");
        } else {
          sb.append(")");
        }
      }

    }
  }

  public Pattern localPattern(RecordDefinition definition) {
    if (!localPatterns.containsKey(definition)) {
      StringBuilder sb = new StringBuilder();
      localPattern(definition, sb);
      localPatterns.put(definition, Pattern.compile(sb.toString()));
    }
    return localPatterns.get(definition);
  }

  private void localPattern(RecordDefinition definition, StringBuilder sb) {
    if (definition.getProperties().isEmpty()) {
      return;
    }

    int currentRow = 0;
    int actualRowLength = 0;
    for (Iterator<Property> iter = definition.getProperties().iterator(); iter.hasNext();) {
      Property property = iter.next();
      if (property.getRow() != currentRow) {
        currentRow = property.getRow();
        addFiller(sb, definition.getLength(), actualRowLength);
        actualRowLength = 0;
        sb.append("\\n");
      }
      actualRowLength += property.getLength();
      if (property.getFixedValue() != null) {
        sb.append("(" + property.getFixedValue() + ")");
      } else if (definition.getPropertyDelimiter() != null && property.getLength() <= 0) {
        sb.append("([^\\").append(definition.getPropertyDelimiter());
        if (definition.getLineSeparator() != "\n") {
          sb.append("^(").append(definition.getLineSeparator().replaceAll("\n", "\\\\n")).append(")");
        } else {
          sb.append("^\\n");
        }
        sb.append("]*)");
      } else if (property.isEnum()) {
        sb.append("(");
        EnumPropertyHelper enumHelper = new EnumPropertyHelper(property);
        String[] values = enumHelper.getStringValues();
        for (int i = 0; i < values.length; i++) {
          sb.append(values[i]).append("[ ]{").append(property.getLength() - values[i].length()).append("}");
          if (i < values.length - 1) {
            sb.append("|");
          }
        }
        sb.append(")");
      } else {
        sb.append("([\\w\\W]{").append(property.getLength()).append("})");
      }
      if (iter.hasNext() && !"".equals(definition.getPropertyDelimiter())) {
        sb.append("\\" + definition.getPropertyDelimiter());
        actualRowLength++;
      }
    }

    addFiller(sb, definition.getLength(), actualRowLength);
  }
}
