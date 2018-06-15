package pl.kielce.tu.pai2.mobile.bank.bankapplication.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamStringConverter {
    public static String streamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;

            try {

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }

}

