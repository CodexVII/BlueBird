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
    
    private List<Product> products;
    private List<User> users;
    private List<CustomerOrder> orders;
    
    private String username;
    private boolean adminPrivelege;

    public boolean isAdminPrivelege() {
        return adminPrivelege;
    }

    public void setAdminPrivelege(boolean adminPrivelege) {
        this.adminPrivelege = adminPrivelege;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
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
    }
    
    public void checkPrivileges(){
        User u= new User();
        u.setUsername(username);
        adminPrivelege = user.isAdmin(u);
    }
    /**
     * Creates a new instance of DataBean
     */
    public DataBean() {
    }
}