package io.github.tjheslin1.westie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class TODOsMustFollowStructure {

    private static final String TODO_REGEX = ".*//[ ]*TODO.*";
    private static final String TODOS_MUST_HAVE_DATE_REGEX = ".*//[ ]*TODO.*[0-9]{1,4}[/-]{1}[A-z0-9]{2,3}[/-]{1}[0-9]{1,4}.*";

    private final List<String> javaFilesToIgnore;

    public TODOsMustFollowStructure(List<String> javaFilesToIgnore) {
        this.javaFilesToIgnore = javaFilesToIgnore;
    }

    public List<Violation> checkAllTodosFollowExpectedStructure(Path pathToCheck) throws Exception {
        return Files.list(pathToCheck)
                .filter(this::isAJavaFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::verifyStructureOfTodos)
                .peek(this::reportViolation)
                .collect(toList());
    }

    private boolean isAJavaFile(Path file) {
        return file.toString().endsWith(".java");
    }

    private boolean notAnExemptFile(Path path) {
        return !javaFilesToIgnore.stream()
                .map(this::postFixedWithJavaExtension)
                .anyMatch(exemptFile -> filenameFromPath(path).equals(exemptFile));
    }

    private String filenameFromPath(Path path) {
        return path.subpath(path.getNameCount() - 1, path.getNameCount()).toString();
    }

    private String postFixedWithJavaExtension(String javaFile) {
        return javaFile.endsWith(".java") ? javaFile : javaFile + ".java";
    }

    private Stream<Violation> verifyStructureOfTodos(Path file) {
        try {
            return Files.lines(file)
                    .filter(this::lineContainsTodo)
                    .filter(this::linesNotConformingToStructure)
                    .map(todoLine -> new Violation(file, todoLine));
        } catch (IOException e) {
            return Stream.of(new Violation(file, "Unable to read file."));
        }
    }

    private boolean lineContainsTodo(String line) {
        return line.matches(TODO_REGEX);
    }

    private boolean linesNotConformingToStructure(String todoLine) {
        return !todoLine.matches(TODOS_MUST_HAVE_DATE_REGEX);
    }

    private void reportViolation(Violation violation) {
        System.out.println(format("Violation!%n'%s'%nThe above violation was caused by the TODO not matching structure with regex: '%s'. " +
                        "%nSpecified in the Westie class: %s%n",
                violation, TODOS_MUST_HAVE_DATE_REGEX, this.getClass().getSimpleName()));
    }
}
