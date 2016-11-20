# Westie

## What is it?

Westie is a tool for creating your own custom static analysis checks.

The project contains already provided checks which can be used straight away, as well as functions which can be used and extended.

The static analysis checker classes all extend [WestieStaticAnalysis.java](src/main/java/io/github/tjheslin1/westie/WestieStaticAnalysis.java), an
abstract class containing useful functions for creating your own static analysis checks for your project and team.

## Example usage of a provided static analysis checker

```java
@Test
public void canOnlyReferenceJiraIssuesInDevelopment() throws Exception {
    JiraIssues jiraIssues = new JiraIssues(HTTP_CLIENT, JIRA_URL_FORMAT, JIRA_USERNAME, JIRA_PASSWORD, singletonList("Development"));
    JiraTodoMustBeInAllowedStatus jiraTodoMustBeInAllowedStatus = new JiraTodoMustBeInAllowedStatus(jiraIssues, "JIRA-[0-9]{3}", emptyList());

    List<Violation> violations = jiraTodoMustBeInAllowedStatus.checkAllJiraTodosAreInAllowedStatuses(BASE_PACKAGE);

    assertThat(violations).isEmpty();
}
```

##How do I get it?

###Maven
```xml
<dependency>
    <groupId>io.github.tjheslin1</groupId>
    <artifactId>Westie</artifactId>
    <version>1.0</version>
</dependency>
```
###Gradle
```groovy
compile 'io.github.tjheslin1:Westie:1.0'
```
###SBT
```scala
libraryDependencies += "io.github.tjheslin1" % "Westie" % "1.0"
```

