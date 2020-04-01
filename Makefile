#
#
#
SHELL := /bin/bash

USERAGENT := 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36'
all:
	@echo all

clean:
	echo rm -rf tmp/*

#
# misc-lib relate targets
#

# to make project independent from misc-lib, copy files from misc-lib
copy-misc-lib-files:
	cp ../misc-lib/tmp/build/jar/misc-lib.jar data/jar/
	cp ../misc-lib/data/jar/*.jar             data/jar/
	cp ../misc-lib/data/market/*.csv          data/market/

build-misc-lib:
	pushd ../misc-lib/; ant build ; popd; make copy-misc-lib-files

# to make project independent from stock-data, copy files from misc-lib
copy-stock-data-files:
	cp ../stock-data/tmp/build/jar/stock-data.jar data/jar/
	cp ../misc-lib/data/jar/*.jar                 data/jar/
	cp ../misc-lib/data/market/*.csv              data/market/

build-stock-data:
	pushd ../stock-data/; ant build ; popd; make copy-stock-data-files


#
# ods
#
save-ods:
	cp ~/Dropbox/Trade/投資活動_monex.ods     ~/Dropbox/Trade/SAVE/投資活動_monex_$$(date +%Y%m%d).ods
	cp ~/Dropbox/Trade/投資活動_firstrade.ods ~/Dropbox/Trade/SAVE/投資活動_firstrade_$$(date +%Y%m%d).ods
	cp ~/Dropbox/Trade/投資活動_gmo.ods       ~/Dropbox/Trade/SAVE/投資活動_gmo_$$(date +%Y%m%d).ods
	cp ~/Dropbox/Trade/投資活動_TEST.ods      ~/Dropbox/Trade/SAVE/投資活動_TEST_$$(date +%Y%m%d).ods
	cp ~/Dropbox/Trade/投資活動_ソニー銀行.ods  ~/Dropbox/Trade/SAVE/投資活動_ソニー銀行_$$(date +%Y%m%d).ods
	cp ~/Dropbox/Trade/TEMPLATE_STOCK_REPORT.ods   ~/Dropbox/Trade/SAVE/TEMPLATE_STOCK_REPORT_$$(date +%Y%m%d).ods
	cp ~/Dropbox/Trade/TEMPLATE_FIRSTRADE_TAX.ods  ~/Dropbox/Trade/SAVE/TEMPLATE_FIRSTRADE_TAX_$$(date +%Y%m%d).ods
	
#
# firstrade
#
firstrade-yahoo-portfolio:
	ant run-firstrade-update-stock-history
	ant run-firstrade-update-yahoo-portfolio
	cp tmp/firstrade/yahoo-portfolio-firstrade.csv ~/Dropbox/Trade

# ant run-firstrade-tax-report require updated forex.csv
update-forex:
	ant run-update-forex
	cp tmp/data/forex.csv ~/Dropbox/Trade/

#
# monex
#
monex-fx-tax:
	curl -A $(USERAGENT) https://mst.monex.co.jp/mst/servlet/ITS/ucu/UsEvaluationRateGST >tmp/monex/UsEvaluationRateGST
	ant run-monex-update-fx-tax
	cp tmp/monex/monex-fx-tax.csv ~/Dropbox/Trade/

monex-stock-us:
	ant run-monex-update-stock-us
	cp tmp/monex/monex-stock-us.csv ~/Dropbox/Trade/

monex-yahoo-portfolio:
	ant run-monex-update-stock-history
	ant run-monex-update-yahoo-portfolio
	cp tmp/monex/yahoo-portfolio-monex.csv ~/Dropbox/Trade

#
# gmo
#
gmo-yahoo-portfolio:
	ant run-gmo-update-stock-history
	ant run-gmo-update-yahoo-portfolio
	cp tmp/gmo/yahoo-portfolio-gmo.csv ~/Dropbox/Trade
