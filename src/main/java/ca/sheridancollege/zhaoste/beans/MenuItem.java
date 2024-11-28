package ca.sheridancollege.zhaoste.beans;

import lombok.Data;

@Data
public class MenuItem {
	private long itemId;
	private String name;
	private double price;
	private int spiceLevel;
}
