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
import org.jrecordbind.schemas.jrb.imported.HeaderType;
import org.jrecordbind.schemas.jrb.imported.TailType;
import org.jrecordbind.schemas.jrb.importing.HeadTailContainer;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class ImportedSchemaMarshallTest {

  @Test
  public void shouldParseImportedTypes() {
    Marshaller<HeadTailContainer> marshaller = new Marshaller<>(Utils.loadDefinition("/record_definitions/importing.def.xsd"));

    HeaderType head = new HeaderType();
    head.setData("data");
    head.setOra("ora");
    head.setType("type");

    TailType tail = new TailType();
    tail.setData("taildata");
    tail.setOra("tailora");
    tail.setType("tailtype");
    tail.setNumRecord(1);

    HeadTailContainer container = new HeadTailContainer();
    container.setHead(head);
    container.setTail(tail);

    StringWriter writer = new StringWriter();
    marshaller.marshall(container, writer);
    assertEquals("type|data|ora\ntailtype|taildata|tailora|1\n", writer.toString());
  }

}
