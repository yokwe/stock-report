# stock-report

## This project is for my stock investment.

Description of targets in build.xml

* run-update-forex  
  Download fx data and generate file  
  Input is `https://www.mizuhobank.co.jp/market/csv/quote.csv`  
  Output is `tmp/data/forex.csv`
  

* run-stock-report  
  Stock Report for firstrade and monex  
  Input are `~/Dropbox/Trade/投資活動_firstrade.ods` and `~/Dropbox/Trade/投資活動_monex.ods`  
  Output is `~/Dropbox/Trade/Report/STOCK_REPORT_YYYYMMDD-HHMMSS.ods`


* run-monex-update-fx-tax  
  Download fx data ccreate csv file. The csv file is used in `~/Dropbox/Trade/投資活動_monex.ods`.  
  Input are `https://mst.monex.co.jp/mst/servlet/ITS/ucu/UsEvaluationRateGST` and `tmp/monex/monex-fx-tax-YYYY.csv`  
  Output are `tmp/monex/monex-fx-tax.csv` and `tmp/monex/monex-fx-tax-YYYY.csv`
  
  
* run-monex-update-stock-history  
  Generate stock history data for YahooPortfolio.
  Input is `~/Dropbox/Trade/投資活動_monex.ods`  
  Output is `tmp/monex/stock-history-monex.csv`


* run-monex-update-yahoo-portfolio  
  Generate Yahoo Finance Portfolio csv file from stock history file.
  Input is `tmp/monex/stock-history-monex.csv`  
  Output is `tmp/monex/yahoo-portfolio-monex.csv`


* run-monex-update-stock-us  
  Download us stock list available in monex and generate csv file.
  Input is `https://mxp1.monex.co.jp/mst/servlet/ITS/ucu/UsMeigaraJsonGST`  
  Output is `tmp/monex/monex-stock-us.csv`


* run-firstrade-update-stock-history  
  Generate stock history data for YahooPortfolio.
  Input is `~/Dropbox/Trade/投資活動_firstrade.ods`  
  Output is `tmp/firstrade/stock-history-firstrade.csv`


* run-monex-update-yahoo-portfolio  
  Generate Yahoo Finance Portfolio csv file from stock history file.
  Input is `tmp/monex/stock-history-monex.csv`  
  Output is `tmp/monex/yahoo-portfolio-monex.csv`


* run-firstrade-tax-report
  Generate report used for tax declaration.
  Input is `~/Dropbox/Trade/投資活動_firstrade.ods`  
  Output is `~/Dropbox/Trade/Report/FIRSTRADE_TAX_YYYYMMDD-HHMMSS.ods`


- run-firstrade-statement-report
  Generate report to check spreadsheet for firstrade.
  Input is `~/Dropbox/Trade/投資活動_firstrade.ods`  
  Output is `~/Dropbox/Trade/Report/FIRSTRADE_STATEMENT_YYYYMMDD-HHMMSS.ods`
