package io.github.tjheslin1.westie;

/**
 * A Violation is the result of a static analysis test finding a discrepancy.
 */
public interface Violation {

    void reportViolation();
}
