package it.assist.jrecordbind.gen;

import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

public class MyRL extends ResourceLoader {

  public long getLastModified(Resource resource) {
    return 0;
  }

  public InputStream getResourceStream(String s) throws ResourceNotFoundException {
    return MyRL.class.getResourceAsStream(s);
  }

  public void init(ExtendedProperties extendedproperties) {
  }

  public boolean isSourceModified(Resource resource) {
    return false;
  }

}
