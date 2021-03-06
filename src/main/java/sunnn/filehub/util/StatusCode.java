package sunnn.filehub.util;

public enum StatusCode {

    OJBK(0),
    ERROR(-1),
    VERIFY_FAILED(1 << 0),
    ILLEGAL_REQUEST(1 << 1),
    NO_ACCESS(1 << 2);

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
