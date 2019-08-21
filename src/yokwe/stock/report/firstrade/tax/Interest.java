package yokwe.stock.report.firstrade.tax;

import yokwe.util.libreoffice.Sheet;
import yokwe.util.libreoffice.SpreadSheet;

@Sheet.SheetName("利子明細")
@Sheet.HeaderRow(0)
@Sheet.DataRow(1)
public class Interest extends Sheet {
	@ColumnName("支払年月日")
	@NumberFormat(SpreadSheet.FORMAT_DATE)
	public final String date;
	
	@ColumnName("利子金額")
	@NumberFormat(SpreadSheet.FORMAT_USD)
	public final double interest;
	
	@ColumnName("為替レート")
	@NumberFormat(SpreadSheet.FORMAT_NUMBER2)
	public final double fxRate;
	
	@ColumnName("邦貨利子金額")
	@NumberFormat(SpreadSheet.FORMAT_JPY)
	public final int interestJPY;
	
	
	public Interest(String date, double interest, double fxRate) {
		this.date        = date;
		this.interest    = interest;
		this.fxRate      = fxRate;
		this.interestJPY = (int)Math.floor(this.interest * this.fxRate);
	}
}
