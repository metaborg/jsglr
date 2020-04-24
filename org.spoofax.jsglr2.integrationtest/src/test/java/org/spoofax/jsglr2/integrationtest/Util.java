package org.spoofax.jsglr2.integrationtest;

public class Util {

    public static String newlines(int n) {
        StringBuilder s = new StringBuilder();

        for(int i = 0; i < n; i++)
            s.append("\n");

        return s.toString();
    }

}
