package com.objectstyle.sku.dao;

import org.dflib.DataFrame;
import org.dflib.csv.Csv;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class SkuDAO {

    private static final DateTimeFormatter US_DATE = DateTimeFormatter.ofPattern("M/d/yyyy");
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("$#,##0.##");

    static {
        MONEY_FORMAT.setParseBigDecimal(true);
    }

    private static BigDecimal parseMoney(String s) {
        try {
            return (BigDecimal) MONEY_FORMAT.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid money value: " + s, e);
        }
    }

    public DataFrame activeSkus(YearMonth asOf) {
        return loadSkus()
                .rows("date(`Start Date`) <= ? and date(`End Date`) >= ?", asOf.atEndOfMonth(), asOf.atDay(1))
                .sort("Person, Client")
                .select();
    }

    private DataFrame loadSkus() {
        return Csv.loader()
                .dateCol("Start Date", US_DATE)
                .dateCol("End Date", US_DATE)
                .col("Unit Price", SkuDAO::parseMoney)
                .load("../data/SKU History.csv");
    }
}
