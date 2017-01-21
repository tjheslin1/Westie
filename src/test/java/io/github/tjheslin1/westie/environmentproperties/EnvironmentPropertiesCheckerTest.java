package io.github.tjheslin1.westie.environmentproperties;

import io.github.tjheslin1.westie.Violation;
import org.assertj.core.api.WithAssertions;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class EnvironmentPropertiesCheckerTest implements WithAssertions {

    private final EnvironmentPropertiesChecker environmentPropertiesChecker = new EnvironmentPropertiesChecker(emptyList());

    @Test
    public void checkAllEnvironmentSpecificPropertiesFilesHaveSameKeys() throws Exception {
        Path environmentExamples = Paths.get("src/test/resources/io/github/tjheslin1/examples/environments");
        List<Violation> violations = environmentPropertiesChecker.propertiesProvidedForAllEnvironments(environmentExamples);

        assertThat(violations).hasSize(3);
        assertThat(violations.get(0).toString()).matches("'Properties file 'localhost.properties' does not have matching property keys as 'ci.properties'' in file '.*/localhost.properties'");
        assertThat(violations.get(1).toString()).matches("'Properties file 'production.properties' does not have matching property keys as 'ci.properties'' in file '.*/production.properties'");
        assertThat(violations.get(2).toString()).matches("'Properties file 'docker.properties' does not have matching property keys as 'ci.properties'' in file '.*/docker.properties'");
    }
}