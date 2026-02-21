package com.objectstyle.sku;

import org.dflib.DataFrame;
import org.dflib.csv.Csv;

public class SkuDAO {

    public DataFrame findSkus() {
        return Csv.loader()
                .dateCol("Start Date")
                .dateCol("End Date")
                .decimalCol("Unit Price")
                .load("../data/SKU History.csv");
    }
}
