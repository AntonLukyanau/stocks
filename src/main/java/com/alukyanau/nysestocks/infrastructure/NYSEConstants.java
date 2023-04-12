package com.alukyanau.nysestocks.infrastructure;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class NYSEConstants {

    public static final int NYSE_FOUNDED_YEAR = 1792;

    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);

    public static final String URL_TEMPLATE = "https://www.marketwatch.com/investing/stock/"
            + "%s/downloaddatapartial"
            + "?startdate=%s 00:00:00"
            + "&enddate=%s 23:59:59"
            + "&daterange=d%d"
            + "&frequency=p1d"
            + "&csvdownload=true"
            + "&downloadpartial=false"
            + "&newdates=false";

}
