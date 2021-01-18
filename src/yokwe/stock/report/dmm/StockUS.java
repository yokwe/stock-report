package yokwe.stock.report.dmm;

public class StockUS implements Comparable<StockUS> {
	public String ticker;
	public String nameJP;
	public String exchange;
	public String category;
	
	public StockUS(String ticker, String nameJP, String exchange, String category) {
		this.ticker      = ticker;
		this.nameJP      = nameJP;
		this.exchange    = exchange;
		this.category    = category;
	}
	public StockUS() {
		this(null, null, null, null);
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s %s %s", ticker, nameJP, exchange, category);
	}

	@Override
	public int compareTo(StockUS that) {
		return this.ticker.compareTo(that.ticker);
	}
}
