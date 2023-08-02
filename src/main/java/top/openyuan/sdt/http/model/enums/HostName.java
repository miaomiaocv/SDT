package top.openyuan.sdt.http.model.enums;

import static java.util.Objects.requireNonNull;

/** 合利宝支付域名 */
public enum HostName {
  API("helifu-trx.helipay.com"),
  QA_API("localhost:8082");
  private final String value;

  HostName(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public boolean equalsWith(String string) {
    requireNonNull(string);
    return string.startsWith(value);
  }
}
