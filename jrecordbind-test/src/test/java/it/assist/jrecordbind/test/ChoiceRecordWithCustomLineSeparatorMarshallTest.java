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
import it.assist.jrecordbind.Marshaller;
import it.assist_si.schemas.jrb.choice_with_custom_line_separator.Choice;
import it.assist_si.schemas.jrb.choice_with_custom_line_separator.HeadTailRecord;
import it.assist_si.schemas.jrb.choice_with_custom_line_separator.One;
import it.assist_si.schemas.jrb.choice_with_custom_line_separator.Record;
import it.assist_si.schemas.jrb.choice_with_custom_line_separator.Two;

import java.io.InputStreamReader;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class ChoiceRecordWithCustomLineSeparatorMarshallTest {

  private Record container;
  private Marshaller<Record> marshaller;
  private StringWriter stringWriter;

  @Test
  public void marshallOne() throws Exception {
    marshaller.marshall(container, stringWriter);

    assertEquals("0001      --\n" + "012       --\n" + "023       --\n" + "014       --\n" + "015       --\n"
        + "016       --\n" + "027       --\n" + "018       --\n" + "029       --\n" + "010       --\n"
        + "021       --\n" + "0002      --\n", stringWriter.toString());
    assertEquals(156, stringWriter.toString().length());
  }

  @Before
  public void setUp() throws Exception {
    container = new Record();
    HeadTailRecord head = new HeadTailRecord();
    head.setRecordId("000");
    head.setCounter(1);
    container.setOpenRecord(head);

    Choice choice = new Choice();
    One one = new One();
    one.setType("01");
    one.setSomething("2");
    choice.setOne(one);
    container.getChoices().add(choice);

    choice = new Choice();
    Two two = new Two();
    two.setType("02");
    two.setSomething("3");
    choice.setTwo(two);
    container.getChoices().add(choice);

    choice = new Choice();
    one = new One();
    one.setType("01");
    one.setSomething("4");
    choice.setOne(one);
    container.getChoices().add(choice);

    choice = new Choice();
    one = new One();
    one.setType("01");
    one.setSomething("5");
    choice.setOne(one);
    container.getChoices().add(choice);

    choice = new Choice();
    one = new One();
    one.setType("01");
    one.setSomething("6");
    choice.setOne(one);
    container.getChoices().add(choice);

    choice = new Choice();
    two = new Two();
    two.setType("02");
    two.setSomething("7");
    choice.setTwo(two);
    container.getChoices().add(choice);

    choice = new Choice();
    one = new One();
    one.setType("01");
    one.setSomething("8");
    choice.setOne(one);
    container.getChoices().add(choice);

    choice = new Choice();
    two = new Two();
    two.setType("02");
    two.setSomething("9");
    choice.setTwo(two);
    container.getChoices().add(choice);

    choice = new Choice();
    one = new One();
    one.setType("01");
    one.setSomething("0");
    choice.setOne(one);
    container.getChoices().add(choice);

    choice = new Choice();
    two = new Two();
    two.setType("02");
    two.setSomething("1");
    choice.setTwo(two);
    container.getChoices().add(choice);

    HeadTailRecord tail = new HeadTailRecord();
    tail.setRecordId("000");
    tail.setCounter(2);
    container.setCloseRecord(tail);

    marshaller = new Marshaller<Record>(new InputStreamReader(ChoiceRecordWithCustomLineSeparatorMarshallTest.class
        .getResourceAsStream("/choiceWithCustomLineSeparator.def.xsd")));

    stringWriter = new StringWriter();
  }

}
