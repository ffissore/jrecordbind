package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.io.Reader;
import java.util.Map;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.tools.xjc.model.CBuiltinLeafInfo;
import com.sun.tools.xjc.model.nav.NType;
import com.sun.xml.bind.api.impl.NameConverter;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XmlString;
import com.sun.xml.xsom.parser.XSOMParser;

public class DefinitionLoader {

  final class Visitor extends AbstractSchemaVisitor {

    private final RecordDefinition recordDefinition;
    private XSParticle particle;

    public Visitor(RecordDefinition recordDefinition) {
      this.recordDefinition = recordDefinition;
    }

    @Override
    public void modelGroup(XSModelGroup xsmodelgroup) {
      for (XSParticle part : xsmodelgroup.getChildren()) {
        particle(part);
      }
    }

    @Override
    public void particle(XSParticle xsparticle) {
      this.particle = xsparticle;
      particle.getTerm().visit(this);
    }

    @Override
    public void elementDecl(XSElementDecl element) {
      if (W3C_SCHEMA.equals(element.getType().getTargetNamespace())) {
        Property property = new Property(element.getName());
        recordDefinition.getProperties().add(property);
        String row = element.getForeignAttribute(JRECORDBIND_XSD, "row");
        row = row != null ? row : "0";
        recordDefinition.addRowNumber(row);
        property.setRow(Integer.parseInt(row));

        XmlString fixedValue = element.getFixedValue();
        if (fixedValue != null) {
          property.setFixedValue(fixedValue.value);
        }
        property.setType(toJavaType(element.getType().getName()));
        property.setLength(Integer.valueOf(element.getForeignAttribute(JRECORDBIND_XSD, "length")).intValue());
        String converter = element.getForeignAttribute(JRECORDBIND_XSD, "converter");
        if (converter != null) {
          property.setConverter(converter);
        } else {
          property.setConverter("it.assist.jrecordbind.converters." + property.getType() + "Converter");
        }
      } else {
        RecordDefinition subDefinition = new RecordDefinition(element.getName()) {
          @Override
          public String getDelimiter() {
            return recordDefinition.getDelimiter();
          }

          @Override
          public int getLength() {
            return recordDefinition.getLength();
          }
        };
        recordDefinition.getSubRecords().add(subDefinition);

        subDefinition.setMinOccurs(particle.getMinOccurs());
        subDefinition.setMaxOccurs(particle.getMaxOccurs());

        XSComplexType complexType = schema.getComplexType(element.getType().getName());
        subDefinition.setFullName(complexType.getName(), NameConverter.standard.toPackageName(schema
            .getTargetNamespace()));

        complexType.getContentType().visit(new Visitor(subDefinition));
      }
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

  }

  private static final String JRECORDBIND_XSD = "http://jrecordbind.dev.java.net/2/xsd";
  private static final String W3C_SCHEMA = "http://www.w3.org/2001/XMLSchema";

  private final RecordDefinition recordDefinition;
  private XSSchema schema;

  public DefinitionLoader() {
    this.recordDefinition = new RecordDefinition();
  }

  private XSSchema findSchema(XSSchemaSet result) {
    for (XSSchema schema : result.getSchemas()) {
      if (!W3C_SCHEMA.equals(schema.getTargetNamespace())) {
        return schema;
      }
    }
    throw new IllegalArgumentException("Unable to find NON W3C namespace");
  }

  public RecordDefinition getDefinition() {
    return recordDefinition;
  }

  public DefinitionLoader load(Reader input) {
    XSOMParser parser = new XSOMParser();
    parser.setErrorHandler(new ErrorHandler() {

      @Override
      public void error(SAXParseException exception) {
        throw new RuntimeException(exception);
      }

      @Override
      public void fatalError(SAXParseException exception) {
        throw new RuntimeException(exception);
      }

      @Override
      public void warning(SAXParseException exception) {
        throw new RuntimeException(exception);
      }

    });

    XSSchemaSet result;
    try {
      parser.parse(input);

      result = parser.getResult();
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }

    schema = findSchema(result);

    XSElementDecl elementDecl = schema.getElementDecl("main");
    recordDefinition.setDelimiter(elementDecl.getForeignAttribute(JRECORDBIND_XSD, "delimiter"));
    recordDefinition.setLength(Integer.parseInt(elementDecl.getForeignAttribute(JRECORDBIND_XSD, "length")));

    XSComplexType asComplexType = elementDecl.getType().asComplexType();
    asComplexType.getContentType().visit(new Visitor(recordDefinition));

    recordDefinition.setFullName(asComplexType.getName(), NameConverter.standard.toPackageName(schema
        .getTargetNamespace()));

    return this;
  }
}
