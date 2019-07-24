package org.fissore.jrecordbindtests;

import org.fissore.jrecordbind.DefinitionLoader;
import org.fissore.jrecordbind.RecordDefinition;
import org.xml.sax.InputSource;

import java.io.InputStreamReader;
import java.io.Reader;

public class Utils {

  public static InputSource toInputSource(String file) {
    return new InputSource(Utils.class.getResource(file).toExternalForm());
  }

  public static RecordDefinition loadDefinition(String file) {
    return new DefinitionLoader().load(toInputSource(file));
  }

  public static Reader openStream(String file) {
    return new InputStreamReader(Utils.class.getResourceAsStream(file));
  }
}
