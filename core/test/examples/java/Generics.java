package examples.java;

import examples.java.support.SuperClass;

import java.util.List;
import java.util.Map;

public class Generics<K extends Map<String, List<String>>, L extends SuperClass, M> {
    public K kField;

    public <R, S extends SuperClass, T> T genericTest2(K a, T b, R d) {
        K aa = a;
        R dd = d;
        S.superStaticMethod();
        return b;
    }
}
