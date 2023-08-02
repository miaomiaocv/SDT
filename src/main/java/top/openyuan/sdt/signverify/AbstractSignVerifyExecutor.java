package top.openyuan.sdt.signverify;

import top.openyuan.sdt.signverify.enums.SignatureType;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

import static java.util.Objects.requireNonNull;

/**
 * 抽象签名验签执行器
 *
 * @author lzy
 */
public abstract class AbstractSignVerifyExecutor implements SignVerifyExecutor {
    private final SignatureType signatureType;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    /**
     * AbstractSigner 构造函数
     *
     * @param signatureType  获取Signature对象时指定的算法，例如SHA256withRSA
     * @param publicKey  商户API公钥
     * @param privateKey 商户API私钥
     */
    protected AbstractSignVerifyExecutor(SignatureType signatureType, PublicKey publicKey, PrivateKey privateKey) {
        this.signatureType = requireNonNull(signatureType);
        this.publicKey = requireNonNull(publicKey);
        this.privateKey = requireNonNull(privateKey);
    }

    /**
     * 签名
     *
     * @param message 签名信息
     * @return 签名后的Base64编码字符串
     * @throws RuntimeException 如果签名过程中发生错误
     */
    @Override
    public String sign(String message) {
        requireNonNull(message);
        try {
            Signature signature = Signature.getInstance(signatureType.name());
            signature.initSign(privateKey);
            signature.update(message.getBytes(StandardCharsets.UTF_8));
            byte[] signBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("当前Java环境不支持 " + signatureType, e);
        } catch (InvalidKeyException | SignatureException e) {
            throw new RuntimeException("签名过程中发生错误", e);
        }
    }

    /**
     * 验签
     *
     * @param message   签名信息
     * @param signature 签名的Base64编码字符串
     * @return true 验签成功， false 验签失败
     */
    @Override
    public boolean verify(String message, String signature) {
        requireNonNull(message);
        requireNonNull(signature);
        try {
            Signature sign = Signature.getInstance(signatureType.name());
            sign.initVerify(publicKey);
            sign.update(message.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return sign.verify(signatureBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("当前Java环境不支持 " + signatureType, e);
        } catch (InvalidKeyException | SignatureException e) {
            throw new IllegalArgumentException("验证签名时发生错误", e);
        }
    }
}
