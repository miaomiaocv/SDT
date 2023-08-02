package top.openyuan.sdt.config;

import java.security.PrivateKey;
import java.security.PublicKey;

/** RSAConfig抽象类 */
public abstract class AbstractConfig implements Config {
  private final PublicKey publicKey;
  private final PrivateKey privateKey;

  protected AbstractConfig(PublicKey publicKey, PrivateKey privateKey) {
    this.publicKey = publicKey;
    this.privateKey = privateKey;
  }

}
