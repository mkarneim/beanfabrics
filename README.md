## Beanfabrics
#### A Presentation Model Component Framework for Java Desktop Applications

Author: Michael Karneim

Project Homepage: https://github.com/mkarneim/beanfabrics

### About
Beanfabrics is a *presentation model component framework* for building rich clients and desktop applications with Java Swing or SWT. The Beanfabrics framework is free and open source software.

The term *presentation model* (PM) is the name of a pattern that was [introduced by Martin Fowler] in 2004. According to Fowler the PM
> "represents the state and behavior of the presentation independently of the GUI controls used in the interface".

### Documentation
The documentation is spread over multiple places:

* At Google Code there is a brief [introduction].
* The Beanfabrics Wiki contains an [overview]], a [tutorial], and some [examples].
* At GitHub there is the [JavaDoc API documentation].
* If you understand German you might want to watch a [presentation] I have given at the TNG headquaters near Munich. Here is a link to the [slides].

### Features
Beanfabrics provides
* a set of ready-to-use PM components.
* a set of ready-to-use visual presentation view components.

Beanfabrics
* has a mechanism for binding visual components dynamically to specific nodes of the PM.
* is compliant with the JavaBeans specification and supports [bean customization] within GUI builders like [WindowBuilder].
* is a framework to create custom PM and view components.

### License
Beanfabrics is open source, and it is distributed under the terms of the [LGPL license].

For more information please read the [license] file.

### Maven Coordinates
Beanfabrics *binaries* are available at [Sonatype OSS Maven Repository] and [Maven Central]. The [releases page] contains a list of all stable releases with a change log.

#### Beanfabrics for Swing
```xml
<dependency>
    <groupId>org.beanfabrics</groupId>
    <artifactId>beanfabrics-swing</artifactId>
    <version>1.4.3</version>
</dependency>
```

#### Beanfabrics for SWT
```xml
<dependency>
    <groupId>org.beanfabrics</groupId>
    <artifactId>beanfabrics-swt</artifactId>
    <version>1.4.3</version>
</dependency>
```

### Dependencies
Beanfabrics has only minimal dependencies.

* [Java] 6
* [slf4j] 1.7.1
* [junit] 4.10 (test scope)
* [logback-classic] 1.0.7 (test scope)

### Beanfabrics Eclipse Plugin
The Eclipse pluing provides a *Bindable Component Wizard* for creating a custom View component for a given PM component.

It can be downloaded and installed with the Update Manager inside Eclipse.
The update site is at
http://www.beanfabrics.org/pub/updatesite/site.xml

### How to Build

Beanfabrics can be built with [Gradle].

Please make sure to set the Java Home property in [gradle.properties] to JDK 1.6,
since Beanfabrics must not be compiled against JDK 1.7 libraries (rt.jar) in order to ensure backward compatibility to Java 6.

To build Beanfabrics, open your terminal app, change into the ```beanfabrics``` directory and run:

```./gradlew build``` (on linux / osx)

or

```gradlew.bat build``` (on windows)

[introduced by Martin Fowler]: http://martinfowler.com/eaaDev/PresentationModel.html
[WindowBuilder]: http://www.eclipse.org/windowbuilder
[LGPL license]: lgpl.txt
[license]: license.txt
[bean customization]: http://docs.oracle.com/javase/tutorial/javabeans/advanced/customization.html
[Sonatype OSS Maven Repository]: https://oss.sonatype.org/content/repositories/releases/org/beanfabrics
[Maven Central]: http://search.maven.org/#search|ga|1|org.beanfabrics
[releases page]: https://github.com/mkarneim/beanfabrics/releases
[Beanfabrics Eclipse plugin]: http://www.beanfabrics.org/index.php?title=Eclipse_Plugin
[Java]: http://www.oracle.com/technetwork/java/
[slf4j]: http://www.slf4j.org/
[junit]: http://junit.org/
[logback-classic]: http://logback.qos.ch/
[introduction]: https://code.google.com/p/beanfabrics/wiki/Introduction
[Beanfabrics homepage]: http://www.beanfabrics.org
[examples]: https://github.com/mkarneim/beanfabrics/wiki/Examples
[overview]: https://github.com/mkarneim/beanfabrics/wiki/Overview
[demo applications]: http://www.beanfabrics.org/index.php?title=Demo_applications
[JavaDoc API documentation]: https://github.com/mkarneim/beanfabrics/wiki/Documentation
[presentation]: http://youtu.be/fwEQ-JBu_bI
[slides]: https://docs.google.com/file/d/0Bzq_i9FFoaRUc0dxbDNPU0lydGs/edit?usp=sharing
[Gradle]: http://www.gradle.org/
[gradle.properties]: http://github.com/mkarneim/beanfabrics/blob/master/gradle.properties
