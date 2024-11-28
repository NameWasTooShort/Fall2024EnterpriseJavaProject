package ca.sheridancollege.zhaoste.beans;

import lombok.Data;

@Data
public class Customer {
	private String name;
	private double totalBill;
	private MenuItem[] order;
	private int tableNum;
}
