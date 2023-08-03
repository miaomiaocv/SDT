package top.openyuan.sdt.auth;

import top.openyuan.sdt.http.model.data.FileRequestBody;
import top.openyuan.sdt.http.model.data.JsonRequestBody;
import top.openyuan.sdt.http.model.data.RequestBody;

/** 抽象请求凭据生成器
 *
 * @author lzy
 */
public abstract class AbstractCredential implements Credential {
    /**
     * 获取body对应的数据
     *
     * @param requestBody 请求body
     * @return body字符串
     */
    protected String getBody(RequestBody requestBody) {
        if (requestBody == null) {
            return "";
        }
        if (requestBody instanceof JsonRequestBody) {
            return ((JsonRequestBody) requestBody).getBody();
        }
        if (requestBody instanceof FileRequestBody) {
            return ((FileRequestBody) requestBody).getMeta();
        }
        throw new UnsupportedOperationException(
                String.format("Unsupported RequestBody Type[%s]", requestBody.getClass().getName()));
    }
}
