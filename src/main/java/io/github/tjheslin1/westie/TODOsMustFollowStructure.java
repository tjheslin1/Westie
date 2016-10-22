package io.github.tjheslin1.westie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class TODOsMustFollowStructure {

    private static final String TODO_REGEX = ".*//[ ]*TODO.*";
    private static final String TODOS_MUST_HAVE_DATE_REGEX = ".*//[ ]*TODO.*[0-9]{1,4}[/-]{1}[A-z0-9]{2,3}[/-]{1}[0-9]{1,4}.*";

    public List<Violation> checkAllTodosFollowExpectedStructure() throws Exception {
        Path resources = Paths.get(TODOsMustFollowStructure.class.getClassLoader().getResource("ClassWithTodos.java").toURI()).getParent();

        return Files.list(resources)
                .filter(this::isAJavaFile)
                .flatMap(this::verifyStructureOfTodos)
                .peek(this::reportViolation)
                .collect(toList());
    }

    private boolean isAJavaFile(Path file) {
        return file.toString().endsWith(".java");
    }

    private Stream<Violation> verifyStructureOfTodos(Path file) {
        try {
            Stream<String> linesContaningTodos = Files.lines(file)
                    .filter(this::lineContainsTodo);

            Stream<String> linesConformingToStructure = linesContaningTodos
                    .filter(this::linesNotConformingToStructure);

            return linesConformingToStructure
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
        System.out.println(format("Violation! '%s' does not conform to the TODO structure with regex: '%s'",
                violation, TODOS_MUST_HAVE_DATE_REGEX));
    }
}
