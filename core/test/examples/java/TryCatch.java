package examples.java;

import java.io.FileInputStream;
import java.io.IOException;

public class TryCatch {
    public void tryCatchTest() {
        try {
            FileInputStream i = new FileInputStream("test");
        } catch (IOException e) {
            int j = 1;
            e.printStackTrace();
        } catch (Exception e) {
            int k = 2;
            e.printStackTrace();
        }
    }
}
