package com.ppgllrd.alfil;

/**
 * Created by pepeg on 28/06/13.
 */
public class StringUtils {
    public static String uppercase(String s0) {
        String s = s0.toLowerCase();
        final StringBuilder result = new StringBuilder(s.length());
        String[] words = s.split("\\s");
        for(int i=0,l=words.length;i<l;++i) {
            if(i>0) result.append(" ");
            result.append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].substring(1));

        }
        return result.toString();
    }
}
