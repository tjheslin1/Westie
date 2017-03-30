package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.nio.file.Path;

public class WestieAnalyserForDirectory {

    private final WestieFileReader fileReader;
    private final Path pathToCheck;

    private String filetype;

    public WestieAnalyserForDirectory(WestieFileReader fileReader, Path pathToCheck) {
        this.fileReader = fileReader;
        this.pathToCheck = pathToCheck;
    }

    public WestieFileLineAnalyser forJavaFiles() {
        return forFileType(".java");
    }

    public WestieFileLineAnalyser forPropertiesFiles() {
        return forFileType(".properties");
    }

    public WestieFileLineAnalyser forFileType(String filetype) {
        if (!filetype.startsWith(".")) {
            this.filetype = "." + filetype;
        } else {
            this.filetype = filetype;
        }

        return new WestieFileLineAnalyser(pathToCheck, filetype, fileReader);
    }

    public WestieFileLineAnalyser forAllFiles() {
        return new WestieFileLineAnalyser(pathToCheck, filetype, fileReader);
    }
}
