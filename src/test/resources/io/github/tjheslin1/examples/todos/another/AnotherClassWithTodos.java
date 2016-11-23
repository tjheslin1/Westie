package io.github.tjheslin1.examples

public class AnotherClassWithTodos {

    public static void main(String args[]) {
        //      TODO  NO_DATE   another todo

        JavaClassExample javaClassExample = new JavaClassExample();
        //set name member of this object
        javaClassExample.setName("Visitor");
        // print the name
        System.out.println("Hello " + javaClassExample.getName());
    }
}