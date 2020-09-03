package com.investscape.navgetter.service;

import com.investscape.navgetter.model.Scheme;

import java.io.IOException;

public interface NavService {
    boolean loadNavForAllFunds() throws IOException;
    Scheme getNav(boolean forceReload, String schemeCode) throws IOException;
}
