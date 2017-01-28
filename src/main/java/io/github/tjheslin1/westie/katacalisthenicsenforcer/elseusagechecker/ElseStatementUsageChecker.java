package io.github.tjheslin1.westie.katacalisthenicsenforcer.elseusagechecker;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WestieChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class ElseStatementUsageChecker extends WestieChecker {

    public ElseStatementUsageChecker(List<String> javaFilesToIgnore) {
        super(javaFilesToIgnore);
    }

    public List<Violation> noUsageOfElseStatement(Path pathToCheck) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isAJavaFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::checkElseStatementUsage)
                .peek(this::reportViolation)
                .collect(toList());
    }

    private Stream<Violation> checkElseStatementUsage(Path file) {
        try {
            List<String> lines = Files.lines(file).collect(toList());
            return lines.stream()
                    .filter(this::elseStatementUsage)
                    .map(elseLine -> new Violation(file, elseLine));
        } catch (IOException e) {
            return Stream.of(new Violation(file, "Unable to read file."));
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

    private void reportViolation(Violation violation) {
        System.out.println(format("Violation!%n'%s'%nThe above line contains an else statement.", violation));
    }
}
