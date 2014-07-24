package it.assist.jrecordbind.test;

import static org.junit.Assert.*;
import it.assist.jrecordbind.Marshaller;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.sigma.clearbox.batch.jaxb.common.HeaderType;
import com.sigma.clearbox.batch.jaxb.common.TailType;
import com.sigma.clearbox.batch.jaxb.voucher.HeadTailContainer;

public class IncludingRecordDefTest {

  @Test
  public void shouldParseIncludedTypes() throws IOException {
    Marshaller<HeadTailContainer> marshaller = new Marshaller<HeadTailContainer>(Marshaller.class.getClassLoader(), new File(IncludingRecordDefTest.class
        .getResource("/including.def.xsd").getFile()));
    HeaderType head = new HeaderType();
    head.setData("data");
    head.setOra("ora");
    head.setType("type");

    TailType tail = new TailType();
    tail.setData("taildata");
    tail.setOra("tailora");
    tail.setType("tailtype");
    tail.setNumRecord(1);

    HeadTailContainer container = new HeadTailContainer();
    container.setHead(head);
    container.setTail(tail);

    StringWriter writer = new StringWriter();
    marshaller.marshall(container, writer);
    assertEquals("type|data|ora\ntailtype|taildata|tailora|1\n", writer.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void doesNotSupportMultipleMainsInDifferentSchemas() {
    new Marshaller<HeadTailContainer>(Marshaller.class.getClassLoader(), new File(IncludingRecordDefTest.class.getResource(
        "/includingWithAnotherMain.def.xsd").getFile()));
  }
}
