package com.investscape.navgetter.constants;

import com.investscape.navgetter.model.Scheme;

public class Constants {
    public static final String FUNDS_CACHE_KEY = "#schemeCode";
    public static final String DO_NOT_USE_CACHE_ON_FORCE_RELOAD = "!#forceReload";
    public static final Scheme EMPTY_SCHEME = new Scheme(null, null, null, null);
    public static final String AMFI_WEBSITE_LINK = "https://www.amfiindia.com/spages/NAVAll.txt";
    public static final String MFAPI_WEBSITE_BASE_URL = "https://api.mfapi.in/mf/";
    public static final String DATE_PATTERN_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String SEPARATOR = ";";
    public static final String FUNDS_CACHE = "fundsCache";
}
