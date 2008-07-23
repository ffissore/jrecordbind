package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;

public class Unmarshaller<E> {

  Map<String, Converter<?>> converters;
  RecordDefinition definition;
  String fqRecordClassName;
  Pattern pattern;

  public Unmarshaller(InputStream input) throws IOException {
    definition = new DefinitionLoader().load(input).getDefinition();
    pattern = new RegexGenerator(definition).pattern();
    fqRecordClassName = definition.getPackageName() + "." + definition.getClassName();
    converters = new HashMap<String, Converter<?>>();
    loadConverters();
  }

  private void loadConverters() {
    for (Iterator<Property> iter = definition.getProperties().iterator(); iter.hasNext();) {
      Property property = iter.next();
      if (!converters.containsKey(property.getConverter())) {
        try {
          converters.put(property.getConverter(), (Converter<?>) Class.forName(property.getConverter()).newInstance());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  public Iterator<E> unmarshall(InputStream input) {
    final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    return new Iterator<E>() {

      private String line;

      @Override
      public boolean hasNext() {
        try {
          line = reader.readLine();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        return line != null;
      }

      @Override
      public E next() {
        try {
          return parse();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      @SuppressWarnings("unchecked")
      private E parse() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        final E record = (E) Class.forName(fqRecordClassName).newInstance();
        final Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          int propCount = 1;
          for (Iterator<Property> iter = definition.getProperties().iterator(); iter.hasNext();) {
            Property property = iter.next();
            try {
              Object convert = converters.get(property.getConverter()).convert(matcher.group(propCount++));
              PropertyUtils.setProperty(record, property.getName(), convert);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }
        }
        return record;
      }

      @Override
      public void remove() {
      }

    };

  }

  public List<E> unmarshallAll(InputStream input) {
    List<E> result = new LinkedList<E>();
    for (Iterator<E> iter = unmarshall(input); iter.hasNext();) {
      result.add(iter.next());
    }
    return result;
  }

}
