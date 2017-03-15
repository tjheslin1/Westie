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
package io.github.tjheslin1.westie.environmentproperties;

import io.github.tjheslin1.westie.ValueType;

import java.nio.file.Path;
import java.util.Set;

/**
 * Represents a properties file and its set of property names (keys).
 */
public class FileKeySet extends ValueType {

    public final Path file;
    public final Set<Object> keySet;

    private FileKeySet(Path file, Set<Object> keySet) {
        this.file = file;
        this.keySet = keySet;
    }

    public static FileKeySet fileKeySet(Path file, Set<Object> keySet) {
        return new FileKeySet(file, keySet);
    }

    /**
     * @return the size of the list of property names (keys).
     */
    public int numKeys() {
        return keySet.size();
    }
}
