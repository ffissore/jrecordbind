package it.assist.jrecordbind.test;

import it.assist_si.schemas.jrb.ggenum.GGEnumRecord;

public class MyGGEnumRecord extends GGEnumRecord {

  public MyGGEnumRecord() {
    System.out.println("yeppa yeppa");
  }

  @Override
  public MyEnum getMyEnum() {
    return (MyEnum) super.getMyEnum();
  }

}
