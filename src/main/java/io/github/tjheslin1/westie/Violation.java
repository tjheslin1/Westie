package io.github.tjheslin1.westie;

import java.nio.file.Path;

public class Violation {

    private final Path file;
    private final String line;

    public Violation(Path file, String line) {
        this.file = file;
        this.line = line;
    }

    @Override
    public String toString() {
        return String.format("Line '%s' in file '%s'", line.trim(), file.toAbsolutePath());
    }
}
