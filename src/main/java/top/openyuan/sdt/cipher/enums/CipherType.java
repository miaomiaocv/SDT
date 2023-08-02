package top.openyuan.sdt.cipher.enums;

public enum CipherType {
    /********************* 非对称加解密算法 *************************/
    RSA("RSA"),
    DSA("DSA"),
    /********************* 对称加解密算法 *************************/
    AES("AES"),
    AES_CBC_PKCS5Padding("AES/CBC/PKCS5Padding"),
    AES_ECB_PKCS5Padding("AES/ECB/PKCS5Padding"),
    DES("DES"),
    DESede("DESede"),
    /********************* 消息摘要算法 *************************/
    MD5("MD5"),
    SHA_1("SHA-1"),
    SHA_256("SHA-256");

    private final String code;

    CipherType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
