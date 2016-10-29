package io.github.tjheslin1.westie;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class TODOsMustFollowStructureTest implements WithAssertions {

    @Test
    public void findsTodosComments() throws Exception {
        TODOsMustFollowStructure todosMustFollowStructure = new TODOsMustFollowStructure(emptyList());

        Path testFilePath = Paths.get(TODOsMustFollowStructure.class.getClassLoader().getResource("io/github/tjheslin1/examples/ClassWithTodos.java").toURI());
        List<Violation> todoViolations = todosMustFollowStructure.checkAllTodosFollowExpectedStructure(testFilePath.getParent());

        assertThat(todoViolations.size()).isEqualTo(3);
        assertThat(todoViolations.get(0).toString()).startsWith("Line '//      TODO  NO_DATE   another todo");
        assertThat(todoViolations.get(1).toString()).startsWith("Line '//TODO make this final");
        assertThat(todoViolations.get(2).toString()).startsWith("Line '//      TODO  NO_DATE   move this to another class");
    }

    @Test
    public void ignoredExemptFile() throws Exception {
        TODOsMustFollowStructure todosMustFollowStructure = new TODOsMustFollowStructure(singletonList("io/github/tjheslin1/examples/AnotherClassWithTodos.java"));

        Path testFilePath = Paths.get(TODOsMustFollowStructure.class.getClassLoader().getResource("io/github/tjheslin1/examples/ClassWithTodos.java").toURI());
        List<Violation> todoViolations = todosMustFollowStructure.checkAllTodosFollowExpectedStructure(testFilePath.getParent());

        assertThat(todoViolations.size()).isEqualTo(2);
        assertThat(todoViolations.get(0).toString()).startsWith("Line '//TODO make this final");
        assertThat(todoViolations.get(1).toString()).startsWith("Line '//      TODO  NO_DATE   move this to another class");
    }
}