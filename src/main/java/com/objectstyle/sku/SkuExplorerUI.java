package com.objectstyle.sku;

import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.ListElement;
import org.dflib.DataFrame;
import org.dflib.row.RowProxy;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static dev.tamboui.toolkit.Toolkit.*;

public class SkuExplorerUI extends ToolkitApp {

    private DataFrame modelSkus;

    private ListElement<?> uiSkus;
    private Element uiRoot;

    public SkuExplorerUI(SkuDAO skuDAO) {
        this.modelSkus = skuDAO.activeSkus(YearMonth.now());
        this.uiSkus = uiSkus();
        this.uiRoot = uiRoot();
    }

    @Override
    protected Element render() {
        return uiRoot;
    }

    private ListElement<?> uiSkus() {

        List<String> skuLabels = new ArrayList<>(modelSkus.height());
        modelSkus.forEach(r -> skuLabels.add(formatSku(r)));

        return list(skuLabels).autoScroll().scrollbar();
    }

    private Element uiRoot() {
        return column(
                // header
                panel(
                        "SKU Explorer",
                        text("Welcome to SKU Explorer!").bold().cyan()
                ).rounded(),

                // body
                uiSkus,

                // footer
                panel(text(" Up/Down: Navigate | q: Quit ").dim()
                ).rounded()
        );
    }

    private static String formatSku(RowProxy r) {
        return String.format("%s %s", r.get("SKU"), r.get("Person"));
    }
}
