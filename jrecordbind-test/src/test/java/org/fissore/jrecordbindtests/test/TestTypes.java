package org.fissore.jrecordbindtests.test;

import org.jrecordbind.schemas.jrb._enum.EnumRecord;
import org.jrecordbind.schemas.jrb.choice.Choice;
import org.jrecordbind.schemas.jrb.choice.One;
import org.jrecordbind.schemas.jrb.choice.Two;

public class TestTypes {

  public enum MyEnum {

    ONE, TWO

  }

  public enum MyOtherEnum {

    THIS, THAT

  }

  public static class MyChoice extends Choice {

    public Object getOneOrTwo() {
      if (getOne() != null) {
        return getOne();
      }
      return getTwo();
    }

    public void setOneOrTwo(Object o) {
      if (o instanceof One) {
        super.setOne((One) o);
      } else if (o instanceof Two) {
        super.setTwo((Two) o);
      } else {
        throw new IllegalArgumentException(o.toString());
      }
    }

  }

  public static class MyEnumRecord extends EnumRecord {

    public MyEnum getMyEnumConverted() {
      return (MyEnum) super.getMyEnum();
    }

    public void setMyEnumConverted(MyEnum value) {
      super.setMyEnum(value);
    }
  }
}
