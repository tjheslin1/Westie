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

import static java.lang.String.format;

/**
 * Represents a violation of a static analysis test.
 */
public class FileViolation implements Violation {

    private final Path file;
    private final String message;

    public FileViolation(Path file, String message) {
        this.file = file;
        this.message = message;
    }

    @Override
    public void reportViolation() {
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return format("Violation in file '%s'%n%s%n", file.getFileName(), message);
    }
}
