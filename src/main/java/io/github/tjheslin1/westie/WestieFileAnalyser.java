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

import static java.util.stream.Collectors.toList;

/**
 * Runs your provided analysis, in the form of a {@link Predicate} which should return 'true' if analysis fails,
 * on the file specified in {@link WestieAnalyser}.
 */
public class WestieFileAnalyser {

    private final Path fileToCheck;
    private final WestieFileReader fileReader;

    public WestieFileAnalyser(Path fileToCheck, WestieFileReader fileReader) {
        this.fileToCheck = fileToCheck;
        this.fileReader = fileReader;
    }

    /**
     * Analyses the file provided in {@link WestieAnalyser}, applying the provided {@link Predicate}.
     * The Predicate takes in the Path to the file.
     *
     * @param analyseFile      The function to apply to the file as a whole.
     *                         The {@link Predicate}, 'analyseFile', takes the {@link Path} to the file to analyse.
     *                         The {@link Predicate} should return true if the file fails the analysis check.
     * @param violationMessage The message to print if the file fails analysis.
     * @return The a list of {@link Violation} for the file which has failed analysis.
     * @throws IOException if an I/O error is thrown when accessing the file.
     */
    public List<Violation> analyse(Predicate<Path> analyseFile, String violationMessage) throws IOException {
        return analyseFile(fileToCheck, analyseFile, violationMessage)
                .collect(toList());
    }

    /**
     * Analyses the file provided in {@link WestieAnalyser}, applying the provided {@link Predicate}.
     * The Predicate takes in the content of the file as a String.
     *
     * @param analyseFile      The function to apply to the file as a whole.
     *                         The {@link Predicate}, 'analyseFile', takes the content of the file as a String.
     *                         The {@link Predicate} should return true if the file fails the analysis check.
     * @param violationMessage The message to print if the file fails analysis.
     * @return The a list of {@link Violation} for the fileswhich has failed analysis.
     * @throws IOException if an I/O error is thrown when accessing the file.
     */
    public List<Violation> analyseFileContent(Predicate<String> analyseFile, String violationMessage) throws IOException {
        return analyseFileContent(fileToCheck, analyseFile, violationMessage)
                .collect(toList());
    }

    /**
     * Analyses the file provided in {@link WestieAnalyser}, applying the provided {@link Predicate}.
     * The Predicate takes in each line of the file as a String.
     *
     * @param analyseLineInFile The function to apply to each line in the file to be analysed.
     *                          The {@link Predicate}, 'analyseFile', takes each line of the file as a String.
     *                          The {@link Predicate} should return true if the file fails the analysis check.
     * @param violationMessage  The message to print if the file's line fails analysis.
     * @return The a list of {@link Violation} for the file which has failed analysis.
     * @throws IOException if an I/O error is thrown when accessing the file.
     */
    public List<Violation> analyseLinesOfFile(Predicate<String> analyseLineInFile, String violationMessage) throws IOException {
        return analyseFileLines(fileToCheck, analyseLineInFile, violationMessage)
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
                return Stream.of(new FileViolation(file, "Empty file!"));
            }
            String fileContent = lines.stream().collect(Collectors.joining(System.lineSeparator()));
            if (analyseFile.test(fileContent)) {
                return Stream.of(new FileViolation(file, violationMessage));
            } else {
                return Stream.empty();
            }
        } catch (IOException e) {
            return Stream.of(new FileViolation(file, "Unable to read file.\n" + e.getMessage()));
        }
    }

    private Stream<Violation> analyseFileLines(Path file, Predicate<String> analyseLine, String violationMessage) {
        try {
            List<String> lines = fileReader.readAllLines(file);
            if (lines.isEmpty()) {
                return Stream.of(new FileViolation(file,
                        "Empty file!"));
            }
            return lines.stream()
                    .filter(analyseLine)
                    .map(line -> new FileLineViolation(file, line, violationMessage));
        } catch (IOException e) {
            return Stream.of(new FileViolation(file, "Unable to read file.\n" + e.getMessage()));
        }
    }
}
