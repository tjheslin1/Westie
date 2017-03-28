[![Build Status](https://travis-ci.org/tjheslin1/Westie.svg?branch=master)](https://travis-ci.org/tjheslin1/Westie)

## What is it?

Westie is a tool for creating your own custom static analysis checks.

The project contains already provided checks which can be used straight away.

All provided Analysers extend [WestieAnalyser.java](src/main/java/io/github/tjheslin1/westie/WestieAnalyser.java), which
contains useful functions for creating your own static analysis checks for your project and team.

## Examples of provided static analysis analysers:

### _EnvironmentPropertiesAnalysers_ ensures that all of your environment '.properties' files share the same keys.
```java
@Ignore
@Test
public void allEnvironmentPropertiesFilesHaveTheSameKeys() throws Exception {
    EnvironmentPropertiesAnalyser analyser = new EnvironmentPropertiesAnalyser();

    List<FileViolation> violations = analyser.propertiesProvidedForAllEnvironments(PROPERTIES_DIR);

    assertThat(violations).isEmpty();
}
```

### _JiraReferenceAnalyser_ ensures that Jira issues referenced are in an accepted status (e.g only in Development stage).
```java
@Test
public void canOnlyReferenceJiraIssuesInDevelopment() throws Exception {
    JiraIssues jiraIssues = new JiraIssues(HTTP_CLIENT, JIRA_HOSTNAME, JIRA_USERNAME, JIRA_PASSWORD, singletonList("Development"));
    JiraReferenceAnalyser jiraReferenceAnalyser = new JiraReferenceAnalyser(jiraIssues, "JIRA-[0-9]{3}");

    List<FileLineViolation> violations = jiraReferenceAnalyser.todosAreInAllowedStatuses(BASE_PACKAGE);

    assertThat(violations).isEmpty();
}
```

## How do I get it?

### Maven
```xml
<dependency>
    <groupId>io.github.tjheslin1</groupId>
    <artifactId>Westie</artifactId>
    <version>1.2</version>
</dependency>
```
### Gradle
```groovy
compile 'io.github.tjheslin1:Westie:1.2'
```
### SBT
```scala
libraryDependencies += "io.github.tjheslin1" % "Westie" % "1.2"
```

