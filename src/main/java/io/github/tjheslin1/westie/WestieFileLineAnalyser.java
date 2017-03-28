package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class WestieFileLineAnalyser {

    private final WestieFileReader fileReader;
    private final List<String> filesToIgnore;
    private final Path pathToCheck;

    private String filetype;

    public WestieFileLineAnalyser(WestieFileReader fileReader, List<String> filesToIgnore, Path pathToCheck) {
        this.fileReader = fileReader;
        this.filesToIgnore = filesToIgnore;
        this.pathToCheck = pathToCheck;
    }

    public WestieFileLineAnalyser forFileType(String filetype) {
        if (!filetype.startsWith(".")) {
            this.filetype = "." + filetype;
        } else {
            this.filetype = filetype;
        }

        return this;
    }

    public WestieFileLineAnalyser forJavaFiles() {
        return forFileType(".java");
    }

    public WestieFileLineAnalyser forPropertiesFiles() {
        return forFileType(".properties");
    }

    public WestieFileLineAnalyser forAllFiles() {
        return this;
    }

    public List<Violation> analyse(Predicate<String> analyseLine) throws IOException {
        return Files.walk(pathToCheck)
                .filter(this::isFileToAnalyse)
                .flatMap(this::dostuff)
                .collect(toList());
    }

    private Stream<Violation> dostuff(Path file, Predicate<String> analyseLine) {
        try {
            List<String> lines = fileReader.readAllLines(file);
            if (lines.isEmpty()) {
                return Stream.of(new FileViolation(file,
                        "Empty file! - Should this file be in the list of ignored files? Or in a different directory?"));
            }
            return lines.stream()
                    .filter(analyseLine)
                    .map(importline -> new FileLineViolation(file, importline, "Violation was caused by an import which " +
                            "does not matching any of the import restrictions."));
        } catch (IOException e) {
            return Stream.of(new FileViolation(file, "Unable to read file.\n" + e.getMessage()));
        }
    }

    private boolean analyseLine(String line) {
    }

    private boolean isFileToAnalyse(Path file) {
        return notAnExemptFile(file) && fileIsOfSpecifiedType(file);
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

    private boolean fileIsOfSpecifiedType(Path file) {
        if (filetype == null) {
            return true;
        }

        return file.toString().endsWith(filetype);
    }
}
