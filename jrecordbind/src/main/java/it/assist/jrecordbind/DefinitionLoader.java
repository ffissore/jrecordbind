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

import java.io.File;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
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

  private final Logger log = Logger.getLogger(DefinitionLoader.class.getName());

  private final EvaluatorBuilder evaluatorBuilder;
  private final RecordDefinition recordDefinition;
  private final XSSchemaSet schemas;
  private boolean loaded;

  public DefinitionLoader(File input) {
    this(DefinitionLoader.class.getClassLoader(), Utils.toInputSource(input));
  }

  public DefinitionLoader(ClassLoader classLoader, File input) {
    this(classLoader, Utils.toInputSource(input));
  }

  public DefinitionLoader(Reader input) {
    this(DefinitionLoader.class.getClassLoader(), Utils.toInputSource(input));
  }

  public DefinitionLoader(ClassLoader classLoader, Reader input) {
    this(classLoader, Utils.toInputSource(input));
  }

  public DefinitionLoader(InputSource input) {
    this(DefinitionLoader.class.getClassLoader(), input);
  }

  public DefinitionLoader(ClassLoader classLoader, InputSource input) {
    this.recordDefinition = new RecordDefinition(classLoader);
    this.schemas = loadSchemas(input);
    this.evaluatorBuilder = new EvaluatorBuilder();
    this.loaded = false;
  }

  private XSSchemaSet loadSchemas(InputSource input) {
    XSOMParser parser = new XSOMParser();
    parser.setErrorHandler(new ErrorHandler() {

      public void error(SAXParseException exception) {
        throw new RuntimeException(exception);
      }

      public void fatalError(SAXParseException exception) {
        throw new RuntimeException(exception);
      }

      public void warning(SAXParseException exception) {
        throw new RuntimeException(exception);
      }

    });

    try {
      parser.parse(input);

      return parser.getResult();
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }
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

    if (log.isLoggable(Level.FINE)) {
      log.log(Level.FINE, "Loading record definition");
    }

    XSSchema mainSchema = findMainSchema();
    XSElementDecl mainElement = mainSchema.getElementDecl("main");

    for (Evaluator<RecordDefinition, XSElementDecl> e : evaluatorBuilder.mainElementEvaluators()) {
      e.eval(recordDefinition, mainElement);
    }

    XSComplexType mainRecordType = mainElement.getType().asComplexType();
    for (Evaluator<RecordDefinition, XSComplexType> e : evaluatorBuilder.typeEvaluators()) {
      e.eval(recordDefinition, mainRecordType);
    }

    mainRecordType.getContentType().visit(new Visitor(evaluatorBuilder, mainSchema, schemas, recordDefinition));

    this.loaded = true;

    return this;
  }

  private XSSchema findMainSchema() {
    List<XSSchema> mainSchemas = new LinkedList<XSSchema>();
    for (XSSchema schema : schemas.getSchemas()) {
      XSElementDecl main = schema.getElementDecl("main");
      if (main != null) {
        mainSchemas.add(schema);
      }
    }

    if (mainSchemas.isEmpty()) {
      throw new IllegalArgumentException("No \"main\" element found");
    }

    if (mainSchemas.size() != 1) {
      throw new IllegalArgumentException("Multiple \"main\" elements found in your schemas");
    }

    return mainSchemas.get(0);
  }

}
