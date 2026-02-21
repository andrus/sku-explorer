package com.objectstyle.sku;

import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Flex;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.TableElement;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widgets.table.Cell;
import dev.tamboui.widgets.table.Row;
import dev.tamboui.widgets.table.TableState;
import org.dflib.DataFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.YearMonth;

import static dev.tamboui.toolkit.Toolkit.*;

public class SkuExplorerUI extends ToolkitApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkuExplorerUI.class);

    private final SkuDAO skuDAO;

    private DataFrame modelSkus;

    private Element uiRoot;
    private TableElement uiSkus;
    private TableState uiSkusState;

    public SkuExplorerUI(SkuDAO skuDAO) {
        this.skuDAO = skuDAO;

        this.modelSkus = modelSkus();
        this.uiSkusState = uiSkusState();
        this.uiSkus = uiSkus();
        this.uiRoot = uiRoot();
    }

    @Override
    protected Element render() {
        return uiRoot;
    }

    private DataFrame modelSkus() {
        return skuDAO
                .activeSkus(YearMonth.now())
                .cols().select("SKU, Person, Client, `Unit Price`");
    }

    private TableState uiSkusState() {
        TableState state = new TableState();
        state.select(0);
        return state;
    }

    private TableElement uiSkus() {
        return table()
                .header(modelSkus.getColumnsIndex().toArray())
                .widths(skuWidths())
                .rows(skuRows())
                .highlightSymbol(">> ")
                .highlightStyle(Style.EMPTY.bg(Color.CYAN).fg(Color.BLACK))
                .state(uiSkusState);
    }

    private Element uiRoot() {
        String status = String.format("%d active sku%s",
                modelSkus.height(),
                modelSkus.height() == 1 ? "" : "(s)");

        String help = "Up/Down: Navigate | q: Quit";

        return column(
                // body
                panel(() -> uiSkus)
                        .title("SKUs")
                        .borderColor(Color.CYAN)
                        .rounded()
                        .fill(),

                // footer
                column(
                        row(text(status), text(help)).flex(Flex.SPACE_BETWEEN).dim()
                )
        ).focusable().fill().onKeyEvent(this::handleKey);
    }

    private Constraint[] skuWidths() {
        return new Constraint[]{
                length(15), fill(50), length(25),

                // expand to all available space
                fill(100)
        };
    }

    private Row[] skuRows() {
        int w = modelSkus.width();
        int h = modelSkus.height();

        Row[] rows = new Row[h];
        Cell[] row = new Cell[w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Object v = modelSkus.get(j, i);
                row[j] = Cell.from(v != null ? v.toString() : "");
            }

            rows[i] = Row.from(row);
        }

        return rows;
    }

    private EventResult handleKey(KeyEvent event) {
        if (event.isDown()) {
            uiSkusState.selectNext(modelSkus.height());
            return EventResult.HANDLED;
        } else if (event.isUp()) {
            uiSkusState.selectPrevious();
            return EventResult.HANDLED;
        }

        LOGGER.info("Event {}", event.code());

        return EventResult.UNHANDLED;
    }
}
