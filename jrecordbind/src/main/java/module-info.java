module org.fissore.jrecordbind {

  exports org.fissore.jrecordbind;
  exports org.fissore.jrecordbind.converters;
  exports org.fissore.jrecordbind.padders;
  exports org.fissore.jrecordbind.util;

  requires java.logging;
  requires jakarta.xml.bind;
  requires com.sun.xml.xsom;
  requires org.glassfish.jaxb.core;

}
