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

import java.io.Reader;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSElementDecl;
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

  private final EvaluatorBuilder evaluatorBuilder;
  private boolean loaded;
  private final RecordDefinition recordDefinition;
  final XSSchema schema;

  public DefinitionLoader(Reader input) {
    this.recordDefinition = new RecordDefinition();
    this.schema = findSchema(input);
    this.evaluatorBuilder = new EvaluatorBuilder(schema);
    this.loaded = false;
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
    if (loaded) {
      throw new IllegalStateException("Already loaded");
    }

    XSElementDecl mainElement = schema.getElementDecl("main");

    for (Evaluator<RecordDefinition, XSElementDecl> e : evaluatorBuilder.mainElementEvaluators()) {
      e.eval(recordDefinition, mainElement);
    }

    XSComplexType mainRecordType = mainElement.getType().asComplexType();
    for (Evaluator<RecordDefinition, XSComplexType> e : evaluatorBuilder.typeEvaluators()) {
      e.eval(recordDefinition, mainRecordType);
    }

    mainRecordType.getContentType().visit(new Visitor(evaluatorBuilder, schema, recordDefinition));

    this.loaded = true;

    return this;
  }

}
