# navGetter 
[![<terriblebassist>](https://circleci.com/gh/terriblebassist/navgetter/tree/master.svg?style=shield)](<https://circleci.com/gh/terriblebassist/navgetter/tree/master>) [![codecov](https://codecov.io/gh/terriblebassist/navgetter/branch/master/graph/badge.svg)](https://codecov.io/gh/terriblebassist/navgetter)

This springboot app will generate the Net Asset Value(NAV) of any mutual fund listed on the AMFI website:
```https://www.amfiindia.com/spages/NAVAll.txt```
  
### 1. API ###
All calls to API must be started with `.../getNav`
                                                        
<table>
<thead>
<tr>
<th>Endpoint</th>
<th>Description</th>
<th>Parameters</th>
<th>Success Response</th>
<th>Failure Response</th>
</tr>
</thead>
<tbody>
<tr>
	<td><code>GET /getNav/{schemeCode}?forceUpdate={boolean}</code></td>
	<td>1. Returns Scheme Information as a JSON, described in Success Response column, for given schemeCode.
	<br/><br/>2. Return a JSON response as described in Failure Response when scheme Not Found.</td>
    <td>Path:<br/><code>schemeCode</code> - Mutual Fund Scheme Code
    <br/><br/>Query:<br/><code>forceUpdate</code> - Boolean value to indicate whether to discard the cache & reload all funds info from AMFI</td>
	<td>
      <pre>
{
  "schemeCode": "119551",
  "schemeName": "Aditya Birla Sun Life Banking & PSU Debt Fund  - Direct Plan-Dividend",
  "nav": "156.5163",
  "date": "02-Sep-2020"
}
	  </pre>
	  <b>Note : "nav" & "date" in above example are sample values.</b>
    </td>
    <td>
          <pre>
    {
      "schemeCode": null,
      "schemeName": null,
      "nav": null,
      "date": null,
    }
    	  </pre>
    </td>
</tr>
</tbody>
</table>

