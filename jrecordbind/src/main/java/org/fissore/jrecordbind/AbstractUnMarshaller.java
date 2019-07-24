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

package org.fissore.jrecordbind;

import org.fissore.jrecordbind.converters.StringConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fissore.jrecordbind.Utils.*;

abstract class AbstractUnMarshaller {

  protected final Map<String, Converter> converters;
  protected final Map<String, Padder> padders;
  protected final RecordDefinition definition;
  protected final PropertyUtils propertyUtils;

  public AbstractUnMarshaller(RecordDefinition definition, Map<String, Converter> converters, Map<String, Padder> padders) {
    this.definition = definition;
    this.propertyUtils = new PropertyUtils();

    List<RecordDefinition.Property> properties = collectProperties(this.definition);

    this.converters = new HashMap<>();
    this.converters.putAll(converters);
    this.converters.put(StringConverter.class.getName(), new StringConverter());
    createAndStoreMissingInstancesOf(properties, RecordDefinition.Property::getConverter, this.converters);

    this.padders = new HashMap<>();
    this.padders.putAll(padders);
    this.padders.put(this.definition.getDefaultPadder(), (Padder) newInstanceOf(this.definition.getDefaultPadder()));
    createAndStoreMissingInstancesOf(properties, RecordDefinition.Property::getPadder, this.padders);
  }

  /**
   * Gets the padder for the specified property or the default one specified by the record definition
   *
   * @param currentDefinition the record definition
   * @param property          the property
   * @return a padder instance
   */
  protected Padder getPadder(RecordDefinition currentDefinition, RecordDefinition.Property property) {
    if (property.getPadder() != null) {
      return padders.get(property.getPadder());
    }
    return padders.get(currentDefinition.getDefaultPadder());
  }

  /**
   * Gets the converter for the specified property or an instance of {@link StringConverter}
   *
   * @param property the property
   * @return a converter instance
   */
  protected Converter getConverter(RecordDefinition.Property property) {
    if (property.getConverter() != null) {
      return converters.get(property.getConverter());
    }

    return converters.get(StringConverter.class.getName());
  }

}
