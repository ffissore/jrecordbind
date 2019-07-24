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

import com.sun.xml.xsom.*;
import com.sun.xml.xsom.visitor.XSTermVisitor;
import com.sun.xml.xsom.visitor.XSVisitor;

abstract class AbstractSchemaVisitor implements XSTermVisitor, XSVisitor {

  @Override
  public void annotation(XSAnnotation ann) {
  }

  @Override
  public void attGroupDecl(XSAttGroupDecl decl) {
  }

  @Override
  public void attributeDecl(XSAttributeDecl decl) {
  }

  @Override
  public void attributeUse(XSAttributeUse use) {
  }

  @Override
  public void complexType(XSComplexType type) {
  }

  @Override
  public void schema(XSSchema schema) {
  }

  @Override
  public void facet(XSFacet facet) {
  }

  @Override
  public void notation(XSNotation notation) {
  }

  @Override
  public void identityConstraint(XSIdentityConstraint decl) {
  }

  @Override
  public void xpath(XSXPath xp) {
  }

  @Override
  public void simpleType(XSSimpleType simpleType) {
  }

  @Override
  public void particle(XSParticle particle) {
  }

  @Override
  public void empty(XSContentType empty) {
  }

  @Override
  public void wildcard(XSWildcard wc) {
  }

  @Override
  public void modelGroupDecl(XSModelGroupDecl decl) {
  }

  @Override
  public void modelGroup(XSModelGroup group) {
  }

  @Override
  public void elementDecl(XSElementDecl decl) {
  }

}
