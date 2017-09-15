[![Build Status](https://travis-ci.org/tjheslin1/Westie.svg?branch=master)](https://travis-ci.org/tjheslin1/Westie)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.tjheslin1/Westie/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.tjheslin1/Westie)

## What is it?

Westie is a tool for creating your own custom static analysis checks.

The project contains already provided checks which can be used straight away.

All provided Analysers utilise [WestieAnalyser.java](src/main/java/io/github/tjheslin1/westie/WestieAnalyser.java), which
is the entrypoint to a fluent API with useful functions for creating your own static analysis checks for your team, on your projects.

## Examples of provided static analysis analysers:

### _EnvironmentPropertiesAnalysers_ ensures that all of your environment '.properties' files share the same keys.
```java
@Test
public void allEnvironmentPropertiesFilesHaveTheSameKeys() throws Exception {
    List<FileViolation> violations = new EnvironmentPropertiesAnalyser()
            .propertiesProvidedForAllEnvironments(PROPERTIES_DIR);

    assertThat(violations).isEmpty();
}
```
_No more forgetting to add a url to all your environment's configurations. Your build will instead fail._

### _JiraReferenceAnalyser_ ensures that Jira issues referenced are in an accepted status (e.g only in Development stage).
```java
@Test
public void canOnlyReferenceJiraIssuesInDevelopment() throws Exception {
    List<Violation> violations = new JiraReferenceAnalyser(jiraIssues, JIRA_STORY_REGEX)
            .todosAreInAllowedStatuses(BASE_PACKAGE);

    assertThat(violations).isEmpty();
}

// below is part of 'JiraReferenceAnalyser.java'
public List<Violation> todosAreInAllowedStatuses(Path pathToCheck, List<String> filesToIgnore) throws IOException {
    return westieAnalyser.analyseDirectory(pathToCheck)
            .forJavaFiles().ignoring(filesToIgnore)
            .analyseLinesOfFile(this::checkJiraTodos, format("Violation was caused by a reference to a " +
                    "Jira issue which is not in any of the accepted statuses: '%s'.", jiraIssues.allowedStatuses()));
}

private boolean checkJiraTodos(String fileLine) {
    return isJiraTodoLine(fileLine) && jiraIssueInUnacceptedState(fileLine);
}
...
```
_No more forgetting to complete TODOs as part of your Jira story. Your build will instead fail. The same can be done for Git Issues._

## How do I get it?

### Maven
```xml
<dependency>
    <groupId>io.github.tjheslin1</groupId>
    <artifactId>Westie</artifactId>
    <version>1.4.1</version>
    <scope>test</scope>
</dependency>
```
### Gradle
```groovy
testCompile 'io.github.tjheslin1:Westie:1.4.1'
```
