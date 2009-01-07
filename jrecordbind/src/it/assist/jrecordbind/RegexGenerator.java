package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Generates the regular expression the matches the {@link RecordDefinition}

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
      sb.append("(\\n");
      deepPattern(subDefinition, sb);
      sb.append("){").append(subDefinition.getMinOccurs()).append(",");

      if (subDefinition.getMaxOccurs() != -1) {
        sb.append(subDefinition.getMaxOccurs());
      }
      sb.append("}");
    }
  }

  public Pattern localPattern(RecordDefinition definition) {
    StringBuilder sb = new StringBuilder();
    localPattern(definition, sb);
    return Pattern.compile(sb.toString());
  }

  private void localPattern(RecordDefinition definition, StringBuilder sb) {
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
