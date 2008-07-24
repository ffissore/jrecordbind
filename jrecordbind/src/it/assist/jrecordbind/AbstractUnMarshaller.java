package it.assist.jrecordbind;

import java.io.IOException;
import java.io.Reader;

abstract class AbstractUnMarshaller {

  protected final RecordDefinition definition;
  protected final ConvertersMap converters;

  public AbstractUnMarshaller(Reader input) throws IOException {
    this.definition = new DefinitionLoader().load(input).getDefinition();
    this.converters = new ConvertersMap(definition.getProperties());
  }

}
