package com.objectstyle.sku.ui;

import com.objectstyle.sku.dao.SkuDAO;
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

public class RootController extends ToolkitApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootController.class);

    private final SkuDAO skuDAO;

    private DataFrame skusModel;

    private Element rootView;
    private TableState skusState;

    public RootController(SkuDAO skuDAO) {
        this.skuDAO = skuDAO;

        this.skusModel = skusModel();
        this.skusState = skusState();
        this.rootView = rootView();
    }

    @Override
    protected Element render() {
        return rootView;
    }

    private DataFrame skusModel() {
        return skuDAO
                .activeSkus(YearMonth.now())
                .cols().select("SKU, Person, Client, `Unit Price`");
    }

    private TableState skusState() {
        TableState state = new TableState();
        state.select(0);
        return state;
    }

    private Element rootView() {
        String status = String.format("%d active sku%s",
                skusModel.height(),
                skusModel.height() == 1 ? "" : "(s)");

        String help = "Up/Down: Navigate | q: Quit";

        return column(
                // body
                panel(() -> viewSkus())
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

    private TableElement viewSkus() {
        return table()
                .header(skusModel.getColumnsIndex().toArray())
                // expand the last column to the entire available space
                .widths(length(15), fill(50), length(25), fill(100))
                .rows(skuRows())
                .highlightSymbol(">> ")
                .highlightStyle(Style.EMPTY.bg(Color.CYAN).fg(Color.BLACK))
                .state(skusState);
    }

    private Row[] skuRows() {
        int w = skusModel.width();
        int h = skusModel.height();

        Row[] rows = new Row[h];
        Cell[] row = new Cell[w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Object v = skusModel.get(j, i);
                row[j] = Cell.from(v != null ? v.toString() : "");
            }

            rows[i] = Row.from(row);
        }

        return rows;
    }

    private EventResult handleKey(KeyEvent event) {
        if (event.isDown()) {
            skusState.selectNext(skusModel.height());
            return EventResult.HANDLED;
        } else if (event.isUp()) {
            skusState.selectPrevious();
            return EventResult.HANDLED;
        }

        LOGGER.info("unhandled event: {}", event.code());

        return EventResult.UNHANDLED;
    }
}
