/**
 * Created by xYzDl Builder
 * Table: t_type
 */
package entity;

import java.util.Date;
import java.util.Map;
import java.util.List;

@SuppressWarnings("unused")
public class Type {
	private int id;
	private boolean balanceType;
	private boolean isForManager;
	private String typeName;

	public Type() {
	}

	public Type(int id, boolean balanceType, boolean isForManager, String typeName) {
		this.id = id;
		this.balanceType = balanceType;
		this.isForManager = isForManager;
		this.typeName = typeName;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(boolean balanceType) {
		this.balanceType = balanceType;
	}

	public boolean getIsForManager() {
		return isForManager;
	}

	public void setIsForManager(boolean isForManager) {
		this.isForManager = isForManager;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}