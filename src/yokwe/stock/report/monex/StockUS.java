package yokwe.stock.report.monex;

public class StockUS implements Comparable<StockUS> {
	public String ticker;
	public String name;
	public String jname;
	public String keyword;
	public String etf;
	public String shijo;
	public String update;
	
	// Stock
	public String gyoshu;
	public String jigyo;
	
	// ETF
	public String benchmark;
	public String shisan;
	public String chiiki;
	public String category;
	public String keihi;
	public String comp;
	public String pdf;
	
	public StockUS(String ticker, String name, String jname, String keyword, String etf, String shijo, String update,
			String gyoshu, String jigyo,
			String benchmark, String shisan, String chiiki, String category, String keihi, String comp, String pdf) {
		this.ticker  = ticker;
		this.name    = name;
		this.jname   = jname;
		this.keyword = keyword;
		this.etf     = etf;
		this.shijo   = shijo;
		this.update  = update;
		
		// Stock
		this.gyoshu = gyoshu;
		this.jigyo  = jigyo;
		
		// ETF
		this.benchmark = benchmark;
		this.shisan    = shisan;
		this.chiiki    = chiiki;
		this.category  = category;
		this.keihi     = keihi;
		this.comp      = comp;
		this.pdf       = pdf;
	}

	@Override
	public int compareTo(StockUS that) {
		return this.ticker.compareTo(that.ticker);
	}
}
