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
import org.jrecordbind.schemas.jrb.enum_with_restrictions.CarType;
import org.jrecordbind.schemas.jrb.enum_with_restrictions.EnumRecord;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class EnumWithRestrictionsRecordUnmarshallTest {

  private Unmarshaller<EnumRecord> unmarshaller;

  @Before
  public void setUp() {
    unmarshaller = new Unmarshaller<>(Utils.loadDefinition("/record_definitions/unsupported/enum_with_restrictions.def.xsd"));
  }

  @Test
  @Ignore
  public void unmarshall() {
    Iterator<EnumRecord> iter = unmarshaller.unmarshallToIterator(Utils.openStream("/record_files/unsupported/enum_with_restrictions.txt"));

    assertTrue(iter.hasNext());
    EnumRecord record = iter.next();
    assertEquals(CarType.AUDI, record.getMyCar());
    record = iter.next();
    assertEquals(CarType.BMW, record.getMyCar());
    record = iter.next();
    assertEquals(CarType.AUDI, record.getMyCar());
    record = iter.next();
    assertEquals(CarType.GOLF, record.getMyCar());

    assertFalse(iter.hasNext());
    assertEquals("", unmarshaller.getCurrentJunk());
  }

}
