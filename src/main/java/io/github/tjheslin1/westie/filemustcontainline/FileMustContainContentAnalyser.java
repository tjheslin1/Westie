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
package io.github.tjheslin1.westie.filemustcontainline;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WestieAnalyser;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.lang.String.format;

/**
 * Checks that the specified file contains the provided content.
 * The contents of the file and the important content are compared as Strings.
 */
public class FileMustContainContentAnalyser {

    private final WestieAnalyser westieAnalyser;

    public FileMustContainContentAnalyser() {
        this.westieAnalyser = new WestieAnalyser();
    }

    public FileMustContainContentAnalyser(WestieFileReader fileReader) {
        this.westieAnalyser = new WestieAnalyser(fileReader);
    }

    /**
     * @param fileToCheck The Path to the file to analyse.
     * @param importantContent The content expected to appear in the file.
     * @return A list of violations in which the file did not meet the requirements of the analyses.
     * @throws IOException if an I/O error occurs when opening the file.
     */
    public List<Violation> checkFileMustContainImportantContent(Path fileToCheck, String importantContent) throws IOException {
        return westieAnalyser.analyseFile(fileToCheck)
                .analyseFileContent(fileContent -> containsImportantContent(fileContent, importantContent),
                        format("Expected file '%s' to contain content\n%s", fileToCheck.getFileName(), importantContent));
    }

    private boolean containsImportantContent(String fileContent, String importantContent) {
        return !fileContent.contains(importantContent);
    }

}
