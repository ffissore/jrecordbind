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

import static org.junit.Assert.*;

import java.io.InputStreamReader;

import org.junit.Test;

public class RegexGeneratorTest {

  @Test
  public void choice() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/choice.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals(
        "(\\n?^(000)([\\w\\W]{1})[ ]{6}){1,1}(((\\n?^(01)([\\w\\W]{4})[ ]{4}){1,1})|((\\n?^(02)([\\w\\W]{4})[ ]{4}){1,1})){0,}(\\n?^(000)([\\w\\W]{1})[ ]{6}){1,1}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());

    assertEquals("((\\n?^(01)([\\w\\W]{4})[ ]{4}){1,1})|((\\n?^(02)([\\w\\W]{4})[ ]{4}){1,1})", regexGenerator.deepPattern(
        definitionLoader.getDefinition().getSubRecords().get(1)).pattern());
  }

  @Test
  public void choiceWithCustomLineSeparator() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/choiceWithCustomLineSeparator.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals(
        "(\\n?^(000)([\\w\\W]{1})[ ]{6}){1,1}(((\\n?^(01)([\\w\\W]{4})[ ]{4}){1,1})|((\\n?^(02)([\\w\\W]{4})[ ]{4}){1,1})){0,}(\\n?^(000)([\\w\\W]{1})[ ]{6}){1,1}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());

    assertEquals("((\\n?^(01)([\\w\\W]{4})[ ]{4}){1,1})|((\\n?^(02)([\\w\\W]{4})[ ]{4}){1,1})", regexGenerator.deepPattern(
        definitionLoader.getDefinition().getSubRecords().get(1)).pattern());
  }

  @Test
  public void choiceWithCustomLineSeparatorDynamicLength() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/choiceWithCustomLineSeparatorDynamicLength.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals(
        "(\\n?^(000)\\Q|\\E([^\\Q|\\E\\n]*)){1,1}(((\\n?^(01)\\Q|\\E([^\\Q|\\E\\n]*)){1,1})|((\\n?^(02)\\Q|\\E([^\\Q|\\E\\n]*)){1,1})){0,}(\\n?^(000)\\Q|\\E([^\\Q|\\E\\n]*)){1,1}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());

    assertEquals("((\\n?^(01)\\Q|\\E([^\\Q|\\E\\n]*)){1,1})|((\\n?^(02)\\Q|\\E([^\\Q|\\E\\n]*)){1,1})", regexGenerator
        .deepPattern(definitionLoader.getDefinition().getSubRecords().get(1)).pattern());
  }

  @Test
  public void delimiter() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/delimiter.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals("\\n?^([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})", regexGenerator.deepPattern(
        definitionLoader.getDefinition()).pattern());

