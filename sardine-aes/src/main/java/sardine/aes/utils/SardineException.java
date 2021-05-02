package sardine.aes.utils;

import lombok.Getter;

/**
 * @author keith
 */
@Getter
public class SardineException extends RuntimeException {

    public static final String INPUT_ERROR = "InputError";

    public static final String RUNTIME_ERROR = "RuntimeError";

    public static final String ENV_ERROR = "EnvironmentError";

    public static final String EXEC_ERROR = "ExecuteError";

    public static final String KEY_MISSING = "KeyMissingError";

    public static final String SYS_ERROR = "SystemError";

    private final String errCode;

    public SardineException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }
}