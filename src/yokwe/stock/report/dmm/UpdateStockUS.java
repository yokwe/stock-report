package yokwe.stock.report.dmm;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yokwe.util.CSVUtil;
import yokwe.util.http.HttpUtil;

public class UpdateStockUS {
	private static final Logger logger = LoggerFactory.getLogger(UpdateStockUS.class);

	public static final String PATH_DMM_US = "tmp/dmm/dmm-stock-us.csv";

	public static void save(List<StockUS> usSecurityList) {
		CSVUtil.write(StockUS.class).file(PATH_DMM_US, usSecurityList);
	}
	public static List<StockUS> load() {
		return CSVUtil.read(StockUS.class).file(PATH_DMM_US);
	}
	
	// Stock
	public static class StockInfo {
		public static final String SOURCE_URL = "https://kabu.dmm.com/_data/us-stock.csv";
		
		static List<StockInfo> getInstance() {
			String string = HttpUtil.getInstance().download(SOURCE_URL).result;
			return CSVUtil.read(StockInfo.class).file(new StringReader(string));
		}
		
//		code,name,detail,market
//		A,アジレント テクノロジー,化学分析ツールの最大手メーカー,NYSE

		public String code;
		public String name;
		public String detail;
		public String market;
		
		public StockInfo() {
			this.code   = null;
			this.name   = null;
			this.detail = null;
			this.market = null;
		}
		
		@Override
		public String toString() {
			return String.format("%s %s %s %s", code, name, detail, market);
		}
	}

	// ETF
	public static class ETFInfo {
		public static final String SOURCE_URL = "https://kabu.dmm.com/_data/us-etf.csv";
		
		static List<ETFInfo> getInstance() {
			String string = HttpUtil.getInstance().download(SOURCE_URL).result;
			return CSVUtil.read(ETFInfo.class).file(new StringReader(string));
		}
		
//		code,name,market,url,management
//		BND,バンガード 米国トータル債券市場ETF,NASDAQ,https://www.vanguardjapan.co.jp/docs/FS_BND_JP.pdf,Vanguard

		public String code;
		public String name;
		public String market;
		public String url;
		public String management;
		
		public ETFInfo() {
			this.code       = null;
			this.name       = null;
			this.market     = null;
			this.url        = null;
			this.management = null;
		}
		
		@Override
		public String toString() {
			return String.format("%s %s %s %s %s", code, name, market, url, management);
		}
	}

	// ADR
	public static class ADRInfo {
		public static final String SOURCE_URL = "https://kabu.dmm.com/_data/us-adr.csv";
		
		static List<ADRInfo> getInstance() {
			String string = HttpUtil.getInstance().download(SOURCE_URL).result;
			return CSVUtil.read(ADRInfo.class).file(new StringReader(string));
		}
		
//			code,name,detail,market,nationality,country
//			ABEV,アンベブ,ブラジルの総合飲料会社,NYSE,ブラジル,BRA

		public String code;
		public String name;
		public String detail;
		public String market;
		public String nationality;
		public String country;
		
		public ADRInfo() {
			this.code        = null;
			this.name        = null;
			this.detail      = null;
			this.market      = null;
			this.nationality = null;
			this.country     = null;
		}
		
		@Override
		public String toString() {
			return String.format("%s %s %s %s %s", code, name, detail, market, nationality, country);
		}
	}

	public static void main(String[] args) {
		logger.info("START");
		
		List<StockInfo> stockInfoList = StockInfo.getInstance();
		int countStock = stockInfoList.size();

		List<ADRInfo> adrInfoList = ADRInfo.getInstance();
		int countADR = adrInfoList.size();

		List<ETFInfo> etfInfoList = ETFInfo.getInstance();
		int countETF = etfInfoList.size();

		List<StockUS> stockUSList = new ArrayList<>();
		
		// String ticker, String nameJP, String exchange, String category
		stockInfoList.stream().map(o -> new StockUS(o.code, o.name, o.market, "STOCK")).forEach(o -> stockUSList.add(o));
		adrInfoList.stream().map(o -> new StockUS(o.code, o.name, o.market, "ADR")).forEach(o -> stockUSList.add(o));
		etfInfoList.stream().map(o -> new StockUS(o.code, o.name, o.market, "ETF")).forEach(o -> stockUSList.add(o));

		Collections.sort(stockUSList);

		logger.info("OUTPUT = {}", PATH_DMM_US);
		save(stockUSList);

		logger.info("STOCK  = {}", String.format("%5d", countStock));
		logger.info("ADR    = {}", String.format("%5d", countADR));
		logger.info("ETF    = {}", String.format("%5d", countETF));
		logger.info("TOTAL  = {}", String.format("%5d", countETF + countADR + countStock));

		logger.info("STOP");		
	}
}
