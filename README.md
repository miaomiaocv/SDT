[![JavaDoc](http://img.shields.io/badge/javadoc-reference-blue.svg)](https://www.javadoc.io/doc/com.github.wechatpay-apiv3/wechatpay-java/latest/index.html)

# SDT APIv1 Java SDK

[SDT APIv1](http://192.168.42.37/liangzhengyuan/SDT) 客户端开发库。

开发库由 `core` 和 `service` 组成：

- core 为基础库（可以扩展其他项目SDK），包含自动签名和验签的 HTTP 客户端、回调处理、加解密库。
- service 为业务服务，包含[业务接口](service/src/main/java/com/wechat/pay/java/service)和[使用示例](service/src/example/java/com/wechat/pay/java/service)。

## 前置条件

- Java 1.8+。
- [成为SDT商户]： 开通SDT商户账号。
- [SDT公钥]：开通SDT商户账号之后，会提供SDT公钥。
- [商户公钥]：需要自主生成公钥，sdk也提供生成公钥的方法，如下例子。
- [商户私钥]：需要自主生成私钥，sdk也提供生成私钥的方法，如下例子。

## 快速开始

### 安装

最新版本没有在 [Maven Central](https://search.maven.org/artifact/top.openyuan/SDT) 发布。

#### Gradle

预计路径：在你的 build.gradle 文件中加入如下的依赖

```groovy
implementation 'top.openyuan:SDT:0.0.1'
```

#### Maven

预计路径：加入以下依赖

```xml
<dependency>
  <groupId>top.openyuan</groupId>
  <artifactId>SDT</artifactId>
  <version>0.0.1</version>
</dependency>
```
### 生成商户密钥对方法
以生成商户的密钥对为例，先初始化 `HeLiPayKeyPairGenerator`SDT密钥生成器，再调用 `createKeyPair`方法即可生成密钥对。
```java
package top.openyuan.java.service.cipher;

import top.openyuan.java.core.cipher.HeLiPayKeyPairGenerator;
import top.openyuan.java.core.keypair.KeyPairGenerator;
import top.openyuan.java.core.keypair.model.KeyPair;
import org.junit.Test;

public class HeLiPayKeyPairGeneratorTest {
    @Test
    public static void createHeLiPayKeyPair() {
        // 初始化SDT密钥生成器
        KeyPairGenerator keyPairGenerator = new HeLiPayKeyPairGenerator();
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.createKeyPair();
        
        System.out.println(String.format("公钥：{}", keyPair.getPublicKey()));
        System.out.println(String.format("私钥：{}", keyPair.getPrivateKey()));
    }
}
```

### 调用业务请求接口

以SDT查询用户状态为例，先补充商户号等必要参数以构建 `config`，再构建 `service` 即可调用 `queryUser()` 发送请求。

```java
package top.openyuan.java.service.user;

import top.openyuan.java.core.exception.HttpException;
import top.openyuan.java.core.exception.MalformedMessageException;
import top.openyuan.java.core.exception.ServiceException;
import top.openyuan.java.core.config.HeLiPayConfig;
import top.openyuan.java.service.realname.UserService;
import top.openyuan.java.service.realname.model.QueryUserRequest;
import top.openyuan.java.service.realname.model.QueryUserResponse;

import static top.openyuan.java.core.http.model.enums.HostName.QA_API;

public class UserServiceTest {
    public static String appNo = "202207051436238006ks3k";
    public static String merchantNo = "C1800000002";
    public static String privateKeyStr="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALGdvRVucvnWYv4pPGaziuQ01lMvEz4S3ep7RgsGke6KAvI/Pufz5D2GQCcpUyPhJgU26RGLdUNDPWw6gp4TnnSjYSNm3ybCF6iVZ7l8f5rgjoeyBO83WwlkIVuT6FZwajk/yD9NFMfBu/96p5Kx5jyTF/CUvkXCMI98QfL5PaNDAgMBAAECgYACJX3bfHI3Qrf/ilAIjbLn/xt39eGtply4MLUv/OxWjaRreQgxlWj0tWKhFobCsD3dYkR+ycio/28Gl85sSqBnk0orjLKaWJRSV2oVvfZaJoYt4vZa3wl4go+S6N8ByeKpHzHYpC5YeXp0uHPFBoUF7OkLnHXTG2op1tVTPWJWwQJBANnoYFhEWXPt3x3BfOfKAPdfPQ/TBDjnM/rCtmiUJMqKKnT6c7xZc6jKoD5KT6Mb8O8GR4nSz6NGomswQyb04J0CQQDQqkS3SF0VtHqvG4T4PAeG0MZiw20hQENM5e1t8RJURoUkWXwKmDUfwNKoz9riKx8Oa/8uqF5yiyMMKuATtp1fAkBqTIODS4RfmzB2MYcfA1nJUrpU19l9cLvYndeh2HLCIvhnLC39OZ3EP2RAPrvuk5jK4UNQpngH7FMa+uYnQNm5AkAmSWesXZm+1su//4OpbJJQ+VO9YXBPrpPqszGaf6ZGUl4xqj2pT/5HYkEE+oaGAzWCluxOqlQSHleC7wcIdvRvAkAEsDrWITyINWa44xKjixyivm3AH8O4LmSzgdXw10POh22bibj2qJkwn7Dx3ym/7mwUlAPjS7Foo4loeJoBiRPz";
    public static String publicKeyStr="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxnb0VbnL51mL+KTxms4rkNNZTLxM+Et3qe0YLBpHuigLyPz7n8+Q9hkAnKVMj4SYFNukRi3VDQz1sOoKeE550o2EjZt8mwheolWe5fH+a4I6HsgTvN1sJZCFbk+hWcGo5P8g/TRTHwbv/eqeSseY8kxfwlL5FwjCPfEHy+T2jQwIDAQAB";
    public static UserService service;

    public static void main(String[] args) {
        // 初始化商户配置
        HeLiPayConfig config =
                new HeLiPayConfig.Builder()
                        .appNo(appNo)
                        .merchantNo(merchantNo)
                        .publicKey(publicKeyStr)
                        .privateKey(privateKeyStr)
                        .build();

        // 初始化服务
        service = new UserService.Builder().hostName(QA_API).config(config).build();
        // ... 调用接口
        try {
            QueryUserResponse response = queryUser();
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
        }
    }
    /** 查询用户状态 */
    public static QueryUserResponse queryUser() {
        QueryUserRequest request = new QueryUserRequest();
        request.setPhone("13560011906");
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        return service.queryUser(request);
    }
}


```

从示例可见，使用 SDK 不需要计算请求签名和验证应答签名。详细代码可从 [QuickStart](http://192.168.42.37/liangzhengyuan/SDT) 获得。

## 示例

### 查询支付订单

```java

```

### 关闭订单

```java

```

### 更多示例

为了方便开发者快速上手，SDT给每个服务生成了示例代码 `XxxServiceExample.java`，可以在 [example](service/src/example) 中查看。
例如：

- [JsapiServiceExtensionExample.java](service/src/example/java/com/wechat/pay/java/service/payments/jsapi/JsapiServiceExtensionExample.java)

## 错误处理

SDK 使用的是 unchecked exception，会抛出四种自定义异常。每种异常发生的场景及推荐的处理方式如下：

- [HttpException](core/src/main/java/com/wechat/pay/java/core/exception/HttpException.java)：调用SDT服务，当发生 HTTP 请求异常时抛出该异常。
    - 构建请求参数失败、发送请求失败、I/O错误：推荐上报监控和打印日志，并获取异常中的 HTTP 请求信息以定位问题。
- [ValidationException](core/src/main/java/com/wechat/pay/java/core/exception/ValidationException.java) ：当验证SDT签名失败时抛出该异常。
    - 验证SDT返回签名失败：上报监控和日志打印。
    - 验证SDT回调通知签名失败：确认输入参数与 HTTP 请求信息是否一致，若一致，说明该回调通知参数被篡改导致验签失败。
- [ServiceException](core/src/main/java/com/wechat/pay/java/core/exception/ServiceException.java)：调用SDT服务，发送 HTTP 请求成功，HTTP 状态码小于200或大于等于300。
    - 状态码为5xx：主动重试。
    - 状态码为其他：获取错误中的 `errorCode` 、`errorMessage`，上报监控和日志打印。
- [MalformedMessageException](core/src/main/java/com/wechat/pay/java/core/exception/MalformedMessageException.java)：服务返回成功，返回内容异常。
    - HTTP 返回 `Content-Type` 不为 `application/json`：不支持其他类型的返回体，[下载账单](#下载账单) 应使用 `download()` 方法。
    - 解析 HTTP 返回体失败：上报监控和日志打印。
    - 回调通知参数不正确：确认传入参数是否与 HTTP 请求信息一致，传入参数是否存在编码或者 HTML 转码问题。
    - 解析回调请求体为 JSON 字符串失败：上报监控和日志打印。
    - 解密回调通知内容失败：确认传入的 apiV3 密钥是否正确。


## 发送 HTTP 请求

如果 SDK 未支持你需要的接口，你可以使用 [OkHttpClientAdapter](core/src/main/java/com/wechat/pay/java/core/http/okhttp/OkHttpClientAdapter.java) 的实现类发送 HTTP 请求，它会自动生成签名和验证签名。

发送请求步骤如下：

1. 初始化 `OkHttpClientAdapter`，建议使用 `DefaultHttpClientBuilder` 构建。
2. 构建请求 `HttpRequest`。
3. 调用 `httpClient.execute` 或者 `httpClient.get` 等方法来发送 HTTP 请求。`httpClient.execute` 支持发送 GET、PUT、POST、PATCH、DELETE 请求，也可以调用指定的 HTTP 方法发送请求。

[OkHttpClientAdapterTest](core/src/test/java/com/wechat/pay/java/core/http/OkHttpClientAdapterTest.java) 中演示了如何构造和发送 HTTP 请求。如果现有的 `OkHttpClientAdapter` 实现类不满足你的需求，可以继承 [AbstractHttpClient](core/src/main/java/com/wechat/pay/java/core/http/AbstractHttpClient.java) 拓展实现。



> **Warning**
>
> 开发者在下载文件之后，应使用第一步获取的账单摘要校验文件的完整性。

## 敏感信息加解密

为了保证通信过程中敏感信息字段（如用户的住址、银行卡号、手机号码等）的机密性，

- SDT要求加密上送的敏感信息
- SDT会加密下行的敏感信息

详见 [接口规则 - 敏感信息加解密](https://pay.weixin.qq.com/docs/merchant/development/interface-rules/sensitive-data-encryption.html)。

### 自动加解密

如果是 SDK 已支持的接口，例如商家转账，SDK 将根据契约自动对敏感信息做加解密：

- 发起请求时，开发者设置原文。SDK 自动加密敏感信息，并设置 `Wechatpay-Serial` 请求头
- 收到应答时，解密器自动解密敏感信息，开发者得到原文

### 手动加解密

如果是 SDK 尚未支持的接口，你可以使用 [cipher](core/src/main/java/com/wechat/pay/java/core/cipher) 中的 `RSAPrivacyEncryptor` 和 `RSAPrivacyDecryptor` ，手动对敏感信息加解密。

```java
// SDT平台证书中的公钥
PublicKey wechatPayPublicKey = null;
String plaintext = "";
PrivacyEncryptor encryptor = new RSAPrivacyEncryptor(wechatPayPublicKey);
String ciphertext = encryptor.encryptToString(plaintext);
```

```java
// 商户私钥
PrivateKey merchantPrivateKey = null;
String ciphertext = "";
PrivacyDecryptor decryptor = new RSAPrivacyDecryptor(merchantPrivateKey);
String plaintext = decryptor.decryptToString(ciphertext);
```

[RSAPrivacyEncryptorTest](core/src/test/java/com/wechat/pay/java/core/cipher/RSAPrivacyEncryptorTest.java) 和 [RSAPrivacyDecryptorTest](core/src/test/java/com/wechat/pay/java/core/cipher/RSAPrivacyDecryptorTest.java) 中演示了如何使用以上函数做敏感信息加解密。

## 日志

SDK 使用了 [SLF4j](http://www.slf4j.org/) 作为日志框架的接口。这样，你可以使用你熟悉的日志框架，例如 [Logback](https://logback.qos.ch/documentation.html)、[Log4j2](https://github.com/apache/logging-log4j2) 或者 [SLF4j-simple](https://www.slf4j.org/manual.html)。
SDK 的日志会跟你的日志记录在一起。

为了启用日志，你应在你的构建脚本中添加日志框架的依赖。如果不配置日志框架，默认是使用 SLF4j 提供的 空（NOP）日志实现，它不会记录任何日志。

## 网络配置

SDK 使用 [OkHttp](https://square.github.io/okhttp/) 作为默认的 HTTP 客户端。
如果开发者不熟悉 OkHttp，推荐使用 SDK 封装的 DefaultHttpClientBuilder 来构造 HTTP 客户端。

目前支持的网络配置方法见下表。

| 方法                                  | 说明                     | 默认值          | 更多信息                                                                                                                                                      |
|-------------------------------------|------------------------|--------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| `readTimeoutMs()`                   | 设置新连接的默认读超时            | 10*1000(10秒） | [OkHttpClient/Builder/readTimeout](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/-builder/read-timeout/)                             |
| `writeTimeoutMs()`                  | 设置新连接的默认写超时            | 10*1000(10秒) | [OkHttpClient/Builder/writeTimeout](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/-builder/write-timeout/)                           |
| `connectTimeoutMs()`                | 设置新连接的默认连接超时           | 10*1000(10秒） | [OkHttpClient/Builder/connectTimeout](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/-builder/connect-timeout/)                       |
| `proxy()`                           | 设置客户端创建的连接时使用的 HTTP 代理 | 无            | [OkHttpClient/Builder/proxy](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/-builder/proxy/)                                          |
| `disableRetryOnConnectionFailure()` | 遇到网络问题时不重试下一个 IP       | 默认重试         | [OkHttpClient/Builder/retryOnConnectionFailure](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/-builder/retry-on-connection-failure/) |
| `enableRetryMultiDomain()`          | 遇到网络问题时重试备域名           | 默认不重试        | 推荐开启，详细说明见下                                                                                                                                               |

下面的示例演示了如何使用 DefaultHttpClientBuilder 初始化某个具体的业务 Service。

```java
HttpClient httpClient =
    new DefaultHttpClientBuilder()
        .config(config)
        .connectTimeoutMs(500)
        .build();

// 以JsapiService为例，使用 httpclient 初始化 service
JsapiService service = new JsapiService.Builder().httpclient(httpClient).build();
```

更多网络配置的说明，请看 [wiki - 网络配置](https://github.com/wechatpay-apiv3/wechatpay-java/wiki/SDK-%E9%85%8D%E7%BD%AE%E8%AF%A6%E8%A7%A3#%E7%BD%91%E7%BB%9C%E9%85%8D%E7%BD%AE)。

### 双域名容灾

为提升商户系统访问 API 的稳定性，SDK 实现了双域名容灾。如果主要域名 `api.mch.weixin.qq.com` 因网络问题无法访问，我们的 SDK 可自动切换到备用域名 `api2.wechatpay.cn` 重试当前请求。
这个机制可以最大限度减少因SDT API 接入点故障或主域名问题(如 DNS 劫持)对商户系统的影响。

默认情况下，双域名容灾机制处于关闭状态，以避免重试降低商户系统的吞吐量。因为 OkHttp 默认会尝试主域名的多个IP地址(目前为2个)，增加备用域名重试很可能会提高异常情况下的处理时间。

我们推荐开发者使用 `disableRetryOnConnectionFailure` 和 `enableRetryMultiDomain` 的组合，启用双域名容灾并关闭 OkHttp 默认重试，这样不会增加重试次数。

假设 `api.mch.weixin.qq.com` 解析得到 [ip1a, ip1b]，`api2.wechatpay.cn` 解析得到 [ip2a, ip2b]，不同的重试策略组合对应的尝试顺序为：

- 默认：[ip1a, ip1b]
- disableRetryOnConnectionFailure：[ip1a]
- enableRetryMultiDomain：[ipa1, ip1b, ip2a, ip2b]
- （推荐）disableRetryOnConnectionFailure + enableRetryMultiDomain: [ip1a, ip2a]

以下是采用推荐重试策略的示例代码：

```java
// 开启双域名重试，并关闭 OkHttp 默认的连接失败后重试
HttpClient httpClient =
    new DefaultHttpClientBuilder()
        .config(config)
        .disableRetryOnConnectionFailure()
        .enableRetryMultiDomain()
        .build();

// 以JsapiService为例，使用 httpclient 初始化 service
JsapiService service = new JsapiService.Builder().httpclient(httpClient).build();
```

开发者应该仔细评估自己的商户系统容量，根据自身情况选择合适的超时时间和重试策略，并做好监控和告警。


```java
PrivacyEncryptor encryptor = config.createEncryptor();
String wechatPayCertificateSerialNumber = encryptor.getWechatpaySerial();
```

## 如何开发其他项目的sdk：依赖helipay-sdk-core包即可
### 1.依赖helipay-sdk-core包

### 2.签名验签器；也可以使用默认的。
以SDT签名验证器为例， 通过继承helipay-sdk-core：AbstractSignVerifyExecutor， 配置SDT签名验签算法即可；本例使用MD5withRSA算法。
```java
package top.openyuan.java.core.signverify;

import top.openyuan.java.core.signverify.enums.SignatureType;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * SDT签名验签器 
 * 
 * @author lzy 
 */
public class HeLiPaySignVerifyExecutor extends AbstractSignVerifyExecutor{
    /**
     * 初始化SDT签名验签器 
     *
     * @param publicKey SDT的公钥
     * @param privateKey 商户的私钥
     */
    public HeLiPaySignVerifyExecutor(PublicKey publicKey, PrivateKey privateKey) {
        super(SignatureType.MD5withRSA, publicKey, privateKey);
    }
}

```
### 3.实现请求凭证器和响应认证器。
```java
package top.openyuan.java.core.auth;

import com.alibaba.fastjson2.JSONObject;
import top.openyuan.java.core.cipher.AsymmetricCipherExecutor;
import top.openyuan.java.core.cipher.SymmetricCipherExecutor;
import top.openyuan.java.core.cipher.enums.SecretKeyType;
import top.openyuan.java.core.http.model.HttpRequest;
import top.openyuan.java.core.http.model.data.JsonRequestBody;
import top.openyuan.java.core.signverify.SignVerifyExecutor;
import top.openyuan.java.core.util.NonceUtil;

import javax.crypto.spec.SecretKeySpec;

/** SDT请求凭据生成器 */
public final class HeLiPayCredential extends AbstractCredential {
  private final String appNo;
  private final String merchantNo;
  private final SymmetricCipherExecutor symmetricCipherExecutor;
  private final AsymmetricCipherExecutor asymmetricCipherExecutor;
  private final SignVerifyExecutor signVerifyExecutor;

  public HeLiPayCredential(String appNo, String merchantNo,
                           SymmetricCipherExecutor symmetricCipherExecutor,
                           AsymmetricCipherExecutor asymmetricCipherExecutor,
                           SignVerifyExecutor signVerifyExecutor){
    this.appNo = appNo;
    this.merchantNo = merchantNo;
    this.symmetricCipherExecutor = symmetricCipherExecutor;
    this.asymmetricCipherExecutor = asymmetricCipherExecutor;
    this.signVerifyExecutor = signVerifyExecutor;
  }

  @Override
  public HttpRequest assembleHttpRequest(HttpRequest httpRequest) {
    String body = getBody(httpRequest.getBody());

    String aesKey = NonceUtil.createNonce(16);
    SecretKeySpec aesKeySpec = new SecretKeySpec(aesKey.getBytes(), SecretKeyType.AES.name());

    String encryptData = symmetricCipherExecutor.encrypt(body, aesKeySpec);
    String sign = signVerifyExecutor.sign(encryptData);

    JSONObject reqBaseBody = new JSONObject();
    reqBaseBody.fluentPut("data", encryptData);
    reqBaseBody.fluentPut("encryptionKey", asymmetricCipherExecutor.encrypt(aesKey));
    reqBaseBody.fluentPut("sign", sign);
    reqBaseBody.fluentPut("signType", "RSA");
    reqBaseBody.fluentPut("appNo", appNo);
    reqBaseBody.fluentPut("parentMerchantNo", merchantNo);

    JsonRequestBody newBody = new JsonRequestBody.Builder().body(reqBaseBody.toJSONString()).build();

    return new HttpRequest.Builder()
            .url(httpRequest.getUrl())
            .headers(httpRequest.getHeaders())
            .httpMethod(httpRequest.getHttpMethod())
            .body(newBody)
            .build();
  }
}

```
### 4.实现项目所需要的配置接口。
```java
package top.openyuan.java.core.config;

import top.openyuan.java.core.auth.Credential;
import top.openyuan.java.core.auth.HeLiPayCredential;
import top.openyuan.java.core.auth.HeLiPayValidator;
import top.openyuan.java.core.auth.Validator;
import top.openyuan.java.core.cipher.AsymmetricCipherExecutor;
import top.openyuan.java.core.cipher.HeLiPayAESCipherExecutor;
import top.openyuan.java.core.cipher.HeLiPayRSACipherExecutor;
import top.openyuan.java.core.cipher.SymmetricCipherExecutor;
import top.openyuan.java.core.signverify.HeLiPaySignVerifyExecutor;
import top.openyuan.java.core.signverify.SignVerifyExecutor;
import top.openyuan.java.core.util.PemUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import static top.openyuan.java.core.keypair.enums.KeyPairType.RSA;

/**
 * 合力付配置文件
 *
 * @author lzy
 **/
public class HeLiPayConfig extends AbstractConfig {
  private final String appNo;
  private final String merchantNo;
  private final PublicKey publicKey;
  private final PrivateKey privateKey;
  private static volatile HeLiPayConfig instance;

  protected HeLiPayConfig(String appNo, String merchantNo, PublicKey publicKey, PrivateKey privateKey) {
    super(publicKey, privateKey);
    this.appNo = appNo;
    this.merchantNo = merchantNo;
    this.publicKey = publicKey;
    this.privateKey = privateKey;
  }

  public static HeLiPayConfig getInstance(String appNo, String merchantNo, PublicKey publicKey, PrivateKey privateKey) {
    if (instance == null) {
      synchronized (HeLiPayConfig.class) {
        if (instance == null) {
          instance = new HeLiPayConfig(appNo, merchantNo, publicKey, privateKey);
        }
      }
    }
    return instance;
  }

  @Override
  public SymmetricCipherExecutor createSymmetricCipherExecutor() {
    return new HeLiPayAESCipherExecutor();
  }

  @Override
  public AsymmetricCipherExecutor createAsymmetricCipherExecutor() {
    return new HeLiPayRSACipherExecutor(publicKey, privateKey);
  }

  @Override
  public SignVerifyExecutor createSignVerifyExecutor() {
    return new HeLiPaySignVerifyExecutor(publicKey, privateKey);
  }

  @Override
  public Credential createCredential() {
    return new HeLiPayCredential(appNo, merchantNo,
            createSymmetricCipherExecutor(), createAsymmetricCipherExecutor(), createSignVerifyExecutor());
  }

  @Override
  public Validator createValidator() {
    return new HeLiPayValidator(createSymmetricCipherExecutor(), createAsymmetricCipherExecutor(), createSignVerifyExecutor());
  }

  public static class Builder extends AbstractConfigBuilder<Builder> {
    static {
      Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    protected String appNo;
    protected String merchantNo;

    public Builder appNo(String appNo) {
      this.appNo = appNo;
      return this;
    }
    public Builder merchantNo(String merchantNo) {
      this.merchantNo = merchantNo;
      return this;
    }

    public Builder publicKey(String publicKey) {
      this.publicKey = PemUtil.loadPublicKeyFromString(publicKey, RSA.name(), "BC");
      return this;
    }

    public Builder privateKey(String privateKey) {
      this.privateKey = PemUtil.loadPrivateKeyFromString(privateKey, RSA.name(), "BC");
      return this;
    }

    @Override
    protected Builder self() {
      return this;
    }

    public HeLiPayConfig build(){
      return HeLiPayConfig.getInstance(appNo, merchantNo, publicKey, privateKey);
    }
  }
}

```
