package examples.java;

import examples.java.support.SuperClass;
import examples.java.support.SuperInterface;

public class MethodInvocation extends SuperClass implements SuperInterface {
    @Override
    public SuperClass superMethod() {
        return new SuperClass();
    }

    @Override
    public void interfaceMethod() {
        int x = 1;
    }

    public void publicMethod(){}
    private void privateMethod(){}
    protected void protectedMethod(){}

    public void classMethodInvocation() {
        publicMethod();
        privateMethod();
        protectedMethod();
        superMethod();
        interfaceMethod();
        super.superMethod();
        SuperClass.superStaticMethod();
    }

    public String methodWithParameters(int i, String s, boolean b, long l, SuperClass superClass, SuperInterface superInterface) {
        long q = l;

        String str = "str";
        String result = methodWithParameters(2, str, b, q, this, new MethodInvocation());

        SuperClass sc = new SuperClass();
        sc.superMethod().superMethod().superMethod().superMethod();
        new SuperClass().superMethod().superMethod().superMethod();

        StringBuilder sb = new StringBuilder("test");
        sb.append("a").append("b").append("c");

        new SuperClass();

        return sb.toString();
    }
}
