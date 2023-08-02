package top.openyuan.sdt.exception;

/** 合利付异常基类 */
public abstract class HeliPayException extends RuntimeException {

  private static final long serialVersionUID = -5896431877288268263L;

  public HeliPayException(String message) {
    super(message);
  }

  public HeliPayException(String message, Throwable cause) {
    super(message, cause);
  }
}
