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

import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;

class Visitor extends AbstractSchemaVisitor {

  private final EvaluatorBuilder evaluatorBuilder;
  private XSModelGroup modelGroup;
  private int numberOfSubRecordsFound;
  private XSParticle particle;
  private final RecordDefinition recordDefinition;
  private final XSSchema schema;

  public Visitor(EvaluatorBuilder evaluatorBuilder, XSSchema schema, RecordDefinition recordDefinition) {
    this.evaluatorBuilder = evaluatorBuilder;
    this.schema = schema;
    this.recordDefinition = recordDefinition;
    this.numberOfSubRecordsFound = 0;
  }

  @Override
  public void elementDecl(XSElementDecl element) {
    if (Constants.W3C_SCHEMA.equals(element.getType().getTargetNamespace())) {
      ensureNotPuttingPropertiesAfterSubRecords(element);

      Property property = new Property(element.getName());

      for (Evaluator<Property, XSElementDecl> p : evaluatorBuilder.propertiesEvaluators()) {
        p.eval(property, element);
      }

      recordDefinition.getProperties().add(property);
    } else {
      numberOfSubRecordsFound++;

      RecordDefinition subDefinition = new RecordDefinition(recordDefinition);
      subDefinition.setGlobalPadder(recordDefinition.getGlobalPadder());
      subDefinition.setLength(recordDefinition.getLength());
      subDefinition.setPropertyDelimiter(recordDefinition.getPropertyDelimiter());
      recordDefinition.getSubRecords().add(subDefinition);
      recordDefinition.setChoice(modelGroup != null);

      subDefinition.setSetterName(element.getName());

      if (recordDefinition.isChoice() && modelGroup.getForeignAttribute(Constants.JRECORDBIND_XSD, "setter") != null) {
        subDefinition.setSetterName(modelGroup.getForeignAttribute(Constants.JRECORDBIND_XSD, "setter"));
      }

      for (Evaluator<RecordDefinition, XSParticle> e : evaluatorBuilder.subRecordsEvaluators()) {
        e.eval(subDefinition, particle);
      }

      XSComplexType complexType = schema.getComplexType(element.getType().getName());
      for (Evaluator<RecordDefinition, XSComplexType> e : evaluatorBuilder.typeEvaluators()) {
        e.eval(subDefinition, complexType);
      }

      complexType.getContentType().visit(new Visitor(evaluatorBuilder, schema, subDefinition));
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
      this.modelGroup = xsmodelgroup;
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