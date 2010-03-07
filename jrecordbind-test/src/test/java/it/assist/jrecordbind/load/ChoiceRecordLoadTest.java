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

package it.assist.jrecordbind.load;

import static org.junit.Assert.*;
import it.assist.jrecordbind.Marshaller;
import it.assist.jrecordbind.Unmarshaller;
import it.assist.jrecordbind.test.SimpleRecordMarshallTest;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.educator.schemas.services.criho.Choice;
import eu.educator.schemas.services.criho.HeadTailRecord;
import eu.educator.schemas.services.criho.One;
import eu.educator.schemas.services.criho.Record;
import eu.educator.schemas.services.criho.Two;

public class ChoiceRecordLoadTest {

  private Record container;
  private File file;
  private Marshaller<Record> marshaller;
  private Unmarshaller<Record> unmarshaller;

  @Test
  public void loadMarshall() throws Exception {
    FileWriter writer = new FileWriter(file);
    for (int i = 0; i < 1000000; i++) {
      marshaller.marshall(container, writer);
    }
    writer.close();
  }

  @Test
  public void loadRoundtrip() throws Exception {
    FileWriter writer = new FileWriter(file);
    for (int i = 0; i < 1000000; i++) {
      marshaller.marshall(container, writer);
    }
    writer.close();

    FileReader reader = new FileReader(file);
    Iterator<Record> iterator = unmarshaller.unmarshall(reader);
    while (iterator.hasNext()) {
      iterator.next();
    }
    reader.close();

    assertEquals("", unmarshaller.getCurrentJunk());
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

    marshaller = new Marshaller<Record>(new InputStreamReader(ChoiceRecordLoadTest.class
        .getResourceAsStream("/choice.def.xsd")));

    unmarshaller = new Unmarshaller<Record>(new InputStreamReader(SimpleRecordMarshallTest.class
        .getResourceAsStream("/choice.def.xsd")));

    file = File.createTempFile("jrecord_bind_simple_record", "test");
  }

  @After
  public void tearDown() throws Exception {
    file.delete();
  }

}
