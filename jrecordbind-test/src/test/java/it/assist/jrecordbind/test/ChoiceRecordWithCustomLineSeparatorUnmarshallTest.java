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

package it.assist.jrecordbind.test;

import static org.junit.Assert.*;
import it.assist.jrecordbind.Unmarshaller;
import it.assist_si.schemas.jrb.choice_with_custom_line_separator.Choice;
import it.assist_si.schemas.jrb.choice_with_custom_line_separator.Record;

import java.io.InputStreamReader;
import java.util.List;

import org.junit.Test;

public class ChoiceRecordWithCustomLineSeparatorUnmarshallTest {

  private Unmarshaller<Record> unmarshaller;

  public ChoiceRecordWithCustomLineSeparatorUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<Record>(new InputStreamReader(
        ChoiceRecordWithCustomLineSeparatorUnmarshallTest.class
            .getResourceAsStream("/choiceWithCustomLineSeparator.def.xsd")));
  }

  @Test
  public void unmarshall() throws Exception {
    List<Record> records = unmarshaller.unmarshallAll(new InputStreamReader(
        ChoiceRecordWithCustomLineSeparatorUnmarshallTest.class
            .getResourceAsStream("choice_with_custom_line_separator.txt")));

    assertEquals(1, records.size());

    Record record = records.get(0);
    assertEquals("000", record.getOpenRecord().getRecordId());
    assertEquals(1, record.getOpenRecord().getCounter());
    assertEquals("000", record.getCloseRecord().getRecordId());
    assertEquals(2, record.getCloseRecord().getCounter());

    assertEquals(10, record.getChoices().size());
    Choice choice = record.getChoices().get(0);
    assertEquals("01", choice.getOne().getType());
    assertEquals("2   ", choice.getOne().getSomething());
    assertNull(choice.getTwo());
    choice = record.getChoices().get(1);
    assertEquals("02", choice.getTwo().getType());
    assertEquals("3   ", choice.getTwo().getSomething());
    assertNull(choice.getOne());
    choice = record.getChoices().get(2);
    assertEquals("01", choice.getOne().getType());
    assertEquals("4   ", choice.getOne().getSomething());
    assertNull(choice.getTwo());
    choice = record.getChoices().get(3);
    assertEquals("01", choice.getOne().getType());
    assertEquals("5   ", choice.getOne().getSomething());
    assertNull(choice.getTwo());
    choice = record.getChoices().get(4);
    assertEquals("01", choice.getOne().getType());
    assertEquals("6   ", choice.getOne().getSomething());
    assertNull(choice.getTwo());
    choice = record.getChoices().get(5);
    assertEquals("02", choice.getTwo().getType());
    assertEquals("7   ", choice.getTwo().getSomething());
    assertNull(choice.getOne());
    choice = record.getChoices().get(6);
    assertEquals("01", choice.getOne().getType());
    assertEquals("8   ", choice.getOne().getSomething());
    assertNull(choice.getTwo());
    choice = record.getChoices().get(7);
    assertEquals("02", choice.getTwo().getType());
    assertEquals("9   ", choice.getTwo().getSomething());
    assertNull(choice.getOne());
    choice = record.getChoices().get(8);
    assertEquals("01", choice.getOne().getType());
    assertEquals("0   ", choice.getOne().getSomething());
    assertNull(choice.getTwo());
    choice = record.getChoices().get(9);
    assertEquals("02", choice.getTwo().getType());
    assertEquals("1   ", choice.getTwo().getSomething());
    assertNull(choice.getOne());

    assertEquals("", unmarshaller.getCurrentJunk());
  }
}
