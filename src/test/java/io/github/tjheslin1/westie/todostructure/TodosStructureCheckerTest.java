package io.github.tjheslin1.westie.todostructure;

import io.github.tjheslin1.westie.FileLineViolation;
import io.github.tjheslin1.westie.LineAssertions;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static io.github.tjheslin1.westie.WestieRegexes.TODOS_MUST_HAVE_DATE_REGEX;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class TodosStructureCheckerTest implements WithAssertions {

    @Test
    public void findsTodosComments() throws Exception {
        TodosStructureChecker todosStructureChecker = new TodosStructureChecker(TODOS_MUST_HAVE_DATE_REGEX, emptyList());

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/todos");
        List<FileLineViolation> todoViolations = todosStructureChecker.checkAllTodosFollowExpectedStructure(pathToCheck);

        assertThat(todoViolations.size()).isEqualTo(3);
        LineAssertions lineAssertions = new LineAssertions(todoViolations);
        lineAssertions.containsViolationMessage("Violation in file 'AnotherClassWithTodos.java'\n" +
                "\n" +
                "        //      TODO  NO_DATE   another todo\n" +
                "\n" +
                "Violation was caused by the TODO not matching structure with regex: .*//[ ]*TODO.*[0-9]{1,4}[/-]{1}[A-z0-9]{2,3}[/-]{1}[0-9]{1,4}.*\n");
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithTodos.java'\n" +
                "\n" +
                "    //TODO make this final\n" +
                "\n" +
                "Violation was caused by the TODO not matching structure with regex: .*//[ ]*TODO.*[0-9]{1,4}[/-]{1}[A-z0-9]{2,3}[/-]{1}[0-9]{1,4}.*\n");
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithTodos.java'\n" +
                "\n" +
                "        //      TODO  NO_DATE   move this to another class\n" +
                "\n" +
                "Violation was caused by the TODO not matching structure with regex: .*//[ ]*TODO.*[0-9]{1,4}[/-]{1}[A-z0-9]{2,3}[/-]{1}[0-9]{1,4}.*\n");
    }

    @Test
    public void ignoresExemptFile() throws Exception {
        TodosStructureChecker todosStructureChecker = new TodosStructureChecker(TODOS_MUST_HAVE_DATE_REGEX, singletonList("io/github/tjheslin1/examples/todos/another/AnotherClassWithTodos.java"));

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/todos");
        List<FileLineViolation> todoViolations = todosStructureChecker.checkAllTodosFollowExpectedStructure(pathToCheck);

        assertThat(todoViolations.size()).isEqualTo(2);
        LineAssertions lineAssertions = new LineAssertions(todoViolations);
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithTodos.java'\n" +
                "\n" +
                "    //TODO make this final\n" +
                "\n" +
                "Violation was caused by the TODO not matching structure with regex: .*//[ ]*TODO.*[0-9]{1,4}[/-]{1}[A-z0-9]{2,3}[/-]{1}[0-9]{1,4}.*\n");
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithTodos.java'\n" +
                "\n" +
                "        //      TODO  NO_DATE   move this to another class\n" +
                "\n" +
                "Violation was caused by the TODO not matching structure with regex: .*//[ ]*TODO.*[0-9]{1,4}[/-]{1}[A-z0-9]{2,3}[/-]{1}[0-9]{1,4}.*\n");
    }
}