package it.assist.jrecordbind;

import java.io.BufferedReader;

public interface LineReader {

  public String readLine(BufferedReader reader);

  public void setGlobalPadder(Padder globalPadder);

  public void setLength(int length);

  public void setPropertyDelimiter(String propertyDelimiter);

}
