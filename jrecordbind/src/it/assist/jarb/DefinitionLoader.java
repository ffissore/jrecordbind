package it.assist.jarb;

import it.assist.jarb.RecordDefinition.Property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefinitionLoader {

  private final static Pattern propertyName;

  static {
    propertyName = Pattern.compile("row\\.(\\d+)\\.(\\w+)");
  }

  private RecordDefinition recordDefinition;

  public DefinitionLoader() {
    recordDefinition = new RecordDefinition();
  }

  public RecordDefinition getDefinition() {
    return recordDefinition;
  }

  private Property getProperty(final String name) {
    Property property = null;
    Iterator<Property> iter = recordDefinition.getProperties().iterator();
    while (property == null && iter.hasNext()) {
      Property current = iter.next();
      if (name.equals(current.getName())) {
        property = current;
      }
    }

    if (property == null) {
      property = new Property(name);
      recordDefinition.getProperties().add(property);
    }

    return property;
  }

  public DefinitionLoader load(InputStream input) throws IOException {
    BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(input));
    String line = null;
    while ((line = inputStreamReader.readLine()) != null) {
      String[] split = line.split("=");
      if (split.length == 2) {
        Matcher propertyNameMatcher = propertyName.matcher(split[0].trim());
        if ("classname".equals(split[0].trim())) {
          String fqn = split[1].trim();
          recordDefinition.setClassName(fqn.substring(fqn.lastIndexOf(".") + 1));
          recordDefinition.setPackageName(fqn.substring(0, fqn.lastIndexOf(".")));
        } else if (propertyNameMatcher.find()) {
          Property property = getProperty(propertyNameMatcher.group(2));
          String[] params = split[1].trim().split(",");
          property.setLength(Integer.valueOf(params[0].trim()).intValue());
          property.setType(params[1].trim());
          if (params.length > 2) {
            property.setConverter(params[2].trim());
          } else {
            property.setConverter("it.assist.jarb.converters." + property.getType() + "Converter");
          }
        }
      }
    }

    return this;
  }

}