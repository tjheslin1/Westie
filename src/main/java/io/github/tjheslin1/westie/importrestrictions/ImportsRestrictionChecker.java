/*
 * Copyright 2016 Thomas Heslin <tjheslin1@gmail.com>.
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
package io.github.tjheslin1.westie.importrestrictions;

import io.github.tjheslin1.westie.FileLineViolation;
import io.github.tjheslin1.westie.FileViolation;
import io.github.tjheslin1.westie.WestieChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * Enforces that the provided {@link ImportRestriction}'s are adhered to.
 * <p>
 * Deprecated as of 1.1.3 in favour of <a href="https://github.com/theangrydev/domain-enforcer">domain-enforcer by @theangrydev</a>.
 */
@Deprecated
public class ImportsRestrictionChecker extends WestieChecker {

    private final List<ImportRestriction> importRestrictions;

    public ImportsRestrictionChecker(List<ImportRestriction> importRestrictions, List<String> javaFilesToIgnore) {
        super(javaFilesToIgnore);
        this.importRestrictions = importRestrictions;
    }

    /**
     * Checks the imports of all source files under the provided path to check.
     *
     * @param pathToCheck The path of the package to be checked, all files within
     *                    this package will be checked with the exception of the 'javaFilesToIgnore'.
     * @return A list of {@link FileViolation}'s where imports have been used outside of their intended package.
     * @throws IOException if an I/O error occurs when opening the directory.
     */
    @Deprecated
    public List<FileLineViolation> checkImportsAreOnlyUsedInAcceptedPackages(Path pathToCheck) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isAJavaFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::verifyImports)
                .peek(FileLineViolation::reportViolation)
                .collect(toList());
    }

    private Stream<FileLineViolation> verifyImports(Path file) {
        try {
            List<String> lines = Files.lines(file).collect(toList());
            String packageLine = lines.stream().findFirst().get();
            return lines.stream()
                    .filter(this::importLines)
                    .filter(importLine -> importUsedOutsideOfAcceptedPackage(packageLine, importLine))
                    .map(importLine -> new FileLineViolation(file, importLine, "Violation was caused by an import which " +
                            "does not matching any of the import restrictions."));
        } catch (IOException e) {
            return Stream.of(new FileLineViolation(file, "Unable to read file.", e.getMessage()));
        }
    }

    private boolean importLines(String line) {
        return line.startsWith("import ");
    }

    private boolean importUsedOutsideOfAcceptedPackage(String packageLine, String importLine) {
        for (ImportRestriction importRestriction : importRestrictions) {
            if (importLine.matches(importRestriction.importRegex)) {
                return !packageLine.startsWith(format("package %s", importRestriction.packagePath));
            }
        }
        return false;
    }
}
