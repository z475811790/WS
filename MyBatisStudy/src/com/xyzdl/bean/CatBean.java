package com.xyzdl.bean;

public class CatBean extends PetBean {

	private static final long serialVersionUID = 1978574561040340989L;

	private Integer fish;

	public CatBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CatBean(Integer id, String name) {
		super(id, name);
		// TODO Auto-generated constructor stub
	}

	public Integer getFish() {
		return fish;
	}

	public void setFish(Integer fish) {
		this.fish = fish;
	}

	@Override
	public String toString() {
		return "CatBean [fish=" + fish + ", toString()=" + super.toString() + "]";
	}
}