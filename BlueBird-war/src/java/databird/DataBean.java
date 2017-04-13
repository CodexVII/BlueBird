/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databird;

import ejb.AdminEJB;
import ejb.UserEJB;
import entity.CustomerOrder;
import entity.Product;
import entity.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author keita
 */
@Named(value = "dataBean")
@SessionScoped
public class DataBean implements Serializable {
    @Inject
    private AdminEJB admin;
    
    @Inject
    private UserEJB user;
    
    List<Product> products;
    List<User> users;
    List<CustomerOrder> orders;

    public List<CustomerOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<CustomerOrder> orders) {
        this.orders = orders;
    }
    
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    /**
     * Creates a new instance of DataBean
     */
    public DataBean() {
    }
    
    public void addProduct(){
        Product product = new Product();
        product.setName("Water");
        admin.addProduct(product);
    }
    
    public void queryAllProducts(){
        products = user.getAllProducts();
    }

    public void queryAllUsers(){
        users = user.getAllUsers();
    }
    
    public void queryAllOrders(){
        orders = user.getAllOrders();
        System.out.println(orders);
    }
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public void updateUser(User u){
        System.out.println("BEAN UPDATE USR");
        System.out.println(u);
        user.updateUser(u);
    }
    
    public void buyProduct(Product prod, int amount){
        System.out.println(prod);
        System.out.println(amount);
        user.purchaseProduct(prod, amount, users.get(0));
    }
    
    
    public void testing(){
        System.out.println("testing() called");
        Product product = new Product();
        User userTemp = new User();
        userTemp.setId(1);
        List<CustomerOrder> returnList = user.getAllOrdersByUser(userTemp);
        //List<CustomerOrder> returnList = user.getAllOrders();
        
        System.out.println("Length: " + returnList.size());
        
        for(int i = 0; i < returnList.size(); i++) {
            System.out.println("Value: " + returnList.get(i).getId());
        }
        
//        product.setId(1);
//        product.setName("Sparkling Water");
//        product.setQuantityOnHand(20); 
//        product.setPrice(1.50);
        
//        user.purchaseProduct(product, 5);
//        admin.removeProduct(product);
//        admin.updateProduct(product);
//        admin.removeProduct(product);
//        admin.addProduct(product);
//        System.out.println("Testing button pressed");
    }
}