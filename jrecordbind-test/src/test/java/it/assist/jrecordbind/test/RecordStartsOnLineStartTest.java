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
import it.assist_si.schemas.jrb.recordstartsonlinestart.RecordA;
import it.assist_si.schemas.jrb.recordstartsonlinestart.RecordB;
import it.assist_si.schemas.jrb.recordstartsonlinestart.RecordSeries;

import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class RecordStartsOnLineStartTest {

  private Unmarshaller<RecordSeries> unmarshaller;

  public RecordStartsOnLineStartTest() throws Exception {
    unmarshaller = new Unmarshaller<RecordSeries>(Unmarshaller.class.getClassLoader(), new InputStreamReader(RecordStartsOnLineStartTest.class
        .getResourceAsStream("/recordStartsOnLineStart.def.xsd")));
  }

  @Test
  public void unmarshall() throws Exception {
    Iterator<RecordSeries> iter = unmarshaller.unmarshall(new InputStreamReader(RecordStartsOnLineStartTest.class
        .getResourceAsStream("recordStartsOnLineStart.txt")));

    assertTrue(iter.hasNext());
    RecordSeries recordSeries = iter.next();
    List<RecordA> recordAList = recordSeries.getRecordA();
    assertEquals(recordAList.size(),2);
    assertEquals("1234567890", recordAList.get(0).getPayLoad());
    assertEquals("1234567890", recordAList.get(0).getPayLoad());
    
    List<RecordB> recordBList = recordSeries.getRecordB();
    assertEquals(recordBList.size(),2);
    assertEquals("12301678901234567890", recordBList.get(0).getPayLoad());
    assertEquals("12301678901234567890", recordBList.get(0).getPayLoad());
    
  }
}
