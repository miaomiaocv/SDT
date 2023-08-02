package top.openyuan.sdt.keypair;

import top.openyuan.sdt.keypair.model.KeyPair;

public interface KeyPairGenerator {
    /**
     * 生成密钥对
     *
     * @return KeyPair 密钥对
     */
    KeyPair createKeyPair();
}
