# HtmlUnit - jsoup support

This is the code repository of the WebSocket support used by HtmlUnit.

The default WebSocket client used by HtmlUnit is based on Jetty 9. To avoid conflicts in projects
using different versions of Jetty, this repo provides a shaded version of the jetty websocket stuff.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.htmlunit/htmlunit-websocket-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.htmlunit/htmlunit-websocket-client)

:heart: [Sponsor](https://github.com/sponsors/rbri)

### Project News

**[Developer Blog](https://htmlunit.github.io/htmlunit-blog/)**

[HtmlUnit@mastodon](https://fosstodon.org/@HtmlUnit) | [HtmlUnit@bsky](https://bsky.app/profile/htmlunit.bsky.social) | [HtmlUnit@Twitter](https://twitter.com/HtmlUnit)

### Latest release Version 4.13.0 / June 03, 2025

### Maven

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>org.htmlunit</groupId>
    <artifactId>htmlunit-websocket-client</artifactId>
    <version>4.13.0</version>
</dependency>
```

### Gradle

Add to your `build.gradle`:

```groovy
implementation group: 'org.htmlunit', name: 'htmlunit-websocket-client', version: '4.13.0'
```

### Last CI build
The latest builds are available from our
[Jenkins CI build server](https://jenkins.wetator.org/job/HtmlUnit%20-%20Websocket%20Client/ "HtmlUnit - Websocket Client CI")

htmlunit-websocket-client
[![Build Status](https://jenkins.wetator.org/buildStatus/icon?job=HtmlUnit+-+websocket+-+client)](https://jenkins.wetator.org/job/HtmlUnit%20-%20Websocket%20Client/)

If you use maven please add:

    <dependency>
        <groupId>org.htmlunit</groupId>
        <artifactId>htmlunit-websocket-client</artifactId>
        <version>4.14.0-SNAPSHOT</version>
    </dependency>

You have to add the sonatype snapshot repository to your pom `repositories` section also:

    <repository>
        <id>OSS Sonatype snapshots</id>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
        <releases>
            <enabled>false</enabled>
        </releases>
    </repository>


## Start HtmlUnit - Websocket Client Development

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

* Go to [Sonatype staging repositories](https://s01.oss.sonatype.org/index.html#stagingRepositories) and process the deploy
  - select the repository and close it - wait until the close is processed
  - release the package and wait until it is processed

* Create the version on Github
    * login to Github and open project https://github.com/HtmlUnit/htmlunit-websocket-client
    * click Releases > Draft new release
    * fill the tag and title field with the release number (e.g. 4.0.0)
    * append 
        * htmlunit-websocket-client-4.x.x.jar
        * htmlunit-websocket-client-4.x.x.jar.asc 
        * htmlunit-websocket-client-4.x.x.pom
        * htmlunit-websocket-client-4.x.x.pom.asc 
        * htmlunit-websocket-client-4.x.x-javadoc.jar
        * htmlunit-websocket-client-4.x.x-javadoc.jar.asc
        * htmlunit-websocket-client-4.x.x-sources.jar
        * htmlunit-websocket-client-4.x.x-sources.jar.asc
    * and publish the release 

* Update the version number in pom.xml to start next snapshot development
* Update the htmlunit pom to use the new release

## Authors

* **RBRi**
* all the contributors to the [Jetty project](https://github.com/jetty/jetty.project)

## License

This project is licensed under the Apache 2.0 License

## Acknowledgments

Many thanks to all of you contributing to HtmlUnit/CSSParser/Rhino in the past.
