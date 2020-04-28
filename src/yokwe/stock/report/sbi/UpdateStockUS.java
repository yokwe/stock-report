package yokwe.stock.report.sbi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yokwe.UnexpectedException;
import yokwe.util.CSVUtil;
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
	
//	<tr>^M
//	<th class="vaM alC">A</th>^M
//	<td>Agilent Technologies<br>アジレント テクノロジーズ</td>^M
//	<td>環境、食品の品質・安全性等の化学分析を行うツールを開発、提供</td>^M
//	<td class="vaM alC">NYSE</td>^M
//	</tr>^M

//	<tr>^M
//	<th class="vaM alC">ACWI</th>^M
//	<td>iShares MSCI ACWI ETF<br>iシェアーズ MSCI ACWI ETF</td>^M
//	<td class="vaM alC">NASDAQ</td>^M
//	</tr>^M

	private static final String PAT_STRING_CAT = "<h3 class=\"ttlM\">(.+?)</h3>";
	private static final String PAT_STRING_TAB = "<table class=\"md-l-table-01 md-l-utl-mt10\">";
	
	private static final String PAT_STRING_BEGIN  = "<tr>";
	private static final String PAT_STRING_SYM = "<th class=\"vaM alC\">(.+?)</th>";
	private static final String PAT_STRING_NAME = "<td>(.+?)<br>(.+?)</td>";
	private static final String PAT_STRING_EXCH = "<td class=\"vaM alC\">(.+?)</td>";
	private static final String PAT_STRING_END  = "</tr>";
			
	private static final Pattern PAT_CAT = Pattern.compile(PAT_STRING_CAT);
	private static final Pattern PAT_TAB = Pattern.compile(PAT_STRING_TAB);
	
	private static final Pattern PAT_BEGIN = Pattern.compile(PAT_STRING_BEGIN);
	private static final Pattern PAT_SYM = Pattern.compile(PAT_STRING_SYM);
	private static final Pattern PAT_NAME = Pattern.compile(PAT_STRING_NAME);
	private static final Pattern PAT_EXCH = Pattern.compile(PAT_STRING_EXCH);
	private static final Pattern PAT_END = Pattern.compile(PAT_STRING_END);
	

	public static void main(String[] args) {
		logger.info("START");
		
		Matcher mCat = PAT_CAT.matcher("");
		Matcher mTable = PAT_TAB.matcher("");
		
		Matcher mBegin = PAT_BEGIN.matcher("");
		Matcher mSym   = PAT_SYM.matcher("");
		Matcher mName  = PAT_NAME.matcher("");
		Matcher mExch  = PAT_EXCH.matcher("");
		Matcher mEnd   = PAT_END.matcher("");
		
		List<StockUS> stockUSList = new ArrayList<>();
		int countStock = 0;
		int countETF = 0;
		
		String content = HttpUtil.getInstance().withCharset(SOURCE_ENCODING).download(SOURCE_URL).result;
		String[] lines = content.split("[\r\n]+");
		
		String category = "???";
		for(int i = 0; i < lines.length; i++) {
			String line = lines[i].trim();
			
			{
				mCat.reset(line);
				if (mCat.find()) {
					category = mCat.group(1);
					category = category.replace("銘柄一覧", "");
					category = category.replace("普通株式一覧", "STOCK");
					logger.info("category {}", category);
					continue;
				}
			}
			
			{
				mTable.reset(line);
				if (mTable.find()) {
					for(;;) {
						line = lines[++i];
						if (line.equals("</table>")) {
							break;
						}
						
						mBegin.reset(line);
						if (mBegin.find()) {
							String line1;
							if (line.startsWith("<tr><th")) {
								line1 = line.substring(4);
							} else {
								line1 = lines[++i];
							}
							String line2;
							if (line1.contains("</th><td>")) {
								int pos = line1.indexOf("<td>");
								line2 = line1.substring(pos);
								line1 = line1.substring(0, pos);
							} else {
								line2 = lines[++i];
							}
							String line3 = lines[++i];
							if (line3.startsWith("<td>")) line3 = lines[++i];
							String line4 = lines[++i];
							
							if (line1.startsWith("<th style=\"width: 13%")) continue;
							
							mSym.reset (line1);
							mName.reset(line2);
							mExch.reset(line3);
							mEnd.reset (line4);
							
							String symbol;
							String name;
							String exch;
							
							if (mSym.find()) {
								symbol = mSym.group(1);
							} else {
								logger.error("Unexpected line  symbol  {}", line1);
								throw new UnexpectedException("Unexpected line");
							}
							if (mName.find()) {
								name = mName.group(1);
							} else {
								logger.error("Unexpected line  name  {}", line2);
								throw new UnexpectedException("Unexpected line");
							}
							if (mExch.find()) {
								exch = mExch.group(1);
							} else {
								logger.error("Unexpected line  exch  {}", line3);
								throw new UnexpectedException("Unexpected line");
							}
							if (!mEnd.find()) {
								logger.error("Unexpected line  end   {}", line4);
								throw new UnexpectedException("Unexpected line");
							}
							//logger.info("{}  {}  {}  {}", symbol, name, exch);
							
							StockUS stockUS = new StockUS(symbol, name, exch, category);
							stockUSList.add(stockUS);
							
							if (category.equals("STOCK")) countStock++;
							else if (category.equals("ETF")) countETF++;
							else {
								logger.error("Unexpected category   {}", category);
								throw new UnexpectedException("Unexpected category");
							}
						}
					}
				}
			}
		}
		
		Collections.sort(stockUSList);

		logger.info("URL    = {}", SOURCE_URL);
		logger.info("OUTPUT = {}", PATH_SBI_US);
		save(stockUSList);

		logger.info("ETF    = {}", String.format("%5d", countETF));
		logger.info("STOCK  = {}", String.format("%5d", countStock));
		logger.info("TOTAL  = {}", String.format("%5d", countETF + countStock));
		
		logger.info("STOP");		
	}
}
