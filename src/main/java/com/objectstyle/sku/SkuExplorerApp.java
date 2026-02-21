package com.objectstyle.sku;

import io.bootique.BQCoreModule;
import io.bootique.BQModule;
import io.bootique.Bootique;
import io.bootique.di.Binder;
import io.bootique.di.Provides;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

public class SkuExplorerApp implements BQModule {

    static void main(String[] args) {
        Bootique.main(args);
    }

    @Override
    public void configure(Binder binder) {
        BQCoreModule.extend(binder)
                .setDefaultCommand(StartUICommand.class)
                .addConfig("classpath:config.yml");
    }

    @Provides
    @Singleton
    StartUICommand startUICommand(Provider<SkuExplorerUI> uiProvider) {
        return new StartUICommand(uiProvider);
    }

    @Provides
    @Singleton
    SkuExplorerUI provideUi(SkuDAO skuDAO) {
        return new SkuExplorerUI(skuDAO);
    }

    @Provides
    @Singleton
    SkuDAO skuDAO() {
        return new SkuDAO();
    }
}
