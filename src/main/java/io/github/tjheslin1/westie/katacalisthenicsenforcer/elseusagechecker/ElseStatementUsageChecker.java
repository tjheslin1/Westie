package io.github.tjheslin1.westie.katacalisthenicsenforcer.elseusagechecker;

import io.github.tjheslin1.westie.FileLineViolation;
import io.github.tjheslin1.westie.WestieChecker;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ElseStatementUsageChecker extends WestieChecker {

    public ElseStatementUsageChecker() {
        super();
    }

    public ElseStatementUsageChecker(WestieFileReader fileReader, List<String> javaFilesToIgnore) {
        super(fileReader, javaFilesToIgnore);
    }

    public List<FileLineViolation> noUsageOfElseStatement(Path pathToCheck) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isAJavaFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::checkElseStatementUsage)
                .peek(FileLineViolation::reportViolation)
                .collect(toList());
    }

    private Stream<FileLineViolation> checkElseStatementUsage(Path file) {
        try {
            return allFileLines(file).stream()
                    .filter(this::elseStatementUsage)
                    .map(elseLine -> new FileLineViolation(file, elseLine, "'else' statement used."));
        } catch (IOException e) {
            return Stream.of(new FileLineViolation(file, "Unable to read file.", e.getMessage()));
        }
    }

    private boolean elseStatementUsage(String line) {
        if (line.contains(" else ")) {
            int elseIndex = line.indexOf(" else ");
            int firstSlashSlash = line.indexOf("//");
            int firstSlashStart = line.indexOf("/*");

            if (firstSlashSlash > -1) {
                if (firstSlashStart > -1) {
                    if (firstSlashStart < firstSlashSlash) {
                        return elseIndex < firstSlashStart;
                    } else {
                        return elseIndex < firstSlashSlash;
                    }
                }
                return elseIndex < firstSlashSlash;
            } else if (firstSlashStart > -1) {
                return elseIndex < firstSlashSlash;
            }

            return true;
        }
        return false;
    }
}
