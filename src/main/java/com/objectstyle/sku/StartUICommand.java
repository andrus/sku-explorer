package com.objectstyle.sku;

import io.bootique.cli.Cli;
import io.bootique.command.Command;
import io.bootique.command.CommandOutcome;
import jakarta.inject.Provider;

public class StartUICommand implements Command {

    private final Provider<SkuExplorerUI> uiProvider;

    public StartUICommand(Provider<SkuExplorerUI> uiProvider) {
        this.uiProvider = uiProvider;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        try {
            uiProvider.get().run();
            return CommandOutcome.succeeded();
        } catch (Exception e) {
            return CommandOutcome.failed(-1, e);
        }
    }
}
