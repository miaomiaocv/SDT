package top.openyuan.sdt.http.model.data;

/** HTTP请求体 */
public interface RequestBody {

  /**
   * 获取请求体的数据类型
   *
   * @return 请求体的数据类型
   */
  String getContentType();
}
