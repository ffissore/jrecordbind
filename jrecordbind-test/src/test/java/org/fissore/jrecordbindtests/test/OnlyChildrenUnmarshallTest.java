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
import org.jrecordbind.schemas.jrb.only_sub_records.OnlyChildrenContainer;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OnlyChildrenUnmarshallTest {

  private Unmarshaller<OnlyChildrenContainer> unmarshaller;

  @Before
  public void setUp() {
    unmarshaller = new Unmarshaller<>(Utils.loadDefinition("/record_definitions/only_sub_records.def.xsd"));
  }

  @Test
  public void unmarshall() {
    Iterator<OnlyChildrenContainer> iter = unmarshaller.unmarshallToIterator(Utils.openStream("/record_files/only_sub_records.txt"));

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
