/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import bean.*;
import ejb.AdminBean;
import entity.Product;
import javax.annotation.ManagedBean;
import javax.ejb.Stateful;
import javax.inject.Inject;

/**
 *
 * @author keita
 */
@Stateful
@ManagedBean
public class UserBean {
    @Inject
    AdminBean bean;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public void addProduct(){
        Product product = new Product();
        product.setName("Spring Water");
        bean.addProduct(product);
    }
}
