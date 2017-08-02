package io.github.tjheslin1.westie.filemustcontainline;

import io.github.tjheslin1.westie.LineAssertions;
import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.testinfrastructure.TestWestieFileReader;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileMustContainContentAnalyserTest implements WithAssertions {

    private static final String IMPORTANT_CONTENT =
            "public String importantMethod() {\n" +
            "    return \"somethingImportant\"; \n" +
            "}";

    @Test
    public void checksFileContainsContent() throws Exception {
        FileMustContainContentAnalyser fileMustContainContentAnalyser = new FileMustContainContentAnalyser(new TestWestieFileReader());

        Path fileToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/importantcontent/ImportantContent.java");
        List<Violation> violations = fileMustContainContentAnalyser.checkFileMustContainImportantContent(fileToCheck, IMPORTANT_CONTENT);

        assertThat(violations).hasSize(1);
        LineAssertions lineAssertions = new LineAssertions(violations);

        lineAssertions.containsViolationMessage("Violation in file 'ImportantContent.java'\n" +
                "Expected file 'ImportantContent.java' to contain content\n" +
                "public String importantMethod() {\n" +
                "    return \"somethingImportant\"; \n" +
                "}\n");
    }
}