package examples.java;

import java.util.List;

public class ControlFlow {
    public void forTest(List<Integer> list, int[] intArray) {
        for (int i : intArray) {
            for (int j : intArray) {
                System.out.println(j);
            }
            System.out.println(i);
            for (int k : list) {
                for (int l : list) {
                    System.out.println(i);
                }
                System.out.println(k);
            }
        }

        int i = 1;
        int j = 2;
        int k = 3;

        if (list == null) {
            int a1 = 1;
            int a2 = 2;
            int a3 = 3;
            int a4 = 4;
            int a5 = 5;
            int a6 = 6;
            int a7 = 7;
            int a8 = 8;

            if (list == null) {
                int z = 9;
            } else {
                int x = 10;
            }
        } else {
            int c = 11;
            for (Integer q : list) {
                System.out.println(q);
            }
            int d = 12;
        }

        for (int forI = k; forI < i; forI++) {
            i++;
            for (int t = forI; t > 0; t--) {
                int y1 = 13;
                j++;
            }
            int y2 = 14;
        }
        int y3 = 15;
    }

    public void ifMethod(int int1, int int2) {
        int i = 1;
        int j = i * i;

        if (j > 1) {
            if (j > 2) {
                if (int1 > 3) {
                    if (j > 4) {
                        if (int2 > 5) {
                            j = 6;
                        }
                    }
                }
            }
        }
        j = 7;
    }

    public void whileTest() {
        int i = 1;
        int j = 2;
        while (i >= 3) {
            i++;

            do {
                i--;
            } while (i < 4);

            while (i * 5 < 6) {
                i = i * 7;
                if (i < 8) {
                    j = 9;
                } else {
                    if (j > 10) {
                        i = 11;
                    } else {
                        i = 12;
                        break;
                    }
                }
                i = 13;
            }
        }
    }
}
