package io.github.tjheslin1.examples.thirdparties.apache;

import io.github.tjheslin1.westie.Response;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassWithApacheImport {

    private final Response response = mock(Response.class);

    public static void main(String args[]) {
        System.out.println("Hello " + javaClassExample.getName());
    }
}