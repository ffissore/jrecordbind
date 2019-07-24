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
import org.jrecordbind.schemas.jrb.head_and_tail_use_same_record_id.HeadTailContainer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class HeadTailSameIDRecordUnmarshallTest {

  private Unmarshaller<HeadTailContainer> unmarshaller;

  @Before
  public void setUp() {
    unmarshaller = new Unmarshaller<>(Utils.loadDefinition("/record_definitions/head_and_tail_use_same_record_id.def.xsd"));
  }

  @Test
  public void unmarshall() {
    List<HeadTailContainer> records = unmarshaller.unmarshallToStream(Utils.openStream("/record_files/head_and_tail_use_same_record_id.txt")).collect(Collectors.toList());

    assertEquals(1, records.size());

    HeadTailContainer record = records.get(0);
    assertEquals("000", record.getHead().getRecordId());
    assertEquals(1, record.getHead().getCounter());
    assertEquals(2, record.getDetails().size());
    assertEquals(2, record.getDetails().get(0).getCounter());
    assertEquals(3, record.getDetails().get(1).getCounter());
    assertEquals("000", record.getTail().getRecordId());
    assertEquals(4, record.getTail().getCounter());

    assertEquals("", unmarshaller.getCurrentJunk());
  }
}
