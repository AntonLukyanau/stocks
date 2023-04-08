package com.example.stonks.util;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class NYSEConstants {

    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);

    public static final String URL_TEMPLATE = "https://www.marketwatch.com/investing/stock/"
            + "{companyCode}/downloaddatapartial"
            + "?startdate={startDate}%2000:00:00"
            + "&enddate={endDate}%2023:59:59"
            + "&daterange=d{period}"
            + "&frequency={frequency}"
            + "&csvdownload=true"
            + "&downloadpartial=false"
            + "&newdates=false";

    public static final String CSV_HEADER = "Date,Open,High,Low,Close,Volume";

}
