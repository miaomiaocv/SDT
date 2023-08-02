package top.openyuan.sdt.cipher;

import top.openyuan.sdt.cipher.model.CipherAlgorithm;
import top.openyuan.sdt.exception.DecryptionException;
import org.apache.commons.lang.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

import static java.util.Objects.requireNonNull;

/**
 * 非对称加密执行器
 *
 * @author lzy
 */
public abstract class AbstractAsymmetricCipherExecutor implements AsymmetricCipherExecutor{
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final Cipher cipher;

    /**
     * 构造函数
     *
     * @param cipherAlgorithm 对称加解密算法
     * @param publicKey 加密对应的密钥
     * @param privateKey 加密对应的密钥
     */
    protected AbstractAsymmetricCipherExecutor(CipherAlgorithm cipherAlgorithm, PublicKey publicKey, PrivateKey privateKey) {
        this.privateKey = requireNonNull(privateKey);
        this.publicKey = requireNonNull(publicKey);
        try {
            // 根据算法和提供者创建Cipher实例
            String algorithm = cipherAlgorithm.getCipherType().getCode();
            String securityProvider = cipherAlgorithm.getSecurityProvider();
            cipher = StringUtils.isNotBlank(securityProvider) ?
                    Cipher.getInstance(algorithm, securityProvider) : Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // 抛出异常，提示当前Java环境不支持该算法
            throw new IllegalArgumentException(
                    "当前Java环境不支持算法 " + cipherAlgorithm.getCipherType(), e);
        } catch (NoSuchProviderException e) {
            // 抛出运行时异常，提示提供者错误
            throw new RuntimeException(e);
        }
    }

    @Override
    public String encrypt(String plaintext) {
        byte[] encryptedBytes = encryptToByte(plaintext);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 将明文加密为字节数组
     *
     * @param plaintext  明文
     * @return 加密后的字节数组
     */
    public byte[] encryptToByte(String plaintext) {
        requireNonNull(plaintext);
        try {
            // 初始化Cipher并进行加密
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        } catch (InvalidKeyException e) {
            // 抛出异常，提示使用非法的加密密钥
            throw new IllegalArgumentException("使用非法的公钥进行加密", e);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            // 抛出异常，提示明文过长
            throw new IllegalArgumentException("明文过长", e);
        }
    }


    @Override
    public String decrypt(String ciphertext) {
        return new String(decryptToByte(ciphertext), StandardCharsets.UTF_8);
    }

    /**
     * 将Base64编码的密文解密为字节数组
     *
     * @param ciphertext 密文（Base64编码）
     * @return 解密后的字节数组
     */
    public byte[] decryptToByte(String ciphertext) {
        requireNonNull(ciphertext);
        try {
            // 初始化Cipher并进行解密
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        } catch (InvalidKeyException e) {
            // 抛出异常，提示使用非法的解密密钥
            throw new IllegalArgumentException("给定的私钥无效，无法解密", e);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            // 抛出解密异常
            throw new DecryptionException("解密失败", e);
        }
    }

    public PrivateKey getPrivateKey() {return privateKey;}

    public PublicKey getPublicKey() {return publicKey;}

    public Cipher getCipher() {return cipher;}
}
