package top.openyuan.sdt.exception;

/** 解析合利宝支付应答或回调报文异常时抛出，例如回调通知参数不正确、应答类型错误。 */
public class MalformedMessageException extends HeliPayException {

  private static final long serialVersionUID = -1049702516796430238L;

  public MalformedMessageException(String message) {
    super(message);
  }

  public MalformedMessageException(String message, Throwable cause) {
    super(message, cause);
  }
}
