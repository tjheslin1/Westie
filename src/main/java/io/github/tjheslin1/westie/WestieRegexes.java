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

/**
 * Reusable regexes for writing project-specific static analysis analysers.
 */
public class WestieRegexes {

    public static final String TODO_REGEX = ".*//.*(T|t)(O|o)(D|d)(O|o).*";
    public static final String TODOS_MUST_HAVE_DATE_REGEX = ".*//.*(T|t)(O|o)(D|d)(O|o).*[0-9]{1,4}[/-]{1}[A-z0-9]{2,3}[/-]{1}[0-9]{1,4}.*";
    public static final String EXTRACT_NUMBER_REGEX = "[0-9]+";
}
