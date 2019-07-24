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

package org.fissore.jrecordbindtests.test;

import org.fissore.jrecordbind.Unmarshaller;
import org.fissore.jrecordbindtests.Utils;
import org.jrecordbind.schemas.jrb.choice.One;
import org.jrecordbind.schemas.jrb.choice.OtherChoice;
import org.jrecordbind.schemas.jrb.choice.Record;
import org.jrecordbind.schemas.jrb.choice.Two;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ChoiceRecordUnmarshallTest {

  private Unmarshaller<Record> unmarshaller;

  @Before
  public void setUp() {
    unmarshaller = new Unmarshaller<>(Utils.loadDefinition("/record_definitions/choice.def.xsd"));
  }

  @Test
  public void unmarshall() {
    List<Record> records = unmarshaller.unmarshallToStream(Utils.openStream("/record_files/choice.txt")).collect(Collectors.toList());

    assertEquals(1, records.size());

    Record record = records.get(0);
    assertEquals("000", record.getOpenRecord().getRecordId());
    assertEquals(1, record.getOpenRecord().getCounter());
    assertEquals("000", record.getCloseRecord().getRecordId());
    assertEquals(2, record.getCloseRecord().getCounter());

    assertEquals(10, record.getChoices().size());
    TestTypes.MyChoice choice = (TestTypes.MyChoice) record.getChoices().get(0);
    assertEquals("01", choice.getOne().getType());
    assertEquals("2", choice.getOne().getSomething());
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("2", ((One) choice.getOneOrTwo()).getSomething());
    assertNull(choice.getTwo());
    choice = (TestTypes.MyChoice) record.getChoices().get(1);
    assertEquals("02", choice.getTwo().getType());
    assertEquals("3", choice.getTwo().getSomething());
    assertEquals("02", ((Two) choice.getOneOrTwo()).getType());
    assertEquals("3", ((Two) choice.getOneOrTwo()).getSomething());
    assertNull(choice.getOne());
    choice = (TestTypes.MyChoice) record.getChoices().get(2);
    assertEquals("01", choice.getOne().getType());
    assertEquals("4", choice.getOne().getSomething());
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("4", ((One) choice.getOneOrTwo()).getSomething());
    assertNull(choice.getTwo());
    choice = (TestTypes.MyChoice) record.getChoices().get(3);
    assertEquals("01", choice.getOne().getType());
    assertEquals("5", choice.getOne().getSomething());
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("5", ((One) choice.getOneOrTwo()).getSomething());
    assertNull(choice.getTwo());
    choice = (TestTypes.MyChoice) record.getChoices().get(4);
    assertEquals("01", choice.getOne().getType());
    assertEquals("6", choice.getOne().getSomething());
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("6", ((One) choice.getOneOrTwo()).getSomething());
    assertNull(choice.getTwo());
    choice = (TestTypes.MyChoice) record.getChoices().get(5);
    assertEquals("02", choice.getTwo().getType());
    assertEquals("7", choice.getTwo().getSomething());
    assertEquals("02", ((Two) choice.getOneOrTwo()).getType());
    assertEquals("7", ((Two) choice.getOneOrTwo()).getSomething());
    assertNull(choice.getOne());
    choice = (TestTypes.MyChoice) record.getChoices().get(6);
    assertEquals("01", choice.getOne().getType());
    assertEquals("8", choice.getOne().getSomething());
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("8", ((One) choice.getOneOrTwo()).getSomething());
    assertNull(choice.getTwo());
    choice = (TestTypes.MyChoice) record.getChoices().get(7);
    assertEquals("02", choice.getTwo().getType());
    assertEquals("9", choice.getTwo().getSomething());
    assertEquals("02", ((Two) choice.getOneOrTwo()).getType());
    assertEquals("9", ((Two) choice.getOneOrTwo()).getSomething());
    assertNull(choice.getOne());
    choice = (TestTypes.MyChoice) record.getChoices().get(8);
    assertEquals("01", choice.getOne().getType());
    assertEquals("0", choice.getOne().getSomething());
    assertEquals("01", ((One) choice.getOneOrTwo()).getType());
    assertEquals("0", ((One) choice.getOneOrTwo()).getSomething());
    assertNull(choice.getTwo());
    choice = (TestTypes.MyChoice) record.getChoices().get(9);
    assertEquals("02", choice.getTwo().getType());
    assertEquals("1", choice.getTwo().getSomething());
    assertEquals("02", ((Two) choice.getOneOrTwo()).getType());
    assertEquals("1", ((Two) choice.getOneOrTwo()).getSomething());
    assertNull(choice.getOne());

    assertEquals(4, record.getOtherChoices().size());
    OtherChoice otherChoice = record.getOtherChoices().get(0);
    assertEquals("03", otherChoice.getThree().getType());
    assertEquals("1", otherChoice.getThree().getSomething());
    assertNull(otherChoice.getFour());
    otherChoice = record.getOtherChoices().get(1);
    assertEquals("04", otherChoice.getFour().getType());
    assertEquals("1", otherChoice.getFour().getSomething());
    assertNull(otherChoice.getThree());
    otherChoice = record.getOtherChoices().get(2);
    assertEquals("03", otherChoice.getThree().getType());
    assertEquals("2", otherChoice.getThree().getSomething());
    assertNull(otherChoice.getFour());
    otherChoice = record.getOtherChoices().get(3);
    assertEquals("03", otherChoice.getThree().getType());
    assertEquals("3", otherChoice.getThree().getSomething());
    assertNull(otherChoice.getFour());

    assertEquals("", unmarshaller.getCurrentJunk());
  }
}
