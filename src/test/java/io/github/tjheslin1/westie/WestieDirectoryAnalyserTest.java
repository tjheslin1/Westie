package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.testinfrastructure.TestWestieFileReader;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class WestieDirectoryAnalyserTest implements WithAssertions {

    @Test
    public void analysesFileSuccessfully() throws Exception {
        Path pathToCheck = Paths.get("/Users/Tom/GitHub/Westie/src/test/resources/io/github/tjheslin1/examples/lineReading");
        WestieDirectoryAnalyser westieDirectoryAnalyser = new WestieDirectoryAnalyser(pathToCheck, ".java", new TestWestieFileReader());

        List<Violation> violations = westieDirectoryAnalyser.analyseFile(pathToFile -> false, "Expected violation message 1234");

        assertThat(violations).isEmpty();
    }

    @Test
    public void analysesFileWithFailure() throws Exception {
        Path pathToCheck = Paths.get("/Users/Tom/GitHub/Westie/src/test/resources/io/github/tjheslin1/examples/lineReading");
        WestieDirectoryAnalyser westieDirectoryAnalyser = new WestieDirectoryAnalyser(pathToCheck, ".java", new TestWestieFileReader());

        List<Violation> violations = westieDirectoryAnalyser.analyseFile(pathToFile -> true, "Expected violation message 1234");

        assertThat(violations.size()).isEqualTo(1);
        LineAssertions lineAssertions = new LineAssertions(violations);

        lineAssertions.containsViolationMessage("Violation in file 'ReadMyLines.java'\n" +
                "Expected violation message 1234\n");
    }

    @Test
    public void analysesFileContentSuccessfully() throws Exception {
        Path pathToCheck = Paths.get("/Users/Tom/GitHub/Westie/src/test/resources/io/github/tjheslin1/examples/lineReading");
        WestieDirectoryAnalyser westieDirectoryAnalyser = new WestieDirectoryAnalyser(pathToCheck, ".java", new TestWestieFileReader());

        List<Violation> violations = westieDirectoryAnalyser.analyseFileContent(pathToFile -> false, "Expected violation message 1234");

        assertThat(violations).isEmpty();
    }

    @Test
    public void analysesFileContentWithFailure() throws Exception {
        Path pathToCheck = Paths.get("/Users/Tom/GitHub/Westie/src/test/resources/io/github/tjheslin1/examples/lineReading");
        WestieDirectoryAnalyser westieDirectoryAnalyser = new WestieDirectoryAnalyser(pathToCheck, ".java", new TestWestieFileReader());

        List<Violation> violations = westieDirectoryAnalyser.analyseFileContent(pathToFile -> true, "Expected violation message 1234");

        assertThat(violations.size()).isEqualTo(1);
        LineAssertions lineAssertions = new LineAssertions(violations);

        lineAssertions.containsViolationMessage("Violation in file 'ReadMyLines.java'\n" +
                "Expected violation message 1234\n");
    }

    @Test
    public void analysesLinesOfFileContentSuccessfully() throws Exception {
        Path pathToCheck = Paths.get("/Users/Tom/GitHub/Westie/src/test/resources/io/github/tjheslin1/examples/lineReading");
        WestieDirectoryAnalyser westieDirectoryAnalyser = new WestieDirectoryAnalyser(pathToCheck, ".java", new TestWestieFileReader());

        List<Violation> violations = westieDirectoryAnalyser.analyseLinesOfFile(pathToFile -> false, "Expected violation message 1234");

        assertThat(violations).isEmpty();
    }

    @Test
    public void analysesLinesOfFileWithFailure() throws Exception {
        Path pathToCheck = Paths.get("/Users/Tom/GitHub/Westie/src/test/resources/io/github/tjheslin1/examples/lineReading");
        WestieDirectoryAnalyser westieDirectoryAnalyser = new WestieDirectoryAnalyser(pathToCheck, ".java", new TestWestieFileReader());

        List<Violation> violations = westieDirectoryAnalyser.analyseLinesOfFile(pathToFile -> true, "Expected violation message 1234");

        // expecting every line to fail based on simple Predicate which returns true every time.
        assertThat(violations.size()).isEqualTo(12);
    }
}