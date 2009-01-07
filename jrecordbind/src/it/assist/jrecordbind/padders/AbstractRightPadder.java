package it.assist.jrecordbind.padders;

abstract class AbstractRightPadder extends AbstractPadder {

  public AbstractRightPadder(char padChar) {
    super(padChar);
  }

  public String pad(String string, int length) {
    return new StringBuilder(length).append(string).append(buildPad(string, length)).toString();
  }
}
