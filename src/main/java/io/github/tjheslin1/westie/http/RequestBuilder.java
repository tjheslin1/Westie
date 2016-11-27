package io.github.tjheslin1.westie.http;

import java.util.ArrayList;
import java.util.List;

public class RequestBuilder {

    private String url;
    private String method;
    private String body = "";
    private List<QueryParameter> queryParameters = new ArrayList<>();

    public RequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public RequestBuilder get() {
        method = "GET";
        return this;
    }

    public RequestBuilder post() {
        method = "POST";
        return this;
    }

    public RequestBuilder withQueryParameter(QueryParameter queryParameter) {
        queryParameters.add(queryParameter);
        return this;
    }

    public Request build() {
        return new Request(url, method, body, queryParameters);
    }
}
