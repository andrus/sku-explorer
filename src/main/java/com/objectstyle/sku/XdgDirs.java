package com.objectstyle.sku;

import java.nio.file.Path;

/**
 * Maps "XDG Base Directory Specification" folders for the app
 */
public class XdgDirs {

    public static Path stateDir() {
        // TODO: respect $XDG_STATE_HOME
        return Path.of(System.getProperty("user.home")).resolve(".local", "state", SkuExplorerApp.APP_NAME);
    }
}
