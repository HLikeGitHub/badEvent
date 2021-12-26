package com.yjy.web.comm.utils.crypt.aes;



import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * @desc AESUtils
 * @author rwx
 * @email aba121mail@qq.com
 */
public class AESUtil {

    private static final String ENCODING = "UTF-8";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String IV = "0246813579fedcba";
    private static IvParameterSpec ivParameterSpec;
    private static Cipher cipher;

    static {
        try {
            ivParameterSpec = new IvParameterSpec(IV.getBytes(ENCODING));
            // 指定加密的算法、工作模式和填充方式
            cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (Exception e) {
            //LoggerUtils.error(null, e);
        }
    }

    /**
     * 使用key加密明文
     * @param plaintext
     * @param key 注意key的长度为16个byte
     * @return 密文
     * @throws Exception
     */
    public static String encrypt(String plaintext, String key) throws Exception {
        if(plaintext == null || plaintext.isEmpty()){
            return "";
        }

        init(key, Cipher.ENCRYPT_MODE);

        byte[] resultBytes = cipher.doFinal(plaintext.getBytes(ENCODING));
        String result = Base64.getEncoder().encodeToString(resultBytes);
        result = URLEncoder.encode(result, ENCODING);//去掉= ? &
        return result;
    }

    private static void init(String key, int mode) throws Exception{
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM);
        cipher.init(mode, secretKeySpec, ivParameterSpec);
    }

    /**
     * 使用key解密密文
     * @param ciphertext
     * @param key 注意key的长度为16个byte
     * @return 明文
     * @throws Exception
     */
    public static String decrypt(String ciphertext, String key) throws Exception {
        if(ciphertext == null || ciphertext.isEmpty()){
            return "";
        }

        init(key, Cipher.DECRYPT_MODE);

        ciphertext = URLDecoder.decode(ciphertext, ENCODING);
        byte[] cipherBytes = Base64.getDecoder().decode(ciphertext);
        byte[] resultBytes = cipher.doFinal(cipherBytes);
        String result = new String(resultBytes, ENCODING);
        return result;
    }
}
