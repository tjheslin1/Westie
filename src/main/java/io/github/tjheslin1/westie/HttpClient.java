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

import io.github.tjheslin1.westie.http.Request;
import io.github.tjheslin1.westie.http.Response;

import java.io.IOException;

/**
 * Domain representation of a HTTP client which executed a {@link Request}
 * and receives a corresponding {@link Response}.
 */
public interface HttpClient {

    /**
     *
     * @param request The {@link Request} to be sent via the httpClient implementation.
     * @return The result of the http request as a {@link Response}.
     * @throws IOException if an I/O exception occurs whilst communicating over HTTP.
     */
    Response execute(Request request) throws IOException;
}
