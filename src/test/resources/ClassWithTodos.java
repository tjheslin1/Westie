public class ClassWithTodos {
    /*
    Syntax of defining memebers of the java class is,
      <modifier> type <name>;
    */

    //TODO make this final
    private String name;

    public void setName(String n) {
        //set passed parameter as name
        name = n;
    }

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