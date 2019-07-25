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

import org.fissore.jrecordbind.Marshaller;
import org.fissore.jrecordbindtests.Utils;
import org.jrecordbind.schemas.jrb.head_and_tail_use_same_record_id.DetailRecord;
import org.jrecordbind.schemas.jrb.head_and_tail_use_same_record_id.HeadTailContainer;
import org.jrecordbind.schemas.jrb.head_and_tail_use_same_record_id.HeadTailRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class HeadTailSameIDRecordMarshallTest {

  private HeadTailContainer container;
  private Marshaller<HeadTailContainer> marshaller;
  private StringWriter stringWriter;

  @Before
  public void setUp() {
    int i = 0;

    container = new HeadTailContainer();
    HeadTailRecord head = new HeadTailRecord();
    head.setRecordId("record id will be ignored because there's a 'fixed' value");
    head.setCounter(++i);
    container.setHead(head);

    DetailRecord detail = new DetailRecord();
    detail.setRecordId("record id will be ignored because there's a 'fixed' value");
    detail.setCounter(++i);
    container.getDetails().add(detail);
    detail = new DetailRecord();
    detail.setRecordId("record id will be ignored because there's a 'fixed' value");
    detail.setCounter(++i);
    container.getDetails().add(detail);

    HeadTailRecord tail = new HeadTailRecord();
    tail.setRecordId("record id will be ignored because there's a 'fixed' value");
    tail.setCounter(++i);
    container.setTail(tail);

    marshaller = new Marshaller<>(Utils.loadDefinition("/record_definitions/head_and_tail_use_same_record_id.def.xsd"));

    stringWriter = new StringWriter();
  }

  @Test
  public void marshallALot() {
    for (int i = 0; i < 1000; i++) {
      marshaller.marshall(container, stringWriter);
    }

    assertEquals(48000, stringWriter.toString().length());
  }

  @Test
  public void marshallOne() {
    marshaller.marshall(container, stringWriter);

    assertEquals("0001       \n" + "5552       \n" + "5553       \n" + "0004       \n", stringWriter.toString());
    assertEquals(48, stringWriter.toString().length());
  }

}
