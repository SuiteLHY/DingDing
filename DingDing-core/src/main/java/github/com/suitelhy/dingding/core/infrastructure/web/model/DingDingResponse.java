package github.com.suitelhy.dingding.core.infrastructure.web.model;

/**
 * @Description 项目自定义 Response 响应格式.
 */
public class DingDingResponse {

    private Object content;

    //===== Constructor =====//

    public DingDingResponse(Object content) {
        this.content = content;
    }

    //===== Getter And Setter =====//

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

}
