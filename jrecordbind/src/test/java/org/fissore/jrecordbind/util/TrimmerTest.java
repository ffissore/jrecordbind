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

package org.fissore.jrecordbind.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrimmerTest {

  private static class Bean {

    private boolean active;
    private String name;
    private String surname;

    public boolean isActive() {
      return active;
    }

    public void setActive(boolean active) {
      this.active = active;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getSurname() {
      return surname;
    }

    public void setSurname(String surname) {
      this.surname = surname;
    }
  }

  private static class BeanWithMethodThatThrowsException extends Bean {

    public String getException() {
      return "value";
    }

    public void setException(String newValue) {
      throw new RuntimeException();
    }

  }

  private Trimmer trimmer;

  @Before
  public void setUp() {
    trimmer = new Trimmer();
  }

  @Test
  public void trim() {
    var bean = new Bean();
    bean.setActive(false);
    bean.setName("JOHN                ");
    bean.setSurname(null);

    trimmer.trim(bean);

    assertEquals("JOHN", bean.getName());
    assertNull(bean.getSurname());
    assertFalse(bean.isActive());
  }

  @Test(expected = TrimmerException.class)
  public void trimThrowsException() {
    var bean = new BeanWithMethodThatThrowsException();
    bean.setActive(false);
    bean.setName("JOHN                ");
    bean.setSurname(null);

    trimmer.trim(bean);
  }
}
