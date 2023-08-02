package top.openyuan.sdt.config;

import top.openyuan.sdt.auth.Credential;
import top.openyuan.sdt.auth.Validator;
import top.openyuan.sdt.cipher.AsymmetricCipherExecutor;
import top.openyuan.sdt.cipher.SymmetricCipherExecutor;
import top.openyuan.sdt.signverify.SignVerifyExecutor;

/**
 * 调用合利宝支付服务的所需配置
 *
 * @author lzy
 */
public interface Config {

  /**
   * 创建对称加解密执行器
   *
   * @return 加解密执行器
   */
  SymmetricCipherExecutor createSymmetricCipherExecutor();

  /**
   * 创建非对称加解密执行器
   *
   * @return 加解密执行器
   */
  AsymmetricCipherExecutor createAsymmetricCipherExecutor();

  /**
   * 创建签名验签执行器
   *
   * @return 签名验签执行器
   */
  SignVerifyExecutor createSignVerifyExecutor();

  /**
   * 创建认证凭据生成器
   *
   * @return 认证凭据生成器
   */
  Credential createCredential();

  /**
   * 创建请求验证器
   *
   * @return 请求验证器
   */
  Validator createValidator();
}
