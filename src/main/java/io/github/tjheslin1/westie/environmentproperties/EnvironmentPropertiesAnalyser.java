/*
 * Copyright 2017 Thomas Heslin <tjheslin1@gmail.com>.
 *
 * This file is part of Westie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.tjheslin1.westie.environmentproperties;

import io.github.tjheslin1.westie.FileLineViolation;
import io.github.tjheslin1.westie.FileViolation;
import io.github.tjheslin1.westie.WestieAnalyser;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

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
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Checks that all `.properties` files under a provided directory share
 * the same property names (keys).
 * <p>
 * Useful for enforcing that properties aren't missed between environment properties.
 */
public class EnvironmentPropertiesAnalyser {

    /**
     * Checks that `.properties` file share the same set of properties (keys) by
     * comparing them to the first file found.
     *
     * @param pathToCheck The directory to recursively search under for `.properties` files to check.
     * @return A list of {@link FileViolation}'s for files which don't share the same properties.
     * @throws IOException if a problem reading the properties files occurs.
     */
    public List<FileViolation> propertiesProvidedForAllEnvironments(Path pathToCheck) throws IOException {
        return propertiesProvidedForAllEnvironments(pathToCheck, emptyList());
    }

    /**
     * Checks that `.properties` file share the same set of properties (keys) by
     * comparing them to the first file found.
     *
     * @param pathToCheck             The directory to recursively search under for `.properties` files to check.
     * @param propertiesFilesToIgnore properties files exempt from analysis.
     * @return A list of {@link FileViolation}'s for files which don't share the same properties.
     * @throws IOException if a problem reading the properties files occurs.
     */
    public List<FileViolation> propertiesProvidedForAllEnvironments(Path pathToCheck, List<String> propertiesFilesToIgnore) throws IOException {
        List<FileKeySet> fileKeySets = Files.walk(pathToCheck)
                .filter(this::isAPropertiesFile)
                .filter(file -> notAnExemptFile(file, propertiesFilesToIgnore))
                .map(this::loadPropertiesKeys)
                .collect(toList());

        return compareAllKeySetsToFirst(fileKeySets);
    }

    private boolean isAPropertiesFile(Path file) {
        return file.toString().endsWith(".properties");
    }

    private boolean notAnExemptFile(Path file, List<String> filesToIgnore) {
        return filesToIgnore.stream()
                .map(this::postFixedWithPropertiesExtension)
                .noneMatch(exemptFile -> file.toString().endsWith(exemptFile));
    }

    private String postFixedWithPropertiesExtension(String propertiesFile) {
        return propertiesFile.endsWith(".properties") ? propertiesFile : propertiesFile + ".properties";
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