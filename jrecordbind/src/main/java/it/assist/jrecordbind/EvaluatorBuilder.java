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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.tools.xjc.model.CBuiltinLeafInfo;
import com.sun.tools.xjc.model.nav.NType;
import com.sun.xml.bind.api.impl.NameConverter;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.XmlString;

class EvaluatorBuilder {

  static final class ConverterEval implements Evaluator<Property, XSElementDecl> {

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

    public void eval(RecordDefinition target, XSElementDecl source) {
      target.setPropertyDelimiter(source.getForeignAttribute(Constants.JRECORDBIND_XSD, "delimiter"));
    }

  }

  static final class ElementTypeEval implements Evaluator<Property, XSElementDecl> {

    private final TypeEval typeEval;

    public ElementTypeEval() {
      typeEval = new TypeEval();
    }

    public void eval(Property target, XSElementDecl source) {
      typeEval.eval(target, source.getType());
    }

  }

  static final class EnumerationRestrictionEval implements Evaluator<Property, XSSimpleType> {

    public void eval(Property target, XSSimpleType source) {
      String packageName = NameConverter.standard.toPackageName(source.getTargetNamespace());
      List<XSFacet> facets = source.getFacets("enumeration");
      if (!facets.isEmpty()) {
        target.setType(packageName + "." + NameConverter.standard.toClassName(source.getName()));
      }
      
      String converter = source.getForeignAttribute(Constants.JRECORDBIND_XSD, "converter");
      if (converter != null) {
        target.setConverter(converter);
      }
    }
  }

  static final class FixedValueEval implements Evaluator<Property, XSElementDecl> {

    public void eval(Property target, XSElementDecl source) {
      XmlString fixedValue = source.getFixedValue();
      if (fixedValue != null) {
        target.setFixedValue(fixedValue.value);
      }
    }
  }

  static final class GlobalPadderEval implements Evaluator<RecordDefinition, XSElementDecl> {

    public void eval(RecordDefinition target, XSElementDecl source) {
      target.setGlobalPadder(source.getForeignAttribute(Constants.JRECORDBIND_XSD, "padder"));
    }

  }

  static final class LengthPropertyEval implements Evaluator<Property, XSElementDecl> {

    public void eval(Property target, XSElementDecl source) {
      String length = source.getForeignAttribute(Constants.JRECORDBIND_XSD, "length");
      if (length != null) {
        target.setLength(Integer.parseInt(length));
      }
    }
  }

  static final class LengthRecordEval implements Evaluator<RecordDefinition, XSElementDecl> {

    public void eval(RecordDefinition target, XSElementDecl source) {
      String length = source.getForeignAttribute(Constants.JRECORDBIND_XSD, "length");
      if (length != null) {
        target.setLength(Integer.parseInt(length));
      }
    }

  }

  static final class LineSeparatorRecordEval implements Evaluator<RecordDefinition, XSElementDecl> {

    public void eval(RecordDefinition target, XSElementDecl source) {
      String separator = source.getForeignAttribute(Constants.JRECORDBIND_XSD, "lineSeparator");
      separator = separator != null ? separator : "\n";
      target.setLineSeparator(separator);
    }

  }

  static final class MinMaxOccursEval implements Evaluator<RecordDefinition, XSParticle> {

    public void eval(RecordDefinition target, XSParticle source) {
      target.setMinOccurs(source.getMinOccurs());
      target.setMaxOccurs(source.getMaxOccurs());
    }

  }

  static final class PadderEval implements Evaluator<Property, XSElementDecl> {

    public void eval(Property target, XSElementDecl source) {
      target.setPadder(source.getForeignAttribute(Constants.JRECORDBIND_XSD, "padder"));
    }
  }

  static final class RowEval implements Evaluator<Property, XSElementDecl> {

    public void eval(Property target, XSElementDecl source) {
      String row = source.getForeignAttribute(Constants.JRECORDBIND_XSD, "row");
      row = row != null ? row : "0";
      target.setRow(Integer.parseInt(row));
    }
  }

