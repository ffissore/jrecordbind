package it.assist.jarb.gen;

import it.assist.jarb.RecordDefinition;

import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

public class BeanGenerator {

  public void generate(RecordDefinition definition, Writer writer) throws Exception {
    VelocityContext context = new VelocityContext();
    context.put("definition", definition);

    VelocityEngine ve = new VelocityEngine();
    ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
    ve.setProperty("class.resource.loader.class", "it.assist.jarb.gen.MyRL");
    ve.init();

    ve.getTemplate("BaseBean.vm").merge(context, writer);
  }
}
