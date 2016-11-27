package io.github.tjheslin1.examples.mockito;

import io.github.tjheslin1.westie.http.Response;

public class ClassWithMockitoImport {

    private final Response response = mock(Response.class);

    public static void main(String args[]) {
        System.out.println("Hello " + javaClassExample.getName());
    }
}