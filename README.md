# What's JRecordBind?

A tiny and super fast library that aims to
- map a fixed or variable length text file to bean instances, ready to be chewed by an import procedure (Unmarshaller)
- and export record beans into a fixed or variable length text file (Marshaller).

## Why?

Almost everybody has written an import procedure of some sort: the customer is always filling your email box with data to import and that he doesn't want to manually type, despite your cool web interface.

Fixed-length are a must for every public institution (at least in Italy): regardless of the age of the destination system, everyone can read a plain text file

JRecordBind aims to leverage the boring parsing task and let the developer focus on real problems: understanding the data and find an easy way to feed the persistence layer.

## Advantages

JRecordBind is (AFAIK) the only tool aimed at fixed-length files that's able to marshall and unmarshall. By the way you may be a producer of fixed length files, not just a consumer.

JRecordBind supports hierarchical fixed length files: records of some type that are "sons" of other record types.

JRecordBind uses XML Schema for the definition file: that could make your learning curve steeper.
Which Java?

Since version 2.3.3, JRecordBind supports both Java 1.5 and Java 6. For Maven users, you need to tweak the artifactId. See below.

## Support

If you need support, [drop an email](federico@fissore.org). If you have found a bug, [file it! file it now!](https://github.com/ffissore/jrecordbind/issues)

## Should I care?

If you are a software developer, yes, you should. At least you should remind the existence of JRecordBind, for the time some customer of yours will ask you to "import this file from our host"

## How it works?

### Record definition

When you need to import a fixed-length file, someone has given you a wide documentation regarding how the file is structured: each field, its length, its value and how to convert it.

JRecordBind needs that specification: it's the starting point. You need to map the documentation into an XSD file. Here's an example:

```
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema targetNamespace="http://schemas.assist-si.it/jrb/simple" xmlns:xs="http://www.w3.org/2001/XMLSchema" 
  xmlns="http://schemas.assist-si.it/jrb/simple" xmlns:jrb="http://jrecordbind.dev.java.net/2/xsd" 
  elementFormDefault="qualified" attributeFormDefault="unqualified">
  <xs:complexType name="SimpleRecord">
    <xs:sequence>
      <xs:element name="name" type="xs:string" jrb:length="20"/>
      <xs:element name="surname" type="xs:string" jrb:length="20"/>
      <xs:element name="taxCode" type="xs:string" jrb:length="16"/>
      <xs:element name="birthday" type="xs:date" jrb:length="8" jrb:converter="it.assist.jrecordbind.test.SimpleRecordDateConverter"/>
      <xs:element name="oneInteger" type="xs:int" jrb:length="2"/>
      <xs:element name="oneFloat" type="xs:float" jrb:length="3" jrb:converter="it.assist.jrecordbind.test.SimpleRecordFloatConverter"/>
    </xs:sequence>
  </xs:complexType>
  <xs:element name="main" type="SimpleRecord" jrb:length="100"/>
</xs:schema>
```

It's a standard XML Schema file (xsd) plus some custom attributes and a mandatory "main" element.
The "main" element will be the starting point, the main bean JRecordBean will (un)marshall.
The custom attributes are:

<table class="bodyTable" border="1">
  <tr class="a">
    <td><b>ATTRIBUTE</b></td>
    <td><b>SCOPE</b></td>
    <td><b>MEANING</b></td>
    <td><b>MANDATORY</b></td>
  </tr>
  <tr class="b">
    <td>jrb:length</td>
    <td>&quot;main&quot; element</td>
    <td>it's the total length of the fixed length file</td>
    <td>Yes. Can be omitted to obtain dynamic length files, if the &quot;delimiter&quot; attribute is specified</td>
  </tr>
  <tr class="a">
    <td>jrb:length</td>
    <td>single elements</td>
    <td>the length of that particular field</td>
    <td>Yes. Can be omitted if the file has dynamic length</td>
  </tr>
  <tr class="b">
    <td>jrb:delimiter</td>
    <td>&quot;main&quot; element</td>
    <td>what delimits each field</td>
    <td>No. It becomes mandatory only if you need dynamic length files</td>
  </tr>
  <tr class="a">
    <td>jrb:padder</td>
    <td>&quot;main&quot; element</td>
    <td>the default padder when not specified</td>
    <td>No. JRecordBind will use its default (see the javadoc)</td>
  </tr>
  <tr class="b">
    <td>jrb:padder</td>
    <td>single elements</td>
    <td>a custom padder for that field</td>
    <td>No. JRecordBind will use its default (see the javadoc)</td>
  </tr>
  <tr class="a">
    <td>jrb:lineSeparator</td>
    <td>&quot;main&quot; element</td>
    <td>what ends each row</td>
    <td>No. By default a &quot;new line&quot; char will be used. DOS format files can be achieved with the value &quot;&amp;#13;&amp;#10;&quot;</td>
  </tr>
  <tr class="b">
    <td>jrb:converter</td>
    <td>single elements</td>
    <td>how to convert that field to/from a string: there're some defaults</td>
    <td>No. JRecordBind will use its default (see the javadoc)</td>
  </tr>
  <tr class="a">
    <td>jrb:row</td>
    <td>single elements</td>
    <td>if a bean is split into more than one row, from the second row on, you need to specify the current row number (zero based)</td>
    <td>No. JRecordBind will default it to 0 (the whole record on one line)</td>
  </tr>
  <tr class="b">
    <td>jrb:subclass</td>
    <td>xs:complexType declaration</td>
    <td>if you need to extend/override some generated bean, you can make JRecordBind instantiate a particular class instead of the one it generates. The specified class must extend the generated class. See below for an example</td>
    <td>No. JRecordBind will default to its generated class</td>
  </tr>
  <tr class="a">
    <td>jrb:setter</td>
    <td>xs:choice declaration</td>
    <td>if you are using jaxb bindings.xjb with the <b>&quot;globalBindings choiceContentProperty='true'&quot;</b> option active, you <b>need</b> to specify the name of the method jaxb has actually generated</td>
    <td>Yes, if you have set choiceContentProperty='true'. No, otherwise</td>
  </tr>
</table>

When you are ready with your definition, generate the beans:

either you use the JAXB2 Maven plugin (for an example configuration, give a look at the test pom.xml)

or an Ant target like the following

```
<target name="regenerate">
  <taskdef name="xjc" classname="com.sun.tools.xjc.XJCTask">
    <classpath refid="classpath" />
  </taskdef>

  <xjc destdir="${src.gen}" binding="bindings.xjb.xml">
    <schema dir="${src.test}" includes="*.def.xsd" />
  </xjc>
</target>
```

You are now ready to (un)marshall your fixed length files

### Unmarshaller

Given an fixed-length text file, honoring the definition, for example

```
WALTER              LIPPMANN            ABCDEF79D18K999A1889092381197
DAVID               JOHNSON             ABCDEF79E18S999B1889092381197
```

you can call the unmarshaller this way:

```
Unmarshaller<SimpleRecord> unmarshaller = new Unmarshaller<SimpleRecord>(new InputStreamReader(
  SimpleRecordUnmarshallTest.class.getResourceAsStream("/simple.def.xsd")));

Iterator<SimpleRecord> iter = unmarshaller.unmarshall(new InputStreamReader(
  SimpleRecordUnmarshallTest.class.getResourceAsStream("simple_test.txt")));

assertTrue(iter.hasNext());

SimpleRecord record = iter.next();
assertEquals("JOHN                ", record.getName());
assertEquals("SMITH               ", record.getSurname());
assertEquals("ABCDEF88L99H123B", record.getTaxCode());
```

The presence of an Iterator assure you a very small memory footprint.

### Marshaller

Given a record bean full of data, you write:

```
//some bean taken somewhere
SimpleRecord record = new SimpleRecord();
record.setName("WALTER");
[...]

//setting up the marshaller

Marshaller<SimpleRecord> marshaller = new Marshaller<SimpleRecord>(new InputStreamReader(
  SimpleRecordMarshallTest.class.getResourceAsStream("/simple.def.xsd")));

//setting up the destination Writer
Writer writer = new StringWriter();

//marshalling
marshaller.marshall(record, writer);
System.out.println(writer.toString());
```

and get the original input back

## How to: different record types

Hierarchical fixed-length files uses ID fields to differentiate the various records: you'll have something like "Record 000 is the address, record A01 are the vehicles..." and so on.

JRecordBind can easily recognize each record type if you use the xsd "fixed" standard attribute: see this example

I.E. you are telling JRecordBind that the "recordId" field, of type string and 3 chars long, will always have the "A00" fixed value

## How to: dynamic length files

Since version 2.1, you can omit the jrb:length attribute while specifying the jrb:delimiter: this way you can achieve dynamic field length.
Click here for an xsd example

## How to: extending the generated bean

Since version 2.2 JRecordBind supports the jrb:subclass attribute at the xs:complexType level. By specifying the fully qualified name of a class extending the generated class, JRecordBind will instantiate that class instead of its generated one, allowing you to extend/override the generated class.
Click here for an xsd example and here for a class example.

## How to: using xs:choice with choiceContentProperty='true'

Since version 2.3, you can specify the jrb:setter attribute at the xs:choice level.
JAXB allows you to have only one method in classes defined as "choice" in the xsd, but that's defined outside of the xsd, in a file called bindings.xjb (using the "choiceContentProperty" option). JRecordBind knows nothing about that file, so you need to duplicate that information into the xsd, in a way JRecordBind can understand.
So, for instance, if you have the elements One and Two inside an xs:choice element, by default the generated choice class will have the methods "setOne" and "setTwo". Specifying choiceContentProperty=true in the bindings.xjb, that class will have the method "setOneOrTwo" only, screwing up JRecordBind. If you add the attribute "jrb:setter='oneOrTwo'" at the xs:choice level, JRecordBind won't be fooled by the JAXB trick.
Click here for an xsd example

## How to: fine grained control on file reading when unmarshalling

When the Unmarshaller reads from the file, by default it returns the current line.
Since version 2.3.3, if you want to customize this behaviour, you can create a new Unmarshaller passing your implementation of the LineReader interface.
Check out this test for an example

## How to: use a custom line separator (aka producing DOS format files)

Since version 2.3.4, you can customize the line separator using the attribute jrb:lineSeparator. By default, lines will be separated by a "new line" char (\n). If you want to produce DOS format files, specify the attribute this way

jrb:lineSeparator="&#13;&#10;"

## How to: removing the spaces from the unmarshalled string fields

When JRecordBind unmarshalls a file, it doesn't know if the spaces it finds in String properties are worth keeping or not, so it keeps them all.

If you are sure these spaces are just a headache and want to get rid of them, you could use the Trimmer utility object. Trimmer will look for String fields (adhering to the JavaBeans specification), get the value, trim it and set it back.

Trimmer is NOT recursive (it doesn't know anything about your object model), so it's up to you to make it work recursively.

## Other examples

Each feature of JRecordBind has at least one xsd file that tests it.

Take a look at the repository

## Where?

JRecordBind comes precompiled against both Java 1.5 and Java 6. Java 6 is the default.

Downloads are available from the download section

Maven users can add JRecordBind as a dependency. First you need to ensure you have the maven2 java.net repository in place

Then add JRecordBind dependency

Java 6 users will add:

```
<dependency>
  <groupId>it.assist.jrecordbind</groupId>
  <artifactId>jrecordbind</artifactId>
  <version>2.3.7</version>
</dependency>
```

Java 1.5 users will add:

```
<dependency>
  <groupId>it.assist.jrecordbind</groupId>
  <artifactId>jrecordbind</artifactId>
  <version>2.3.7</version>
  <classifier>jdk5</classifier>
</dependency>
```