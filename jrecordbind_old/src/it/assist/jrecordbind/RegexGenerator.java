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

import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Generates the regular expression the matches the {@link RecordDefinition}
 * 
 * @author Federico Fissore
 */
class RegexGenerator {

  private void addFiller(final StringBuilder sb, int definitionLength, int length) {
    int fillerLength = definitionLength - length;
    if (fillerLength > 0) {
      sb.append("[ ]{").append(fillerLength).append("}");
    }
  }

  public Pattern deepPattern(RecordDefinition definition) {
    StringBuilder sb = new StringBuilder();
    deepPattern(definition, sb);
    return Pattern.compile(sb.toString());
  }

  private void deepPattern(RecordDefinition definition, StringBuilder sb) {
    localPattern(definition, sb);

    for (Iterator<RecordDefinition> iter = definition.getSubRecords().iterator(); iter.hasNext();) {
      RecordDefinition subDefinition = iter.next();
      boolean firstRecord = sb.length() == 0;
      sb.append("(");
      if (!firstRecord) {
        sb.append("\\n");
      }
      deepPattern(subDefinition, sb);
      sb.append("){").append(subDefinition.getMinOccurs()).append(",");

      if (subDefinition.getMaxOccurs() != -1) {
        sb.append(subDefinition.getMaxOccurs());
      }
      sb.append("}");
    }

    if (definition.getLength() <= 0) {
      sb.append("\\n");
    }

  }

  public Pattern localPattern(RecordDefinition definition) {
    StringBuilder sb = new StringBuilder();
    localPattern(definition, sb);
    return Pattern.compile(sb.toString());
  }

  private void localPattern(RecordDefinition definition, StringBuilder sb) {
    if (definition.getProperties().isEmpty()) {
      return;
    }

    int currentRow = 0;
    int length = 0;
    for (Iterator<Property> iter = definition.getProperties().iterator(); iter.hasNext();) {
      Property property = iter.next();
      if (property.getRow() != currentRow) {
        currentRow = property.getRow();
        addFiller(sb, definition.getLength(), length);
        length = 0;
        sb.append("\\n");
      }
      length += property.getLength();
      if (property.getFixedValue() != null) {
        sb.append("(" + property.getFixedValue() + ")");
      } else if (definition.getDelimiter() != null && property.getLength() <= 0) {
        sb.append("([^\\" + definition.getDelimiter() + "^\\n]*)");
      } else {
        sb.append("([\\w\\W]{").append(property.getLength()).append("})");
      }
      if (iter.hasNext() && !"".equals(definition.getDelimiter())) {
        sb.append("\\" + definition.getDelimiter());
        length++;
      }
    }

    addFiller(sb, definition.getLength(), length);
  }
}
