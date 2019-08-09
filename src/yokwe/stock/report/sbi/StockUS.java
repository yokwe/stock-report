package yokwe.stock.report.sbi;

public class StockUS implements Comparable<StockUS> {
	public String ticker;
	public String name;
	public String exchange;
	public String category;
	
	public StockUS(String ticker, String name, String exchange, String category) {
		this.ticker      = ticker;
		this.name        = name;
		this.exchange    = exchange;
		this.category    = category;
	}
	public StockUS() {
		this(null, null, null, null);
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s %s %s", ticker, name, exchange, category);
	}

	@Override
	public int compareTo(StockUS that) {
		return this.ticker.compareTo(that.ticker);
	}
}
