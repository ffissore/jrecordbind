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

import it.assist.jrecordbind.Unmarshaller;

import java.io.InputStreamReader;
import java.util.List;

import junit.framework.TestCase;
import eu.educator.schemas.services.crihowithcustomsetter.One;
import eu.educator.schemas.services.crihowithcustomsetter.Record;
import eu.educator.schemas.services.crihowithcustomsetter.Two;

public class ChoiceWithCustomSetterRecordUnmarshallTest extends TestCase {

  private Unmarshaller<Record> unmarshaller;

  public ChoiceWithCustomSetterRecordUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<Record>(new InputStreamReader(ChoiceWithCustomSetterRecordUnmarshallTest.class
        .getResourceAsStream("/choiceWithCustomSetter.def.xsd")));
  }

  public void testUnmarshall() throws Exception {
    List<Record> records = unmarshaller.unmarshallAll(new InputStreamReader(
        ChoiceWithCustomSetterRecordUnmarshallTest.class.getResourceAsStream("choice_test.txt")));

    assertEquals(1, records.size());

    Record record = records.get(0);
    assertEquals("000", record.getOpenRecord().getRecordId());
    assertEquals(1, record.getOpenRecord().getCounter());
    assertEquals("000", record.getCloseRecord().getRecordId());
    assertEquals(2, record.getCloseRecord().getCounter());

    assertEquals(10, record.getChoices().size());
    MyChoice choice = (MyChoice) record.getChoices().get(0);
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("2   ", ((One) choice.getOneOrTwo()).getSomething());
    choice = (MyChoice) record.getChoices().get(1);
    assertEquals("02", ((Two) choice.getOneOrTwo()).getType());
    assertEquals("3   ", ((Two) choice.getOneOrTwo()).getSomething());
    choice = (MyChoice) record.getChoices().get(2);
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("4   ", ((One) choice.getOneOrTwo()).getSomething());
    choice = (MyChoice) record.getChoices().get(3);
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("5   ", ((One) choice.getOneOrTwo()).getSomething());
    choice = (MyChoice) record.getChoices().get(4);
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("6   ", ((One) choice.getOneOrTwo()).getSomething());
    choice = (MyChoice) record.getChoices().get(5);
    assertEquals("02", ((Two) choice.getOneOrTwo()).getType());
    assertEquals("7   ", ((Two) choice.getOneOrTwo()).getSomething());
    choice = (MyChoice) record.getChoices().get(6);
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("8   ", ((One) choice.getOneOrTwo()).getSomething());
    choice = (MyChoice) record.getChoices().get(7);
    assertEquals("02", ((Two) choice.getOneOrTwo()).getType());
    assertEquals("9   ", ((Two) choice.getOneOrTwo()).getSomething());
    choice = (MyChoice) record.getChoices().get(8);
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("0   ", ((One) choice.getOneOrTwo()).getSomething());
    choice = (MyChoice) record.getChoices().get(9);
    assertEquals("02", ((Two) choice.getOneOrTwo()).getType());
    assertEquals("1   ", ((Two) choice.getOneOrTwo()).getSomething());

    assertEquals("", unmarshaller.getCurrentJunk());
  }
}
