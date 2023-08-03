package top.openyuan.sdt.auth;

import top.openyuan.sdt.http.model.OriginalResponse;

import static java.net.HttpURLConnection.HTTP_MULT_CHOICE;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * 抽象响应数据验证器
 *
 * @author lzy
 */
public abstract class AbstractValidator implements Validator {

    /**
     * 定义验证业务请求码抽象方法
     *
     * @param originalResponse 响应源
     */
    public abstract void validHttpCode(OriginalResponse originalResponse);

    /**
     * 校验请求状态码
     *
     * @param httpCode 请求验证码
     * @return boolean 是否成功请求
     */
    protected boolean isInvalidHttpCode(int httpCode) {
        return httpCode < HTTP_OK || httpCode >= HTTP_MULT_CHOICE;
    }
}
