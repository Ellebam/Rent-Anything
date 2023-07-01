package io.bootify.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {

    private static final String TAG = "[RENT_ANYTHING_BACKEND]";
    private Logger logger;
    private boolean enabled = false; // set to false by default

    public LogHelper(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void info(String message, Object... args) {
        logger.info(formatMessage(message), args);
    }

    public void error(String message, Object... args) {
        logger.error(formatMessage(message), args);
    }

    private String formatMessage(String message) {
        if (enabled) {
            return TAG + " " + message;
        } else {
            return message;
        }
    }
}
