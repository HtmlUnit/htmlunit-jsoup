# HtmlUnit - jsoup support

This is the code repository of the HtmlUnit to jsoup bride.

The htmlunit-jsoup library provides helper utilities 
to bridge the gap between [HtmlUnit](https://htmlunit.org) and [jsoup](https://jsoup.org/).
This converter enables seamless integration between HtmlUnit's comprehensive browser simulation capabilities 
and all the jsoup-based libraries, allowing you to leverage the full ecosystem of jsoup tools 
while maintaining HtmlUnit's JavaScript execution and dynamic content handling.


[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.htmlunit/htmlunit-jsoup/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.htmlunit/htmlunit-jsoup)

:heart: [Sponsor](https://github.com/sponsors/rbri)

## Sample use cases

**[FlexmarkConverterSample](https://github.com/HtmlUnit/htmlunit-jsoup/blob/main/src/test/java/org/htmlunit/jsoup/example/FlexmarkConverterSample.java)**

The FlexmarkConverterSample class demonstrates advanced HTML-to-Markdown conversion using the [Flexmark](https://github.com/vsch/flexmark-java) library.

### Project News

**[Developer Blog](https://htmlunit.github.io/htmlunit-blog/)**

[HtmlUnit@mastodon](https://fosstodon.org/@HtmlUnit) | [HtmlUnit@bsky](https://bsky.app/profile/htmlunit.bsky.social) | [HtmlUnit@Twitter](https://twitter.com/HtmlUnit)

### Latest release Version 4.16.0 / August 29, 2025

### Maven

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>org.htmlunit</groupId>
    <artifactId>htmlunit-jsoup</artifactId>
    <version>4.16.0</version>
</dependency>
```

### Gradle

Add to your `build.gradle`:

```groovy
implementation group: 'org.htmlunit', name: 'htmlunit-jsoup', version: '4.16.0'
```

### Last CI build
The latest builds are available from our
[Jenkins CI build server](https://jenkins.wetator.org/job/HtmlUnit%20-%20Jsoup/ "HtmlUnit - Jsoup CI")

htmlunit-jsoup
[![Build Status](https://jenkins.wetator.org/buildStatus/icon?job=HtmlUnit+-+Jsoup)](https://jenkins.wetator.org/job/HtmlUnit%20-%20Jsoup/)

If you use maven please add:

    <dependency>
        <groupId>org.htmlunit</groupId>
        <artifactId>htmlunit-jsoup</artifactId>
        <version>4.17.0-SNAPSHOT</version>
    </dependency>

You have to add the Central Portal Snapshots repository to your pom `repositories` section also:

    <repositories>
        <repository>
            <name>Central Portal Snapshots</name>
            <id>central-portal-snapshots</id>
            <url>https://central.sonatype.com/repository/maven-snapshots/</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>


## Start HtmlUnit - Jsoup Development

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You simply only need a local maven installation.


### Building

Create a local clone of the repository and you are ready to start.

Open a command line window from the root folder of the project and call

```
mvn compile
```

### Running the tests

```
mvn test
```

## Contributing

Pull Requests and and all other Community Contributions are essential for open source software.
Every contribution - from bug reports to feature requests, typos to full new features - are greatly appreciated.

## Deployment and Versioning

This part is intended for committer who are packaging a release.

* Check all your files are checked in
* Execute these mvn commands to be sure all tests are passing and everything is up to data

```
   mvn versions:display-plugin-updates
   mvn versions:display-dependency-updates
   mvn -U clean test
```

* Update the version number in pom.xml and README.md
* Commit the changes


* Build and deploy the artifacts 

```
   mvn -up clean deploy
```

* Go to [Maven Central Portal](https://central.sonatype.com/) and process the deploy
  - publish the package and wait until it is processed

* Create the version on Github
    * login to Github and open project https://github.com/HtmlUnit/htmlunit-jsoup
    * click Releases > Draft new release
    * fill the tag and title field with the release number (e.g. 4.0.0)
    * append 
        * htmlunit-jsoup-4.x.x.jar
        * htmlunit-jsoup-4.x.x.jar.asc 
        * htmlunit-jsoup-4.x.x.pom
        * htmlunit-jsoup-4.x.x.pom.asc 
        * htmlunit-jsoup-4.x.x-javadoc.jar
        * htmlunit-jsoup-4.x.x-javadoc.jar.asc
        * htmlunit-jsoup-4.x.x-sources.jar
        * htmlunit-jsoup-4.x.x-sources.jar.asc
    * and publish the release 

* Update the version number in pom.xml to start next snapshot development
* Update the htmlunit pom to use the new release

## Authors

* **RBRi**

## License

This project is licensed under the Apache 2.0 License

## Acknowledgments

Many thanks to all of you contributing to HtmlUnit/CSSParser/Rhino in the past.
