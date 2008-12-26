package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Marshalls beans following the definition property file instructions
 * 
 * @author Federico Fissore
 */
public class Marshaller<E> extends AbstractUnMarshaller {

  private final Padder padder;

  /**
   * Creates a new marshaller, reading the configuration specified in the definition properties file given as input. Fields that will need padding will be left aligned
   * @param input the definition properties file
   * @throws IOException
   */
  public Marshaller(InputStream input) throws Exception {
    this(input, new Padder() {

      public String pad(String string, int length) {
        int pads = length - string.length();
        StringBuilder sb = new StringBuilder(length);
        sb.append(string);
        for (int i = 0; i < pads; i++) {
          sb.append(" ");
        }
        return sb.toString();
      }

    });
  }

  /**
   * Creates a new marshaller, reading the configuration specified in the definition properties file given as input
   * @param input the definition properties file
   * @param padder a custom padder
   * @throws IOException
   */
  public Marshaller(InputStream input, Padder padder) throws Exception {
    super(input);
    this.padder = padder;
  }

  /**
   * Marshalls a bean to a writer
   * @param record the bean to marshall
   * @param writer the target writer
   * @throws IOException
   */
  public void marshall(E record, Writer writer) throws IOException {
    marshall(record, definition, writer);
  }

  private void marshall(Object record, RecordDefinition definition, Writer writer) throws IOException {
    StringBuilder sb = new StringBuilder(definition.getLength());
    int currentRow = 0;
    for (Iterator<Property> iter = definition.getProperties().iterator(); iter.hasNext();) {
      Property property = iter.next();
      if (property.getRow() != currentRow) {
        currentRow = property.getRow();
        sb.append("\n");
      }
      try {
        sb.append(padder.pad(((Converter) converters.get(property.getConverter())).toString(PropertyUtils.getProperty(
            record, property.getName())), property.getLength()));
        if (iter.hasNext()) {
          sb.append(definition.getDelimiter());
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    sb.append('\n');

    writer.append(sb.toString());

    try {
      for (RecordDefinition subDefinition : definition.getSubRecords()) {
        Object subRecord = PropertyUtils.getProperty(record, subDefinition.getName());
        if (subRecord instanceof Collection) {
          Collection<Object> subRecords = (Collection<Object>) subRecord;
          for (Object o : subRecords) {
            marshall(o, subDefinition, writer);
          }
        } else {
          marshall(subRecord, subDefinition, writer);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
