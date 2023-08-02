package top.openyuan.sdt.cipher;

import java.security.Key;

/**
 * 对称加解密执行器
 *
 * @author lzy
 */
public interface SymmetricCipherExecutor {
    /**
     * 加密并转换为字符串
     *
     * @param plaintext 明文
     * @return Base64编码的密文
     */
    String encrypt(String plaintext, Key encryptKey);

    /**
     * 解密并转换为字符串
     *
     * @param ciphertext 密文
     * @return UTF-8编码的明文
     */
    String decrypt(String ciphertext, Key decryptKey);
}
