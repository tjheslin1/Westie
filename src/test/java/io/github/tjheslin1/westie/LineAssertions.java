package io.github.tjheslin1.westie;

import org.assertj.core.api.WithAssertions;

import java.util.List;

/**
 * This class is used to solve the issue of different environments asserting on files
 * in different orders.
 */
public class LineAssertions implements WithAssertions {

    private final List<Violation> violations;

    public LineAssertions(List<Violation> violations) {
        this.violations = violations;
    }

    public void violationsContainLineStartingWith(String line) {
        assertThat(violations.stream().anyMatch(violation -> violation.toString()
                .startsWith(line)))
                .isTrue();
    }

    public void violationsContainLineMatching(String regex) {
        assertThat(violations.stream().anyMatch(violation -> violation.toString()
                .matches(regex)))
                .isTrue();
    }
}
