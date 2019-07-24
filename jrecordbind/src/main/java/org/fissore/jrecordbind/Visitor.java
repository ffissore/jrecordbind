/*
 * JRecordBind, fixed-length file (un)marshaller
 * Copyright 2019, Federico Fissore, and individual contributors. See
 * AUTHORS.txt in the distribution for a full listing of individual
 * contributors.
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

package org.fissore.jrecordbind;

import com.sun.xml.bind.api.impl.NameConverter;
import com.sun.xml.xsom.*;
import org.fissore.jrecordbind.RecordDefinition.Property;

import static org.fissore.jrecordbind.Constants.JRECORDBIND_XSD;
import static org.fissore.jrecordbind.Constants.W3C_SCHEMA;

class Visitor extends AbstractSchemaVisitor {

  private final Evaluators evaluators;
  private final XSSchema mainSchema;
  private final XSSchemaSet schemas;
  private final RecordDefinition recordDefinition;
  private int numberOfSubRecordsFound;
  private XSModelGroup modelGroup;
  private XSParticle particle;

  public Visitor(Evaluators evaluators, XSSchema mainSchema, XSSchemaSet schemas, RecordDefinition recordDefinition) {
    this.evaluators = evaluators;
    this.mainSchema = mainSchema;
    this.schemas = schemas;
    this.recordDefinition = recordDefinition;
    this.numberOfSubRecordsFound = 0;
  }

  @Override
  public void elementDecl(XSElementDecl element) {
    if (W3C_SCHEMA.equals(element.getType().getTargetNamespace())) {
      ensureNotPuttingPropertiesAfterSubRecords(element);

      Property property = new Property(NameConverter.standard.toVariableName(element.getName()));

      for (Evaluator<Property, XSElementDecl> p : evaluators.propertiesEvaluators()) {
        p.eval(property, element);
      }

      recordDefinition.getProperties().add(property);
    } else if (mainSchema.getSimpleType(element.getType().getName()) != null) {
      XSSimpleType simpleType = mainSchema.getSimpleType(element.getType().getName());

      Property property = new Property(element.getName());

      for (Evaluator<Property, XSElementDecl> p : evaluators.simpleTypePropertyEvaluators()) {
        p.eval(property, element);
      }
      for (Evaluator<Property, XSSimpleType> e : evaluators.simpleTypeEvaluators()) {
        e.eval(property, simpleType);
      }

      recordDefinition.getProperties().add(property);
    } else {
      numberOfSubRecordsFound++;

      RecordDefinition subDefinition = new RecordDefinition(recordDefinition);
      recordDefinition.getSubRecords().add(subDefinition);
      recordDefinition.setChoice(modelGroup != null);

      subDefinition.setSetterName(element.getName());

      if (recordDefinition.isChoice() && modelGroup.getForeignAttribute(JRECORDBIND_XSD, "setter") != null) {
        subDefinition.setSetterName(modelGroup.getForeignAttribute(JRECORDBIND_XSD, "setter"));
      }

      for (Evaluator<RecordDefinition, XSParticle> e : evaluators.subRecordsEvaluators()) {
        e.eval(subDefinition, particle);
      }

      XSComplexType complexType = schemas.getComplexType(element.getType().getTargetNamespace(), element.getType()
        .getName());
      for (Evaluator<RecordDefinition, XSComplexType> e : evaluators.typeEvaluators()) {
        e.eval(subDefinition, complexType);
      }

      complexType.getContentType().visit(new Visitor(evaluators, mainSchema, schemas, subDefinition));
    }
  }

  private void ensureNotPuttingPropertiesAfterSubRecords(XSElementDecl element) {
    if (numberOfSubRecordsFound > 0) {
      throw new IllegalArgumentException(
        String.format("Mixing records and properties is not supported: records have to go after properties (current element name: %s, type: %s)",
          element.getName(), element.getType().getName()));
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
