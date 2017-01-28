package io.github.tjheslin1.examples

public class ClassWithMultipleLevelsOfIndentation {

    private String name;

    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public static void main(String args[]) {
        JavaClassExample javaClassExample = new JavaClassExample();
        if("MONDAY".toLowerCase().equals("monday")) {
            System.out.println("hello world!");
        } else { // wait, I cant use else?
            System.out.println("Who knows what day it is!");
        }
        System.out.println("Hello " + javaClassExample.getName());
    }
}