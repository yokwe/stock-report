#
#
#
SHELL := /bin/bash

all:
	@echo all

clean:
	echo rm -rf tmp/*

#
# misc-lib relate targets
#
setup-iex-cloud:
	cd data; rm -rf market; ln -s ../../misc-lib/data/market/

copy-iex-cloud.jar:
	cp ../iex-cloud/tmp/build/jar/iex-cloud.jar data/jar/

build-iex-cloud:
	pushd ../iex-cloud/; ant build ; popd; make copy-iex-cloud.jar


#
# ods
#
save-ods:
	cp ~/Dropbox/Trade/投資活動_monex.ods     ~/Dropbox/Trade/SAVE/投資活動_monex_$$(date +%Y%m%d).ods
	cp ~/Dropbox/Trade/投資活動_firstrade.ods ~/Dropbox/Trade/SAVE/投資活動_firstrade_$$(date +%Y%m%d).ods

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
	ant run-monex-update-fx-tax
	cp tmp/monex/monex-fx-tax.csv ~/Dropbox/Trade/

monex-stock-us:
	ant run-monex-update-stock-us
	cp tmp/monex/monex-stock-us.csv ~/Dropbox/Trade/

monex-yahoo-portfolio:
	ant run-monex-update-stock-history
	ant run-monex-update-yahoo-portfolio
	cp tmp/monex/yahoo-portfolio-monex.csv ~/Dropbox/Trade
