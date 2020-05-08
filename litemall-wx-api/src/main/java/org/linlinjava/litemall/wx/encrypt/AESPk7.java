package org.linlinjava.litemall.wx.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.linlinjava.litemall.core.config.SecretProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

@Component
public class AESPk7 {
    @Resource
    private SecretProperties properties;
    /**
     * @author ngh
     * AES128 算法
     * <p>
     * CBC 模式
     * <p>
     * PKCS7Padding 填充模式
     * <p>
     * CBC模式需要添加偏移量参数iv，必须16位
     * 密钥 sessionKey，必须16位
     * <p>
     * 介于java 不支持PKCS7Padding，只支持PKCS5Padding 但是PKCS7Padding 和 PKCS5Padding 没有什么区别
     * 要实现在java端用PKCS7Padding填充，需要用到bouncycastle组件来实现
     */


    // 算法名称
    final String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    final String algorithmStr = "AES/CBC/PKCS7Padding";
    // 加解密 密钥 16位

    byte[] ivByte;
    byte[] keybytes;
    private Key key;
    private Cipher cipher;
    boolean initial = false;

    public void init() {
        if (!initial) {
            String secret = properties.getAesKey();
            // 偏移量 16位
            String iv = properties.getAesIv();
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            keybytes = secret.getBytes();
            ivByte = iv.getBytes();
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            // 转化成JAVA的密钥格式
            key = new SecretKeySpec(keybytes, KEY_ALGORITHM);
            try {
                // 初始化cipher
                cipher = Cipher.getInstance(algorithmStr, "BC");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
            initial = true;
        }
    }

    public String encrypt(String content) {
        byte[] encryptedText = null;
        byte[] contentByte = content.getBytes();
        init();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivByte));
            encryptedText = cipher.doFinal(contentByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(Hex.encode(encryptedText));
    }

    public String decrypt(String encryptedData) {
        byte[] encryptedText = null;
        byte[] encryptedDataByte = Hex.decode(encryptedData);
        init();
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivByte));
            encryptedText = cipher.doFinal(encryptedDataByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(encryptedText);
    }

    public static void main(String[] args) {
        AESPk7 aes = new AESPk7();

        //加密字符串
        String content = "孟飞快跑";
        System.out.println("加密前的：" + content);
//        System.out.println("加密密钥：" + new String(keybytes));
        // 加密方法
        String enc = aes.encrypt(content);
        System.out.println("加密后的内容：" + enc);

        // 解密方法
        String dec = aes.decrypt(enc);
        System.out.println("解密后的内容：" + dec);
    }
}