package sunnn.filehub.entity;

public enum  Word {

    Other(0),
    Img(1),
    Mov(2),
    Audio(3),
    Pdf(4),
    Doc(5),
    Zip(6);

    private int code;

    Word(int code) {
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
