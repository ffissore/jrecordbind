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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

/**
 * Reads the .xsd definition and creates as many {@link RecordDefinition}s as
 * needed
 * 
 * @author Federico Fissore
 */
class DefinitionLoader {

  final class Visitor extends AbstractSchemaVisitor {

    private boolean choice;
    private final EvaluatorBuilder evaluatorBuilder;
    private int numberOfPropertiesFound;
    private int numberOfSubRecordsFound;
    private XSParticle particle;
    final RecordDefinition recordDefinition;
    private String setterName;

    public Visitor(EvaluatorBuilder evaluatorBuilder, RecordDefinition recordDefinition) {
      this.evaluatorBuilder = evaluatorBuilder;
      this.recordDefinition = recordDefinition;
      this.numberOfPropertiesFound = 0;
      this.numberOfSubRecordsFound = 0;
      this.choice = false;
    }

    @Override
    public void elementDecl(XSElementDecl element) {
      if (Constants.W3C_SCHEMA.equals(element.getType().getTargetNamespace())) {
        numberOfPropertiesFound++;

        ensureNotPuttingPropertiesAfterSubRecords(element);

        Property property = new Property(element.getName());

        for (Evaluator<Property, XSElementDecl> p : evaluatorBuilder.propertiesEvaluators()) {
          p.eval(property, element);
        }

        recordDefinition.getProperties().add(property);
      } else {
        numberOfSubRecordsFound++;

        RecordDefinition subDefinition = new RecordDefinition(recordDefinition) {
          @Override
          public String getGlobalPadder() {
            return recordDefinition.getGlobalPadder();
          }

          @Override
          public int getLength() {
            return recordDefinition.getLength();
          }

          @Override
          public String getPropertyDelimiter() {
            return recordDefinition.getPropertyDelimiter();
          }
        };
        recordDefinition.getSubRecords().add(subDefinition);

        if (choice && setterName != null) {
          subDefinition.setSetterName(setterName);
        } else {
          subDefinition.setSetterName(element.getName());
        }

        recordDefinition.setChoice(choice);

        for (Evaluator<RecordDefinition, XSParticle> e : evaluatorBuilder.subRecordsEvaluators()) {
          e.eval(subDefinition, particle);
        }

        XSComplexType complexType = schema.getComplexType(element.getType().getName());
        for (Evaluator<RecordDefinition, XSComplexType> e : evaluatorBuilder.typeEvaluators()) {
          e.eval(subDefinition, complexType);
        }

        complexType.getContentType().visit(new Visitor(evaluatorBuilder, subDefinition));
      }
    }

    private void ensureNotPuttingPropertiesAfterSubRecords(XSElementDecl element) {
      if (numberOfSubRecordsFound > 0) {
        throw new IllegalArgumentException(
            "You are mixing records and properties! That is ILLEGAL (current element name: " + element.getName()
                + ", type:" + element.getType().getName() + ")");
      }
    }

    @Override
    public void modelGroup(XSModelGroup xsmodelgroup) {
      if (XSModelGroup.CHOICE.equals(xsmodelgroup.getCompositor())) {
        this.choice = true;
        this.setterName = xsmodelgroup.getForeignAttribute(Constants.JRECORDBIND_XSD, "setter");
      }

      for (XSParticle part : xsmodelgroup.getChildren()) {
        particle(part);
      }
    }

    @Override
    public void particle(XSParticle xsparticle) {
      this.particle = xsparticle;
      particle.getTerm().visit(this);
    }

  }

  private final EvaluatorBuilder evaluatorBuilder;
  private final RecordDefinition recordDefinition;
  final XSSchema schema;

  public DefinitionLoader(Reader input) {
    this.recordDefinition = new RecordDefinition();
    this.schema = findSchema(input);
    this.evaluatorBuilder = new EvaluatorBuilder(schema);
  }

  private XSSchema findSchema(Reader input) {
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

    for (XSSchema s : result.getSchemas()) {
      if (!Constants.W3C_SCHEMA.equals(s.getTargetNamespace())) {
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
   * @return this loader
   */
  public DefinitionLoader load() {
    XSElementDecl mainElement = schema.getElementDecl("main");

    for (Evaluator<RecordDefinition, XSElementDecl> e : evaluatorBuilder.mainElementEvaluators()) {
      e.eval(recordDefinition, mainElement);
    }

    XSComplexType mainRecordType = mainElement.getType().asComplexType();
    for (Evaluator<RecordDefinition, XSComplexType> e : evaluatorBuilder.typeEvaluators()) {
      e.eval(recordDefinition, mainRecordType);
    }

    mainRecordType.getContentType().visit(new Visitor(evaluatorBuilder, recordDefinition));

    return this;
  }

}
