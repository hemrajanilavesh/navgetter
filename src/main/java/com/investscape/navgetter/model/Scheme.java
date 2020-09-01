package com.investscape.navgetter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Setter
@Getter
@AllArgsConstructor
public class Scheme implements Serializable {
    String schemeCode;
    String schemeName;
    String nav;
    String date;
}
