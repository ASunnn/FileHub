package sunnn.filehub.util;

public enum StatusCode {

    OJBK(0),
    ERROR(-1),
    VERIFY_FAILED(1 << 0);

    private int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ":" + this.name();
    }
}
