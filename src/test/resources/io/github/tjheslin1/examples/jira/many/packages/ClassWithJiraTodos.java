package io.github.tjheslin1.examples

public class ClassWithJiraTodos {
    //TODO MON-100 make this final
    private String name;

    public void setName(String n) {
        // TODO MON-101 set passed parameter as name
        name = n;
    }

    @Notes("MON-101")
    public String getName() {
        // TODO 22/10/2016 rename this method
        return name;
    }

    //main method will be called first when program is executed
    public static void main(String args[]) {
        //      TODO  NO_DATE   move this to another class

        JavaClassExample javaClassExample = new JavaClassExample();
        //set name member of this object
        javaClassExample.setName("Visitor");
        // print the name
        System.out.println("Hello " + javaClassExample.getName());
    }
}