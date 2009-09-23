package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.tools.xjc.model.CBuiltinLeafInfo;
import com.sun.tools.xjc.model.nav.NType;
import com.sun.xml.bind.api.impl.NameConverter;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XmlString;

public class EvaluatorBuilder {

  static final class ConverterEval implements Evaluator<Property, XSElementDecl> {
    @Override
    public void eval(Property target, XSElementDecl source) {
      String converter = source.getForeignAttribute(Constants.JRECORDBIND_XSD, "converter");
      if (converter != null) {
        target.setConverter(converter);
      } else {
        target.setConverter("it.assist.jrecordbind.converters." + target.getType() + "Converter");
      }
    }
  }

  static final class DelimiterEval implements Evaluator<RecordDefinition, XSElementDecl> {

    @Override
    public void eval(RecordDefinition target, XSElementDecl source) {
      target.setPropertyDelimiter(source.getForeignAttribute(Constants.JRECORDBIND_XSD, "delimiter"));
    }

  }

  static final class FixedValueEval implements Evaluator<Property, XSElementDecl> {
    @Override
    public void eval(Property target, XSElementDecl source) {
      XmlString fixedValue = source.getFixedValue();
      if (fixedValue != null) {
        target.setFixedValue(fixedValue.value);
      }
    }
  }

  static final class GlobalPadderEval implements Evaluator<RecordDefinition, XSElementDecl> {

    @Override
    public void eval(RecordDefinition target, XSElementDecl source) {
      target.setGlobalPadder(source.getForeignAttribute(Constants.JRECORDBIND_XSD, "padder"));
    }

  }

  static final class LengthPropertyEval implements Evaluator<Property, XSElementDecl> {
    @Override
    public void eval(Property target, XSElementDecl source) {
      String length = source.getForeignAttribute(Constants.JRECORDBIND_XSD, "length");
      if (length != null) {
        target.setLength(Integer.parseInt(length));
      }
    }
  }

  static final class LengthRecordEval implements Evaluator<RecordDefinition, XSElementDecl> {

    @Override
    public void eval(RecordDefinition target, XSElementDecl source) {
      String length = source.getForeignAttribute(Constants.JRECORDBIND_XSD, "length");
      if (length != null) {
        target.setLength(Integer.parseInt(length));
      }
    }

  }

  static final class MinMaxOccursEval implements Evaluator<RecordDefinition, XSParticle> {

    @Override
    public void eval(RecordDefinition target, XSParticle source) {
      target.setMinOccurs(source.getMinOccurs());
      target.setMaxOccurs(source.getMaxOccurs());
    }

  }

  static final class PadderEval implements Evaluator<Property, XSElementDecl> {
    @Override
    public void eval(Property target, XSElementDecl source) {
      target.setPadder(source.getForeignAttribute(Constants.JRECORDBIND_XSD, "padder"));
    }
  }

  static final class RowEval implements Evaluator<Property, XSElementDecl> {
    @Override
    public void eval(Property target, XSElementDecl source) {
      String row = source.getForeignAttribute(Constants.JRECORDBIND_XSD, "row");
      row = row != null ? row : "0";
      target.setRow(Integer.parseInt(row));
    }
  }

  static final class SubClassEval implements Evaluator<RecordDefinition, XSComplexType> {

    private final String packageName;

    public SubClassEval(XSSchema schema) {
      this.packageName = NameConverter.standard.toPackageName(schema.getTargetNamespace());
    }

    public void eval(RecordDefinition target, XSComplexType source) {
      String subclass = source.getForeignAttribute(Constants.JRECORDBIND_XSD, "subclass");
      if (subclass != null) {
        target.setClassName(subclass);
      } else {
        target.setClassName(NameConverter.standard.toClassName(source.getName()), packageName);
      }
    }
  }

  static final class TypeEval implements Evaluator<Property, XSElementDecl> {
    @Override
    public void eval(Property target, XSElementDecl source) {
      target.setType(toJavaType(source.getType().getName()));
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

  private final LinkedList<Evaluator<RecordDefinition, XSElementDecl>> mainElementEvals;
  private final LinkedList<Evaluator<Property, XSElementDecl>> propertiesEvals;
  private final LinkedList<Evaluator<RecordDefinition, XSComplexType>> typeEvaluators;
  private final LinkedList<Evaluator<RecordDefinition, XSParticle>> subRecordsEvaluators;

  public EvaluatorBuilder(XSSchema schema) {
    propertiesEvals = new LinkedList<Evaluator<Property, XSElementDecl>>();
    propertiesEvals.add(new RowEval());
    propertiesEvals.add(new FixedValueEval());
    propertiesEvals.add(new TypeEval());
    propertiesEvals.add(new LengthPropertyEval());
    propertiesEvals.add(new ConverterEval());
    propertiesEvals.add(new PadderEval());

    mainElementEvals = new LinkedList<Evaluator<RecordDefinition, XSElementDecl>>();
    mainElementEvals.add(new DelimiterEval());
    mainElementEvals.add(new GlobalPadderEval());
    mainElementEvals.add(new LengthRecordEval());

    typeEvaluators = new LinkedList<Evaluator<RecordDefinition, XSComplexType>>();
    typeEvaluators.add(new SubClassEval(schema));

    subRecordsEvaluators = new LinkedList<Evaluator<RecordDefinition, XSParticle>>();
    subRecordsEvaluators.add(new MinMaxOccursEval());
  }

  public List<Evaluator<RecordDefinition, XSElementDecl>> mainElementEvaluators() {
    return mainElementEvals;
  }

  public List<Evaluator<Property, XSElementDecl>> propertiesEvaluators() {
    return propertiesEvals;
  }

  public List<Evaluator<RecordDefinition, XSParticle>> subRecordsEvaluators() {
    return subRecordsEvaluators;
  }

  public List<Evaluator<RecordDefinition, XSComplexType>> typeEvaluators() {
    return typeEvaluators;
  }

}
