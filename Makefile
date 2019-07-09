#
#
#

all:
	@echo all

clean:
	echo rm -rf tmp/*

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
