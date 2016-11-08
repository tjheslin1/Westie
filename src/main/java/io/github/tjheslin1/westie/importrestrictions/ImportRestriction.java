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
package io.github.tjheslin1.westie.importrestrictions;

import io.github.tjheslin1.westie.ValueType;

public class ImportRestriction extends ValueType {

    public final String packagePath;
    public final String importRegex;

    private ImportRestriction(String acceptedPackagePath, String importRegex) {
        this.packagePath = acceptedPackagePath;
        this.importRegex = importRegex;
    }

    public static ImportRestriction importRestriction(String packagePath, String importRegex) {
        return new ImportRestriction(packagePath, importRegex);
    }
}
