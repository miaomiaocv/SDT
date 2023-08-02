package top.openyuan.sdt.keypair;

import top.openyuan.sdt.keypair.enums.KeyPairType;
import top.openyuan.sdt.keypair.model.KeyPair;

public class RSAKeyPairGenerator extends AbstractKeyPairGenerator {
    /**
     * 生成2048位RSA密钥对
     *
     * @return 密钥对
     */
    @Override
    public KeyPair createKeyPair() {
        return createKeyPair(KeyPairType.RSA, 2048);
    }
}
