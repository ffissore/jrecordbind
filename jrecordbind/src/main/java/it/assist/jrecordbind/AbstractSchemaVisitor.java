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

  public void annotation(XSAnnotation xsannotation) {
  }

  public void attGroupDecl(XSAttGroupDecl xsattgroupdecl) {
  }

  public void attributeDecl(XSAttributeDecl xsattributedecl) {
  }

  public void attributeUse(XSAttributeUse xsattributeuse) {
  }

  public void complexType(XSComplexType xscomplextype) {
  }

  public void elementDecl(XSElementDecl xselementdecl) {
  }

  public void empty(XSContentType xscontenttype) {
  }

  public void facet(XSFacet xsfacet) {
  }

  public void identityConstraint(XSIdentityConstraint xsidentityconstraint) {
  }

  public void modelGroup(XSModelGroup xsmodelgroup) {
  }

  public void modelGroupDecl(XSModelGroupDecl xsmodelgroupdecl) {
  }

  public void notation(XSNotation xsnotation) {
  }

  public void particle(XSParticle xsparticle) {
  }

  public void schema(XSSchema xsschema) {
  }

  public void simpleType(XSSimpleType xssimpletype) {
  }

  public void wildcard(XSWildcard xswildcard) {
  }

  public void xpath(XSXPath xsxpath) {
  }

}
