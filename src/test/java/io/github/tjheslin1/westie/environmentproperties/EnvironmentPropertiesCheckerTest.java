package io.github.tjheslin1.westie.environmentproperties;

import io.github.tjheslin1.westie.Violation;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.emptyList;

public class EnvironmentPropertiesCheckerTest implements WithAssertions {

    private final EnvironmentPropertiesChecker environmentPropertiesChecker = new EnvironmentPropertiesChecker(emptyList());

    @Test
    public void checkAllEnvironmentSpecificPropertiesFilesHaveSameKeys() throws Exception {
        List<Violation> violations = environmentPropertiesChecker.propertiesProvidedForAllEnvironments(Paths.get("src/test/resources/io/github/tjheslin1/environments"));

        assertThat(violations).hasSize(1);
    }
}