package examples.java;

import examples.java.support.SuperClass;
import examples.java.support.SuperInterface;

import java.util.ArrayList;

public class Types extends SuperClass implements SuperInterface{
    @Override
    public void interfaceMethod() {
    }

    public Types returnTypes() {
        return new Types();
    }

    public void testMethod(Types types, int z, int[] arrayOneDim, int[][] arrayTwoDim, String[][] stringArray) {
        ArrayList<String> arrayListTest = new ArrayList<String>();

        int[] intArray = new int[3];
        int[][] twoDimensionIntArray = new int[2][2];

        String[][] twoDimensionStringArray1 = new String[1][1];
        String[][] twoDimensionStringArray2 = twoDimensionStringArray1;

        String[] row = twoDimensionStringArray2[0];
        String element = twoDimensionStringArray1[0][0];

        SuperInterface te = returnTypes();
        SuperInterface newTest = new Types();
        SuperInterface variableTest = types;

        Types types1e = types.returnTypes();

        SuperClass superTest1 = superMethod();
        SuperClass superTest2 = super.superMethod();
        SuperClass superTest3 = SuperClass.superStaticMethod();

        boolean instanceOfTest = superTest1 instanceof SuperClass;

        ((SuperClass) variableTest).superMethod();
    }

    public void varargMethod(String ... args) {
        varargMethod("1", "2", "3");
    }
}
