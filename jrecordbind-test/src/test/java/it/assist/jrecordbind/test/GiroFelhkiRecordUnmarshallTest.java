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
import hu.acs.crm.analitika.girofelhki.GiroFelhki;
import hu.acs.crm.analitika.girofelhki.GiroFelhkiAcs;
import hu.acs.crm.analitika.girofelhki.GiroFelhkiFejRekord;
import it.assist.jrecordbind.Unmarshaller;

import java.io.InputStreamReader;
import java.util.Iterator;

import org.junit.Test;

public class GiroFelhkiRecordUnmarshallTest {

  private Unmarshaller<GiroFelhki> unmarshaller;

  public GiroFelhkiRecordUnmarshallTest() throws Exception {
    unmarshaller = new Unmarshaller<GiroFelhki>(new InputStreamReader(GiroFelhkiRecordUnmarshallTest.class
        .getResourceAsStream("/GiroFelhki.xsd")));
  }

  @Test
  public void unmarshall() throws Exception {
    Iterator<GiroFelhki> iter = unmarshaller.unmarshall(new InputStreamReader(GiroFelhkiRecordUnmarshallTest.class
        .getResourceAsStream("girofelhki.txt")));

    assertTrue(iter.hasNext());
    GiroFelhki record = iter.next();
    GiroFelhkiFejRekord fej = record.getFej();
    assertEquals("01", fej.getRekordtipus());
    assertEquals("FELHKI", fej.getUzenettipus());
    assertEquals("bbbbbbbbbbbbb", fej.getSzolgaltatoKodja());

    assertEquals(2, record.getAcsRekord().size());
    GiroFelhkiAcs giroFelhkiAcs = record.getAcsRekord().get(0);
    assertEquals("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb", giroFelhkiAcs.getAcsFej().getInditoBankNeve());
    assertEquals(2, giroFelhkiAcs.getTetel().size());
    assertEquals(279, giroFelhkiAcs.getTetel().get(0).getFelhbeFelhat().length());

    assertEquals(1111, giroFelhkiAcs.getAcsLab().getAlcsoportbeliFelhatTetelekSzama());

    assertEquals(222222, record.getLab().getFelhatTetelekSzamaOsszesen());
  }
}
