package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;
import it.assist.jrecordbind.padders.SpaceRightPadder;

import java.io.IOException;
import java.io.Reader;
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

  private final Padder defaultPadder;

  /**
   * Creates a new marshaller, reading the configuration specified in the
   * definition properties file given as input. Fields that will need padding
   * will be left aligned
   * 
   * @param input
   *          the definition properties file
   */
  public Marshaller(Reader input) {
    this(input, new SpaceRightPadder());
  }

  /**
   * Creates a new marshaller, reading the configuration specified in the
   * definition properties file given as input
   * 
   * @param input
   *          the definition properties file
   * @param defaultPadder
   *          a custom padder
   */
  public Marshaller(Reader input, Padder defaultPadder) {
    super(input);
    this.defaultPadder = defaultPadder;
  }

  private void addFiller(final StringBuilder sb, int definitionLength, int length) {
    int fillerLength = definitionLength - length;
    while (fillerLength-- >= 0) {
      sb.append(" ");
    }
  }

  private Object ensureCorrectLength(int length, String value) {
    if (value.length() > length) {
      return value.substring(0, length);
    }
    return value;
  }

  /**
   * Marshalls a bean to a writer
   * 
   * @param record
   *          the bean to marshall
   * @param writer
   *          the target writer
   * @throws IOException
   */
  public void marshall(E record, Writer writer) throws IOException {
    marshall(record, definition, writer);
  }

  private void marshall(Object record, RecordDefinition currentDefinition, Writer writer) throws IOException {
    StringBuilder sb = new StringBuilder(currentDefinition.getLength());
    int currentRow = 0;
    int length = 0;
    Padder currentPadder;
    for (Iterator<Property> iter = currentDefinition.getProperties().iterator(); iter.hasNext();) {
      Property property = iter.next();
      if (property.getRow() != currentRow) {
        currentRow = property.getRow();
        addFiller(sb, currentDefinition.getLength(), length);
        length = 0;
        sb.append("\n");
      }
      if (property.getPadder() != null) {
        currentPadder = padders.get(property.getPadder());
      } else {
        currentPadder = defaultPadder;
      }
      try {
        String value = currentPadder.pad(converters.get(property.getConverter()).toString(
            PropertyUtils.getProperty(record, property.getName())), property.getLength());
        sb.append(ensureCorrectLength(property.getLength(), value));
        length += property.getLength();
        if (iter.hasNext()) {
          sb.append(currentDefinition.getDelimiter());
          length += currentDefinition.getDelimiter().length();
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    addFiller(sb, currentDefinition.getLength(), length);

    sb.append('\n');

    writer.append(sb.toString());

    try {
      for (RecordDefinition subDefinition : currentDefinition.getSubRecords()) {
        Object subRecord = PropertyUtils.getProperty(record, subDefinition.getSetterName());
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
