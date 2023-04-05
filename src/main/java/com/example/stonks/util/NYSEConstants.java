package com.example.stonks.util;

public class NYSEConstants {
    public static final String COMPANY_PARAMETER = "companyName";
    public static final String FREQUENCY_PARAMETER = "frequency";
    public static final String START_DATE = "startdate";
    public static final String END_DATE = "enddate";

    public static final String URL_TEMPLATE = "https://www.marketwatch.com/investing/stock/"
            + "{companyCode}/downloaddatapartial"
            + "?startdate={startDate}%2000:00:00"
            + "&enddate={endDate}%2023:59:59"
            + "&daterange=d{period}"
            + "&frequency={frequency}"
            + "&csvdownload=true"
            + "&downloadpartial=false"
            + "&newdates=false";
}
