package top.openyuan.sdt.keypair.model;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class KeyPair {
    private String publicKey;
    private String privateKey;

    public KeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = encodeKeyToString(publicKey);
        this.privateKey = encodeKeyToString(privateKey);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    private static String encodeKeyToString(Key key) {
        byte[] publicKeyBytes = key.getEncoded();
        return Base64.getEncoder().encodeToString(publicKeyBytes);
    }
}
