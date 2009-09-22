package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.tools.xjc.model.CBuiltinLeafInfo;
import com.sun.tools.xjc.model.nav.NType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XmlString;

public class PropertyEvaluatorBuilder {

  private final PropertyEvaluator converterEval;
  private final PropertyEvaluator fixedValueEval;
  private final PropertyEvaluator lengthEval;
  private final PropertyEvaluator padderEval;
  private PropertyEvaluator rowEval;
  private PropertyEvaluator typeEval;

  public PropertyEvaluatorBuilder() {
    this.converterEval = new PropertyEvaluator() {

      @Override
      public void eval(Property property, XSElementDecl element) {
        String converter = element.getForeignAttribute(DefinitionLoader.JRECORDBIND_XSD, "converter");
        if (converter != null) {
          property.setConverter(converter);
        } else {
          property.setConverter("it.assist.jrecordbind.converters." + property.getType() + "Converter");
        }
      }
    };

    this.fixedValueEval = new PropertyEvaluator() {

      @Override
      public void eval(Property property, XSElementDecl element) {
        XmlString fixedValue = element.getFixedValue();
        if (fixedValue != null) {
          property.setFixedValue(fixedValue.value);
        }
      }
    };

    this.lengthEval = new PropertyEvaluator() {

      @Override
      public void eval(Property property, XSElementDecl element) {
        String length = element.getForeignAttribute(DefinitionLoader.JRECORDBIND_XSD, "length");
        if (length != null) {
          property.setLength(Integer.parseInt(length));
        }
      }
    };

    this.padderEval = new PropertyEvaluator() {

      @Override
      public void eval(Property property, XSElementDecl element) {
        property.setPadder(element.getForeignAttribute(DefinitionLoader.JRECORDBIND_XSD, "padder"));
      }
    };

    this.rowEval = new PropertyEvaluator() {

      @Override
      public void eval(Property property, XSElementDecl element) {
        String row = element.getForeignAttribute(DefinitionLoader.JRECORDBIND_XSD, "row");
        row = row != null ? row : "0";
        property.setRow(Integer.parseInt(row));
      }
    };

    this.typeEval = new PropertyEvaluator() {

      @Override
      public void eval(Property property, XSElementDecl element) {
        property.setType(toJavaType(element.getType().getName()));
      }

      private String toJavaType(String name) {
        if (name.startsWith("date")) {
          return "java.util.Date";
        }
        for (Map.Entry<NType, CBuiltinLeafInfo> entry : CBuiltinLeafInfo.LEAVES.entrySet()) {
          if (name.equals(entry.getValue().getTypeNames()[0].getLocalPart())) {
            return entry.getKey().fullName().replaceAll("java\\.lang\\.", "");
          }
        }
        throw new IllegalArgumentException(name);
      }
    };
  }

  public List<PropertyEvaluator> properties() {
    LinkedList<PropertyEvaluator> list = new LinkedList<PropertyEvaluator>();
    list.add(rowEval);
    list.add(fixedValueEval);
    list.add(typeEval);
    list.add(lengthEval);
    list.add(converterEval);
    list.add(padderEval);

    return list;
  }

}
