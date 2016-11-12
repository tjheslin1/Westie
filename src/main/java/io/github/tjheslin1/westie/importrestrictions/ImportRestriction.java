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

/**
 * Represents the package in which an import can only be used in.
 *
 * For example:
 * Set 'packagePath' to "com.example.database" and
 * and 'importRegex' to "import.*oracle.jdbc.*".
 *
 * Adding this restriction to {@link ImportsRestrictedToSpecifiedPackages}
 * will enforce that any oracle.jdbc import be only used in the database package, complaining if used elsewhere.
 */
public class ImportRestriction extends ValueType {

    public final String packagePath;
    public final String importRegex;

    private ImportRestriction(String acceptedPackagePath, String importRegex) {
        this.packagePath = acceptedPackagePath;
        this.importRegex = importRegex;
    }

    /**
     * Static constructor.
     *
     * @param packagePath The package in which the import can only be used in.
     * @param importRegex The regex matching against imports which can only be used in this packagePath and under it.
     * @return A {@link ImportRestriction} instance matching the provided import regex to all java files in the
     * provided package.
     */
    public static ImportRestriction importRestriction(String packagePath, String importRegex) {
        return new ImportRestriction(packagePath, importRegex);
    }
}
