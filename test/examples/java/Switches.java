package examples.java;

public class Switches {
    public void switchMethod() {
        int i = 1;
        int j = i * 2;

        switch (j) {
            case 1:
                double a1 = 4;
                break;
            case 4:
                double a2 = 6;
            default:
                double a3 = 26;
                break;
            case 6:
                double a4 = 8;
                break;
            case 8:
                switch (j + 10) {
                    case 1:
                        double a5 = 12;
                        break;
                    case 2:
                        double a6 = 14;
                    case 3:
                        j = i * 16;
                        i = i * j;
                        break;
                    case 4:
                        double a7 = 25;
                        break;
                    default:
                        int a8 = 8;
                        break;
                }
            case 9:
                int a9 = 9;
                switch (j + 10) {
                    case 1:
                        double a10 = 12;
                        break;
                    case 4:
                        double a11 = 14;
                    case 5:
                        j = i * 16;
                        i = i * j;
                        break;
                    case 7:
                        double a12 = 25;
                        break;
                }
                switch (j + 100) {
                    case 1:
                        double a13 = 12;
                        break;
                    case 4:
                        double a14 = 14;
                    case 5:
                        j = i * 16;
                        i = i * j;
                        break;
                    case 7:
                        double a15 = 25;
                        break;
                }
                break;
        }
    }
}
