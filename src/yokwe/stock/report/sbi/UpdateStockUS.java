package yokwe.stock.report.sbi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yokwe.util.CSVUtil;
import yokwe.util.ScrapeUtil;
import yokwe.util.StringUtil;
import yokwe.util.StringUtil.MatcherFunction;
import yokwe.util.http.HttpUtil;

public class UpdateStockUS {
	private static final Logger logger = LoggerFactory.getLogger(UpdateStockUS.class);

	public static final String SOURCE_URL       = "https://search.sbisec.co.jp/v2/popwin/info/stock/pop6040_usequity_list.html";
	public static final String SOURCE_ENCODING  = "SHIFT_JIS";
	
	public static final String PATH_SBI_US = "tmp/sbi/sbi-stock-us.csv";

	public static void save(List<StockUS> usSecurityList) {
		CSVUtil.write(StockUS.class).file(PATH_SBI_US, usSecurityList);
	}
	public static List<StockUS> load() {
		return CSVUtil.read(StockUS.class).file(PATH_SBI_US);
	}
	
// STOCK
//	<tr>
//	<th class="vaM alC">A</th>
//	<td>Agilent Technologies<br>アジレント テクノロジーズ</td>
//	<td>環境、食品の品質・安全性等の化学分析を行うツールを開発、提供</td>
//	<td class="vaM alC">NYSE</td>
//	</tr>

	public static class StockInfo {
		public static final Pattern PAT = Pattern.compile(
				"<tr>\\s+" +
				"<th class=\"vaM alC\">(?<symbol>.+?)</th>\\s+" +
				"<td>(?<nameEN>.+?)<br>(?<nameJP>.+?)</td>\\s+" +
				"<td>(?<note>.+?)\\s*</td>\\s+" +
				"<td class=\"vaM alC\">(?<exchange>.+?)</td>\\s+" +
				"</tr>"
		);
		public static List<StockInfo> getInstance(String page) {
			return ScrapeUtil.getList(StockInfo.class, PAT, page);
		}
		
		public final String symbol;
		public final String nameEN;
		public final String nameJP;
		public final String note;
		public final String exchange;
		
		public StockInfo(String symbol, String nameEN, String nameJP, String note, String exchange) {
			this.symbol   = symbol;
			this.nameEN   = nameEN;
			this.nameJP   = nameJP;
			this.note     = note;
			this.exchange = exchange;
		}
		
		@Override
		public String toString() {
			return String.format("%s %s %s %s %s", symbol, nameEN, nameJP, note, exchange);
		}
	}

// ETF
//	<tr>
//	<th class="vaM alC">QQQ</th>
//	<td>Invesco QQQ Trust,Series 1 ETF<br>インベスコ QQQ トラスト シリーズ1 ETF</td>
//	<td class="vaM alC">NASDAQ</td>
//	</tr>
//
//	<tr>
//	<th class="vaM alC">FINX</th><td>Global X FinTech ETF<br>グローバルX フィンテック ETF</td>
//	<td class="vaM alC">NASDAQ</td>
//	</tr>
//
//	<tr>
//	<th class="vaM alC">AIQ</th>
//	<td>Global X Funds Global X Artificial Intelligence & Technology ETF<br>グローバルX AI＆ビッグデータETF
//	</td>
//	<td class="vaM alC">NASDAQ</td>
//	</tr>    
//
//	<tr><th class="vaM alC">XLC</th>
//	<td>Communication Service Select Sector SPDR Fund<br>コミュニケーション サービス セレクト セクターSPDRファンド</td>
//	<td class="vaM alC">NYSE Arca</td>
//	</tr>
//	<tr>


	public static class ETFInfo {
		public static final Pattern PAT = Pattern.compile(
				"<tr>\\s*" +
				"<th class=\"vaM alC\">(?<symbol>.+?)</th>\\s*" +
				"<td>(?<nameEN>.+?)<br>(?<nameJP>.+?)(\\s+)?</td>\\s+" +
				"<td class=\"vaM alC\">(?<exchange>.+?)</td>\\s+" +
				"</tr>"
		);
		public static List<ETFInfo> getInstance(String page) {
			return ScrapeUtil.getList(ETFInfo.class, PAT, page);
		}
		
		public final String symbol;
		public final String nameEN;
		public final String nameJP;
		public final String exchange;
		
		public ETFInfo(String symbol, String nameEN, String nameJP, String exchange) {
			this.symbol   = symbol;
			this.nameEN   = nameEN;
			this.nameJP   = nameJP;
			this.exchange = exchange;
		}
		
