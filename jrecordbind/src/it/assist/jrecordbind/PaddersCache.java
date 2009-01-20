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

package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.util.List;

/**
 * Padders used in a fixed-length file definition are instantiated and cached
 * here
 * 
 * @author Federico Fissore
 */
class PaddersCache extends Cache<Padder> {

  public PaddersCache(RecordDefinition definition) {
    List<Property> properties = collectProperties(definition);
    for (Property property : properties) {
      addPadder(property.getPadder());
    }
    addPadder(definition.getGlobalPadder());
  }

  private void addPadder(String padderClassName) {
    if (padderClassName != null && !containsKey(padderClassName)) {
      try {
        put(padderClassName, (Padder) Class.forName(padderClassName).newInstance());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

}
