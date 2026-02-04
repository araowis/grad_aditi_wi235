package context;

public enum Mode {
    ADMIN("-a"),
    OWNER("-o"),
    GENERAL("-g");

    private final String flag;

    Mode(String flag) {
        this.flag = flag;
    }

    public static Mode fromArg(String arg) {
        for (Mode mode : values()) {
            if (mode.flag.equals(arg)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown option: " + arg);
    }
}