    assertEquals("\\n?^([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})", regexGenerator.localPattern(
        definitionLoader.getDefinition()).pattern());
  }

  @Test
  public void dynamicLength() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/dynamicLength.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();
    assertEquals("\\n?^([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());    
  }

  @Test
  public void enumWithRestrictions() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/enumWithRestrictions.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals("\\n?^([\\w\\W]{10})(Audi[ ]{6}|Golf[ ]{6}|BMW[ ]{7})", regexGenerator.deepPattern(
        definitionLoader.getDefinition()).pattern());

    assertEquals("\\n?^([\\w\\W]{10})(Audi[ ]{6}|Golf[ ]{6}|BMW[ ]{7})", regexGenerator.localPattern(
        definitionLoader.getDefinition()).pattern());
  }

  @Test
  public void girofehlki() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/GiroFelhki.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals(
        "(\\n?^(01)(FELHKI)([\\w\\W]{1})([\\w\\W]{8})([\\w\\W]{4})([\\w\\W]{6})([\\w\\W]{13})){1,1}((\\n?^(02)([\\w\\W]{25})([\\w\\W]{35})){1,1}(\\n?^(03)([\\w\\W]{279})){1,}(\\n?^(04)([\\w\\W]{4})){1,1}){1,}(\\n?^(05)([\\w\\W]{2})([\\w\\W]{6})){1,1}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());
  }

  @Test
  public void headTailSameID() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/headTailRecordSameID.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals("(\\n?^(000)([\\w\\W]{1})[ ]{7}){1,1}(\\n?^(555)([\\w\\W]{1})[ ]{7}){1,}(\\n?^(000)([\\w\\W]{1})[ ]{7}){1,1}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());
  }

  @Test
  public void hierarchical() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/hierarchical.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals(
        "\\n?^(000)\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})(\\n?^(A00)\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})[ ]{11}(\\n?^(A01)[ ]{33}){1,1}){0,}(\\n?^(B01)\\Q|\\E([\\w\\W]{8})[ ]{24}){1,1}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());

    assertEquals("\\n?^(000)\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})", regexGenerator.localPattern(
        definitionLoader.getDefinition()).pattern());

    assertEquals("\\n?^(A00)\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})[ ]{11}", regexGenerator.localPattern(
        definitionLoader.getDefinition().getSubRecords().get(0)).pattern());

    assertEquals("\\n?^(A01)[ ]{33}", regexGenerator.localPattern(
        definitionLoader.getDefinition().getSubRecords().get(0).getSubRecords().get(0)).pattern());

    assertEquals("\\n?^(B01)\\Q|\\E([\\w\\W]{8})[ ]{24}", regexGenerator.localPattern(
        definitionLoader.getDefinition().getSubRecords().get(1)).pattern());
  }

  @Test
  public void hierarchical_variable_length() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/hierarchicalDynamicLength.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals("\\n?^(000)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)(\\n?^(A00)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)(\\n?^(A01)){1,1}){0,}(\\n?^(B01)\\Q|\\E([^\\Q|\\E\\n]*)){1,1}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());

    assertEquals("\\n?^(A00)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)(\\n?^(A01)){1,1}", regexGenerator.deepPattern(
        definitionLoader.getDefinition().getSubRecords().get(0)).pattern());

    assertEquals("\\n?^(000)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)", regexGenerator.localPattern(
        definitionLoader.getDefinition()).pattern());

    assertEquals("\\n?^(A00)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)", regexGenerator.localPattern(
        definitionLoader.getDefinition().getSubRecords().get(0)).pattern());

    assertEquals("\\n?^(A01)", regexGenerator.localPattern(
        definitionLoader.getDefinition().getSubRecords().get(0).getSubRecords().get(0)).pattern());

    assertEquals("\\n?^(B01)\\Q|\\E([^\\Q|\\E\\n]*)", regexGenerator.localPattern(
        definitionLoader.getDefinition().getSubRecords().get(1)).pattern());
  }

  @Test
  public void multiRow() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/multi-row.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals(
        "\\n?^([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})\\n?^([\\w\\W]{20})([\\w\\W]{20})[ ]{29}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());

    assertEquals(
        "\\n?^([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})\\n?^([\\w\\W]{20})([\\w\\W]{20})[ ]{29}",
        regexGenerator.localPattern(definitionLoader.getDefinition()).pattern());
  }

  @Test
  public void onlyChildren() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/onlyChildren.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals("(\\n?^(000)[ ]{7}){1,1}(\\n?^(555)[ ]{7}){1,}(\\n?^(999)[ ]{7}){1,1}", regexGenerator.deepPattern(
        definitionLoader.getDefinition()).pattern());
  }

  @Test
  public void simple() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/simple.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals(
        "\\n?^([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})([\\w\\W]{1})[ ]{30}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());

    assertEquals(
        "\\n?^([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})([\\w\\W]{1})[ ]{30}",
        regexGenerator.localPattern(definitionLoader.getDefinition()).pattern());
  }

  @Test
  public void simpleWithCustomLineSeparator() throws Exception {
    DefinitionLoader definitionLoader = new DefinitionLoader(DefinitionLoader.class.getClassLoader(), new InputStreamReader(RegexGeneratorTest.class
        .getResourceAsStream("/simpleWithCustomLineSeparator.def.xsd"))).load();

    RegexGenerator regexGenerator = new RegexGenerator();

    assertEquals(
        "\\n?^([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})([\\w\\W]{1})[ ]{30}",
        regexGenerator.deepPattern(definitionLoader.getDefinition()).pattern());

    assertEquals(
        "\\n?^([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})([\\w\\W]{1})[ ]{30}",
        regexGenerator.localPattern(definitionLoader.getDefinition()).pattern());
  }
}
