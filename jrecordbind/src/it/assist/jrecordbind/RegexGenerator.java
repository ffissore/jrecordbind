package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Generates the regular expression the matches the {@link RecordDefinition}

 * @author Federico Fissore
 */
class RegexGenerator {

  private final Pattern pattern;

  public RegexGenerator(RecordDefinition definition) {
    final StringBuilder sb = new StringBuilder(100);
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
        sb.append("([a-zA-Z_0-9\\s]{").append(property.getLength()).append("})");
      }
      if (iter.hasNext() && !"".equals(definition.getDelimiter())) {
        sb.append("\\" + definition.getDelimiter());
        length++;
      }
    }

    addFiller(sb, definition.getLength(), length);

    for (Iterator<RecordDefinition> iter = definition.getSubRecords().iterator(); iter.hasNext();) {
      RecordDefinition subDefinition = iter.next();
      sb.append("(\\n").append(new RegexGenerator(subDefinition).pattern().pattern()).append(")");
      sb.append("{").append(subDefinition.getMinOccurs()).append(",");

      if (subDefinition.getMaxOccurs() != -1) {
        sb.append(subDefinition.getMaxOccurs());
      }
      sb.append("}");
    }

    pattern = Pattern.compile(sb.toString());
  }

  private void addFiller(final StringBuilder sb, int definitionLength, int length) {
    int fillerLength = definitionLength - length;
    if (fillerLength > 0) {
      sb.append("[\\s]{").append(fillerLength).append("}");
    }
  }

  public Pattern pattern() {
    return pattern;
  }
}
