package io.github.tjheslin1.westie.katacalisthenicsenforcer.elseusageanalyser;

import io.github.tjheslin1.westie.FileLineViolation;
import io.github.tjheslin1.westie.LineAssertions;
import io.github.tjheslin1.westie.testinfrastructure.TestWestieFileReader;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.singletonList;

public class ElseStatementUsageAnalyserTest implements WithAssertions {

    @Test
    public void reportViolationOfUsageOfElseStatement() throws Exception {
        ElseStatementUsageAnalyser analyser = new ElseStatementUsageAnalyser(new TestWestieFileReader());
        List<FileLineViolation> violations = analyser.noUsageOfElseStatement(Paths.get("src/test/resources/io/github/tjheslin1/examples/katacalisthenics/elsestatementusage"), singletonList("ClassWithCommentedOutElseStatements.java"));

        assertThat(violations.size()).isEqualTo(1);
        LineAssertions lineAssertions = new LineAssertions(violations);
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithElseStatement.java'\n" +
                "\n" +
                "        } else { // wait, I cant use else?\n" +
                "\n" +
                "'else' statement used.\n");
    }

    @Test
    public void commentedOutElseStatementsAreNotViolations() throws Exception {
        ElseStatementUsageAnalyser analyser = new ElseStatementUsageAnalyser(new TestWestieFileReader());
        List<FileLineViolation> violations = analyser.noUsageOfElseStatement(Paths.get("src/test/resources/io/github/tjheslin1/examples/katacalisthenics/elsestatementusage/nested"));

        assertThat(violations.size()).isEqualTo(0);
    }
}