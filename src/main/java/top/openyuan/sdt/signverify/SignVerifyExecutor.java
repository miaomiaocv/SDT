package top.openyuan.sdt.signverify;

public interface SignVerifyExecutor {
    /**
     * 生成签名结果
     *
     * @param message 签名信息
     * @return 签名结果
     */
    String sign(String message);

    /**
     * 验证签名
     *
     * @param message 签名信息
     * @param signature 待验证的签名
     * @return 是否验证通过
     */
    boolean verify(String message, String signature);
}
