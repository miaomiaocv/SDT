package top.openyuan.sdt.cipher;

public interface AsymmetricCipherExecutor {
    /**
     * 加密并转换为字符串
     *
     * @param plaintext 明文
     * @return Base64编码的密文
     */
    String encrypt(String plaintext);

    /**
     * 解密并转换为字符串
     *
     * @param ciphertext 密文
     * @return UTF-8编码的明文
     */
    String decrypt(String ciphertext);
}
