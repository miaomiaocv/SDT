package top.openyuan.sdt.keypair;

import top.openyuan.sdt.keypair.enums.KeyPairType;
import top.openyuan.sdt.keypair.model.KeyPair;

import java.security.NoSuchAlgorithmException;

/**
 * 抽象生成密钥对
 *
 * @author lzy
 */
public abstract class AbstractKeyPairGenerator implements KeyPairGenerator {

    /**
     * 生成密钥对
     *
     * @param keyPairType 密钥类型
     * @param keyLength     生成密钥长度
     * @return 密钥对
     * @throws IllegalArgumentException 如果提供的算法不支持或密钥长度小于等于0，抛出异常
     */
    public KeyPair createKeyPair(KeyPairType keyPairType, Integer keyLength) {
        if (keyLength <= 0) {
            throw new IllegalArgumentException("密钥长度必须大于0");
        }

        try {
            // 获取指定算法的密钥对生成器
            java.security.KeyPairGenerator keyGen = java.security.KeyPairGenerator.getInstance(keyPairType.name());
            // 初始化密钥生成器，并指定密钥长度为 keyLength 位
            keyGen.initialize(keyLength);
            // 生成密钥对
            java.security.KeyPair keyPair = keyGen.generateKeyPair();
            return new KeyPair(keyPair.getPublic(), keyPair.getPrivate());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("不支持该算法或提供者", e);
        }
    }
}
