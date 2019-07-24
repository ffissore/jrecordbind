package org.fissore.jrecordbind;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyUtilsTest {

  private static class Bean {

    private String name;
    private int age;
    private boolean set;

    public boolean isSet() {
      return set;
    }

    public void setSet(boolean set) {
      this.set = set;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }

  private PropertyUtils propertyUtils;
  private Bean bean;

  @Before
  public void setUp() {
    propertyUtils = new PropertyUtils();
    bean = new Bean();
  }

  @Test
  public void getSet() {
    assertNull(propertyUtils.getProperty(bean, "name"));
    assertEquals(0, propertyUtils.getProperty(bean, "age"));
    assertFalse((Boolean) propertyUtils.getProperty(bean, "set"));

    propertyUtils.setProperty(bean, "name", "federico");
    propertyUtils.setProperty(bean, "age", 42);
    propertyUtils.setProperty(bean, "set", true);

    assertEquals("federico", propertyUtils.getProperty(bean, "name"));
    assertEquals(42, propertyUtils.getProperty(bean, "age"));
    assertTrue((Boolean) propertyUtils.getProperty(bean, "set"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalType() {
    propertyUtils.setProperty(bean, "age", "world");
  }
}
