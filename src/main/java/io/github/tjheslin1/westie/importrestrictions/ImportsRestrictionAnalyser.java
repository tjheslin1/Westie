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
package io.github.tjheslin1.westie.importrestrictions;

import io.github.tjheslin1.westie.FileViolation;
import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WestieAnalyser;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

/**
 * Enforces that the provided {@link ImportRestriction}'s are adhered to.
 */
public class ImportsRestrictionAnalyser {

    private final List<ImportRestriction> importRestrictions;
    private final WestieAnalyser westieAnalyser;

    private String packageLine;

    public ImportsRestrictionAnalyser(List<ImportRestriction> importRestrictions) {
        this.importRestrictions = importRestrictions;
        this.westieAnalyser = new WestieAnalyser();
    }

    public ImportsRestrictionAnalyser(List<ImportRestriction> importRestrictions, WestieFileReader fileReader) {
        this.importRestrictions = importRestrictions;
        this.westieAnalyser = new WestieAnalyser(fileReader);
    }

    /**
     * Checks the imports of all source files under the provided path to check.
     *
     * @param pathToCheck The path of the package to be checked, all files within
     *                    this package will be checked with the exception of the 'javaFilesToIgnore'.
     * @return A list of {@link FileViolation}'s where imports have been used outside of their intended package.
     * @throws IOException if an I/O error occurs when opening the directory.
     */
    public List<Violation> checkImportsAreOnlyUsedInAcceptedPackages(Path pathToCheck) throws IOException {
        return checkImportsAreOnlyUsedInAcceptedPackages(pathToCheck, emptyList());
    }

    /**
     * Checks the imports of all source files under the provided path to check.
     *
     * @param pathToCheck   The path of the package to be checked, all files within
     *                      this package will be checked with the exception of the 'javaFilesToIgnore'.
     * @param filesToIgnore files exempt from analysis.
     * @return A list of {@link FileViolation}'s where imports have been used outside of their intended package.
     * @throws IOException if an I/O error occurs when opening the directory.
     */
    public List<Violation> checkImportsAreOnlyUsedInAcceptedPackages(Path pathToCheck, List<String> filesToIgnore) throws IOException {
        return westieAnalyser.analyseDirectory(pathToCheck)
                .forJavaFiles().ignoring(filesToIgnore)
                .analyseLinesOfFile(this::verifyImports, "Violation was caused by the above import which " +
                        "was used outside of its accepted package.");
    }

    private boolean verifyImports(String line) {
        if (packageLine == null && line.startsWith("package")) {
            packageLine = line;
        }

        return isImportLine(line) && importUsedOutsideOfAcceptedPackage(packageLine, line);
    }

    private boolean isImportLine(String line) {
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
