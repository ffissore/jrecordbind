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

package org.fissore.jrecordbindtests.load;

import org.fissore.jrecordbind.Marshaller;
import org.fissore.jrecordbind.Unmarshaller;
import org.fissore.jrecordbindtests.Utils;
import org.fissore.jrecordbindtests.test.TestTypes;
import org.jrecordbind.schemas.jrb.choice.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class ChoiceRecordLoadTest {

  private Record container;
  private File file;
  private Marshaller<Record> marshaller;
  private Unmarshaller<Record> unmarshaller;

  @Before
  public void setUp() throws Exception {
    container = new Record();
    HeadTailRecord head = new HeadTailRecord();
    head.setCounter(1);
    container.setOpenRecord(head);

    TestTypes.MyChoice choice = new TestTypes.MyChoice();
    One one = new One();
    one.setSomething("2");
    choice.setOneOrTwo(one);
    container.getChoices().add(choice);

    choice = new TestTypes.MyChoice();
    Two two = new Two();
    two.setSomething("3");
    choice.setOneOrTwo(two);
    container.getChoices().add(choice);

    choice = new TestTypes.MyChoice();
    one = new One();
    one.setSomething("4");
    choice.setOneOrTwo(one);
    container.getChoices().add(choice);

    choice = new TestTypes.MyChoice();
    one = new One();
    one.setSomething("5");
    choice.setOneOrTwo(one);
    container.getChoices().add(choice);

    choice = new TestTypes.MyChoice();
    one = new One();
    one.setSomething("6");
    choice.setOneOrTwo(one);
    container.getChoices().add(choice);

    choice = new TestTypes.MyChoice();
    two = new Two();
    two.setSomething("7");
    choice.setOneOrTwo(two);
    container.getChoices().add(choice);

    choice = new TestTypes.MyChoice();
    one = new One();
    one.setSomething("8");
    choice.setOneOrTwo(one);
    container.getChoices().add(choice);

    choice = new TestTypes.MyChoice();
    two = new Two();
    two.setSomething("9");
    choice.setOneOrTwo(two);
    container.getChoices().add(choice);

    choice = new TestTypes.MyChoice();
    one = new One();
    one.setSomething("0");
    choice.setOneOrTwo(one);
    container.getChoices().add(choice);

    choice = new TestTypes.MyChoice();
    two = new Two();
    two.setSomething("1");
    choice.setOneOrTwo(two);
    container.getChoices().add(choice);

    OtherChoice otherChoice = new OtherChoice();
    Three three = new Three();
    three.setSomething("1");
    otherChoice.setThree(three);
    container.getOtherChoices().add(otherChoice);

    otherChoice = new OtherChoice();
    Four four = new Four();
    four.setSomething("1");
    otherChoice.setFour(four);
    container.getOtherChoices().add(otherChoice);

    otherChoice = new OtherChoice();
    three = new Three();
    three.setSomething("2");
    otherChoice.setThree(three);
    container.getOtherChoices().add(otherChoice);

    otherChoice = new OtherChoice();
    three = new Three();
    three.setSomething("3");
    otherChoice.setThree(three);
    container.getOtherChoices().add(otherChoice);

    HeadTailRecord tail = new HeadTailRecord();
    tail.setCounter(2);
    container.setCloseRecord(tail);

    marshaller = new Marshaller<>(Utils.loadDefinition("/record_definitions/choice.def.xsd"));

    unmarshaller = new Unmarshaller<>(Utils.loadDefinition("/record_definitions/choice.def.xsd"));

    file = File.createTempFile("jrecord_bind_simple_record", "test");
  }

  @Test
  public void marshall() throws Exception {
    FileWriter writer = new FileWriter(file);
    for (int i = 0; i < 1000000; i++) {
      marshaller.marshall(container, writer);
    }
    writer.close();
  }

  @Test
  @Ignore
  public void marshallUnmarshall() throws Exception {
    FileWriter writer = new FileWriter(file);
    for (int i = 0; i < 1000000; i++) {
      marshaller.marshall(container, writer);
    }
    writer.close();

    FileReader reader = new FileReader(file);
    Iterator<Record> iterator = unmarshaller.unmarshallToIterator(reader);
    while (iterator.hasNext()) {
      iterator.next();
    }
    reader.close();

    assertEquals("", unmarshaller.getCurrentJunk());
  }

  @After
  public void tearDown() {
    file.delete();
  }

}