  static final class SimpleRestrictionEval implements Evaluator<Property, XSSimpleType> {

    private final TypeEval typeEval;

    public SimpleRestrictionEval() {
      typeEval = new TypeEval();
    }

    public void eval(Property target, XSSimpleType source) {
      if (source.getFacets("enumeration").isEmpty()) {
        typeEval.eval(target, source.getBaseType());
        target.setConverter("it.assist.jrecordbind.converters." + target.getType() + "Converter");
      }
    }

  }

  static final class SubClassEval implements Evaluator<RecordDefinition, XSComplexType> {

    public void eval(RecordDefinition target, XSComplexType source) {
      String packageName = NameConverter.standard.toPackageName(source.getTargetNamespace());
      String subclass = source.getForeignAttribute(Constants.JRECORDBIND_XSD, "subclass");
      if (subclass != null) {
        target.setClassName(subclass);
      } else {
        target.setClassName(NameConverter.standard.toClassName(source.getName()), packageName);
      }
    }
  }

  static final class TypeEval implements Evaluator<Property, XSType> {

    public void eval(Property target, XSType source) {
      target.setType(toJavaType(source.getName()));
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

  private final List<Evaluator<RecordDefinition, XSElementDecl>> mainElementEvals;
  private final List<Evaluator<Property, XSElementDecl>> propertiesEvals;
  private final List<Evaluator<Property, XSSimpleType>> simpleTypeEvaluators;
  private final List<Evaluator<Property, XSElementDecl>> simpleTypePropertiesEvals;
  private final List<Evaluator<RecordDefinition, XSParticle>> subRecordsEvaluators;
  private final List<Evaluator<RecordDefinition, XSComplexType>> typeEvaluators;

  public EvaluatorBuilder() {
    propertiesEvals = new LinkedList<Evaluator<Property, XSElementDecl>>();
    propertiesEvals.add(new RowEval());
    propertiesEvals.add(new FixedValueEval());
    propertiesEvals.add(new ElementTypeEval());
    propertiesEvals.add(new LengthPropertyEval());
    propertiesEvals.add(new ConverterEval());
    propertiesEvals.add(new PadderEval());

    mainElementEvals = new LinkedList<Evaluator<RecordDefinition, XSElementDecl>>();
    mainElementEvals.add(new DelimiterEval());
    mainElementEvals.add(new GlobalPadderEval());
    mainElementEvals.add(new LengthRecordEval());
    mainElementEvals.add(new LineSeparatorRecordEval());

    typeEvaluators = new LinkedList<Evaluator<RecordDefinition, XSComplexType>>();
    typeEvaluators.add(new SubClassEval());

    simpleTypeEvaluators = new LinkedList<Evaluator<Property, XSSimpleType>>();
    simpleTypeEvaluators.add(new EnumerationRestrictionEval());
    simpleTypeEvaluators.add(new SimpleRestrictionEval());

    simpleTypePropertiesEvals = new LinkedList<Evaluator<Property, XSElementDecl>>();
    simpleTypePropertiesEvals.add(new LengthPropertyEval());

    subRecordsEvaluators = new LinkedList<Evaluator<RecordDefinition, XSParticle>>();
    subRecordsEvaluators.add(new MinMaxOccursEval());
  }

  public List<Evaluator<RecordDefinition, XSElementDecl>> mainElementEvaluators() {
    return mainElementEvals;
  }

  public List<Evaluator<Property, XSElementDecl>> propertiesEvaluators() {
    return propertiesEvals;
  }

  public List<Evaluator<Property, XSSimpleType>> simpleTypeEvaluators() {
    return simpleTypeEvaluators;
  }

  public List<Evaluator<Property, XSElementDecl>> simpleTypePropertyEvaluators() {
    return simpleTypePropertiesEvals;
  }

  public List<Evaluator<RecordDefinition, XSParticle>> subRecordsEvaluators() {
    return subRecordsEvaluators;
  }

  public List<Evaluator<RecordDefinition, XSComplexType>> typeEvaluators() {
    return typeEvaluators;
  }

}
