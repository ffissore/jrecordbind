package it.assist.jrecordbind.padders;

import it.assist.jrecordbind.Padder;

abstract class AbstractPadder implements Padder {

  private final char padChar;

  public AbstractPadder(char padChar) {
    this.padChar = padChar;
  }

  protected String buildPad(String string, int length) {
    int origLength = getInputLength(string);
    int padSize = length - origLength;
    if (padSize <= 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder(padSize);
    while (padSize-- > 0) {
      sb.append(padChar);
    }
    return sb.toString();
  }

  private int getInputLength(String string) {
    if (string == null) {
      return 0;
    }
    return string.length();
  }

}
