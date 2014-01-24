## Beanfabrics
#### A Presentation Model Component Framework for Java Desktop Applications

Author: Michael Karneim

Project Homepage: http://www.beanfabrics.org

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

### Building Beanfabrics using ANT 
* Install Ant 1.7 or later (http://ant.apache.org/)
* Navigate to the Beanfabrics folder
* Run ANT on build.xml with the target "create-bin-zip":
```ant create-bin-zip```

### Building Beanfabrics using MAVEN 
* To install beanfabrics to your local repository run
```mvn install```

[LGPL license]: lgpl.txt
[license]: license.txt
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

