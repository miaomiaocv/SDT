package top.openyuan.sdt.cipher;

import top.openyuan.sdt.cipher.model.CipherAlgorithm;
import top.openyuan.sdt.exception.DecryptionException;
import org.apache.commons.lang.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;

import static java.util.Objects.requireNonNull;


/**
 * 抽象对称加解密执行器
 *
 * @author lzy
 */
public abstract class AbstractSymmetricCipherExecutor implements SymmetricCipherExecutor {
    private final Cipher cipher;

    /**
     * 构造函数
     *
     * @param cipherAlgorithm 对称加解密算法
     */
    protected AbstractSymmetricCipherExecutor(CipherAlgorithm cipherAlgorithm) {
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
    public String encrypt(String plaintext, Key encryptKey) {
        byte[] encryptedBytes = encryptToByte(plaintext, encryptKey);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 将明文加密为字节数组
     *
     * @param plaintext  明文
     * @param encryptKey 加密密钥
     * @return 加密后的字节数组
     */
    public byte[] encryptToByte(String plaintext, Key encryptKey) {
        requireNonNull(plaintext);
        try {
            // 初始化Cipher并进行加密
            cipher.init(Cipher.ENCRYPT_MODE, encryptKey);
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
    public String decrypt(String ciphertext, Key decryptKey) {
        return new String(decryptToByte(ciphertext, decryptKey), StandardCharsets.UTF_8);
    }

    /**
     * 将Base64编码的密文解密为字节数组
     *
     * @param ciphertext 密文（Base64编码）
     * @param decryptKey 解密密钥
     * @return 解密后的字节数组
     */
    public byte[] decryptToByte(String ciphertext, Key decryptKey) {
        requireNonNull(ciphertext);
        try {
            // 初始化Cipher并进行解密
            cipher.init(Cipher.DECRYPT_MODE, decryptKey);
            return cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        } catch (InvalidKeyException e) {
            // 抛出异常，提示使用非法的解密密钥
            throw new IllegalArgumentException("给定的私钥无效，无法解密", e);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            // 抛出解密异常
            throw new DecryptionException("解密失败", e);
        }
    }

    public Cipher getCipher() {
        return cipher;
    }
}
