/**
 * Created by xYzDl Builder
 * Table: t_busi
 */
package entity;

import java.util.Date;
import java.util.Map;
import java.util.List;

@SuppressWarnings("unused")
public class Busi {
	private int id;
	private String ps;
	private Date busiDate;
	private int busiManId;
	private int busiType;
	private double money;
	private String shop;

	public Busi() {
	}

	public Busi(int id, String ps, Date busiDate, int busiManId, int busiType, double money, String shop) {
		this.id = id;
		this.ps = ps;
		this.busiDate = busiDate;
		this.busiManId = busiManId;
		this.busiType = busiType;
		this.money = money;
		this.shop = shop;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPs() {
		return ps;
	}

	public void setPs(String ps) {
		this.ps = ps;
	}

	public Date getBusiDate() {
		return busiDate;
	}

	public void setBusiDate(Date busiDate) {
		this.busiDate = busiDate;
	}

	public int getBusiManId() {
		return busiManId;
	}

	public void setBusiManId(int busiManId) {
		this.busiManId = busiManId;
	}

	public int getBusiType() {
		return busiType;
	}

	public void setBusiType(int busiType) {
		this.busiType = busiType;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}
}