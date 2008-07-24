package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;

public class Unmarshaller<E> extends AbstractUnMarshaller {

  private static final class UnmarshallerIterator<E> implements Iterator<E> {

    private final Map<String, Converter<?>> converters;
    private final RecordDefinition definition;
    private final String fqRecordClassName;
    private String line;
    private final Pattern pattern;
    private final BufferedReader reader;

    public UnmarshallerIterator(RecordDefinition definition, Map<String, Converter<?>> converters, BufferedReader reader) {
      this.converters = converters;
      this.reader = reader;
      this.definition = definition;
      this.fqRecordClassName = definition.getPackageName() + "." + definition.getClassName();
      this.pattern = new RegexGenerator(definition).pattern();
    }

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
    private E parse() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
        InvocationTargetException, NoSuchMethodException {
      final E record = (E) Class.forName(fqRecordClassName).newInstance();
      final Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
        int propCount = 1;
        for (Iterator<Property> iter = definition.getProperties().iterator(); iter.hasNext();) {
          Property property = iter.next();
          Object convert = converters.get(property.getConverter()).convert(matcher.group(propCount++));
          PropertyUtils.setProperty(record, property.getName(), convert);
        }
      }
      return record;
    }

    @Override
    public void remove() {
    }
  }

  public Unmarshaller(Reader input) throws IOException {
    super(input);
  }

  public Iterator<E> unmarshall(Reader input) {
    BufferedReader reader = new BufferedReader(input);

    return new UnmarshallerIterator<E>(definition, converters, reader);

  }

  public List<E> unmarshallAll(Reader input) {
    List<E> result = new LinkedList<E>();
    for (Iterator<E> iter = unmarshall(input); iter.hasNext();) {
      result.add(iter.next());
    }
    return result;
  }

}
