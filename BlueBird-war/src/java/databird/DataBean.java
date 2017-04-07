/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databird;

import ejb.AdminEJB;
import ejb.UserEJB;
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
    
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    
    public void testing(){
        Product product = new Product();
        
        
        
        product.setId(1);
        product.setName("Sparkling Water");
        product.setQuantityOnHand(20); 
        product.setPrice(1.50);
        
        //user.purchaseProduct(product, 5);
        //admin.removeProduct(product);
        //admin.updateProduct(product);
//        admin.removeProduct(product);
        //admin.addProduct(product);
        //System.out.println("Testing button pressed");
    }
}