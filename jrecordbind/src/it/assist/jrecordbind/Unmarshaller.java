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

/**
 * Unmarshalls a source reader into beans. The constructor takes the record definition file, while the {@link #unmarshall(Reader)} takes the fixed length file. You'll get back an Iterator instance: each next() call will give back the next bean

 * @author Federico Fissore
 */
public class Unmarshaller extends AbstractUnMarshaller {

  private static final class UnmarshallerIterator implements Iterator {

    private final Map converters;
    private final RecordDefinition definition;
    private final String fqRecordClassName;
    private String line;
    private final Pattern pattern;
    private final BufferedReader reader;

    public UnmarshallerIterator(RecordDefinition definition, Map converters, BufferedReader reader) {
      this.converters = converters;
      this.reader = reader;
      this.definition = definition;
      this.fqRecordClassName = definition.getPackageName() + "." + definition.getClassName();
      this.pattern = new RegexGenerator(definition).pattern();
    }

    public boolean hasNext() {
      try {
        line = reader.readLine();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return line != null;
    }

    public Object next() {
      try {
        return parse();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    private Object parse() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
        InvocationTargetException, NoSuchMethodException {
      Object record = Class.forName(fqRecordClassName).newInstance();
      Matcher matcher = pattern.matcher(line);
      if (matcher.find()) {
        int groupCount = 1;
        for (Iterator iter = definition.getProperties().iterator(); iter.hasNext();) {
          Property property = (Property) iter.next();
          Object convert = ((Converter) converters.get(property.getConverter())).convert(matcher.group(groupCount++));
          PropertyUtils.setProperty(record, property.getName(), convert);
        }
      }
      return record;
    }

    public void remove() {
    }
  }

  /**
   * Creates a new unmarshaller, reading the configuration specified in the definition properties file given as input
   * @param input the definition properties file
   * @throws IOException
   */
  public Unmarshaller(Reader input) throws IOException {
    super(input);
  }

  /**
   * Unmarshalls the input fixed length file, a bean at a time
   * @param input the input fixed length file
   * @return an Iterator: each next() call will give back the next bean
   */
  public Iterator unmarshall(Reader input) {
    BufferedReader reader = new BufferedReader(input);

    return new UnmarshallerIterator(definition, converters, reader);
  }

  /**
   * Unmarshalls the whole file and give back a list of bean. USE WITH CAUTION: for big files, this will lead to out of memory errors
   * @param input the input fixed length file
   * @return a list of beans
   */
  public List unmarshallAll(Reader input) {
    List result = new LinkedList();
    for (Iterator iter = unmarshall(input); iter.hasNext();) {
      result.add(iter.next());
    }
    return result;
  }

}
