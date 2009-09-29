package it.assist.jrecordbind;

import java.io.BufferedReader;
import java.io.IOException;

class SimpleLineReader implements LineReader {

  @Override
  public String readLine(BufferedReader reader) {
    try {
      return reader.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setGlobalPadder(Padder globalPadder) {
  }

  @Override
  public void setLength(int length) {
  }

  @Override
  public void setPropertyDelimiter(String propertyDelimiter) {
  }

}
