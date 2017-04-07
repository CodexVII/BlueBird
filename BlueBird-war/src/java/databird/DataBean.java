/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databird;

import ejb.AdminEJB;
import ejb.UserEJB;
import entity.Product;
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
    
    public void getAllProducts(){
        products = user.getAllProducts();
    }
    
    public void testing(){
        Product product = new Product();
        
        product.setId(10);
        product.setName("Fight Milk");
        product.setQuantityOnHand(200);
        product.setPrice(25.50);
        product.setDescription("New description");
        
        //user.purchaseProduct(product, 5);
        //admin.removeProduct(product);
        //admin.updateProduct(product);
        admin.removeProduct(product);
        //admin.addProduct(product);
        
        //System.out.println("Testing button pressed");
    }
}