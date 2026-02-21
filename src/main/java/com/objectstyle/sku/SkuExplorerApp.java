package com.objectstyle.sku;

import com.objectstyle.sku.dao.SkuDAO;
import io.bootique.BQCoreModule;
import io.bootique.BQModule;
import io.bootique.Bootique;
import io.bootique.di.Binder;
import io.bootique.di.Provides;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

public class SkuExplorerApp implements BQModule {

    static final String APP_NAME = "sku-explorer";

    static void main(String[] args) {
        Bootique.main(args);
    }

    @Override
    public void configure(Binder binder) {
        BQCoreModule.extend(binder)
                .setDefaultCommand(StartUICommand.class)
                .setApplicationDescription("A terminal UI app for managing ObjectStyle SKU dataset")

                // logging
                .setProperty("bq.log.appenders[0].type", "file")
                .setProperty("bq.log.appenders[0].file", XdgDirs.stateDir().resolve("app.log").toAbsolutePath().toString());
    }

    @Provides
    @Singleton
    StartUICommand startUICommand(Provider<SkuDAO> skuDAO) {
        return new StartUICommand(skuDAO);
    }

    @Provides
    @Singleton
    SkuDAO skuDAO() {
        return new SkuDAO();
    }
}
