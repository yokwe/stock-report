package yokwe.stock.report.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.LoggerFactory;

import yokwe.stock.data.DataContext;
import yokwe.stock.data.Previous;

public class StockPrevious {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(StockPrevious.class);
	
	private static final Map<String, Previous> getCache = new TreeMap<>();
	
	public static Previous get(String symbol) {
		if (!getCache.containsKey(symbol)) {
			List<Previous> previousList = Previous.load(DataContext.DEFAULT, symbol);
			if (previousList.isEmpty()) {
				logger.error("Unpexpected symbol {}", symbol);
			}
			Collections.sort(previousList);
			Previous previous = previousList.get(previousList.size() - 1);
			getCache.put(symbol, previous);
		}
		return getCache.get(symbol);		
	}
}
