package top.openyuan.sdt.cipher.model;

import top.openyuan.sdt.cipher.enums.CipherType;

public class CipherAlgorithm {
    private CipherType cipherType;
    private String securityProvider;

    public CipherAlgorithm(CipherType cipherType){
        this.cipherType = cipherType;
    }

    public CipherAlgorithm(CipherType cipherType, String securityProvider){
        this.cipherType = cipherType;
        this.securityProvider = securityProvider;
    }

    public CipherType getCipherType() {return cipherType;}

    public void setCipherType(CipherType cipherType) {this.cipherType = cipherType;}

    public String getSecurityProvider() {
        return securityProvider;
    }

    public void setSecurityProvider(String securityProvider) {
        this.securityProvider = securityProvider;
    }
}
