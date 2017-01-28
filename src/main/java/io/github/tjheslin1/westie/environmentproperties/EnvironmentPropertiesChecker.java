package io.github.tjheslin1.westie.environmentproperties;

import io.github.tjheslin1.westie.FileLineViolation;
import io.github.tjheslin1.westie.FileViolation;
import io.github.tjheslin1.westie.WestieChecker;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static io.github.tjheslin1.westie.environmentproperties.FileKeySet.fileKeySet;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class EnvironmentPropertiesChecker extends WestieChecker {

    public EnvironmentPropertiesChecker(List<String> javaFilesToIgnore) {
        super(javaFilesToIgnore);
    }

    public List<FileViolation> propertiesProvidedForAllEnvironments(Path pathToCheck) throws IOException {
        List<FileKeySet> fileKeySets = Files.walk(pathToCheck)
                .filter(this::isAPropertiesFile)
                .filter(this::notAnExemptFile)
                .map(this::loadPropertiesKeys)
                .collect(toList());

        return compareAllKeySetsToFirst(fileKeySets);
    }

    private FileKeySet loadPropertiesKeys(Path file) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(file.toFile())) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            FileLineViolation violation = new FileLineViolation(file, format("Unable to read properties file '%s'.",
                    file.getFileName()), e.getMessage());
            return fileKeySet(file, Collections.singleton(violation));
        }

        return fileKeySet(file, properties.keySet());
    }

    private List<FileViolation> compareAllKeySetsToFirst(List<FileKeySet> fileKeySets) {
        List<FileViolation> violations = new ArrayList<>();
        FileKeySet firstKeySet = fileKeySets.get(0);
        for (FileKeySet fileKeySet : fileKeySets) {
            if (!fileKeySet.keySet.equals(firstKeySet.keySet)) {
                FileViolation violation = new FileViolation(fileKeySet.file,
                        format("'%s' does not have matching property keys as '%s'",
                                fileKeySet.file.getFileName(), firstKeySet.file.getFileName()));
                violation.reportViolation();
                violations.add(violation);
            }
        }
        return violations;
    }
}
