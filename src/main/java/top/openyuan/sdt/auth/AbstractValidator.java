package top.openyuan.sdt.auth;

import top.openyuan.sdt.exception.ServiceException;
import top.openyuan.sdt.http.model.OriginalResponse;

import static java.net.HttpURLConnection.HTTP_MULT_CHOICE;
import static java.net.HttpURLConnection.HTTP_OK;

/** 抽象响应数据验证器 */
public abstract class AbstractValidator implements Validator {

    protected void validHttpCode(OriginalResponse originalResponse){
        if (isInvalidHttpCode(originalResponse.getStatusCode())) {
            throw new ServiceException(originalResponse.getRequest(),
                    originalResponse.getStatusCode(),
                    originalResponse.getBody());
        }
    }

    protected boolean isInvalidHttpCode(int httpCode) {
        return httpCode < HTTP_OK || httpCode >= HTTP_MULT_CHOICE;
    }
}
