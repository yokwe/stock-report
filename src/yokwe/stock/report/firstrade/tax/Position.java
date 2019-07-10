package yokwe.stock.report.firstrade.tax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.LoggerFactory;

import yokwe.stock.UnexpectedException;
import yokwe.stock.util.DateMap;
import yokwe.stock.util.DoubleUtil;

public class Position {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Position.class);

	public static final double COMMISSION = 0;

	public final String symbol;
	public final double quantity;
	
	public Position(String symbol, double quantity) {
		this.symbol   = symbol;
		this.quantity = DoubleUtil.isAlmostZero(quantity) ? 0 : quantity;
	}
	public Position(String symbol) {
		this(symbol, 0);
	}
	public Position(Position that) {
		this(that.symbol, that.quantity);
	}
	
	@Override
	public String toString() {
		return String.format("[%s %.5f]", symbol, quantity);
	}
	
	private static Map<String, Position>   positionMap = new TreeMap<>();
	public static void buy(String date, String symbol, double quantity) {
		Position position;
		if (positionMap.containsKey(symbol)) {
			position = positionMap.get(symbol);
		} else {
			position = new Position(symbol);
			positionMap.put(symbol, position);
		}
		// Replace with new value
		positionMap.put(symbol, new Position(symbol, position.quantity + quantity));
		// Update dateMap
		dateMap.put(date, getPositionList());
	}
	public static void sell(String date, String symbol, double quantity) {
		Position position;
		if (positionMap.containsKey(symbol)) {
			position = positionMap.get(symbol);
		} else {
			logger.error("Unknown symbol  {}", symbol);
			throw new UnexpectedException("Unexpected");
		}
		// Replace with new value
		positionMap.put(symbol, new Position(symbol, position.quantity - quantity));
		// Update dateMap
		dateMap.put(date, getPositionList());
	}
	public static void change(String date, String symbol, double quantity, String newSymbol, double newQuantity) {
		Position position;
		if (positionMap.containsKey(symbol)) {
			position = positionMap.get(symbol);
		} else {
			logger.error("Unknown symbol  {}", symbol);
			throw new UnexpectedException("Unexpected");
		}
		// Sanity check
		if (-position.quantity != quantity) {
			logger.error("Unexpected quantity {}  {} != {}", symbol, quantity, position.quantity);
			throw new UnexpectedException("Unexpected");
		}
		// Replace with new value
		positionMap.remove(symbol);
		positionMap.put(newSymbol, new Position(newSymbol, newQuantity));
		// Update dateMap
		dateMap.put(date, getPositionList());
	}

	
	private static DateMap<List<Position>> dateMap     = new DateMap<>();
	private static List<Position> getPositionList() {
		List<Position> ret = new ArrayList<>();
		for(Position position: positionMap.values()) {
			if (position.quantity == 0) continue;
			ret.add(new Position(position));
		}
		return ret;
	}
	public static List<Position> getPositionList(String date) {
		return dateMap.get(date);
	}
}
