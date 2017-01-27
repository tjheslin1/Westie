package io.github.tjheslin1.westie.environmentproperties;

import io.github.tjheslin1.westie.LineAssertions;
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

        LineAssertions lineMatchAssertions = new LineAssertions(violations);
        lineMatchAssertions.violationsContainLineMatching("'Properties file 'localhost.properties' does not have matching property keys as 'ci.properties'' in file '.*/localhost.properties'");
        lineMatchAssertions.violationsContainLineMatching("'Properties file 'production.properties' does not have matching property keys as 'ci.properties'' in file '.*/production.properties'");
        lineMatchAssertions.violationsContainLineMatching("'Properties file 'docker.properties' does not have matching property keys as 'ci.properties'' in file '.*/docker.properties'");
    }
}