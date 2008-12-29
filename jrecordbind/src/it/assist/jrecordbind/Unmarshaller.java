package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Unmarshalls a source reader into beans. The constructor takes the record definition file, while the {@link #unmarshall(Reader)} takes the fixed length file. You'll get back an Iterator instance: each next() call will give back the next bean

 * @author Federico Fissore
 */
public class Unmarshaller<E> extends AbstractUnMarshaller {

  private final class UnmarshallerIterator<T> implements Iterator<T> {

    private final StringBuilder buffer;
    private final ConvertersMap converters;
    private final String fqRecordClassName;
    private final Pattern pattern;
    private final BufferedReader reader;

    public UnmarshallerIterator(ConvertersMap converters, BufferedReader reader) {
      this.reader = reader;
      this.buffer = new StringBuilder();
      this.converters = converters;
      this.fqRecordClassName = definition.getClassName();
      this.pattern = new RegexGenerator().deepPattern(definition);
    }

    public boolean hasNext() {
      try {
        String current = null;
        while (!patternMatches() && (current = reader.readLine()) != null) {
          buffer.append(current).append("\n");
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      return patternMatches();
    }

    private boolean patternMatches() {
      return pattern.matcher(buffer).find();
    }

    public T next() {
      try {
        T record = (T) Class.forName(fqRecordClassName).newInstance();
        Matcher matcher = pattern.matcher(buffer);
        if (matcher.find()) {
          int groupCount = 1;
          for (Iterator<Property> iter = definition.getProperties().iterator(); iter.hasNext();) {
            Property property = iter.next();
            Object convert = ((Converter) converters.get(property.getConverter())).convert(matcher.group(groupCount++));
            PropertyUtils.setProperty(record, property.getName(), convert);
          }
          for (RecordDefinition subDefinition : definition.getSubRecords()) {
            groupCount++;
            Object subRecord = Class.forName(subDefinition.getClassName()).newInstance();
            for (Iterator<Property> iter = subDefinition.getProperties().iterator(); iter.hasNext();) {
              Property property = iter.next();
              Object convert = ((Converter) converters.get(property.getConverter())).convert(matcher
                  .group(groupCount++));
              PropertyUtils.setProperty(subRecord, property.getName(), convert);
            }
            Object property = PropertyUtils.getProperty(record, subDefinition.getName());
            if (property instanceof Collection) {
              Collection<Object> collection = (Collection<Object>) property;
              collection.add(subRecord);
            } else {
              PropertyUtils.setProperty(record, subDefinition.getName(), subRecord);
            }
          }
          buffer.delete(matcher.start(), matcher.end() + 1);
        }
        return record;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    public void remove() {
    }

  }

  /**
   * Creates a new unmarshaller, reading the configuration specified in the definition properties file given as input
   * @param input the definition properties file
   * @param string 
   * @throws IOException
   */
  public Unmarshaller(InputStream input) throws Exception {
    super(input);
  }

  /**
   * Unmarshalls the input fixed length file, a bean at a time
   * @param input the input fixed length file
   * @return an Iterator: each next() call will give back the next bean
   */
  public Iterator<E> unmarshall(InputStream input) {
    return new UnmarshallerIterator<E>(converters, new BufferedReader(new InputStreamReader(input)));
  }

  /**
   * Unmarshalls the whole file and give back a list of bean. USE WITH CAUTION: for big files, this will lead to out of memory errors
   * @param input the input fixed length file
   * @return a list of beans
   */
  public List<E> unmarshallAll(InputStream input) {
    List<E> result = new LinkedList<E>();
    for (Iterator<E> iter = unmarshall(input); iter.hasNext();) {
      result.add(iter.next());
    }
    return result;
  }

}
