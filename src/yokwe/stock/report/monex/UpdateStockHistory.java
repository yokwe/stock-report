package yokwe.stock.report.monex;

import java.util.Collections;
import java.util.List;

import org.slf4j.LoggerFactory;

import yokwe.UnexpectedException;
import yokwe.util.CSVUtil;
import yokwe.util.libreoffice.SpreadSheet;
import yokwe.stock.report.data.StockHistory;
import yokwe.stock.report.data.StockHistoryUtil;

public class UpdateStockHistory {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UpdateStockHistory.class);
	
	public static List<StockHistory> getStockHistoryList() {
		try (SpreadSheet docActivity = new SpreadSheet(Transaction.URL_ACTIVITY, true)) {

			// Create transaction from activity
			List<Transaction> transactionList = Transaction.getTransactionList(docActivity);
			StockHistory.Builder builder = new StockHistory.Builder();
			
			for(Transaction transaction: transactionList) {
				switch(transaction.type) {
				case BUY:
					builder.buy(transaction.date, transaction.symbol, transaction.quantity, transaction.fee, transaction.total);
					break;
				case SELL:
					builder.sell(transaction.date, transaction.symbol, transaction.quantity, transaction.fee, transaction.total);
					break;
				case DIVIDEND:
					builder.dividend(transaction.date, transaction.symbol, transaction.fee, transaction.total);
					break;
				case CHANGE:
					builder.change(transaction.date, transaction.symbol, -transaction.quantity, transaction.newSymbol, transaction.newQuantity);
					break;
				case JPY_IN:
				case JPY_OUT:
				case USD_IN:
				case USD_OUT:
				case FEE:
					break;
				default:
					logger.error("Unexpected {}", transaction);
					throw new UnexpectedException("Unexpected");
				}
			}
			
			List<StockHistory> stockHistoryList = builder.getStockList();
			
			// Change symbol style from ".PR." to "-"
			for(StockHistory stockHistory: stockHistoryList) {
				stockHistory.group  = stockHistory.group.replace(".PR.", "-");
				stockHistory.symbol = stockHistory.symbol.replace(".PR.", "-");
			}
			Collections.sort(stockHistoryList);
			
			return stockHistoryList;
		}
	}

	public static void main(String[] args) {
		logger.info("START");
		
		List<StockHistory>stockHistoryList = getStockHistoryList();
		logger.info("stockHistoryList = {}", stockHistoryList.size());

		CSVUtil.write(StockHistory.class).file(StockHistoryUtil.PATH_STOCK_HISTORY_MONEX, stockHistoryList);
		
		logger.info("STOP");
		System.exit(0);
	}
}
