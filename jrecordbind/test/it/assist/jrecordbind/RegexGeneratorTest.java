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

import java.io.InputStreamReader;

import junit.framework.TestCase;

public class RegexGeneratorTest extends TestCase {

  public void testDelimiter() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(new InputStreamReader(RegexGeneratorTest.class.getResourceAsStream("/delimiter.def.xsd")));

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals("([\\w\\W]{10})\\|([\\w\\W]{10})\\|([\\w\\W]{10})", regexGenerator.deepPattern(
        definitionLoader.getDefinition()).pattern());

    assertEquals("([\\w\\W]{10})\\|([\\w\\W]{10})\\|([\\w\\W]{10})", regexGenerator.localPattern(
        definitionLoader.getDefinition()).pattern());
  }

  public void testDynamicLength() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader
        .load(new InputStreamReader(RegexGeneratorTest.class.getResourceAsStream("/dynamicLength.def.xsd")));

    RegexGenerator regexGenerator = new RegexGenerator();
    assertEquals(
        "([^\\|^\\n]*)\\|([^\\|^\\n]*)\\|([^\\|^\\n]*)\\|([^\\|^\\n]*)\\|([^\\|^\\n]*)\\|([^\\|^\\n]*)\\|([^\\|^\\n]*)\\n",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());
  }

  public void testHierarchical() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(new InputStreamReader(RegexGeneratorTest.class.getResourceAsStream("/hierarchical.def.xsd")));

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals(
        "(000)\\|([\\w\\W]{10})\\|([\\w\\W]{10})\\|([\\w\\W]{10})(\\n(A00)\\|([\\w\\W]{10})\\|([\\w\\W]{10})[ ]{11}(\\n(A01)[ ]{33}){1,1}){0,}(\\n(B01)\\|([\\w\\W]{8})[ ]{24}){1,1}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());

    assertEquals("(000)\\|([\\w\\W]{10})\\|([\\w\\W]{10})\\|([\\w\\W]{10})", regexGenerator.localPattern(
        definitionLoader.getDefinition()).pattern());

    assertEquals("(A00)\\|([\\w\\W]{10})\\|([\\w\\W]{10})[ ]{11}", regexGenerator.localPattern(
        definitionLoader.getDefinition().getSubRecords().get(0)).pattern());

    assertEquals("(A01)[ ]{33}", regexGenerator.localPattern(
        definitionLoader.getDefinition().getSubRecords().get(0).getSubRecords().get(0)).pattern());

    assertEquals("(B01)\\|([\\w\\W]{8})[ ]{24}", regexGenerator.localPattern(
        definitionLoader.getDefinition().getSubRecords().get(1)).pattern());
  }

  public void testMultiRow() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(new InputStreamReader(RegexGeneratorTest.class.getResourceAsStream("/multi-row.def.xsd")));

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals(
        "([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})\\n([\\w\\W]{20})([\\w\\W]{20})[ ]{29}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());

    assertEquals(
        "([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})\\n([\\w\\W]{20})([\\w\\W]{20})[ ]{29}",
        regexGenerator.localPattern(definitionLoader.getDefinition()).pattern());
  }

  public void testOnlyChildren() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(new InputStreamReader(RegexGeneratorTest.class.getResourceAsStream("/onlyChildren.def.xsd")));

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals("((000)[ ]{7}){1,1}(\\n(555)[ ]{7}){1,}(\\n(999)[ ]{7}){1,1}", regexGenerator.deepPattern(
        definitionLoader.getDefinition()).pattern());
  }

  public void testSimple() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(new InputStreamReader(RegexGeneratorTest.class.getResourceAsStream("/simple.def.xsd")));

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals(
        "([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})([\\w\\W]{1})[ ]{30}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());

    assertEquals(
        "([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})([\\w\\W]{1})[ ]{30}",
        regexGenerator.localPattern(definitionLoader.getDefinition()).pattern());
  }
}
