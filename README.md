## What is it?

Westie is a tool for creating your own custom static analysis checks.

The project contains already provided checks which can be used straight away.

All provided Checkers extend [WestieChecker.java](src/main/java/io/github/tjheslin1/westie/WestieChecker.java), which 
contains useful functions for creating your own static analysis checks for your project and team.

## Examples of provided static analysis checkers:

### _EnvironmentPropertiesChecker_ ensures that all of your environment `.properties` files share the same keys. 
```java
@Test
public void allEnvironmentPropertiesFilesHaveTheSameKeys() throws Exception {
    EnvironmentPropertiesChecker checker = new EnvironmentPropertiesChecker(FILES_TO_IGNORE);

    List<Violation> violations = checker.propertiesProvidedForAllEnvironments(PROPERTIES_DIR);
    
    assertThat(violations).isEmpty();
}
```

### _JiraReferenceChecker_ ensures that Jira issues referenced are in an accepted status (e.g only in Development stage).
```java
@Test
public void canOnlyReferenceJiraIssuesInDevelopment() throws Exception {
    JiraIssues jiraIssues = new JiraIssues(HTTP_CLIENT, JIRA_HOSTNAME, JIRA_USERNAME, JIRA_PASSWORD, singletonList("Development"));
    JiraReferenceChecker jiraReferenceChecker = new JiraReferenceChecker(jiraIssues, "JIRA-[0-9]{3}", FILES_TO_IGNORE);

    List<Violation> violations = jiraReferenceChecker.todosAreInAllowedStatuses(BASE_PACKAGE);

    assertThat(violations).isEmpty();
}
```

##How do I get it?

###Maven
```xml
<dependency>
    <groupId>io.github.tjheslin1</groupId>
    <artifactId>Westie</artifactId>
    <version>1.1.3</version>
</dependency>
```
###Gradle
```groovy
compile 'io.github.tjheslin1:Westie:1.1.3'
```
###SBT
```scala
libraryDependencies += "io.github.tjheslin1" % "Westie" % "1.1.3"
```

