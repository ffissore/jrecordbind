# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

### [3.2.0] - 2019-07-25
##### Fixed
- When unpadding a field made only of padding char (for example spaces), padder failed to unpad. Thx @jzaratei for reporting

### [3.1.0] - 2019-07-25
##### Added
- Added support to standard XML Schema attribute `fixed` when marshalling. Previously, only the unmarshaller honoured it, so users could potentially create files which didn't comply
with the xsd definition. Now, when a property has a `fixed` value, that valuw will be used, regardless of whatever value was set by users. 

### [3.0.0] - 2019-07-24
##### Changed
- Minimum Java version set to 11
- Package name renamed to `org.fissore.jrecordbind`
- Copyright holder from Assist (the company) to Federico Fissore (the individual), and contributors
- Using `bindings.xjb` is now mandatory due to latext JAXB using `XMLGregorianCalendar` for `xs:date` elements
- Padders now pad AND unpad: a property value of "john   " in a text file will be unmarshalled as "john"
- Various changes in the API (both constructors and methods signature) in order to improve the overall design
##### Added
- Example module
- Support to inject converters and padders from outside, thus making JRecordBind more dependency-injection-framework-friendly
##### Removed
- Pojo module
- Many tests, whenever it was unclear what they were testing and/or the feature under test was also tested in other tests. Goal was to provide contributors a clearer test suite
- ant support
- Internal cache for padders and converters

- - -
At the beginning, no proper changelog was made, commits were not descriptive, and there were no tags. Thus, there are holes. Sorry about that.

### [2.3.4] - 2009-11-24
##### Added
- `jrb:lineSeparator` and support to custom character sequences that mark the end of a line (aka DOS format files)

### [2.3.3] - 2009-10-11
##### Added
- `LineReader`, a way to customize how text files are read

### [2.3.0] - 2009-09-21
##### Added
- `jrb:setter` and support to custom setter methods when using `xs:choice`

### [2.2.1] - 2009-06-19
##### Changed
- Switched to maven
##### Added
- `jrb:subclass` and support to custom classes extending generated ones

### [2.1.1] - 2009-02-24
##### Fixed
- Bug with dynamic-length files
- Marshaller was always adding an extra space

### [2.1.0] - 2009-01-26
##### Added
- `jrb:delimiter` and support to dynamic-length files
