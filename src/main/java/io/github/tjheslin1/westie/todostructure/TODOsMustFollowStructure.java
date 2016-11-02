package io.github.tjheslin1.westie.todostructure;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WestieStaticAnalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class TodosMustFollowStructure extends WestieStaticAnalysis {

    private static final String TODO_REGEX = ".*//[ ]*TODO.*";

    private final String todosStructureRegex;

    public TodosMustFollowStructure(String todosStructureRegex, List<String> javaFilesToIgnore) {
        super(javaFilesToIgnore);
        this.todosStructureRegex = todosStructureRegex;
    }

    public List<Violation> checkAllTodosFollowExpectedStructure(Path pathToCheck) throws Exception {
        return Files.list(pathToCheck)
                .filter(this::isAJavaFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::verifyStructureOfTodos)
                .peek(this::reportViolation)
                .collect(toList());
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
        return !todoLine.matches(todosStructureRegex);
    }

    private void reportViolation(Violation violation) {
        System.out.println(format("Violation!%n'%s'%nThe above violation was caused by the TODO not matching structure with regex: '%s'. " +
                        "%nSpecified in the Westie class: %s%n",
                violation, todosStructureRegex, this.getClass().getSimpleName()));
    }
}
