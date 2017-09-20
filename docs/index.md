# Westie

## Writing a static analysis test

Let's say we want to check that all of our java files start with the open source license comment:

```
/*
 * Copyright 2017 Me <me@email.com>.
 *
 * ....
 */

*/
```

We could write this using `Westie` as such:


Starting with a meaningfully named unit test and instantiating a `WestieAnalyser`.
```java
    @Test
    public void srcJavaFilesShouldStartWithOpenSourceLicenseDoc() throws Exception {
        new WestieAnalyser();
    }
```

We want to analyse all files under a given directory and only java files:

(The other option would be to analyse a specific file using: `analyseFiles()`.
```java
    @Test
    public void srcJavaFilesShouldStartWithOpenSourceLicenseDoc() throws Exception {
        new WestieAnalyser()
                .analyseDirectory(WORKING_DIR.resolve("main").resolve("java"))
                .forJavaFiles();
    }
```

Now, we want to check the contents of each file, as a whole, treating it as a string:

(LICENSE is a `static String` with your license header comment)

`analyseFileContent()` expects a `Predicate` which takes in a String representing the file content.

It also takes a violation message which is reported when the Predicate is applied to a file which returns false.
```java
    @Test
    public void srcJavaFilesShouldStartWithOpenSourceLicenseDoc() throws Exception {
        new WestieAnalyser()
                .analyseDirectory(WORKING_DIR.resolve("main").resolve("java"))
                .forJavaFiles().analyseFileContent(fileContent -> !fileContent.startsWith(LICENSE),
                        "All src java files must start with open source license header.");
    }
```

Now we can capture the result and assert there are no Violations, failing the test if there are any.
The Violations will automatically be printed to the console, including your provided violation message.
```java
@Test
public void srcJavaFilesShouldStartWithOpenSourceLicenseDoc() throws Exception {
    List<Violation> violations = new WestieAnalyser()
            .analyseDirectory(WORKING_DIR.resolve("main").resolve("java"))
            .forJavaFiles().analyseFileContent(fileContent -> !fileContent.startsWith(LICENSE),
                    "All src java files must start with open source license header.");

    assertThat(violations).isEmpty();
}
```