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

public class WestieFileLineAnalyser {

    private final Path pathToCheck;
    private final String filetype;
    private final WestieFileReader fileReader;

    private List<String> filesToIgnore = emptyList();

    public WestieFileLineAnalyser(Path pathToCheck, String filetype, WestieFileReader fileReader) {
        this.pathToCheck = pathToCheck;
        this.fileReader = fileReader;
        this.filetype = filetype;
    }

    public WestieFileLineAnalyser ignoring(List<String> fileToIgnore) {
        this.filesToIgnore = fileToIgnore;
        return this;
    }

    public List<Violation> analyseFile(Predicate<Path> analyseFile, String violationMessage) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isFileToAnalyse)
                .flatMap(file -> analyseFile(file, analyseFile, violationMessage))
                .collect(toList());
    }

    public List<Violation> analyseFileContent(Predicate<String> analyseFile, String violationMessage) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isFileToAnalyse)
                .flatMap(file -> analyseFileContent(file, analyseFile, violationMessage))
                .collect(toList());
    }

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
        } catch (IOException e) {
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
        } catch (IOException e) {
            return Stream.of(new FileViolation(file, "Unable to read file.\n" + e.getMessage()));
        }
    }

    private boolean isFileToAnalyse(Path file) {
        return notAnExemptFile(file) && fileIsOfSpecifiedType(file);
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
