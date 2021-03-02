package com.investscape.navgetter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class Scheme implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 585620707939795736L;

    String schemeCode;
    String schemeName;
    String nav;
    String date;
}
