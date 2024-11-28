package ca.sheridancollege.zhaoste.controllers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.zhaoste.beans.MenuItem;
import ca.sheridancollege.zhaoste.beans.Order;
import ca.sheridancollege.zhaoste.databases.DatabaseAccess;

@Controller
public class RestaurantController {
	
	@Lazy
	@Autowired
	DatabaseAccess restaurantDB;
	
	List<MenuItem> menuList = new CopyOnWriteArrayList<MenuItem>();
	List<Order> orderList = new CopyOnWriteArrayList<Order>();
	List<String> displayList = new CopyOnWriteArrayList<String>();
	
	// Security
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@GetMapping("/permission-denied")
	public String permissionDenied() {
		return "/error/permission-denied";
	}
	
	// Pages with info
	@GetMapping("/")
	public String getIndex(Model model) {
		model.addAttribute("menuList", restaurantDB.getMenuList(""));
		return "index";
	}
	
	@GetMapping("/order_form")
	public String getOrder(Model model) {
		model.addAttribute("order", new Order());
		model.addAttribute("orderList", restaurantDB.getOrderList(""));
		model.addAttribute("displayList", displayList);
		return "order_form";
	}
	
	@PostMapping("/insertOrder")
	public String postOrder(Model model, @ModelAttribute Order order) {
		restaurantDB.insertOrder(order);
		orderList.add(order);
		
		displayList.add(restaurantDB.getItemById(orderList.get(orderList.size()-1).getFoodId()).getName());
		
		model.addAttribute("order", new Order());
		model.addAttribute("orderList", restaurantDB.getOrderList(""));
		model.addAttribute("displayList", displayList);
		return "order_form";
	}
	
	@GetMapping("/secure")
	public String getAdminPage(Model model) {
		model.addAttribute("menuList", restaurantDB.getMenuList(""));
		model.addAttribute("menuItem", new MenuItem());
		return "/secure/adminForm";
	}
	
	// This is important, since we are using secure as admin role
	// We also need to map to secure when posting otherwise we do not have permission
	@PostMapping("/secure/addMenu")
	public String addMenuItem(Model model, @ModelAttribute MenuItem menuitem) {
		restaurantDB.insertMenuItem(menuitem);
		model.addAttribute("menuList", restaurantDB.getMenuList(""));
		model.addAttribute("menuItem", new MenuItem());
		return "/secure/adminForm";
	}
	
	@GetMapping("/deleteOrderId/{id}")
	public String deleteOrder(Model model, @PathVariable Long id) {
		// Note: id is OrderId, NOT foodID
		Order targetOrder = restaurantDB.getOrderById(id);
		MenuItem targetItem = restaurantDB.getItemById(targetOrder.getFoodId());
		
		displayList.remove(targetItem.getName());
		restaurantDB.deleteOrder(id);
		orderList = restaurantDB.getOrderList("");
		
		//System.out.println(displayList);
		
		model.addAttribute("orderList", orderList);
		model.addAttribute("order", new Order());
		model.addAttribute("displayList", displayList);
		
		return "order_form";
	}
	
	@GetMapping("/editOrderId/{id}")
	public String editOrder(Model model, @PathVariable Long id, @ModelAttribute Order order2) {
		// Note: id is OrderId, NOT foodID
		// Process:
		// Delete the stuff we dont want from the DB and from the Lists (can update list based on DB)
		// Get the targeted Order
		Order targetOrder = restaurantDB.getOrderById(id);
		// Add information it to HTML form
		model.addAttribute("order", targetOrder);
		// Remove the Order from the DB
		restaurantDB.deleteOrder(id);
		// Get the value from displayList we are removing, want it saved for now
		MenuItem toRemove = restaurantDB.getItemById(targetOrder.getFoodId());
		
		// Make the updates based on new info
		// Changes:
		// Update the Lists
		orderList = restaurantDB.getOrderList(""); // Update
		displayList.remove(toRemove.getName()); // Remove from displayList
		
		model.addAttribute("orderList", orderList);
		model.addAttribute("displayList", displayList);
		
		return "order_form";
	}
	
}