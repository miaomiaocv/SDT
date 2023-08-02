package top.openyuan.sdt.cipher;

import top.openyuan.sdt.cipher.enums.CipherType;
import top.openyuan.sdt.cipher.model.CipherAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 非对称加密执行器
 *
 * @author lzy
 */
public abstract class RSACipherExecutor extends AbstractAsymmetricCipherExecutor{
    /**
     * 构造函数
     *
     * @param publicKey 加密对应的密钥
     * @param privateKey 加密对应的密钥
     */
    protected RSACipherExecutor(PublicKey publicKey, PrivateKey privateKey) {
        super(new CipherAlgorithm(CipherType.RSA), publicKey, privateKey);
    }
}
