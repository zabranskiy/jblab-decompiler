package examples.java;

import com.sdc.ast.Type;

public class Fields {
    public static String s = new StringBuilder().append("2") + "ab";
    private static Type t = new Type(s);
    private int i;

    //private  static int h= j--;
    private  static double d =Fields.getDouble()+5;
    private  static int j= 2*Math.abs(-3);
    private int z=0;


    public Fields(int i) {
        this.i = --i;
    }

    public Fields(double c, int g, long p){
        z= (int) (c+g+p);
    }
    static {
       d = Fields.getDouble();
    }
    public static  double getDouble(){
        return 3.1;
    }
}
