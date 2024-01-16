package display.Status;

public class ResponseVO {
    private String code;
    private Object msg;

    public ResponseVO(String code, Object msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseVO() {
    }

    public String getCode() {
        return code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
