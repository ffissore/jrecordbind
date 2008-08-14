package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.util.Iterator;
import java.util.regex.Pattern;

public class RegexGenerator {

  private final Pattern pattern;

  public RegexGenerator(RecordDefinition definition) {
    final StringBuffer sb = new StringBuffer(100);
    for (Iterator iter = definition.getProperties().iterator(); iter.hasNext();) {
      sb.append("([a-zA-Z_0-9\\s]{").append(((Property) iter.next()).getLength()).append("})");
      if (iter.hasNext() && !"".equals(definition.getSeparator())) {
        sb.append("\\" + definition.getSeparator());
      }
    }

    pattern = Pattern.compile(sb.toString());
  }

  public Pattern pattern() {
    return pattern;
  }
}
