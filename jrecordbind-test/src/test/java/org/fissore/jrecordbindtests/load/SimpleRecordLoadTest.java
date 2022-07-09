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
import org.jrecordbind.schemas.jrb.simple.SimpleRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class SimpleRecordLoadTest {

  private File file;
  private Marshaller<SimpleRecord> marshaller;
  private SimpleRecord record;
  private Unmarshaller<SimpleRecord> unmarshaller;

  @Before
  public void setUp() throws Exception {
    record = new SimpleRecord();
    record.setName("A NAME");
    record.setSurname("A SURNAME");
    record.setTaxCode("ABCDEF88L99H123B");

    var calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 1979);
    calendar.set(Calendar.MONTH, 4);
    calendar.set(Calendar.DAY_OF_MONTH, 18);
    record.setBirthday(calendar);

    record.setOneInteger(81);
    record.setOneFloat(1.97f);

    record.setSelected(true);

    record.setThreeInteger(24);

    var definition = Utils.loadDefinition("/record_definitions/simple.def.xsd");

    marshaller = new Marshaller<>(definition);
    unmarshaller = new Unmarshaller<>(definition);

    file = File.createTempFile("jrecord_bind_simple_record", "test");
  }

  @Test
  public void marshall() throws Exception {
    FileWriter writer = new FileWriter(file);
    for (int i = 0; i < 1000000; i++) {
      marshaller.marshall(record, writer);
    }
    writer.close();
  }

  @Test
  public void marshallUnmarshall() throws Exception {
    FileWriter writer = new FileWriter(file);
    for (int i = 0; i < 1000000; i++) {
      marshaller.marshall(record, writer);
    }
    writer.close();

    FileReader reader = new FileReader(file);
    Iterator<SimpleRecord> iterator = unmarshaller.unmarshallToIterator(reader);
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
