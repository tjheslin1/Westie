package io.github.tjheslin1.examples

import io.github.tjheslin1.westie.Response;
import org.mockito.Mockito;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassWithThirdPartyImport {

    private final Response response = mock(Response.class);

    public static void main(String args[]) {
        System.out.println("Hello " + javaClassExample.getName());
    }
}