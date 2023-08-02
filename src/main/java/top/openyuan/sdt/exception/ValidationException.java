package top.openyuan.sdt.exception;

/** 验证签名失败时抛出 */
public class ValidationException extends HeliPayException {

  private static final long serialVersionUID = -5439484392712167452L;

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
