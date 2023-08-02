package top.openyuan.sdt.signverify;

import top.openyuan.sdt.signverify.enums.SignatureType;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * SHA256withRSA 签名验签执行器
 *
 * @author lzy
 */
public abstract class SHA256withRSASignVerifyExecutor extends AbstractSignVerifyExecutor {
    /**
     * SHA256withRSASignVerifyExecutor 构造函数
     *
     * @param publicKey  商户API公钥
     * @param privateKey 商户API私钥
     */
    public SHA256withRSASignVerifyExecutor(PublicKey publicKey, PrivateKey privateKey) {
        super(SignatureType.SHA512withRSA, publicKey, privateKey);
    }
}
