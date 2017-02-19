package fr.piotr.reactions.utils;

import java.util.StringJoiner;

/**
 * Created by piotr_000 on 30/12/2016.
 *
 */

public class PatternConverter {

    public static String asString(long[] pattern){
        StringBuffer sb = new StringBuffer();

        if(pattern.length>0) {

            sb.append(pattern[0]);

            for (int i=1;i<pattern.length;i++) {
                sb.append(";");
                sb.append(pattern[i]);
            }

        }

        return sb.toString();
    }

    public static long[] asPattern(String str){
        String[] split = str.split(";");
        long[] pattern = new long[split.length];
        for (int i = 0; i < split.length; i++) {
            pattern[i] = Long.valueOf(split[i]);
        }
        return pattern;
    }
}
