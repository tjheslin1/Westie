package io.github.tjheslin1.westie;

import static java.lang.String.format;

public class Response extends ValueType {

    public final int statusCode;
    public final String body;
    public final String protocol;

    public Response(int statusCode, String body, String protocol) {
        this.statusCode = statusCode;
        this.body = body;
        this.protocol = protocol;
    }

    public boolean isSuccessful() {
        return statusCode <= 200;
    }

    @Override
    public String toString() {
        return format("%s %s%n%s", protocol, statusCode, body);
    }
}
