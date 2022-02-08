package cn.zhengcaiyun.idata.commons.util;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;

/**
 * @author caizhedong
 * @date 2021-06-21 17:43
 */
public class DesUtil {

    private static Key generateKey(String key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(dks);
    }

    public static String encrypt(String password) {

        try {
            Cipher ecipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, generateKey("idata_ds"));

            byte[] utf8 = password.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);
            enc = BASE64EncoderStream.encode(enc);

            return new String(enc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String password) {
        try {
            Cipher dcipher = Cipher.getInstance("DES");
            dcipher.init(Cipher.DECRYPT_MODE, generateKey("idata_ds"));

            byte[] dec = BASE64DecoderStream.decode(password.getBytes());
            byte[] utf8 = dcipher.doFinal(dec);

            return new String(utf8, "UTF8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//
//        try {
//            String encrypted = encrypt("This is test!");
//            String decrypted = decrypt(encrypted);
//            System.out.println("Decrypted: " + decrypted);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

}
