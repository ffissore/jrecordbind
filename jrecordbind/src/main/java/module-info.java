module org.fissore.jrecordbind {

  exports org.fissore.jrecordbind;
  exports org.fissore.jrecordbind.converters;
  exports org.fissore.jrecordbind.padders;
  exports org.fissore.jrecordbind.util;

  requires java.logging;
  requires com.sun.xml.bind;
  requires com.sun.xml.xsom;

}
