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

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSIdentityConstraint;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSWildcard;
import com.sun.xml.xsom.XSXPath;
import com.sun.xml.xsom.visitor.XSTermVisitor;
import com.sun.xml.xsom.visitor.XSVisitor;

abstract class AbstractSchemaVisitor implements XSTermVisitor, XSVisitor {

  @Override
  public void annotation(XSAnnotation xsannotation) {
  }

  @Override
  public void attGroupDecl(XSAttGroupDecl xsattgroupdecl) {
  }

  @Override
  public void attributeDecl(XSAttributeDecl xsattributedecl) {
  }

  @Override
  public void attributeUse(XSAttributeUse xsattributeuse) {
  }

  @Override
  public void complexType(XSComplexType xscomplextype) {
  }

  @Override
  public void elementDecl(XSElementDecl xselementdecl) {
  }

  @Override
  public void empty(XSContentType xscontenttype) {
  }

  @Override
  public void facet(XSFacet xsfacet) {
  }

  @Override
  public void identityConstraint(XSIdentityConstraint xsidentityconstraint) {
  }

  @Override
  public void modelGroup(XSModelGroup xsmodelgroup) {
  }

  @Override
  public void modelGroupDecl(XSModelGroupDecl xsmodelgroupdecl) {
  }

  @Override
  public void notation(XSNotation xsnotation) {
  }

  @Override
  public void particle(XSParticle xsparticle) {
  }

  @Override
  public void schema(XSSchema xsschema) {
  }

  @Override
  public void simpleType(XSSimpleType xssimpletype) {
  }

  @Override
  public void wildcard(XSWildcard xswildcard) {
  }

  @Override
  public void xpath(XSXPath xsxpath) {
  }

}
