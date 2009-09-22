package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import com.sun.xml.xsom.XSElementDecl;

public interface PropertyEvaluator {

  public void eval(Property property, XSElementDecl element);

}
