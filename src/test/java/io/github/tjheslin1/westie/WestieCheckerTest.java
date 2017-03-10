package io.github.tjheslin1.westie;

import org.assertj.core.api.WithAssertions;

public class WestieCheckerTest implements WithAssertions {

    private static class TestWestieChecker extends WestieChecker {

        TestWestieChecker() {
            super(new TestWestieFileReader());
        }
    }
}