## Beanfabrics
#### A Presentation Model Component Framework for Java Desktop Applications

Author: Michael Karneim

Project Homepage: http://www.beanfabrics.org

### About
Beanfabrics is a *presentation model component framework* for building rich clients and desktop applications with Java Swing or SWT (still experimental). The Beanfabrics framework is free and open source software.

The term *presentation model* (PM) is the name of a pattern that was [introduced by Martin Fowler] in 2004. According to Fowler the PM
> "represents the state and behavior of the presentation independently of the GUI controls used in the interface".

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

### Download
* The [releases page] contains links to all stable releases with a change log and the download links.
* There is also a [Beanfabrics Eclipse plugin] for creating custom view classes.

### Dependencies
Beanfabrics has only minimal dependencies.

* [Java] 6
* [slf4j] 1.7.1
* [junit] 4.10 (test scope)
* [logback-classic] 1.0.7 (test scope)

### Documentation
The documentation is spread over multiple places:

* At Google Code there is a brief [introduction].
* The [Beanfabrics homepage] contains an [overview], a [tutorial], some [code examples] and [demo applications].
* At GitHub there is the [JavaDoc API documentation].
* If you understand German you might want to watch a [presentation] I have given at the TNG headquaters near Munich. Here is a link to the [slides].

### Source

This project's source code was formerly hosted at http://beanfabrics.googlecode.com.
Since December 2013 it is hosted at https://github.com/mkarneim/beanfabrics.

You can build the Beanfabrics libraries yourself using ANT or MAVEN.

### Building Beanfabrics with Gradle

First, make sure that Gradle is using Jdk 1.6, since Beanfabrics can not be compiled with
Jdk 1.7 (or later) because of backward compatibility issues.

You must set the Java Home property in ```gradle.properties``` to some sensible value.

For example:
```
org.gradle.java.home=/usr/lib/jvm/java-6-oracle/jre
```

To build Beanfabrics with Gradle, open your terminal app, change into the ```beanfabrics``` directory and run:

```./gradlew build``` (on linux / osx)

or

```gradlew .bat build``` (on windows)

[introduced by Martin Fowler]: http://martinfowler.com/eaaDev/PresentationModel.html
[WindowBuilder]: http://www.eclipse.org/windowbuilder
[LGPL license]: lgpl.txt
[license]: license.txt
[bean customization]: http://docs.oracle.com/javase/tutorial/javabeans/advanced/customization.html
[releases page]: https://github.com/mkarneim/beanfabrics/releases
[Beanfabrics Eclipse plugin]: http://www.beanfabrics.org/index.php?title=Eclipse_Plugin
[Java]: http://www.oracle.com/technetwork/java/
[slf4j]: http://www.slf4j.org/
[junit]: http://junit.org/
[logback-classic]: http://logback.qos.ch/
[introduction]: https://code.google.com/p/beanfabrics/wiki/Introduction
[Beanfabrics homepage]: http://www.beanfabrics.org
[overview]: http://www.beanfabrics.org/index.php?title=Overview
[tutorial]: http://www.beanfabrics.org/index.php?title=Tutorial
[code examples]: http://www.beanfabrics.org/index.php?title=Examples
[demo applications]: http://www.beanfabrics.org/index.php?title=Demo_applications
[JavaDoc API documentation]: https://github.com/mkarneim/beanfabrics/wiki/Documentation
[presentation]: http://youtu.be/fwEQ-JBu_bI
[slides]: https://docs.google.com/file/d/0Bzq_i9FFoaRUc0dxbDNPU0lydGs/edit?usp=sharing
