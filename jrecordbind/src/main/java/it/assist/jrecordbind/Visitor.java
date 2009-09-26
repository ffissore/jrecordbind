package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;

class Visitor extends AbstractSchemaVisitor {

  private final EvaluatorBuilder evaluatorBuilder;
  private int numberOfSubRecordsFound;
  private XSParticle particle;
  private final RecordDefinition recordDefinition;
  private final XSSchema schema;
  private XSModelGroup modelGroup;

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