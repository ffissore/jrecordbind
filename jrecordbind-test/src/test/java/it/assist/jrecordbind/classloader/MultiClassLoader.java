package it.assist.jrecordbind.classloader;

import it.assist.jrecordbind.Marshaller;
import it.assist.jrecordbind.Unmarshaller;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.classloader.ShrinkWrapClassLoader;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Iterator;

public class MultiClassLoader {

  private static ClassLoader classLoader;

  @BeforeClass
  public static void init() {
    File file = Maven.configureResolver()
        .workOffline()
        .withMavenCentralRepo(false)
        .withClassPathResolution(true)
        .resolve("it.assist.jrecordbind:jrecordbind-pojo:2.3.9-SNAPSHOT")
        .withTransitivity().asSingleFile();

    JavaArchive archive = ShrinkWrap.create(ZipImporter.class)
        .importFrom(file).as(JavaArchive.class);

    classLoader = new ShrinkWrapClassLoader(archive);
  }

  @Test
  public void testFailUnmarshall() throws Throwable {
    Reader reader = new StringReader("A NAME              A SURNAME           ABCDEF88L99H123B1979051881197Y                              \n");
    InputStreamReader definition = new InputStreamReader(MultiClassLoader.class.getResourceAsStream("/classloader/record.def.xsd"));

    try {
      Unmarshaller unmarshaller = new Unmarshaller(definition);
      unmarshaller.unmarshall(reader);
    } catch (Throwable e) {
      //That's ugly, I just want to find out if the root cause is ClassNotFoundException
      while(e.getCause()!=null) {
        e = e.getCause();
      }
      Assert.assertEquals(ClassNotFoundException.class, e.getClass());
    }
  }

  @Test
  public void testUnmarshallWithClassLoader() {
    Reader reader = new StringReader("A NAME              A SURNAME           ABCDEF88L99H123B1979051881197Y                              \n");
    InputStreamReader definition = new InputStreamReader(MultiClassLoader.class.getResourceAsStream("/classloader/record.def.xsd"));

    Unmarshaller unmarshaller = new Unmarshaller(classLoader, definition);
    Iterator iterator = unmarshaller.unmarshall(reader);

    Assert.assertTrue(iterator.hasNext());
    Object object = iterator.next();
    Assert.assertEquals("it.assist.jrecordbind.classloader.domain.ClassLoaderRecord", object.getClass().getName());
    Assert.assertFalse(iterator.hasNext());
  }

  @Test
  public void testFailMarshall() throws Throwable {
    Object instance = createRecordObject();

    StringWriter writer = new StringWriter();

    InputStreamReader definition = new InputStreamReader(MultiClassLoader.class.getResourceAsStream("/classloader/record.def.xsd"));

    try {
      Marshaller marshaller = new Marshaller(definition);
      marshaller.marshall(instance, writer);
    } catch(Throwable e) {
      //That's ugly, I just want to find out if the root cause is ClassNotFoundException
      while(e.getCause()!=null) {
        e = e.getCause();
      }
      Assert.assertEquals(ClassNotFoundException.class, e.getClass());
    }
  }

  @Test
  public void testMarshallWithClassLoader() throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
    Object instance = createRecordObject();

    StringWriter writer = new StringWriter();

    InputStreamReader definition = new InputStreamReader(MultiClassLoader.class.getResourceAsStream("/classloader/record.def.xsd"));

    Marshaller marshaller = new Marshaller(classLoader, definition);
    marshaller.marshall(instance, writer);

    Assert.assertEquals(
        "A NAME              A SURNAME           ABCDEF88L99H123B1979051881197Y                              \n",
        writer.toString());

    Assert.assertEquals(101, writer.toString().length());
  }

  private Object createRecordObject() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 1979);
    calendar.set(Calendar.MONTH, 4);
    calendar.set(Calendar.DAY_OF_MONTH, 18);

    Class<?> recordClass = classLoader.loadClass("it.assist.jrecordbind.classloader.domain.ClassLoaderRecord");
    Object instance = recordClass.newInstance();

    call(instance, "setName", "A NAME");
    call(instance, "setSurname", "A SURNAME");
    call(instance, "setTaxCode", "ABCDEF88L99H123B");

    call(instance, "setBirthday", calendar);

    call(instance, "setOneInteger", 81);
    call(instance, "setOneFloat", 1.97f);

    call(instance, "setSelected", true);
    return instance;
  }

  private void call(Object instance, String methodName, Object... params) throws InvocationTargetException, IllegalAccessException {
    Method[] methods = instance.getClass().getMethods();
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        method.invoke(instance, params);
        return;
      }
    }
    throw new IllegalStateException("Method [" + methodName + "] not found");
  }
}