		@Override
		public String toString() {
			return String.format("%s %s %s %s", symbol, nameEN, nameJP, exchange);
		}
	}

// ADR
//	<tr>
//	<th><div class="thM alC"><p class="fm01">BMA</p></div></th>
//	<td><div class="tdM"><p class="fm01">Banco Macro SA<br>バンコ マクロ</p></div></td>
//	<td><div class="tdM"><p class="fm01">アルゼンチンの金融機関</p></div></td>
//	<td><div class="tdM alC"><p class="fm01">NYSE</p></div></td>
//	</tr>

//	<tr>
//	<th><div class="thM alC"><p class="fm01">PKX</p></div></th>
//	<td><div class="tdM"><p class="fm01">POSCO<BR>ポスコ</p></div></td>
//	<td><div class="tdM"><p class="fm01">韓国の鉄鋼メーカー</p></div></td>
//	<td><div class="tdM alC"><p class="fm01">NYSE</p></div></td>
//	</tr>

		public static class ADRInfo {
			public static final Pattern PAT = Pattern.compile(
					"<tr>\\s*" +
					"<th><div class=\"thM alC\"><p class=\"fm01\">(?<symbol>.+?)</p></div></th>\\s+" +
					"<td><div class=\"tdM\"><p class=\"fm01\">(?<nameEN>.+?)<(br|BR)>(?<nameJP>.+?)</p></div></td>\\s+" +
					"<td><div class=\"tdM\"><p class=\"fm01\">(?<note>.+?)\\s*</p></div></td>\\s+" +
					"<td><div class=\"tdM alC\"><p class=\"fm01\">(?<exchange>.+?)</p></div></td>\\s+" +
					"</tr>"
			);
			public static List<ADRInfo> getInstance(String page) {
				return ScrapeUtil.getList(ADRInfo.class, PAT, page);
			}
			
			public final String symbol;
			public final String nameEN;
			public final String nameJP;
			public final String note;
			public final String exchange;
			
			public ADRInfo(String symbol, String nameEN, String nameJP, String note, String exchange) {
				this.symbol   = symbol;
				this.nameEN   = nameEN;
				this.nameJP   = nameJP;
				this.note     = note;
				this.exchange = exchange;
			}
			
			@Override
			public String toString() {
				return String.format("%s %s %s %s %s", symbol, nameEN, nameJP, note, exchange);
			}
		}

	public static void main(String[] args) {
		logger.info("START");

		String string = HttpUtil.getInstance().withCharset(SOURCE_ENCODING).download(SOURCE_URL).result;
				
		// remove XML comment
		Pattern PAT_COMMENT = Pattern.compile("<!--.*?-->", Pattern.MULTILINE | Pattern.DOTALL);
		MatcherFunction<String> OP_REMOVE_COMMENT = (m) -> "";
		String page = StringUtil.replace(string, PAT_COMMENT, OP_REMOVE_COMMENT);
//		logger.info("string {}", string.length());
//		logger.info("page   {}", page.length());
		
		List<StockInfo> stockInfoList = StockInfo.getInstance(page);
		int countStock = stockInfoList.size();

		List<ADRInfo> adrInfoList = ADRInfo.getInstance(page);
		int countADR = adrInfoList.size();

		List<ETFInfo> etfInfoList = ETFInfo.getInstance(page);
		int countETF = etfInfoList.size();

		List<StockUS> stockUSList = new ArrayList<>();
		
		// String ticker, String name, String exchange, String category
		stockInfoList.stream().map(o -> new StockUS(o.symbol, o.nameEN, o.exchange, "STOCK")).forEach(o -> stockUSList.add(o));
		adrInfoList.stream().map(o -> new StockUS(o.symbol, o.nameEN, o.exchange, "ADR")).forEach(o -> stockUSList.add(o));
		etfInfoList.stream().map(o -> new StockUS(o.symbol, o.nameEN, o.exchange, "ETF")).forEach(o -> stockUSList.add(o));

		Collections.sort(stockUSList);

		logger.info("URL    = {}", SOURCE_URL);
		logger.info("OUTPUT = {}", PATH_SBI_US);
		save(stockUSList);

		logger.info("STOCK  = {}", String.format("%5d", countStock));
		logger.info("ADR    = {}", String.format("%5d", countADR));
		logger.info("ETF    = {}", String.format("%5d", countETF));
		logger.info("TOTAL  = {}", String.format("%5d", countETF + countADR + countStock));

		logger.info("STOP");		
	}
}
