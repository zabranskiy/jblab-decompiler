package examples.java;

public class Expressions {


    private boolean[] privateIncMethod(int c, int j, int i) {
        i = c + 4;
        i *= 7;
        c--;
        return new boolean[100];
    }

    private boolean swap(int a, int b) {
        int c = a;
        a = b;
        b = c;
        return true;
    }

    private void method(String... strings) {
        int i = strings.length;
        int a;
        int g;
        int h;
        Object o;

    }

    private void meth(short d) {
        d = (short) (d + 1);
    }

    public void interfaceMethod() {
        int i = 123;
        int j = (short) (5 * i);
        long k = 1;
        double p = 10;
        byte e = 3;
        byte[] d = {1, 2, 3, 4, 5};
        float[][] w = {{1, 2}, {4, 5, 6}, {2, 3}};
        String[][] strs = {{"a", "b"}, {"c"}};
        d[1]++;
        p = p - i + j;
        k++;
        i *= k + -j;
        i >>= k -= (j *= 4);
        strs[2][3] = strs[1][1] + 1 + "w";
        method("w" + 23 + strs[0][0] + " s" + 2.4, "ast" + strs[1][2]);
        long t = 1 + i;
        t = t + 1;
        k <<= (i *= (i |= 2) + (j >>= 1) * (i &= j <<= (int) (p *= i)));
        if (swap(1, 1)) {
            i++;
        } else if (privateIncMethod(1, 1, 1)[1]) {
            i++;
        }

        int[][] a = new int[100][100];
        String[][] arr = new String[100][100];
        i = arr[i].length;
        arr[i][j] += 2;
        System.out.println(arr[99][99] = arr[i + (j ^ (i | j))][arr.length + 'z']);
        a[1][1] = 1;
        k = a.length;
        String[] s = new String[10];
        char c = s[5].charAt(0);
        if (privateIncMethod(1, 1, 1)[(int) k * j]) {
            a[100][i + (int) k / j] = 3;
        }
        a[2][3] = 6;
        k = new double[1000].length - a.length;
/*        int p = 0;
        for(int forI = k; forI < i; forI++) {
           i++;
            for(int t=0;t<10;t++){
                j++;
            }
        }*/
        /*do {
            swap(a[5][k], p);
        } while (p != a[5][k / 2 + i]);*/
        //j = ++j;
        //i = i + j;
        //i += 4;
        // i=i*j+j/i;
        //i = --k | (j +=(-(k*=2 -(j%=2))));
        //i=j+=-(3+j);
        //k = k - 5;
        //k = i - (k - (i + k)) + (j + (j - k));

        /*if (!(i < k)) {
            i++;
        }
        if (k > 123) {
            k = i + 1;
            if (k != 5) {
                i = i + 1;
                if (i < k) {
                    k = 2 * k;
                }
            } else {
                k = (3 * i) * k;
            }
        } else {
            k = k - 4;
        }
        k = k - 5;
        if (k > 123) {
            if (k > 123) {
                k = i + 1;
                if (k > 123) {
                    k = i + 2;
                }
            }
            k = i + 3;
        }*/

        i = (j += (-3 + j));
        i= (j+=(-(3+j)));
        j= (4|j % ((i+=(-3))+1))^(2&i|(3 <<j));
        int new_var = i++ / j % 2;
        i += new_var;
        ++i;
        i+=5;


    }
}