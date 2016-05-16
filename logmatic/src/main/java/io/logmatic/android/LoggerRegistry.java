package io.logmatic.android;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of all logger references
 */
public class LoggerRegistry {

    private static Map<String, Logger> loggers = new HashMap();


    /**
     * Broadcast the network status to all loggers
     *
     * @param isConnected True if the network is available
     */
    public static void updateNetworkStatus(boolean isConnected) {
        for (Map.Entry<String, Logger> entry : loggers.entrySet()) {
            Log.i(LoggerRegistry.class.getSimpleName(), "Notify network event to '" + entry.getKey() + " logger");
            entry.getValue().getAppender().updateNetworkStatus(isConnected);
        }
    }

    /**
     * Store the logger reference
     **/
    public static void register(String name, Logger logger) {
        loggers.put(name, logger);
    }

    /**
     * Get a already instantiated logger
     *
     * @param name the logger name
     * @return the desired logger
     */
    public static Logger getLogger(String name) {
        return loggers.get(name);
    }

    /**
     * Get the default instantiated logger
     *
     * @return the default logger
     */
    public static Logger getDefaultLogger() {
        return getLogger(LoggerBuilder.DEFAULT_LOGGERNAME);
    }
}
