package it.assist.jrecordbind.padders;

import junit.framework.TestCase;

public class ZeroPaddersTest extends TestCase {

  private ZeroLeftPadder zeroLeftPadder;
  private ZeroRightPadder zeroRightPadder;

  public ZeroPaddersTest() {
    zeroLeftPadder = new ZeroLeftPadder();
    zeroRightPadder = new ZeroRightPadder();
  }

  public void testLeftPad() throws Exception {
    assertEquals("000example", zeroLeftPadder.pad("example", 10));
    assertEquals("example", zeroLeftPadder.pad("example", 5));
  }

  public void testRIghtPad() throws Exception {
    assertEquals("example000", zeroRightPadder.pad("example", 10));
    assertEquals("example", zeroRightPadder.pad("example", 5));
  }
}
