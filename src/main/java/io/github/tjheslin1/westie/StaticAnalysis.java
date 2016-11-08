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
package io.github.tjheslin1.westie;

import java.nio.file.Path;
import java.util.List;

/**
 * Base class of a static analysis test with useful methods to reuse.
 */
public abstract class StaticAnalysis {

    private final List<String> javaFilesToIgnore;

    public StaticAnalysis(List<String> javaFilesToIgnore) {
        this.javaFilesToIgnore = javaFilesToIgnore;
    }

    /**
     * @param file Path to a source file.
     * @return 'true' if the file's extension is '.java'
     */
    protected boolean isAJavaFile(Path file) {
        return file.toString().endsWith(".java");
    }

    /**
     * @param file Path to a source file.
     * @return 'true' if the file provided does not appear in the provided list of 'javaFilesToIgnore'
     */
    protected boolean notAnExemptFile(Path file) {
        return !javaFilesToIgnore.stream()
                .map(this::postFixedWithJavaExtension)
                .anyMatch(exemptFile -> file.toString().endsWith((exemptFile)));
    }

    /**
     * Extracts the filename of a file from its full path.
     *
     * @param file Path to a source file.
     * @return The filename and extension of a file, provided its path.
     */
    protected String filenameFromPath(Path file) {
        return file.subpath(file.getNameCount() - 1, file.getNameCount()).toString();
    }

    private String postFixedWithJavaExtension(String javaFile) {
        return javaFile.endsWith(".java") ? javaFile : javaFile + ".java";
    }
}
