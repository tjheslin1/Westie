package io.github.tjheslin1.westie;

import org.assertj.core.api.WithAssertions;

import java.util.List;

/**
 * This class is used to solve the issue of different environments asserting on files
 * in different orders.
 */
public class LineAssertions implements WithAssertions {

    private final List<? extends Violation> violations;

    public LineAssertions(List<? extends Violation> violations) {
        this.violations = violations;
    }

    public void containsViolationMessage(String message) {
        assertThat(violations.stream().anyMatch(violation -> violation.toString()
                .equals(message)))
                .isTrue();

    }
}
