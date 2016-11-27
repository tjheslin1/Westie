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
package io.github.tjheslin1.westie.infrastructure;

import io.github.tjheslin1.westie.HttpClient;
import io.github.tjheslin1.westie.http.Request;
import io.github.tjheslin1.westie.http.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

/**
 * Implementation of {@link HttpClient} which can be used, which uses 'org.apache.http.client.HttpClient'.
 */
public class ApacheHttpClient implements HttpClient {

    private final org.apache.http.client.HttpClient httpClient;

    public ApacheHttpClient(Duration maxIdleTime) {
        httpClient = HttpClientBuilder.create()
                .evictIdleConnections(maxIdleTime.getSeconds(), TimeUnit.SECONDS)
                .build();
    }

    /**
     * Adapts the {@link Request} to a an Apache {@link HttpUriRequest}.
     *
     * @param request The {@link Request} to be sent.
     * @return The {@link Response} which has been adapted from a {@link HttpResponse}.
     * @throws IOException if an I/O exeception occurs during the HTTP request/response.
     */
    @Override
    public Response execute(Request request) throws IOException {
        HttpResponse response = httpClient.execute(adaptRequest(request));
        return adaptResponse(response);
    }

    private HttpUriRequest adaptRequest(Request request) throws IOException {
        GenericHttpUriRequest httpUriRequest = new GenericHttpUriRequest(request.method, adaptUrl(request));
        adaptBody(request, httpUriRequest);
        return httpUriRequest;
    }

    private void adaptBody(Request request, GenericHttpUriRequest httpUriRequest) throws IOException {
        if (!request.body.isEmpty()) {
            try {
                httpUriRequest.setEntity(new StringEntity(request.body));
            } catch (UnsupportedEncodingException e) {
                throw new IOException(format("Unsupported Encoding for request '%s'", request.toString()));
            }
        }
    }

    private Response adaptResponse(HttpResponse response) throws IOException {
        return new Response(adaptStatusCode(response),
                adaptBody(response),
                adaptProtocol(response));
    }

    private URI adaptUrl(Request request) throws IOException {
        try {
            return new URIBuilder(request.url).build();
        } catch (URISyntaxException e) {
            throw new IOException(format("Invalid url '%s'", request.url), e);
        }
    }

    private String adaptProtocol(HttpResponse response) {
        return response.getProtocolVersion().toString().toUpperCase();
    }

    private String adaptBody(HttpResponse response) throws IOException {
        HttpEntity httpEntity = response.getEntity();
        if (httpEntity == null) {
            return "";
        }
        return EntityUtils.toString(httpEntity);
    }

    private int adaptStatusCode(HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    private static class GenericHttpUriRequest extends HttpEntityEnclosingRequestBase {

        private final String method;

        private GenericHttpUriRequest(String method, URI uri) {
            this.method = method;
            setURI(uri);
        }

        @Override
        public String getMethod() {
            return method;
        }
    }
}
