package com.objectstyle.sku;

import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.ListElement;

import static dev.tamboui.toolkit.Toolkit.*;

public class SkuExplorerUI extends ToolkitApp {

    private final SkuDAO skuDAO;
    private final ListElement<?> skuList;

    public SkuExplorerUI(SkuDAO skuDAO) {
        this.skuDAO = skuDAO;
        this.skuList = list()
                .add("line 1")
                .add("line 2")
                .add("line 3")
                .add("line 4")
                .add("line 5")
                .autoScroll()
                .scrollbar();
    }


    @Override
    protected Element render() {

        return column(
                // header
                panel(
                        "SKU Explorer",
                        text("Welcome to SKU Explorer!").bold().cyan()
                ).rounded(),

                // body
                skuList,

                // footer
                panel(text(" Up/Down: Navigate | q: Quit ").dim()
                ).rounded()
        );
    }
}
