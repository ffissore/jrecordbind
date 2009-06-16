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
import it.assist_si.schemas.jrb.onlychildren.OnlyChildrenContainer;

import java.io.InputStreamReader;
import java.util.Iterator;

import junit.framework.TestCase;

public class OnlyChildrenUnmarshallTest extends TestCase {

  private Unmarshaller<OnlyChildrenContainer> unmarshaller;

  public OnlyChildrenUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<OnlyChildrenContainer>(new InputStreamReader(OnlyChildrenUnmarshallTest.class
        .getResourceAsStream("/onlyChildren.def.xsd")));
  }

  public void testUnmarshall() throws Exception {
    Iterator<OnlyChildrenContainer> iter = unmarshaller.unmarshall(new InputStreamReader(
        OnlyChildrenUnmarshallTest.class.getResourceAsStream("only_children.txt")));

    assertTrue(iter.hasNext());
    OnlyChildrenContainer record = iter.next();
    assertEquals("000", record.getHeader().getRecordId());
    assertEquals("999", record.getTrailer().getRecordId());
    assertEquals("555", record.getDetails().get(0).getRecordId());
    assertEquals(4, record.getDetails().size());

    assertTrue(iter.hasNext());
    record = iter.next();
    assertEquals(2, record.getDetails().size());

    assertEquals("", unmarshaller.getCurrentJunk());
  }
}
