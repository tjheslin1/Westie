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

import static java.lang.String.format;

/**
 * Domain representation of a HTTP response.
 */
public class Response extends ValueType {

    public final int statusCode;
    public final String body;
    public final String protocol;

    public Response(int statusCode, String body, String protocol) {
        this.statusCode = statusCode;
        this.body = body;
        this.protocol = protocol;
    }

    /**
     * @return true if the statusCode is within the 200 range or below. false otherwise.
     */
    public boolean isSuccessful() {
        return statusCode < 300;
    }

    /**
     * @return The response in a readable format.
     */
    @Override
    public String toString() {
        return format("%s %s%n%s", protocol, statusCode, body);
    }
}
