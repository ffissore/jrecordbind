package it.assist.jrecordbind.test;

import eu.educator.schemas.services.crihowithcustomsetter.Choice;
import eu.educator.schemas.services.crihowithcustomsetter.One;
import eu.educator.schemas.services.crihowithcustomsetter.Two;

public class MyChoice extends Choice {

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
