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
import org.jrecordbind.schemas.jrb._enum.CarType;
import org.junit.Test;

import java.math.BigDecimal;

import static org.fissore.jrecordbindtests.Utils.toInputSource;
import static org.junit.Assert.*;

public class DefinitionLoaderTest {

  @Test
  public void simple() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/simple.def.xsd"));

    assertEquals("org.jrecordbind.schemas.jrb.simple.SimpleRecord", definition.getClassName());
    assertEquals(120, definition.getLength());
    assertEquals(0, definition.getMaxOccurs());
    assertEquals(0, definition.getMinOccurs());
    assertNull(definition.getSetterName());
    assertEquals("", definition.getPropertyDelimiter());
    assertEquals("", definition.getPrintableLineSeparator());
    assertEquals("\n", definition.getLineSeparator());
    assertEquals("org.fissore.jrecordbind.padders.SpaceRightPadder", definition.getDefaultPadder());
    assertNull(definition.getPropertyPattern());
    assertFalse(definition.isDelimited());
    assertFalse(definition.isChoice());

    assertEquals(8, definition.getProperties().size());

    definition.getProperties().forEach(p -> {
      assertNull(p.getFixedValue());
      assertEquals(0, p.getRow());
      assertFalse(p.isEnum());
    });

    var nameProperty = definition.getProperties().stream().filter(p -> "name".equals(p.getName())).findFirst().get();
    assertEquals("name", nameProperty.getName());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", nameProperty.getConverter());
    assertEquals("String", nameProperty.getType());
    assertEquals(20, nameProperty.getLength());
    assertNull(nameProperty.getPadder());

    var taxcodeProperty = definition.getProperties().stream().filter(p -> "taxCode".equals(p.getName())).findFirst().get();
    assertEquals("taxCode", taxcodeProperty.getName());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", taxcodeProperty.getConverter());
    assertEquals("String", taxcodeProperty.getType());
    assertEquals(16, taxcodeProperty.getLength());
    assertNull(taxcodeProperty.getPadder());

    var birthdayProperty = definition.getProperties().stream().filter(p -> "birthday".equals(p.getName())).findFirst().get();
    assertEquals("birthday", birthdayProperty.getName());
    assertEquals("org.fissore.jrecordbindtests.test.TestConverters$SimpleRecordDateConverter", birthdayProperty.getConverter());
    assertEquals("java.util.Date", birthdayProperty.getType());
    assertEquals(8, birthdayProperty.getLength());
    assertNull(birthdayProperty.getPadder());

    var oneIntegerProperty = definition.getProperties().stream().filter(p -> "oneInteger".equals(p.getName())).findFirst().get();
    assertEquals("oneInteger", oneIntegerProperty.getName());
    assertEquals("org.fissore.jrecordbind.converters.IntegerConverter", oneIntegerProperty.getConverter());
    assertEquals("Integer", oneIntegerProperty.getType());
    assertEquals(10, oneIntegerProperty.getLength());
    assertEquals("org.fissore.jrecordbind.padders.ZeroLeftPadder", oneIntegerProperty.getPadder());

    var twoIntegerProperty = definition.getProperties().stream().filter(p -> "twoInteger".equals(p.getName())).findFirst().get();
    assertEquals("twoInteger", twoIntegerProperty.getName());
    assertEquals("org.fissore.jrecordbind.converters.IntegerConverter", twoIntegerProperty.getConverter());
    assertEquals("Integer", twoIntegerProperty.getType());
    assertEquals(15, twoIntegerProperty.getLength());
    assertEquals("org.fissore.jrecordbind.padders.SpaceRightPadder", twoIntegerProperty.getPadder());

    var oneFloatProperty = definition.getProperties().stream().filter(p -> "oneFloat".equals(p.getName())).findFirst().get();
    assertEquals("oneFloat", oneFloatProperty.getName());
    assertEquals("org.fissore.jrecordbindtests.test.TestConverters$SimpleRecordFloatConverter", oneFloatProperty.getConverter());
    assertEquals("Float", oneFloatProperty.getType());
    assertEquals(10, oneFloatProperty.getLength());
    assertEquals("org.fissore.jrecordbind.padders.SpaceLeftPadder", oneFloatProperty.getPadder());

