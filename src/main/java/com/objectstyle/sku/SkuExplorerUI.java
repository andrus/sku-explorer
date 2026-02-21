package com.objectstyle.sku;

import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.ListElement;
import org.dflib.DataFrame;

import java.util.ArrayList;
import java.util.List;

import static dev.tamboui.toolkit.Toolkit.*;

public class SkuExplorerUI extends ToolkitApp {

    private final SkuDAO skuDAO;

    private ListElement<?> skus;
    private Element root;

    public SkuExplorerUI(SkuDAO skuDAO) {
        this.skuDAO = skuDAO;

        this.skus = recreateSkus();
        this.root = recreateRoot();
    }

    @Override
    protected Element render() {
        return root;
    }

    private ListElement<?> recreateSkus() {
        DataFrame skusDf = skuDAO.findSkus();

        List<String> skus = new ArrayList<>(skusDf.height());
        skusDf.forEach(r -> skus.add(r.get(0, String.class)));

        return list(skus).autoScroll().scrollbar();
    }

    private Element recreateRoot() {
        return column(
                // header
                panel(
                        "SKU Explorer",
                        text("Welcome to SKU Explorer!").bold().cyan()
                ).rounded(),

                // body
                skus,

                // footer
                panel(text(" Up/Down: Navigate | q: Quit ").dim()
                ).rounded()
        );
    }
}
