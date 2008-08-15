package it.assist.jrecordbind.gen;

import it.assist.jrecordbind.RecordDefinition;

import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 * Generates the bean source code, using a Velocity template
 * 
 * @author Federico Fissore
 */
public class RecordBeanGenerator {

  public void generate(RecordDefinition definition, Writer writer) throws Exception {
    VelocityContext context = new VelocityContext();
    context.put("definition", definition);

    VelocityEngine ve = new VelocityEngine();
    ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
    ve.setProperty("class.resource.loader.class", "it.assist.jrecordbind.gen.MyRL");
    ve.init();

    ve.getTemplate("BaseBean.vm").merge(context, writer);
  }
}
