package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

public class Marshaller extends AbstractUnMarshaller {

  private final Padder padder;

  public Marshaller(Reader input) throws IOException {
    this(input, new Padder() {

      public String pad(String string, int length) {
        return StringUtils.rightPad(string, length);
      }

    });
  }

  public Marshaller(Reader input, Padder padder) throws IOException {
    super(input);
    this.padder = padder;
  }

  public void marshall(Object record, Writer writer) throws IOException {
    StringBuffer sb = new StringBuffer(definition.getLength());
    for (Iterator iter = definition.getProperties().iterator(); iter.hasNext();) {
      Property property = (Property) iter.next();
      try {
        sb.append(padder.pad(((Converter) converters.get(property.getConverter())).toString(PropertyUtils.getProperty(
            record, property.getName())), property.getLength()));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    sb.append('\n');

    writer.write(sb.toString());
  }
}
