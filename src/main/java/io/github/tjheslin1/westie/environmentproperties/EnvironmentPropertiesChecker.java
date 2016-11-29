package io.github.tjheslin1.westie.environmentproperties;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WestieChecker;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class EnvironmentPropertiesChecker extends WestieChecker {

    public EnvironmentPropertiesChecker(List<String> javaFilesToIgnore) {
        super(javaFilesToIgnore);
    }

    public List<Violation> propertiesProvidedForAllEnvironments(Path pathToCheck) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isAPropertiesFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::allEnvsPresent)
                .peek(this::reportViolation)
                .collect(toList());
    }

    private Stream<Violation> allEnvsPresent(Path file) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(file.toFile())) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            return Stream.of(new Violation(file, ""));
        }

        return Stream.empty();
    }

    private void reportViolation(Violation violation) {
        // TODO
    }
}
