package it.assist.jrecordbind;

import java.io.InputStream;

abstract class AbstractUnMarshaller {

  protected final RecordDefinition definition;
  protected final ConvertersMap converters;

  public AbstractUnMarshaller(InputStream input) throws Exception {
    this.definition = new DefinitionLoader().load(input).getDefinition();
    this.converters = new ConvertersMap(definition);
  }

}
