package io.github.tjheslin1.westie.environmentproperties;

import io.github.tjheslin1.westie.FileViolation;
import io.github.tjheslin1.westie.LineAssertions;
import io.github.tjheslin1.westie.testinfrastructure.TestWestieFileReader;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Collections.emptyList;

public class EnvironmentPropertiesCheckerTest implements WithAssertions {

    private final EnvironmentPropertiesChecker environmentPropertiesChecker = new EnvironmentPropertiesChecker(new TestWestieFileReader(), emptyList());

    @Test
    public void checkAllEnvironmentSpecificPropertiesFilesHaveSameKeys() throws Exception {
        Path environmentExamples = Paths.get("src/test/resources/io/github/tjheslin1/examples/environments");
        List<FileViolation> violations = environmentPropertiesChecker.propertiesProvidedForAllEnvironments(environmentExamples);

        assertThat(violations).hasSize(3);

        LineAssertions lineMatchAssertions = new LineAssertions(violations);
        lineMatchAssertions.containsViolationMessage("Violation in file 'localhost.properties'\n" +
                "'localhost.properties' does not have matching property keys as 'ci.properties'\n");
        lineMatchAssertions.containsViolationMessage("Violation in file 'docker.properties'\n" +
                "'docker.properties' does not have matching property keys as 'ci.properties'\n");
        lineMatchAssertions.containsViolationMessage("Violation in file 'production.properties'\n" +
                "'production.properties' does not have matching property keys as 'ci.properties'\n");
    }
}