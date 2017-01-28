package io.github.tjheslin1.westie.katacalisthenicsenforcer.elseusagechecker;

import io.github.tjheslin1.westie.LineAssertions;
import io.github.tjheslin1.westie.Violation;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class ElseStatementUsageCheckerTest implements WithAssertions {

    @Test
    public void reportViolationOfUsageOfElseStatement() throws Exception {
        ElseStatementUsageChecker checker = new ElseStatementUsageChecker(singletonList("ClassWithCommentedOutElseStatements.java"));
        List<Violation> violations = checker.noUsageOfElseStatement(Paths.get("src/test/resources/io/github/tjheslin1/examples/katacalisthenics/elsestatementusage"));

        assertThat(violations.size()).isEqualTo(1);
        LineAssertions lineAssertions = new LineAssertions(violations);
        lineAssertions.violationsContainLineMatching("'\\} else \\{ \\/\\/ wait, I cant use else\\?' in file '.*\\/ClassWithElseStatement.java'");
    }

    @Test
    public void commentedOutElseStatementsAreNotViolations() throws Exception {
        ElseStatementUsageChecker checker = new ElseStatementUsageChecker(emptyList());
        List<Violation> violations = checker.noUsageOfElseStatement(Paths.get("src/test/resources/io/github/tjheslin1/examples/katacalisthenics/elsestatementusage/nested"));

        assertThat(violations.size()).isEqualTo(0);
    }
}