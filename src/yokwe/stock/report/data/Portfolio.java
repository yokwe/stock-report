package yokwe.stock.report.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;

import yokwe.stock.util.CSVUtil;

public class Portfolio {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Portfolio.class);
	
	public static class Entry {
		public final String             symbol;
		public final boolean            active;
		public final List<StockHistory> stockHistoryList;
		public final StockHistory       lastStockHistory;
		public final List<StockHistory> lastStockHistoryList;
		
		private Entry (List<StockHistory> stockHistory) {
			StockHistory lastStockHistory = stockHistory.get(stockHistory.size() - 1);

			this.symbol               = lastStockHistory.group;
			this.stockHistoryList     = Collections.unmodifiableList(stockHistory);
			this.lastStockHistory     = lastStockHistory;
			this.lastStockHistoryList = Collections.unmodifiableList(stockHistoryList.stream().filter(o -> o.session == lastStockHistory.session).collect(Collectors.toList()));
			this.active               = lastStockHistory.totalQuantity != 0;
		}
	}
	
	public static class Active {
		public final String             symbol;
		public final List<StockHistory> stockHistoryList;
		
		private Active (String symbol, List<StockHistory> stockHistoryList) {
			this.symbol           = symbol;
			this.stockHistoryList = stockHistoryList;
		}
	}
	
	private final Map<String, Entry>  entryMap;
	private final Map<String, Active> activeMap;
	
	public Portfolio(String pathBase, String fileName) {
		Map<String, Entry>  map1 = new TreeMap<>();
		Map<String, Active> map2 = new TreeMap<>();
		
		{
			String path = String.format("%s/%s", pathBase, fileName);
			List<StockHistory> stockHistoryList = CSVUtil.loadWithHeader(path, StockHistory.class);

			Map<String, List<StockHistory>> stockHistoryMap = new TreeMap<>();
			
			for(StockHistory stockHistory: stockHistoryList) {
				String key = stockHistory.group;
				if (!stockHistoryMap.containsKey(key)) {
					stockHistoryMap.put(key, new ArrayList<>());
				}
				stockHistoryMap.get(key).add(stockHistory);
			}
			
			for(Map.Entry<String, List<StockHistory>> entry: stockHistoryMap.entrySet()) {
				Collections.sort(entry.getValue());
			}

			for(List<StockHistory> list: stockHistoryMap.values()) {
				Entry entry = new Entry(list);
				map1.put(entry.symbol, entry);
				
				if (entry.active) {
					Active active = new Active(entry.symbol, entry.lastStockHistoryList);
					map2.put(active.symbol, active);
				}
			}
		}
		
		entryMap  = Collections.unmodifiableMap(map1);
		activeMap = Collections.unmodifiableMap(map2);
	}
	
	public Map<String, Entry> getEntryMap() {
		return entryMap;
	}
	
	public Map<String, Active> getActiveMap() {
		return activeMap;
	}
	
	public static void main(String[] args) {
		logger.info("START");
		
		{
			int count_active = 0;
			Portfolio portfolio = new Portfolio(".", "tmp/firstrade/stock-history-firstrade.csv");
			
			Map<String, Entry> entryMap = portfolio.entryMap;
			for(Entry entry: entryMap.values()) {				
				logger.info("{} {}", String.format("%-8s", entry.symbol), entry.active ? "ACTIVE" : "");
				if (entry.active) {
					count_active++;
					for(StockHistory stockHistory: entry.lastStockHistoryList) {
						logger.info("  {}", stockHistory);
					}
				}
			}
			logger.info("stockHistoryInfoMap {} / {}", count_active, entryMap.size());
		}
		
		{
			int count_active = 0;
			Portfolio portfolio = new Portfolio(".", "tmp/monex/stock-history-monex.csv");
			
			Map<String, Entry> entryMap = portfolio.entryMap;
			for(Entry entry: entryMap.values()) {				
				logger.info("{} {}", String.format("%-8s", entry.symbol), entry.active ? "ACTIVE" : "");
				if (entry.active) {
					count_active++;
					for(StockHistory stockHistory: entry.lastStockHistoryList) {
						logger.info("  {}", stockHistory);
					}
				}
			}
			logger.info("stockHistoryInfoMap {} / {}", count_active, entryMap.size());
		}
		
		logger.info("STOP");
	}
}
