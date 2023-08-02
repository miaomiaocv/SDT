package top.openyuan.sdt.exception;

public class DecryptionException extends HeliPayException {
  private static final long serialVersionUID = 1L;

  public DecryptionException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