    var selectedProperty = definition.getProperties().stream().filter(p -> "selected".equals(p.getName())).findFirst().get();
    assertEquals("selected", selectedProperty.getName());
    assertEquals("org.fissore.jrecordbindtests.test.TestConverters$YNBooleanConverter", selectedProperty.getConverter());
    assertEquals("Boolean", selectedProperty.getType());
    assertEquals(1, selectedProperty.getLength());
    assertNull(selectedProperty.getPadder());
  }

  @Test(expected = IllegalStateException.class)
  public void doubleLoad() {
    DefinitionLoader definitionLoader = new DefinitionLoader();
    definitionLoader.load(toInputSource("/record_definitions/simple.def.xsd"));
    definitionLoader.load(toInputSource("/record_definitions/simple.def.xsd"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void failWithNoMainElement() {
    new DefinitionLoader().load(toInputSource("/record_definitions/unhappy_path/noMainElement.def.xsd"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void failWithMultipleMainElements() {
    new DefinitionLoader().load(toInputSource("/record_definitions/unhappy_path/importing_double_main_element.def.xsd"));
  }

  @Test
  public void choice() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/choice.def.xsd"));

    assertEquals("--\n", definition.getLineSeparator());
    assertEquals("--", definition.getPrintableLineSeparator());

    assertEquals(0, definition.getProperties().size());

    assertEquals(4, definition.getSubRecords().size());

    var subDefinition = definition.getSubRecords().get(0);
    assertEquals("--\n", subDefinition.getLineSeparator());
    assertEquals("--", subDefinition.getPrintableLineSeparator());
    assertEquals(1, subDefinition.getMinOccurs());
    assertEquals(1, subDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.choice.HeadTailRecord", subDefinition.getClassName());
    assertEquals("openRecord", subDefinition.getSetterName());
    assertEquals(2, subDefinition.getProperties().size());
    assertFalse(subDefinition.isChoice());

    subDefinition = definition.getSubRecords().get(1);
    assertEquals(0, subDefinition.getMinOccurs());
    assertEquals(-1, subDefinition.getMaxOccurs());
    assertEquals("org.fissore.jrecordbindtests.test.TestTypes$MyChoice", subDefinition.getClassName());
    assertEquals("choices", subDefinition.getSetterName());
    assertEquals(0, subDefinition.getProperties().size());
    assertEquals(2, subDefinition.getSubRecords().size());
    assertTrue(subDefinition.isChoice());

    var subSubDefinition = subDefinition.getSubRecords().get(0);
    assertEquals(1, subSubDefinition.getMinOccurs());
    assertEquals(1, subSubDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.choice.One", subSubDefinition.getClassName());
    assertEquals("oneOrTwo", subSubDefinition.getSetterName());
    assertEquals(2, subSubDefinition.getProperties().size());
    assertEquals(0, subSubDefinition.getSubRecords().size());

    subSubDefinition = subDefinition.getSubRecords().get(1);
    assertEquals(1, subSubDefinition.getMinOccurs());
    assertEquals(1, subSubDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.choice.Two", subSubDefinition.getClassName());
    assertEquals("oneOrTwo", subSubDefinition.getSetterName());
    assertEquals(2, subSubDefinition.getProperties().size());
    assertEquals(0, subSubDefinition.getSubRecords().size());

    subDefinition = definition.getSubRecords().get(2);
    assertEquals(0, subDefinition.getMinOccurs());
    assertEquals(-1, subDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.choice.OtherChoice", subDefinition.getClassName());
    assertEquals("otherChoices", subDefinition.getSetterName());
    assertEquals(0, subDefinition.getProperties().size());
    assertEquals(2, subDefinition.getSubRecords().size());
    assertTrue(subDefinition.isChoice());

    subSubDefinition = subDefinition.getSubRecords().get(0);
    assertEquals(1, subSubDefinition.getMinOccurs());
    assertEquals(1, subSubDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.choice.Three", subSubDefinition.getClassName());
    assertEquals("three", subSubDefinition.getSetterName());
    assertEquals(2, subSubDefinition.getProperties().size());
    assertEquals(0, subSubDefinition.getSubRecords().size());

    subSubDefinition = subDefinition.getSubRecords().get(1);
    assertEquals(1, subSubDefinition.getMinOccurs());
    assertEquals(1, subSubDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.choice.Four", subSubDefinition.getClassName());
    assertEquals("four", subSubDefinition.getSetterName());
    assertEquals(2, subSubDefinition.getProperties().size());
    assertEquals(0, subSubDefinition.getSubRecords().size());

    subDefinition = definition.getSubRecords().get(3);
    assertEquals(1, subDefinition.getMinOccurs());
    assertEquals(1, subDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.choice.HeadTailRecord", subDefinition.getClassName());
    assertEquals("closeRecord", subDefinition.getSetterName());
    assertEquals(2, subDefinition.getProperties().size());
  }

  @Test
  public void enumDef() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/enum.def.xsd"));

    assertEquals("org.fissore.jrecordbindtests.test.TestTypes$MyEnumRecord", definition.getClassName());

    assertEquals(4, definition.getProperties().size());

    var property = definition.getProperties().get(0);
    assertEquals("myEnum", property.getName());
    assertEquals("Object", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("org.fissore.jrecordbindtests.test.TestConverters$MyEnumConverter", property.getConverter());
    assertNull(property.getFixedValue());
    assertNull(property.getPadder());

    property = definition.getProperties().get(1);
    assertEquals("bigNumber", property.getName());
    assertEquals(BigDecimal.class.getName(), property.getType());
    assertEquals(20, property.getLength());
    assertEquals("org.fissore.jrecordbindtests.test.TestConverters$BigDecimalConverter", property.getConverter());
    assertNull(property.getFixedValue());
    assertNull(property.getPadder());

    property = definition.getProperties().get(2);
    assertEquals("myCar", property.getName());
    assertEquals(CarType.class.getName(), property.getType());
    assertEquals(10, property.getLength());
    assertEquals("org.fissore.jrecordbindtests.test.TestConverters$CarTypeConverter", property.getConverter());
    assertNull(property.getFixedValue());
    assertNull(property.getPadder());

    property = definition.getProperties().get(3);
    assertEquals("myOtherEnum", property.getName());
    assertEquals("Object", property.getType());
    assertEquals(10, property.getLength());
    assertEquals("org.fissore.jrecordbindtests.test.TestConverters$MyOtherEnumConverter", property.getConverter());
    assertNull(property.getFixedValue());
    assertNull(property.getPadder());
  }

  @Test
  public void headAndTailUseSameRecordID() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/head_and_tail_use_same_record_id.def.xsd"));

    assertEquals(0, definition.getProperties().size());

    assertEquals(3, definition.getSubRecords().size());

    var subDefinition = definition.getSubRecords().get(0);
    assertEquals(1, subDefinition.getMinOccurs());
    assertEquals(1, subDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.head_and_tail_use_same_record_id.HeadTailRecord", subDefinition.getClassName());
    assertEquals("head", subDefinition.getSetterName());
    assertEquals(2, subDefinition.getProperties().size());

    subDefinition = definition.getSubRecords().get(1);
    assertEquals(1, subDefinition.getMinOccurs());
    assertEquals(-1, subDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.head_and_tail_use_same_record_id.DetailRecord", subDefinition.getClassName());
    assertEquals("details", subDefinition.getSetterName());
    assertEquals(2, subDefinition.getProperties().size());

    subDefinition = definition.getSubRecords().get(2);
    assertEquals(1, subDefinition.getMinOccurs());
    assertEquals(1, subDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.head_and_tail_use_same_record_id.HeadTailRecord", subDefinition.getClassName());
    assertEquals("tail", subDefinition.getSetterName());
    assertEquals(2, subDefinition.getProperties().size());
  }

  @Test
  public void hierarchical() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/hierarchical.def.xsd"));

    assertEquals("org.jrecordbind.schemas.jrb.hierarchical.MasterRecord", definition.getClassName());
    assertEquals("|", definition.getPropertyDelimiter());
    assertEquals(36, definition.getLength());

    assertEquals(4, definition.getProperties().size());

    var property = definition.getProperties().get(0);
    assertEquals("recordId", property.getName());
    assertEquals("String", property.getType());
    assertEquals(3, property.getLength());
    assertEquals("000", property.getFixedValue());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());

    property = definition.getProperties().get(1);
    assertEquals("name", property.getName());
    assertEquals("String", property.getType());
    assertEquals(10, property.getLength());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());

    property = definition.getProperties().get(2);
    assertEquals("surname", property.getName());
    assertEquals("String", property.getType());
    assertEquals(10, property.getLength());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());

    property = definition.getProperties().get(3);
    assertEquals("taxCode", property.getName());
    assertEquals("String", property.getType());
    assertEquals(10, property.getLength());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());

    assertEquals(2, definition.getSubRecords().size());

    var subDefinition = definition.getSubRecords().get(0);

    assertEquals("org.jrecordbind.schemas.jrb.hierarchical.RowRecord", subDefinition.getClassName());
    assertEquals("|", subDefinition.getPropertyDelimiter());
    assertEquals(0, subDefinition.getMinOccurs());
    assertEquals(-1, subDefinition.getMaxOccurs());
    assertEquals("rows", subDefinition.getSetterName());

    assertEquals(3, subDefinition.getProperties().size());

    property = subDefinition.getProperties().get(0);
    assertEquals("recordId", property.getName());
    assertEquals("String", property.getType());
    assertEquals(3, property.getLength());
    assertEquals("A00", property.getFixedValue());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());

    property = subDefinition.getProperties().get(1);
    assertEquals("name", property.getName());
    assertEquals("String", property.getType());
    assertEquals(10, property.getLength());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());

    property = subDefinition.getProperties().get(2);
    assertEquals("surname", property.getName());
    assertEquals("String", property.getType());
    assertEquals(10, property.getLength());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());

    assertEquals(1, subDefinition.getSubRecords().size());

    subDefinition = subDefinition.getSubRecords().get(0);

    assertEquals("org.jrecordbind.schemas.jrb.hierarchical.RowChildRecord", subDefinition.getClassName());
    assertEquals("|", subDefinition.getPropertyDelimiter());
    assertEquals(1, subDefinition.getMinOccurs());
    assertEquals(1, subDefinition.getMaxOccurs());
    assertEquals("child", subDefinition.getSetterName());

    assertEquals(1, subDefinition.getProperties().size());

    property = subDefinition.getProperties().get(0);
    assertEquals("recordId", property.getName());
    assertEquals("String", property.getType());
    assertEquals(3, property.getLength());
    assertEquals("A01", property.getFixedValue());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());

    subDefinition = definition.getSubRecords().get(1);
    assertEquals(1, subDefinition.getMinOccurs());
    assertEquals(1, subDefinition.getMaxOccurs());

    assertEquals("org.jrecordbind.schemas.jrb.hierarchical.ChildRecord", subDefinition.getClassName());
    assertEquals("|", subDefinition.getPropertyDelimiter());
    assertEquals("child", subDefinition.getSetterName());

    assertEquals(2, subDefinition.getProperties().size());

    property = subDefinition.getProperties().get(0);
    assertEquals("recordId", property.getName());
    assertEquals("String", property.getType());
    assertEquals(3, property.getLength());
    assertEquals("B01", property.getFixedValue());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());

    property = subDefinition.getProperties().get(1);
    assertEquals("when", property.getName());
    assertEquals("java.util.Date", property.getType());
    assertEquals(8, property.getLength());
    assertEquals("org.fissore.jrecordbindtests.test.TestConverters$SimpleRecordDateConverter", property.getConverter());
  }

  @Test(expected = IllegalArgumentException.class)
  public void mixingPropertiesAndSubRecordsShouldRaiseAnException() {
    new DefinitionLoader().load(toInputSource("/record_definitions/unhappy_path/mixed_properties_and_sub_records.def.xsd"));
  }

  @Test
  public void multiRow() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/multi_row.def.xsd"));

    assertEquals("org.jrecordbind.schemas.jrb.multi_row.MultiRowRecord", definition.getClassName());
    assertEquals("", definition.getPropertyDelimiter());
    assertEquals(69, definition.getLength());

    assertEquals(8, definition.getProperties().size());

    var property = definition.getProperties().get(0);
    assertEquals("name", property.getName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = definition.getProperties().get(1);
    assertEquals("surname", property.getName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = definition.getProperties().get(2);
    assertEquals("taxCode", property.getName());
    assertEquals("String", property.getType());
    assertEquals(16, property.getLength());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = definition.getProperties().get(3);
    assertEquals("birthday", property.getName());
    assertEquals("java.util.Date", property.getType());
    assertEquals(8, property.getLength());
    assertEquals("org.fissore.jrecordbindtests.test.TestConverters$SimpleRecordDateConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = definition.getProperties().get(4);
    assertEquals("oneInteger", property.getName());
    assertEquals("Integer", property.getType());
    assertEquals(2, property.getLength());
    assertEquals("org.fissore.jrecordbind.converters.IntegerConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = definition.getProperties().get(5);
    assertEquals("oneFloat", property.getName());
    assertEquals("Float", property.getType());
    assertEquals(3, property.getLength());
    assertEquals("org.fissore.jrecordbindtests.test.TestConverters$SimpleRecordFloatConverter", property.getConverter());
    assertEquals(0, property.getRow());

    property = definition.getProperties().get(6);
    assertEquals("fatherName", property.getName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());
    assertEquals(1, property.getRow());

    property = definition.getProperties().get(7);
    assertEquals("motherName", property.getName());
    assertEquals("String", property.getType());
    assertEquals(20, property.getLength());
    assertEquals("org.fissore.jrecordbind.converters.StringConverter", property.getConverter());
    assertEquals(1, property.getRow());
  }

  @Test
  public void onlySubRecords() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/only_sub_records.def.xsd"));

    assertEquals("org.jrecordbind.schemas.jrb.only_sub_records.OnlyChildrenContainer", definition.getClassName());

    assertEquals(0, definition.getProperties().size());

    assertEquals(3, definition.getSubRecords().size());

    var subDefinition = definition.getSubRecords().get(0);
    assertEquals(1, subDefinition.getMinOccurs());
    assertEquals(1, subDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.only_sub_records.HeaderRecord", subDefinition.getClassName());
    assertEquals("header", subDefinition.getSetterName());
    assertEquals(1, subDefinition.getProperties().size());

    subDefinition = definition.getSubRecords().get(1);
    assertEquals(1, subDefinition.getMinOccurs());
    assertEquals(-1, subDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.only_sub_records.DetailRecord", subDefinition.getClassName());
    assertEquals("details", subDefinition.getSetterName());
    assertEquals(1, subDefinition.getProperties().size());

    subDefinition = definition.getSubRecords().get(2);
    assertEquals(1, subDefinition.getMinOccurs());
    assertEquals(1, subDefinition.getMaxOccurs());
    assertEquals("org.jrecordbind.schemas.jrb.only_sub_records.TrailerRecord", subDefinition.getClassName());
    assertEquals("trailer", subDefinition.getSetterName());
    assertEquals(1, subDefinition.getProperties().size());
  }

  @Test
  public void simpleFixedLengthLineSeparatorAndDelimiter() {
    var definition = new DefinitionLoader().load(toInputSource("/record_definitions/simple_fixed_length_line_separator_and_delimiter.def.xsd"));

    assertEquals("org.jrecordbind.schemas.jrb.simple_fixed_length_line_separator_and_delimiter.SimpleRecord", definition.getClassName());
    assertEquals("|", definition.getPropertyDelimiter());
    assertEquals(100, definition.getLength());
    assertEquals("\r\n", definition.getLineSeparator());
  }

}
