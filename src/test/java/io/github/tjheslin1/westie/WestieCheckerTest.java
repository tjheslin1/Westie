package io.github.tjheslin1.westie;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;

public class WestieCheckerTest implements WithAssertions {

    @Test
    public void readsAllLines() throws Exception {
        TestWestieChecker checker = new TestWestieChecker();
        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/lineReading/ReadMyLines.java");

        List<String> lines = checker.readAllLines(pathToCheck);
        assertThat(lines).isEqualTo(READ_MY_LINES_FILE);
    }

    @Test
    public void readAllLinesBlowsUpIfAnythingOtherThanRegularFileIsPassedIn() throws Exception {
        TestWestieChecker checker = new TestWestieChecker();
        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/lineReading");

        assertThatThrownBy(() -> checker.readAllLines(pathToCheck))
                .hasMessage("Expected a file to read. Instead provided: 'src/test/resources/io/github/tjheslin1/examples/lineReading'");
    }

    private static final List<String> READ_MY_LINES_FILE = asList(
            "package io.github.tjheslin1.examples",
            "",
            "public class ReadMyLines {",
            "    /*",
            "    Syntax of defining memebers of the java class is,",
            "      <modifier> type <name>;",
            "    */",
            "",
            "    public static void main(String args[]) {",
            "        System.out.println(\"Hello\");",
            "    }",
            "}"
    );

    private static class TestWestieChecker extends WestieChecker {

        TestWestieChecker() {
            super();
        }
    }
}