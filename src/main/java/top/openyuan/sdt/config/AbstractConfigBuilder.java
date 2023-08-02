package top.openyuan.sdt.config;

import top.openyuan.sdt.util.PemUtil;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * ConfigBuilder抽象类
 *
 * @author lzy
 */
public abstract class AbstractConfigBuilder<T extends AbstractConfigBuilder<T>> {
  protected PrivateKey privateKey;
  protected PublicKey publicKey;

  protected abstract T self();

  public T privateKey(String privateKey) {
    this.privateKey = PemUtil.loadPrivateKeyFromString(privateKey);
    return self();
  }

  public T privateKeyFromPath(String keyPath) {
    this.privateKey = PemUtil.loadPrivateKeyFromPath(keyPath);
    return self();
  }

  public T privateKey(String privateKey, String algorithm, String provider) {
    this.privateKey = PemUtil.loadPrivateKeyFromString(privateKey, algorithm, provider);
    return self();
  }

  public T privateKeyFromPath(String keyPath, String algorithm, String provider) {
    this.privateKey = PemUtil.loadPrivateKeyFromPath(keyPath, algorithm, provider);
    return self();
  }

  public T publicKey(String publicKey) {
    this.publicKey = PemUtil.loadPublicKeyFromString(publicKey);
    return self();
  }

  public T publicKeyFromPath(String keyPath) {
    this.publicKey = PemUtil.loadPublicKeyFromPath(keyPath);
    return self();
  }

  public T publicKey(String publicKey, String algorithm, String provider) {
    this.publicKey =PemUtil.loadPublicKeyFromString(publicKey, algorithm, provider);
    return self();
  }

  public T publicKeyFromPath(String keyPath, String algorithm, String provider) {
    this.publicKey = PemUtil.loadPublicKeyFromPath(keyPath, algorithm, provider);
    return self();
  }
}
