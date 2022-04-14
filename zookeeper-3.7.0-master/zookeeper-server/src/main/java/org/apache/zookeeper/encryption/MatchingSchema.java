package org.apache.zookeeper.encryption;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MatchingSchema {
    public static final String TOPICS_PATH = "/brokers/topics";
    public static final String BROKERS_PATH = "/brokers/ids";
    //encryption part
    public static final BigInteger N = new BigInteger("3337");
    public static final int EI2 = 2173;
    public static final int EJ2 = 2467;

    private static String ZEnc(String str, int e) {
        int index = 0;
        List<BigInteger> Cwi = new ArrayList<>();
        try {
            while (index < str.length()) {
                String tmp = str.substring(index, Math.min(index + 4, str.length()));
                BigInteger tmp_int = new BigInteger(tmp, 16);
                Cwi.add(tmp_int);
                index += 4;
            }
            BigInteger[] Cwi_star = new BigInteger[Cwi.size()];
            String CwiStar = "";
            for (int i = 0; i < Cwi.size(); i++) {
                BigInteger bigCw = Cwi.get(i).pow(e);
                Cwi_star[i] = bigCw.mod(N);
                CwiStar = CwiStar + Cwi_star[i].toString(10);
                return CwiStar;
            }

        } catch (Exception exception) {
            //exception.printStackTrace();
        }
        return "";
    }

    public static Boolean ms_compare(String str1, String str2) {
        String prod_topic_mA = ZEnc(str1, EI2);
        String cons_topic_mA = ZEnc(str2, EJ2);
        return (prod_topic_mA.equals(cons_topic_mA));
    }
}
