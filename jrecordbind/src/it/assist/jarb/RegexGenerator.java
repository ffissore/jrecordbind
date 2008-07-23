package it.assist.jarb;

import it.assist.jarb.RecordDefinition.Property;

import java.util.Iterator;
import java.util.regex.Pattern;

public class RegexGenerator {

  private final Pattern pattern;

  public RegexGenerator(RecordDefinition definition) {
    final StringBuilder sb = new StringBuilder(10);
    for (Iterator<Property> iter = definition.getProperties().iterator(); iter.hasNext();) {
      sb.append("([a-zA-Z_0-9\\s]{").append(iter.next().getLength()).append("})");
    }

    pattern = Pattern.compile(sb.toString());
  }

  public Pattern pattern() {
    return pattern;
  }
}
