package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Marshalls beans following the definition property file instructions
 * 
 * @author Federico Fissore
 */
public class Marshaller extends AbstractUnMarshaller {

  private final Padder padder;

  /**
   * Creates a new marshaller, reading the configuration specified in the definition properties file given as input. Fields that will need padding will be left aligned
   * @param input the definition properties file
   * @throws IOException
   */
  public Marshaller(Reader input) throws IOException {
    this(input, new Padder() {

      public String pad(String string, int length) {
        int pads = length - string.length();
        StringBuffer sb = new StringBuffer(length);
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
  public Marshaller(Reader input, Padder padder) throws IOException {
    super(input);
    this.padder = padder;
  }

  /**
   * Marshalls a bean to a writer
   * @param record the bean to marshall
   * @param writer the target writer
   * @throws IOException
   */
  public void marshall(Object record, Writer writer) throws IOException {
    StringBuffer sb = new StringBuffer(definition.getLength());
    for (Iterator iter = definition.getProperties().iterator(); iter.hasNext();) {
      Property property = (Property) iter.next();
      try {
        sb.append(padder.pad(((Converter) converters.get(property.getConverter())).toString(PropertyUtils.getProperty(
            record, property.getName())), property.getLength()));
        if (iter.hasNext()) {
          sb.append(definition.getSeparator());
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    sb.append('\n');

    writer.write(sb.toString());
  }
}
