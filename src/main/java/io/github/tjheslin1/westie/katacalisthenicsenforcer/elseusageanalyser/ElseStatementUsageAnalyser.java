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
package io.github.tjheslin1.westie.katacalisthenicsenforcer.elseusageanalyser;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WestieAnalyser;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Not yet in use.
 */
public class ElseStatementUsageAnalyser {

    private final WestieAnalyser westieAnalyser;

    public ElseStatementUsageAnalyser() {
        this.westieAnalyser = new WestieAnalyser();
    }

    public ElseStatementUsageAnalyser(WestieFileReader fileReader) {
        this.westieAnalyser = new WestieAnalyser(fileReader);
    }

    /**
     * Not yet in use.
     */
    public List<Violation> noUsageOfElseStatement(Path pathToCheck) throws IOException {
        return noUsageOfElseStatement(pathToCheck, emptyList());
    }

    /**
     * Not yet in use.
     */
    public List<Violation> noUsageOfElseStatement(Path pathToCheck, List<String> filesToIgnore) throws IOException {
        return westieAnalyser.analyseDirectory(pathToCheck)
                .forJavaFiles().ignoring(filesToIgnore)
                .analyseLinesOfFile(this::checkElseStatementUsage, "'else' statement used.");
    }

    private boolean checkElseStatementUsage(String line) {
        return elseStatementUsage(line);
    }

    private boolean elseStatementUsage(String line) {
        if (line.contains(" else ")) {
            int elseIndex = line.indexOf(" else ");
            int firstSlashSlash = line.indexOf("//");
            int firstSlashStart = line.indexOf("/*");

            if (firstSlashSlash > -1) {
                if (firstSlashStart > -1) {
                    if (firstSlashStart < firstSlashSlash) {
                        return elseIndex < firstSlashStart;
                    } else {
                        return elseIndex < firstSlashSlash;
                    }
                }
                return elseIndex < firstSlashSlash;
            } else if (firstSlashStart > -1) {
                return elseIndex < firstSlashSlash;
            }

            return true;
        }
        return false;
    }
}
