package io.github.tjheslin1.westie;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.util.List;

public class TODOsMustFollowStructureTest implements WithAssertions {

    private final TODOsMustFollowStructure todosMustFollowStructure = new TODOsMustFollowStructure();

    @Test
    public void findsTodosComments() throws Exception {
        List<Violation> todoViolations = todosMustFollowStructure.checkAllTodosFollowExpectedStructure();

        assertThat(todoViolations.size()).isEqualTo(2);
        assertThat(todoViolations.get(0).toString()).startsWith("Line '//TODO make this final");
        assertThat(todoViolations.get(1).toString()).startsWith("Line '//      TODO  NO_DATE   move this to another class");
    }
}