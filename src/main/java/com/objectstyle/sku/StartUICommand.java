package com.objectstyle.sku;

import com.objectstyle.sku.dao.SkuDAO;
import com.objectstyle.sku.ui.RootController;
import io.bootique.cli.Cli;
import io.bootique.command.Command;
import io.bootique.command.CommandOutcome;
import jakarta.inject.Provider;

public class StartUICommand implements Command {

    private final Provider<SkuDAO> skuDAO;

    public StartUICommand(Provider<SkuDAO> skuDAO) {
        this.skuDAO = skuDAO;
    }

    @Override
    public CommandOutcome run(Cli cli) {
        try {
            new RootController(skuDAO.get()).run();
            return CommandOutcome.succeeded();
        } catch (Exception e) {
            return CommandOutcome.failed(-1, e);
        }
    }
}
