package top.openyuan.sdt.http.model.data;

import top.openyuan.sdt.http.model.enums.MediaType;
import top.openyuan.sdt.util.GsonUtil;

/** JSON类型返回体 */
public final class JsonResponseBody implements ResponseBody {

  private final String body;

  private JsonResponseBody(String body) {
    this.body = body;
  }

  /**
   * 获取返回体
   *
   * @return 返回体
   */
  public String getBody() {
    return body;
  }

  @Override
  public String getContentType() {
    return MediaType.APPLICATION_JSON.getValue();
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }

  public static class Builder {

    private String body;

    public Builder body(String body) {
      this.body = body;
      return this;
    }

    public JsonResponseBody build() {
      return new JsonResponseBody(body);
    }
  }
}
