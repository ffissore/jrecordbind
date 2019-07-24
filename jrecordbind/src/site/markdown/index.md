[![Build Status](https://travis-ci.org/ffissore/jrecordbind.svg?branch=master)](https://travis-ci.org/ffissore/jrecordbind)
![License](https://img.shields.io/github/license/ffissore/jrecordbind.svg)
![Version](https://img.shields.io/maven-central/v/org.fissore.jrecordbind/jrecordbind.svg)
[![security status](https://www.meterian.com/badge/gh/ffissore/jrecordbind/security)](https://www.meterian.com/report/gh/ffissore/jrecordbind)
[![stability status](https://www.meterian.com/badge/gh/ffissore/jrecordbind/stability)](https://www.meterian.com/report/gh/ffissore/jrecordbind)

# What's JRecordBind?

A tiny and super fast library that aims to

- parse fixed or variable length text files and map them to java beans (`Unmarshaller`)
- export java beans to fixed or variable length text files (`Marshaller`).

## Why?

Almost everybody has written an import procedure of some sort.

Fixed-length are a must for every public institution (at least in Italy): regardless of the age of the destination system, everyone can read a plain text file

JRecordBind aims to leverage the boring parsing task and let the developer focus on the real problem: understanding the data and find a way to feed the persistence layer.

## What's a record?

A record is a structured text, a line usually, which contains typed information. For example:

```
JOHN                SMITH               ABCDEF88L99H123B1979051800000000811804                  197Y
```

This record starts with a "name" property from column 0 to column 20, right padded with spaces. Then it has a "surname" property, equally padded, from 21 to 40.

Second to last property is a float property from column 89 to 99, left padded with spaces, where the last 2 digits are the decimal part: its value is 1.97

It ends with a boolean property, where "Y" means "true" and "N" means "false".

## Advantages

JRecordBind is (AFAIK) the only tool aimed at fixed-length files that's able to **marshall (write)** and **unmarshall (read)**. By the way you may be a producer of fixed-length files, not just a consumer.

JRecordBind supports **hierarchical** fixed-length files: records of some type that are used only after parent record types.

JRecordBind uses **XML Schema** to define the file format: that could make JRecordBind quickier to learn.

## Which Java?

Since version 3.0.0, JRecordBind requires Java 11+.

Your application `module-info.java` file needs to require `org.fissore.jrecordbind` and `java.xml.bind`, and to export generated classes to JRecordBind, like so:

```java
module com.mycompany {

  requires org.fissore.jrecordbind;
  requires java.xml.bind;

  exports com.mycompany.generated to org.fissore.jrecordbind;

}
```

## Maven

Maven users can add JRecordBind as a dependency

```xml
<dependency>
  <groupId>org.fissore.jrecordbind</groupId>
  <artifactId>jrecordbind</artifactId>
  <version>3.0.0</version>
</dependency>
```

## Support

If you need support, [drop me an email](mailto:federico@fissore.org). If you have found a bug, [please report it! report it now!](https://github.com/ffissore/jrecordbind/issues)

## Is it good?

Yes, it is.

## Show me!

Take a look at the [example project](https://github.com/ffissore/jrecordbind/tree/3.0.0/jrecordbind-example), read the how-tos below, and play with the [tests](https://github.com/ffissore/jrecordbind/tree/3.0.0/jrecordbind-test/src/test/resources/record_definitions).

## How does it work?

### Record definition

When importing a fixed-length file, someone has provided a thorough documentation regarding how the file is structured: each property, their length, their value and how to convert them.

JRecordBind needs that documentation: it's the starting point. Your task is to write an XML Schema version of it. Here's an example:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema targetNamespace="http://schemas.jrecordbind.org/jrb/simple" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns="http://schemas.jrecordbind.org/jrb/simple" xmlns:jrb="http://jrecordbind.org/3/xsd" elementFormDefault="qualified">
  <xs:complexType name="SimpleRecord">
    <xs:sequence>
      <xs:element name="name" type="xs:string" jrb:length="20"/>
      <xs:element name="surname" type="xs:string" jrb:length="20"/>
      <xs:element name="taxCode" type="xs:string" jrb:length="16"/>
      <xs:element name="birthday" type="xs:date" jrb:length="8" jrb:converter="org.fissore.jrecordbindtests.test.TestConverters$SimpleRecordDateConverter"/>
      <xs:element name="oneInteger" type="xs:int" jrb:length="10" jrb:padder="org.fissore.jrecordbind.padders.ZeroLeftPadder"/>
      <xs:element name="twoInteger" type="xs:int" jrb:length="15" jrb:padder="org.fissore.jrecordbind.padders.SpaceRightPadder"/>
      <xs:element name="oneFloat" type="xs:float" jrb:length="10" jrb:converter="org.fissore.jrecordbindtests.test.TestConverters$SimpleRecordFloatConverter" jrb:padder="org.fissore.jrecordbind.padders.SpaceLeftPadder"/>
      <xs:element name="selected" type="xs:boolean" jrb:length="1" jrb:converter="org.fissore.jrecordbindtests.test.TestConverters$YNBooleanConverter"/>
    </xs:sequence>
  </xs:complexType>
  <xs:element name="main" type="SimpleRecord" jrb:length="120"/>
</xs:schema>
```

It's a standard XML Schema file (xsd) plus some special **jrb** attributes and a mandatory **main** element.

The **main** element will be the starting point, the main bean JRecordBean will read/write.

Here's the list of **jrb** attributes:

<table border="1">
<tr>
  <td>
    <strong>ATTRIBUTE</strong>
  </td>
  <td>
    <strong>SCOPE</strong>
  </td>
  <td>
    <strong>MEANING</strong>
  </td>
  <td>
    <strong>MANDATORY</strong>
  </td>
</tr>
<tr>
  <td>jrb:length</td>
  <td>main element</td>
  <td>The length of the fixed-length file</td>
  <td>Yes, unless "delimiter" is specified. When "length" is specified, it's a fixed-length file. When "length" is not specified, it's a dynamic-length file</td>
</tr>
<tr>
  <td>jrb:length</td>
  <td>other elements</td>
  <td>The length of that particular property</td>
  <td>Yes, unless the file is a dynamic-length file</td>
</tr>
<tr>
  <td>jrb:delimiter</td>
  <td>main element</td>
  <td>What delimits each property</td>
  <td>No, unless it's dynamic-length file</td>
</tr>
<tr>
  <td>jrb:padder</td>
  <td>main element</td>
  <td>The default padder to use when not otherwise specified on elements (see below)</td>
  <td>No. JRecordBind will use its default (right padding with spaces)</td>
</tr>
<tr>
  <td>jrb:padder</td>
  <td>other elements</td>
  <td>A custom padder for that property</td>
  <td>No. JRecordBind will use the default one (see above)</td>
</tr>
<tr>
  <td>jrb:lineSeparator</td>
  <td>main element</td>
  <td>What ends each line in the file</td>
  <td>No. By default a "new line" char will be used. DOS format files can be achieved using the value "&amp;#13;&amp;#10;"</td>
</tr>
<tr>
  <td>jrb:converter</td>
  <td>other elements</td>
  <td>How to convert that field to/from a string. Elements with types xs:string, xs:int, and xs:long are automatically converted.</td>
  <td>No. JRecordBind will treat the property as a string</td>
</tr>
<tr>
  <td>jrb:row</td>
  <td>other elements</td>
  <td>If a record is split into more than one line, from the second line on, specify the line number (zero based)</td>
  <td>No. JRecordBind will default it to 0: the whole record on one line. See the multi-row.def.xsd example</td>
</tr>
<tr>
  <td>jrb:subclass</td>
  <td>xs:complexType tag</td>
  <td>If you need to extend/override some generated class, you can make JRecordBind instantiate the specified class instead of the generated one. The specified class must extend the generated class. See the enum.def.xsd example</td>
  <td>No. JRecordBind will default to the generated class</td>
</tr>
<tr>
  <td>jrb:setter</td>
  <td>xs:choice declaration</td>
  <td>When using JAXB bindings.xjb with the <strong>"globalBindings choiceContentProperty='true'"</strong>, specify the name of the method JAXB has generated</td>
  <td>Yes, if using choiceContentProperty='true' in bindings.xjb</td>
</tr>
</table>

When the definition is ready, generate the beans: use [JAXB2 Maven plugin](https://github.com/highsource/maven-jaxb2-plugin) (for an example configuration, give a look at the [test pom.xml](https://github.com/ffissore/jrecordbind/blob/3.0.0/jrecordbind-test/pom.xml)).

Congratulations! You are now ready to read/write fixed-length files.

### A note on dates

When an element is of type `xs:date`, by default JAXB will generate a class field of type `XMLGregorianCalendar`, which JRecordBind does not support. It's mandatory to use a minimal `bindings.xjb` file in order to make JAXB generate `Calendar` fields, like this one:

```xml
<bindings xmlns="http://java.sun.com/xml/ns/jaxb" version="2.0" xmlns:xs="http://www.w3.org/2001/XMLSchema">
  <globalBindings>
    <javaType name="java.util.Calendar" xmlType="xs:date" parseMethod="javax.xml.bind.DatatypeConverter.parseDate" printMethod="javax.xml.bind.DatatypeConverter.printDate"/>
    <javaType name="java.util.Calendar" xmlType="xs:dateTime" parseMethod="javax.xml.bind.DatatypeConverter.parseDate" printMethod="javax.xml.bind.DatatypeConverter.printDate"/>
  </globalBindings>
</bindings>
```

Take a look at the [example project](https://github.com/ffissore/jrecordbind/tree/3.0.0/jrecordbind-example).

### Read/Unmarshall

Given an fixed-length text file, honoring the definition, for example

```
WALTER              LIPPMANN            ABCDEF79D18K999A1889092381197
DAVID               JOHNSON             ABCDEF79E18S999B1889092381197
```

you can read/unmarshall it this way:

```java
Unmarshaller<SimpleRecord> unmarshaller = new Unmarshaller<SimpleRecord>(
    new InputStreamReader(getClass().getResourceAsStream("/simple.def.xsd")));

Iterator<SimpleRecord> iter = unmarshaller.unmarshall(
    new InputStreamReader(getClass().getResourceAsStream("simple_test.txt")));

assertTrue(iter.hasNext());

SimpleRecord record = iter.next();
assertEquals("WALTER", record.getName());
assertEquals("LIPPMANN", record.getSurname());
assertEquals("ABCDEF79D18K999A", record.getTaxCode());
```

### Write/Marshall

Given a bean loaded with data, you can write/marshall it this way:

```java
SimpleRecord record = new SimpleRecord();
record.setName("WALTER");
record.setSurname("LIPPMANN");
// other properties omitted

Marshaller<SimpleRecord> marshaller = new Marshaller<SimpleRecord>(
    new InputStreamReader(getClass().getResourceAsStream("/simple.def.xsd")));

Writer writer = new StringWriter();

marshaller.marshall(record, writer);

System.out.println(writer.toString());
```

## How-tos

### Different record types

Hierarchical fixed-length files use ID fields to distinguish the records. Documentation will say something like "Record 000 is an address, records A01 are vehicles..." and so on.

JRecordBind can easily recognize each record type using the XML Schema standard attribute `fixed`: [see this example](https://github.com/ffissore/jrecordbind/blob/3.0.0/jrecordbind-test/src/test/resources/record_definitions/hierarchical.def.xsd).

### Dynamic length files

You can omit the `jrb:length` attribute and instead specify the `jrb:delimiter` attribute: this way you get a **dynamic-length** file: [see this example](https://github.com/ffissore/jrecordbind/blob/3.0.0/jrecordbind-test/src/test/resources/record_definitions/dynamic_length.def.xsd).

### Extend the generated bean

Add the `jrb:subclass` attribute to the `xs:complexType` tag. By specifying the fully qualified name of a class extending the generated class, JRecordBind will instantiate that class instead of the generated one, allowing you to extend/override the generated class: [see this example](https://github.com/ffissore/jrecordbind/blob/3.0.0/jrecordbind-test/src/test/resources/record_definitions/enum.def.xsd).

### Using xs:choice with choiceContentProperty='true'

Add the `jrb:setter` attribute to the `xs:choice` tag.

With the types `One` and `Two` inside an `xs:choice` element, the default generated class will have methods `setOne` and `setTwo`. JAXB can generate only one method by specifying `choiceContentProperty=true` in file `bindings.xjb`: the generated method will be `setOneOrTwo`.

JRecordBind is not aware of this JAXB trick, it will look for methods `setOne` and `setTwo` and it will fail: that information must be duplicated into the XML Schema, in a way JRecordBind can understand.

Add the attribute `jrb:setter="oneOrTwo"` to the `xs:choice` tag, and JRecordBind will work as expected.

[See this example](https://github.com/ffissore/jrecordbind/blob/3.0.0/jrecordbind-test/src/test/resources/record_definitions/choice.def.xsd#L24).

### Fine grained control on file reading when unmarshalling

When the `Unmarshaller` reads from the file, by default it returns the current line.

In order to customize such behaviour, create an `Unmarshaller` passing an implementation of the `LineReader` interface: [see this example](https://github.com/ffissore/jrecordbind/blob/3.0.0/jrecordbind-test/src/test/java/org/fissore/jrecordbindtests/test/SimpleNotPaddedLineReaderUnmarshallTest.java).

### Use a custom line separator (and reading/writing DOS format files)

Line separator can be customized using the attribute `jrb:lineSeparator`. By default, lines will be separated by a "new line" char (`\n`). If order to read/write DOS format files, specify the attribute this way

```
jrb:lineSeparator="&#13;&#10;"
```

which is the XML equivalent of `\r\n`.

### Other examples

Each feature of JRecordBind has at least one xsd file that tests it.

Take a look at the [repository](https://github.com/ffissore/jrecordbind/tree/3.0.0/jrecordbind-test/src/test/resources/record_definitions).
