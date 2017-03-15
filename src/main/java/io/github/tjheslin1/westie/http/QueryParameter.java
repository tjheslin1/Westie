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
package io.github.tjheslin1.westie.http;

import io.github.tjheslin1.westie.ValueType;

/**
 * Domain representation of a HTTP request parameter.
 */
public class QueryParameter extends ValueType {

    public final String key;
    public final String value;

    private QueryParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Static constructor.
     *
     * @param key The key (left-side) of the query parameter.
     * @param value The value (right-side) of the query parameter.
     * @return A constructed {@link QueryParameter}
     */
    public static QueryParameter queryParameter(String key, String value) {
        return new QueryParameter(key, value);
    }

    /**
     * @return A HTTP url friendly representation of the key/value pair.
     */
    @Override
    public String toString() {
        return key + "=" + value;
    }
}
