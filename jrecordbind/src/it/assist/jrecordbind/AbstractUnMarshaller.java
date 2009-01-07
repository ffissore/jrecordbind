package it.assist.jrecordbind;

import java.io.Reader;

abstract class AbstractUnMarshaller {

  protected final RecordDefinition definition;
  protected final ConvertersCache converters;
  protected final PaddersCache padders;

  public AbstractUnMarshaller(Reader input) {
    this.definition = new DefinitionLoader().load(input).getDefinition();
    this.converters = new ConvertersCache(definition);
    this.padders = new PaddersCache(definition);
  }

}
