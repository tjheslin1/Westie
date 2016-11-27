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
package io.github.tjheslin1.westie.http;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder pattern for constructing a HTTP {@link Request}.
 */
public class RequestBuilder {

    private String url;
    private String method;
    private String body = "";
    private List<QueryParameter> queryParameters = new ArrayList<>();

    public RequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Sets the 'method' to "GET"
     *
     * @return The current state of the {@link RequestBuilder}
     */
    public RequestBuilder get() {
        this.method = "GET";
        return this;
    }

    /**
     * Sets the 'method' to "POST"
     *
     * @return The current state of the {@link RequestBuilder}
     */
    public RequestBuilder post() {
        this.method = "POST";
        return this;
    }

    /**
     * @param method The value to set the {@link Request} method to.
     * @return The current state of the {@link RequestBuilder}
     */
    public RequestBuilder withMethod(String method) {
        this.method = method;
        return this;
    }

    /**
     * @param queryParameter The query parameter to append to the {@link Request}
     * @return The current state of the {@link RequestBuilder}
     */
    public RequestBuilder withQueryParameter(QueryParameter queryParameter) {
        this.queryParameters.add(queryParameter);
        return this;
    }

    /**
     * @return A {@link Request} constructed with the fields set by the builder methods.
     */
    public Request build() {
        return new Request(url, method, body, queryParameters);
    }
}
