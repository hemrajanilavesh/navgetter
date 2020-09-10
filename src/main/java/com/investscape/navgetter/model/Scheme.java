package com.investscape.navgetter.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@ApiModel(value = "Scheme", description = "Details of Mutual Fund Scheme including the Net Asset Value (NAV)")
@Slf4j
@Setter
@Getter
@ToString
@AllArgsConstructor
public class Scheme implements Serializable {

    @ApiModelProperty(value = "Uniquely identifiable code of the Mutual Fund Scheme.", example = "119551")
    String schemeCode;

    @ApiModelProperty(value = "Name of the Mutual Fund Scheme.", example = "Aditya Birla Sun Life Banking & PSU Debt Fund  - Direct Plan-Dividend")
    String schemeName;

    @ApiModelProperty(value = "The Net Asset Value of the Mutual Fund Scheme, reported on the corresponding date to AMFI.", example = "156.2123")
    String nav;

    @ApiModelProperty(value = "Date corresponding to the Net Asset Value.", example = "09-Sep-2020")
    String date;
}
