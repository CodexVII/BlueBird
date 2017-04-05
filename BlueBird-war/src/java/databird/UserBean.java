/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databird;

import ejb.AdminBean;
import entity.Product;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.inject.Inject;

/**
 *
 * @author keita
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    @Inject
    private AdminBean admin;
    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }
    
    public void addProduct(){
        Product product = new Product();
        product.setName("Water");
        admin.addProduct(product);
    }

}