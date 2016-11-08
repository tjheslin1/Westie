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

public abstract class StaticAnalysis {

    private final List<String> javaFilesToIgnore;

    public StaticAnalysis(List<String> javaFilesToIgnore) {
        this.javaFilesToIgnore = javaFilesToIgnore;
    }

    protected boolean isAJavaFile(Path file) {
        return file.toString().endsWith(".java");
    }

    protected boolean notAnExemptFile(Path file) {
        return !javaFilesToIgnore.stream()
                .map(this::postFixedWithJavaExtension)
                .anyMatch(exemptFile -> file.toString().endsWith((exemptFile)));
    }

    protected String filenameFromPath(Path path) {
        return path.subpath(path.getNameCount() - 1, path.getNameCount()).toString();
    }

    private String postFixedWithJavaExtension(String javaFile) {
        return javaFile.endsWith(".java") ? javaFile : javaFile + ".java";
    }
}
