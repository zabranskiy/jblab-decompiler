package examples.java;

import examples.java.support.SuperClass;

import java.io.PrintStream;

public class FieldInitializationAndAccess {
    public String str = "strField";
    public int anInt = 1;
    public SuperClass sc = new SuperClass();
    public PrintStream ps = System.out;

    FieldInitializationAndAccess() {
        anInt = 2;
        FieldInitializationAndAccess ie = new FieldInitializationAndAccess();
        ie.ps = System.out;
        int anInt2 = ie.anInt;
        int anInt3 = new FieldInitializationAndAccess().anInt;
        ie.ps = getA().getA().getA().ps;
    }

    FieldInitializationAndAccess(int asd) {
        System.out.println(asd);
    }

    public FieldInitializationAndAccess getA() {
        return this;
    }
}
