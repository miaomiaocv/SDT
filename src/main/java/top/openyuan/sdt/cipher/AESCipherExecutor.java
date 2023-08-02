package top.openyuan.sdt.cipher;

import top.openyuan.sdt.cipher.enums.CipherType;
import top.openyuan.sdt.cipher.model.CipherAlgorithm;

/**
 * 非对称加密执行器
 *
 * @author lzy
 */
public abstract class AESCipherExecutor extends AbstractSymmetricCipherExecutor{
    /**
     * 构造函数
     */
    protected AESCipherExecutor() {
        super(new CipherAlgorithm(CipherType.AES_CBC_PKCS5Padding));
    }
}
