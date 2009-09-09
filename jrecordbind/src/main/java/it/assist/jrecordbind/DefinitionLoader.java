/*
 * JRecordBind, fixed-length file (un)marshaller
 * Copyright 2009, Assist s.r.l., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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

/**
 * Reads the .xsd definition and creates as many {@link RecordDefinition}s as
 * needed
 * 
 * @author Federico Fissore
 */
class DefinitionLoader {

  final class Visitor extends AbstractSchemaVisitor {

    private final RecordDefinition recordDefinition;
    private XSParticle particle;
    private int numberOfPropertiesFound;
    private int numberOfSubRecordsFound;

    public Visitor(RecordDefinition recordDefinition) {
      this.recordDefinition = recordDefinition;
      this.numberOfPropertiesFound = 0;
      this.numberOfSubRecordsFound = 0;
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
        String row = element.getForeignAttribute(JRECORDBIND_XSD, "row");
        row = row != null ? row : "0";
        property.setRow(Integer.parseInt(row));

        XmlString fixedValue = element.getFixedValue();
        if (fixedValue != null) {
          property.setFixedValue(fixedValue.value);
        }
        property.setType(toJavaType(element.getType().getName()));
        String length = element.getForeignAttribute(JRECORDBIND_XSD, "length");
        if (length != null) {
          property.setLength(Integer.parseInt(length));
        }
        String converter = element.getForeignAttribute(JRECORDBIND_XSD, "converter");
        if (converter != null) {
          property.setConverter(converter);
        } else {
          property.setConverter("it.assist.jrecordbind.converters." + property.getType() + "Converter");
        }
        property.setPadder(element.getForeignAttribute(JRECORDBIND_XSD, "padder"));

        if (numberOfSubRecordsFound > 0) {
          throw new IllegalArgumentException("You are defining a simple type (name: " + element.getName() + ", type:"
              + element.getType().getName() + ") but you have already defined a sub record");
        }
        numberOfPropertiesFound++;
        recordDefinition.getProperties().add(property);
      } else {
        numberOfSubRecordsFound++;
        RecordDefinition subDefinition = new RecordDefinition(element.getName()) {
          @Override
          public String getDelimiter() {
            return recordDefinition.getDelimiter();
          }

          @Override
          public int getLength() {
            return recordDefinition.getLength();
          }

          @Override
          public String getGlobalPadder() {
            return recordDefinition.getGlobalPadder();
          }
        };
        recordDefinition.getSubRecords().add(subDefinition);

        subDefinition.setMinOccurs(particle.getMinOccurs());
        subDefinition.setMaxOccurs(particle.getMaxOccurs());

        XSComplexType complexType = schema.getComplexType(element.getType().getName());
        subDefinition.setClassName(complexType.getName(), NameConverter.standard.toPackageName(schema
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
    for (XSSchema s : result.getSchemas()) {
      if (!W3C_SCHEMA.equals(s.getTargetNamespace())) {
        return s;
      }
    }
    throw new IllegalArgumentException("Unable to find NON W3C namespace");
  }

  /**
   * Gets the created definition
   * 
   * @return the created definition
   */
  public RecordDefinition getDefinition() {
    return recordDefinition;
  }

  /**
   * Parses the input .xsd and creates as many {@link RecordDefinition}s as
   * needed
   * 
   * @param input
   *          the .xsd definition
   * @return this loader
   */
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
    recordDefinition.setGlobalPadder(elementDecl.getForeignAttribute(JRECORDBIND_XSD, "padder"));
    String length = elementDecl.getForeignAttribute(JRECORDBIND_XSD, "length");
    if (length != null) {
      recordDefinition.setLength(Integer.parseInt(length));
    }

    XSComplexType asComplexType = elementDecl.getType().asComplexType();
    asComplexType.getContentType().visit(new Visitor(recordDefinition));

    String subclass = asComplexType.getForeignAttribute(JRECORDBIND_XSD, "subclass");
    if (subclass != null) {
      recordDefinition.setClassName(subclass);
    } else {
      recordDefinition.setClassName(NameConverter.standard.toClassName(asComplexType.getName()), NameConverter.standard
          .toPackageName(schema.getTargetNamespace()));
    }

    return this;
  }
}
