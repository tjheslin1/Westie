package io.github.tjheslin1.examples.apache;

import io.github.tjheslin1.westie.http.Response;

public class ClassWithApacheImport {

    private final Response response = mock(Response.class);

    public static void main(String args[]) {
        System.out.println("Hello " + javaClassExample.getName());
    }
}