package it.assist.jrecordbind.padders;

import junit.framework.TestCase;

public class SpacePaddersTest extends TestCase {

  private SpaceLeftPadder spaceLeftPadder;
  private SpaceRightPadder spaceRightPadder;

  public SpacePaddersTest() {
    spaceLeftPadder = new SpaceLeftPadder();
    spaceRightPadder = new SpaceRightPadder();
  }

  public void testLeftPad() throws Exception {
    assertEquals("   example", spaceLeftPadder.pad("example", 10));
    assertEquals("example", spaceLeftPadder.pad("example", 5));
  }

  public void testRIghtPad() throws Exception {
    assertEquals("example   ", spaceRightPadder.pad("example", 10));
    assertEquals("example", spaceRightPadder.pad("example", 5));
  }
}
