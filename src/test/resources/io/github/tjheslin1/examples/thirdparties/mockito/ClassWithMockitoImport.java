package io.github.tjheslin1.examples.thirdparties.mockito;

import io.github.tjheslin1.westie.Response;
import org.mockito.Mockito;

public class ClassWithMockitoImport {

    private final Response response = mock(Response.class);

    public static void main(String args[]) {
        System.out.println("Hello " + javaClassExample.getName());
    }
}