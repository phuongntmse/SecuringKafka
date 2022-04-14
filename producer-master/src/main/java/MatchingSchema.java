import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MatchingSchema {
    public static final BigInteger N = new BigInteger("3337");
    public static final int EI1 = 3;
    public static final int HEX_LENGTH = 4;

    public static String Enc(String str, int e) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            BigInteger[] Cwi = new BigInteger[hash.length];
            String hashString = "";
            for (int i = 0; i < hash.length; i++) {
                BigInteger big = BigInteger.valueOf(hash[i]).pow(e);
                Cwi[i] = big.mod(N);
                String tmp = Cwi[i].toString(16);
                while (tmp.length() < HEX_LENGTH) {
                    tmp = "0" + tmp;
                }
                hashString = hashString + tmp;
            }
            return hashString;
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        return "";
    }
}
