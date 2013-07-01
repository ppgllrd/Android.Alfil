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

    public static String removeAccents(String s0) {
        char[] accents = new char[]{'á','é','í','ó','ú','Á','É','Í','Ó','Ú'};
        char[] noAccents = new char[]{'a','e','i','o','u','A','E','I','O','U'};

        String s1 = s0;
        for(int i = 0; i < accents.length; i++)
          s1 = s1.replace(accents[i],noAccents[i]);
        return s1;
    }


}
