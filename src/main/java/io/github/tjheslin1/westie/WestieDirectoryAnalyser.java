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
package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Runs your provided analysis, in the form of a {@link Predicate} which should return 'true' if analysis fails,
 * on files under a directory specified in {@link WestieAnalyser}.
 */
public class WestieDirectoryAnalyser {

    private final Path pathToCheck;
    private final String filetype;
    private final WestieFileReader fileReader;

    private List<String> filesToIgnore = emptyList();

    public WestieDirectoryAnalyser(Path pathToCheck, String filetype, WestieFileReader fileReader) {
        this.pathToCheck = pathToCheck;
        this.fileReader = fileReader;
        this.filetype = filetype;
    }

    /**
     * Sets the files to ignore from analysis.
     *
     * @param fileToIgnore The files, by name, exempty from analysis.
     * @return this {@link WestieDirectoryAnalyser} back with 'fileToIgnore' set to the provided list.
     */
    public WestieDirectoryAnalyser ignoring(List<String> fileToIgnore) {
        this.filesToIgnore = fileToIgnore;
        return this;
    }

    /**
     * Analyses the files under the directory provided in {@link WestieAnalyser},
     * applying the provided {@link Predicate}. The Predicate takes in the Path to the file.
     *
     * @param analyseFile      The function to apply to the file as a whole.
     *                         The {@link Predicate}, 'analyseFile', takes the {@link Path} to one of the files under the directory to analyse.
     *                         The {@link Predicate} should return true if the file fails the analysis check.
     * @param violationMessage The message to print if a file under the provided directory fails analysis.
     * @return The a list of {@link Violation} for the files which have failed analysis.
     * @throws IOException if an I/O error is thrown when accessing the file.
     */
    public List<Violation> analyseFile(Predicate<Path> analyseFile, String violationMessage) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isFileToAnalyse)
                .flatMap(file -> analyseFile(file, analyseFile, violationMessage))
                .collect(toList());
    }

    /**
     * Analyses the files under the directory provided in {@link WestieAnalyser},
     * applying the provided {@link Predicate}. The Predicate takes in the file content as a String.
     *
     * @param analyseFile      The function to apply to the file as a whole.
     *                         The {@link Predicate}, 'analyseFile', takes the content of one of the file's under the directory, as a String.
     *                         The {@link Predicate} should return true if the file fails the analysis check.
     * @param violationMessage The message to print if a file under the provided directory fails analysis.
     * @return The a list of {@link Violation} for the files which have failed analysis.
     * @throws IOException if an I/O error is thrown when accessing the file.
     */
    public List<Violation> analyseFileContent(Predicate<String> analyseFile, String violationMessage) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isFileToAnalyse)
                .flatMap(file -> analyseFileContent(file, analyseFile, violationMessage))
                .collect(toList());
    }

    /**
     * Analyses the files under the directory provided in {@link WestieAnalyser},
     * applying the provided {@link Predicate}. The Predicate takes in a each line of the file as a String.
     *
     * @param analyseLineInFile The function to apply to each line in each file to be analysed.
     *                          The {@link Predicate}, 'analyseFile', takes a line of one of the file's under the directory, as a String.
     *                          The {@link Predicate} should return true if the file's line fails the analysis check.
     * @param violationMessage  The message to print if a file's line fails analysis.
     * @return The a list of {@link Violation} for the files which have failed analysis.
     * @throws IOException if an I/O error is thrown when accessing the file.
     */
    public List<Violation> analyseLinesOfFile(Predicate<String> analyseLineInFile, String violationMessage) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isFileToAnalyse)
                .flatMap(file -> analyseFileLines(file, analyseLineInFile, violationMessage))
                .collect(toList());
    }

    private Stream<Violation> analyseFile(Path file, Predicate<Path> analyseFile, String violationMessage) {
        try {
            if (analyseFile.test(file)) {
                return Stream.of(new FileViolation(file, violationMessage));
            } else {
                return Stream.empty();
            }
        } catch (Exception e) {
            return Stream.of(new FileViolation(file, "Error occurred analysing file.\n" + e.getMessage()));
        }
    }

    private Stream<Violation> analyseFileContent(Path file, Predicate<String> analyseFile, String violationMessage) {
        try {
            List<String> lines = fileReader.readAllLines(file);
            if (lines.isEmpty()) {
                return Stream.of(new FileViolation(file, "Empty file! - Should this file be in the list of ignored files? Or in a different directory?"));
            }
            String fileContent = lines.stream().collect(Collectors.joining(System.lineSeparator()));
            if (analyseFile.test(fileContent)) {
                return Stream.of(new FileViolation(file, violationMessage));
            } else {
                return Stream.empty();
            }
        } catch (Exception e) {
            return Stream.of(new FileViolation(file, "Unable to read file.\n" + e.getMessage()));
        }
    }

    private Stream<Violation> analyseFileLines(Path file, Predicate<String> analyseLine, String violationMessage) {
        try {
            List<String> lines = fileReader.readAllLines(file);
            if (lines.isEmpty()) {
                return Stream.of(new FileViolation(file,
                        "Empty file! - Should this file be in the list of ignored files? Or in a different directory?"));
            }
            return lines.stream()
                    .filter(analyseLine)
                    .map(line -> new FileLineViolation(file, line, violationMessage));
        } catch (Exception e) {
            return Stream.of(new FileViolation(file, "Unable to read file.\n" + e.getMessage()));
        }
    }

    private boolean isFileToAnalyse(Path file) {
        return Files.isRegularFile(file) && !isHidden(file) && !filesParentIsHidden(file) && notAnExemptFile(file) && fileIsOfSpecifiedType(file);
    }

    private boolean isHidden(Path file) {
        try {
            return Files.isHidden(file);
        } catch (IOException e) {
            return false;
        }
    }

    private boolean filesParentIsHidden(Path file) {
        if (!isHidden(pathToCheck)) {
            Path dirToCheck = file.getParent();
            while (!dirToCheck.equals(pathToCheck)) {
                if (isHidden(dirToCheck)) {
                    return true;
                }
                dirToCheck = dirToCheck.getParent();
            }
        }
        return false;
    }

    private boolean fileIsOfSpecifiedType(Path file) {
        if (filetype == null) {
            return true;
        }

        return file.toString().endsWith(filetype);
    }

    private boolean notAnExemptFile(Path file) {
        return filesToIgnore.stream()
                .map(this::postfixedWithTypeExtension)
                .noneMatch(exemptFile -> file.toString().endsWith(exemptFile));
    }

    private String postfixedWithTypeExtension(String file) {
        if (filetype == null) {
            return file;
        }

        return file.endsWith(filetype) ? file : file + filetype;
    }
}
