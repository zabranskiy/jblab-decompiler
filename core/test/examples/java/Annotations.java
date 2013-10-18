package examples.java;

import examples.java.support.FancyAnnotation;
import examples.java.support.MarkerAnnotation;

@Deprecated
@FancyAnnotation
@MarkerAnnotation(intProperty = 456, stringProperty = "class")
public class Annotations {

    @FancyAnnotation
    @MarkerAnnotation(intProperty = 789, stringProperty = "method")
    public void methodAndParameterAnnotationTest(int x, @MarkerAnnotation(intProperty = 123, stringProperty = "parameter") long y, String s) {
    }
}
