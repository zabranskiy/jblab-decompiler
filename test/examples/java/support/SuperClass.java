package examples.java.support;

public class SuperClass {
    public SuperClass superMethod() {
        return new SuperClass();
    }

    public static SuperClass superStaticMethod() {
        return new SuperClass();
    }

    public class Kolasd {}
}
