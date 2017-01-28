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
        for (int i = 0; i < 10; i++) {
            javaClassExample.setName("Visitor");
        }
        System.out.println("Hello " + javaClassExample.getName());
    }
}