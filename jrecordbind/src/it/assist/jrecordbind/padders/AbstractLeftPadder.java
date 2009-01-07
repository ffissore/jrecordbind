package it.assist.jrecordbind.padders;

abstract class AbstractLeftPadder extends AbstractPadder {

  public AbstractLeftPadder(char padChar) {
    super(padChar);
  }

  public String pad(String string, int length) {
    return new StringBuilder(length).append(buildPad(string, length)).append(string).toString();
  }

}
