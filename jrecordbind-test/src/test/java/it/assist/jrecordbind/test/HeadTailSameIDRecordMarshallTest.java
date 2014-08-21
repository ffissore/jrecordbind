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
import it.assist_si.schemas.jrb.headtailsameid.DetailRecord;
import it.assist_si.schemas.jrb.headtailsameid.HeadTailContainer;
import it.assist_si.schemas.jrb.headtailsameid.HeadTailRecord;

import java.io.InputStreamReader;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class HeadTailSameIDRecordMarshallTest {

  private HeadTailContainer container;
  private Marshaller<HeadTailContainer> marshaller;
  private StringWriter stringWriter;

  @Test
  public void marshallALot() throws Exception {
    for (int i = 0; i < 1000; i++) {
      marshaller.marshall(container, stringWriter);
    }

    assertEquals(48000, stringWriter.toString().length());
  }

  @Test
  public void marshallOne() throws Exception {
    marshaller.marshall(container, stringWriter);

    assertEquals("0001       \n" + "5552       \n" + "5553       \n" + "0004       \n", stringWriter.toString());
    assertEquals(48, stringWriter.toString().length());
  }

  @Before
  public void setUp() throws Exception {
    int i = 0;

    container = new HeadTailContainer();
    HeadTailRecord head = new HeadTailRecord();
    head.setRecordId("000");
    head.setCounter(++i);
    container.setHead(head);

    DetailRecord detail = new DetailRecord();
    detail.setRecordId("555");
    detail.setCounter(++i);
    container.getDetails().add(detail);
    detail = new DetailRecord();
    detail.setRecordId("555");
    detail.setCounter(++i);
    container.getDetails().add(detail);

    HeadTailRecord tail = new HeadTailRecord();
    tail.setRecordId("000");
    tail.setCounter(++i);
    container.setTail(tail);

    marshaller = new Marshaller<HeadTailContainer>(new InputStreamReader(HeadTailSameIDRecordMarshallTest.class
        .getResourceAsStream("/headTailRecordSameID.def.xsd")));

    stringWriter = new StringWriter();
  }

}
