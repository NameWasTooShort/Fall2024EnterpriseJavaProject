package ca.sheridancollege.zhaoste.beans;

import lombok.Data;

@Data
public class Order {
	private long orderId;
	private String customerName;
	private long foodId;
}
