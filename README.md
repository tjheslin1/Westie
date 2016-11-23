## What is it?

Westie is a tool for creating your own custom static analysis checks.

The project contains already provided checks which can be used straight away.

All provided Checkers extend [WestieChecker.java](src/main/java/io/github/tjheslin1/westie/WestieChecker.java), which 
contains useful functions for creating your own static analysis checks for your project and team.

## Examples of provided static analysis checkers:

### _ImportsRestrictionChecker_: limit library imports to a particular package
```java
@Test
public void oracleImportsConfinedToDatabasePackage() throws Exception {
    ImportRestriction oracleImportRestriction = importRestriction("io.github.tjheslin1.database", "import oracle.jdbc.*");
    ImportsRestrictionChecker importCheck = new ImportsRestrictionChecker(singletonList(oracleImportRestriction), FILES_TO_IGNORE);

    List<Violation> violations = importCheck.checkImportsAreOnlyUsedInAcceptedPackages(BASE_PACKAGE);

    assertThat(violations).isEmpty();
}
```

### _JiraReferenceChecker_: ensure Jira issues referenced are in an accepted status (e.g only in Development stage)
```java
@Test
public void canOnlyReferenceJiraIssuesInDevelopment() throws Exception {
    JiraIssues jiraIssues = new JiraIssues(HTTP_CLIENT, JIRA_HOSTNAME, JIRA_USERNAME, JIRA_PASSWORD, singletonList("Development"));
    JiraReferenceChecker jiraReferenceChecker = new JiraReferenceChecker(jiraIssues, "JIRA-[0-9]{3}", emptyList());

    List<Violation> violations = jiraReferenceChecker.checkAllJiraTodosAreInAllowedStatuses(BASE_PACKAGE);

    assertThat(violations).isEmpty();
}
```

##How do I get it?

###Maven
```xml
<dependency>
    <groupId>io.github.tjheslin1</groupId>
    <artifactId>Westie</artifactId>
    <version>1.1.2</version>
</dependency>
```
###Gradle
```groovy
compile 'io.github.tjheslin1:Westie:1.1.2'
```
###SBT
```scala
libraryDependencies += "io.github.tjheslin1" % "Westie" % "1.1.2"
```

