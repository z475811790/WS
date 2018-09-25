/**
 * Created by xYzDl Builder
 * Table: t_credit
 */
package entity;

import java.util.Date;
import java.util.Map;
import java.util.List;

@SuppressWarnings("unused")
public class Credit {
	private int id;
	private String customer;
	private boolean isCheckout;

	public Credit() {
	}

	public Credit(int id, String customer, boolean isCheckout) {
		this.id = id;
		this.customer = customer;
		this.isCheckout = isCheckout;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public boolean getIsCheckout() {
		return isCheckout;
	}

	public void setIsCheckout(boolean isCheckout) {
		this.isCheckout = isCheckout;
	}
}