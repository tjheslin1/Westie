package io.github.tjheslin1.westie.todostructure;

import io.github.tjheslin1.westie.Violation;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class TodosStructureCheckerTest implements WithAssertions {

    private static final String TODOS_MUST_HAVE_DATE_REGEX = ".*//[ ]*TODO.*[0-9]{1,4}[/-]{1}[A-z0-9]{2,3}[/-]{1}[0-9]{1,4}.*";

    @Test
    public void findsTodosComments() throws Exception {
        TodosStructureChecker todosStructureChecker = new TodosStructureChecker(TODOS_MUST_HAVE_DATE_REGEX, emptyList());

        List<Violation> todoViolations = todosStructureChecker.checkAllTodosFollowExpectedStructure(Paths.get("src/test/resources/io/github/tjheslin1/examples/todos"));

        assertThat(todoViolations.size()).isEqualTo(3);
        assertThat(todoViolations.get(0).toString()).startsWith("Line '//      TODO  NO_DATE   another todo");
        assertThat(todoViolations.get(1).toString()).startsWith("Line '//TODO make this final");
        assertThat(todoViolations.get(2).toString()).startsWith("Line '//      TODO  NO_DATE   move this to another class");
    }

    @Test
    public void ignoresExemptFile() throws Exception {
        TodosStructureChecker todosStructureChecker = new TodosStructureChecker(TODOS_MUST_HAVE_DATE_REGEX, singletonList("io/github/tjheslin1/examples/todos/another/AnotherClassWithTodos.java"));

        List<Violation> todoViolations = todosStructureChecker.checkAllTodosFollowExpectedStructure(Paths.get("src/test/resources/io/github/tjheslin1/examples/todos"));

        assertThat(todoViolations.size()).isEqualTo(2);
        assertThat(todoViolations.get(0).toString()).startsWith("Line '//TODO make this final");
        assertThat(todoViolations.get(1).toString()).startsWith("Line '//      TODO  NO_DATE   move this to another class");
    }
}