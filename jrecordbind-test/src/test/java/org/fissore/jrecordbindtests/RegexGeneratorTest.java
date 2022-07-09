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

package org.fissore.jrecordbindtests;

import org.fissore.jrecordbind.DefinitionLoader;
import org.fissore.jrecordbind.RegexGenerator;
import org.junit.Test;

import static org.fissore.jrecordbindtests.Utils.toInputSource;
import static org.junit.Assert.assertEquals;

public class RegexGeneratorTest {

  @Test
  public void choice() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/choice.def.xsd"));
    var regexGenerator = new RegexGenerator();

    assertEquals(
      "(\\n?^(000)([\\w\\W]{1})[ ]{6}){1,1}(((\\n?^(01)([\\w\\W]{4})[ ]{4}){1,1})|((\\n?^(02)([\\w\\W]{4})[ ]{4}){1,1})){0,}(((\\n?^(03)([\\w\\W]{4})[ ]{4}){1,1})|((\\n?^(04)([\\w\\W]{4})[ ]{4}){1,1})){0,}(\\n?^(000)([\\w\\W]{1})[ ]{6}){1,1}",
      regexGenerator.deepPattern(definition).pattern());

    assertEquals("((\\n?^(01)([\\w\\W]{4})[ ]{4}){1,1})|((\\n?^(02)([\\w\\W]{4})[ ]{4}){1,1})",
      regexGenerator.deepPattern(definition.getSubRecords().get(1)).pattern());
  }

  @Test
  public void choiceWithCustomLineSeparatorDynamicLength() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/choice_dynamic_length_line_separator.def.xsd"));
    var regexGenerator = new RegexGenerator();

    assertEquals(
      "(\\n?^(000)\\Q|\\E([^\\Q|\\E\\n]*)){1,1}(((\\n?^(01)\\Q|\\E([^\\Q|\\E\\n]*)){1,1})|((\\n?^(02)\\Q|\\E([^\\Q|\\E\\n]*)){1,1})){0,}(\\n?^(000)\\Q|\\E([^\\Q|\\E\\n]*)){1,1}",
      regexGenerator.deepPattern(definition).pattern());

    assertEquals("((\\n?^(01)\\Q|\\E([^\\Q|\\E\\n]*)){1,1})|((\\n?^(02)\\Q|\\E([^\\Q|\\E\\n]*)){1,1})",
      regexGenerator.deepPattern(definition.getSubRecords().get(1)).pattern());
  }

  @Test
  public void dynamicLength() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/dynamic_length.def.xsd"));
    var regexGenerator = new RegexGenerator();

    assertEquals("\\n?^([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)\\Q|\\E([^\\Q|\\E\\n]*)",
      regexGenerator.deepPattern(definition).pattern());
  }

  @Test
  public void enumWithRestrictions() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/unsupported/enum_with_restrictions.def.xsd"));
    var regexGenerator = new RegexGenerator();

    assertEquals("\\n?^([\\w\\W]{10})(Audi[ ]{6}|Golf[ ]{6}|BMW[ ]{7})",
      regexGenerator.deepPattern(definition).pattern());

    assertEquals("\\n?^([\\w\\W]{10})(Audi[ ]{6}|Golf[ ]{6}|BMW[ ]{7})",
      regexGenerator.localPattern(definition).pattern());
  }

  @Test
  public void headTailSameID() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/head_and_tail_use_same_record_id.def.xsd"));
    var regexGenerator = new RegexGenerator();

    assertEquals("(\\n?^(000)([\\w\\W]{1})[ ]{7}){1,1}(\\n?^(555)([\\w\\W]{1})[ ]{7}){1,}(\\n?^(000)([\\w\\W]{1})[ ]{7}){1,1}",
      regexGenerator.deepPattern(definition).pattern());
  }

  @Test
  public void hierarchical() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/hierarchical.def.xsd"));
    var regexGenerator = new RegexGenerator();

    assertEquals(
      "\\n?^(000)\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})(\\n?^(A00)\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})[ ]{11}(\\n?^(A01)[ ]{33}){1,1}){0,}(\\n?^(B01)\\Q|\\E([\\w\\W]{8})[ ]{24}){1,1}",
      regexGenerator.deepPattern(definition).pattern());

    assertEquals("\\n?^(000)\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})",
      regexGenerator.localPattern(definition).pattern());

    assertEquals("\\n?^(A00)\\Q|\\E([\\w\\W]{10})\\Q|\\E([\\w\\W]{10})[ ]{11}",
      regexGenerator.localPattern(definition.getSubRecords().get(0)).pattern());

    assertEquals("\\n?^(A01)[ ]{33}",
      regexGenerator.localPattern(definition.getSubRecords().get(0).getSubRecords().get(0)).pattern());

    assertEquals("\\n?^(B01)\\Q|\\E([\\w\\W]{8})[ ]{24}",
      regexGenerator.localPattern(definition.getSubRecords().get(1)).pattern());
  }

  @Test
  public void multiRow() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/multi_row.def.xsd"));
    var regexGenerator = new RegexGenerator();

    assertEquals(
      "\\n?^([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})\\n?^([\\w\\W]{20})([\\w\\W]{20})[ ]{29}",
      regexGenerator.deepPattern(definition).pattern());

    assertEquals(
      "\\n?^([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{2})([\\w\\W]{3})\\n?^([\\w\\W]{20})([\\w\\W]{20})[ ]{29}",
      regexGenerator.localPattern(definition).pattern());
  }

  @Test
  public void onlyChildren() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/only_sub_records.def.xsd"));
    var regexGenerator = new RegexGenerator();

    assertEquals("(\\n?^(000)[ ]{7}){1,1}(\\n?^(555)[ ]{7}){1,}(\\n?^(999)[ ]{7}){1,1}",
      regexGenerator.deepPattern(definition).pattern());
  }

  @Test
  public void simple() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/simple.def.xsd"));
    var regexGenerator = new RegexGenerator();

    assertEquals(
      "\\n?^([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{10})([\\w\\W]{15})([\\w\\W]{10})([\\w\\W]{1})([\\w\\W]{3})[ ]{17}",
      regexGenerator.deepPattern(definition).pattern());

    assertEquals(
      "\\n?^([\\w\\W]{20})([\\w\\W]{20})([\\w\\W]{16})([\\w\\W]{8})([\\w\\W]{10})([\\w\\W]{15})([\\w\\W]{10})([\\w\\W]{1})([\\w\\W]{3})[ ]{17}",
      regexGenerator.localPattern(definition).pattern());
  }

  @Test
  public void simpleFixedLengthLineSeparatorAndDelimiter() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/simple_fixed_length_line_separator_and_delimiter.def.xsd"));
    var regexGenerator = new RegexGenerator();

    assertEquals(
      "\\n?^([\\w\\W]{20})\\Q|\\E([\\w\\W]{20})\\Q|\\E([\\w\\W]{16})\\Q|\\E([\\w\\W]{8})\\Q|\\E([\\w\\W]{2})\\Q|\\E([\\w\\W]{3})\\Q|\\E([\\w\\W]{1})[ ]{24}",
      regexGenerator.deepPattern(definition).pattern());

    assertEquals(
      "\\n?^([\\w\\W]{20})\\Q|\\E([\\w\\W]{20})\\Q|\\E([\\w\\W]{16})\\Q|\\E([\\w\\W]{8})\\Q|\\E([\\w\\W]{2})\\Q|\\E([\\w\\W]{3})\\Q|\\E([\\w\\W]{1})[ ]{24}",
      regexGenerator.localPattern(definition).pattern());
  }
}
