/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.Product;
import entity.User;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author keita & alan <3
 */
@Stateless
@LocalBean
public class AdminEJB {
    @PersistenceContext
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    public void addProduct(Product product) {
        em.merge(product);
    }
    
    public void updateProduct(Product product) {
        Product prod = em.find(Product.class, product.getId());
        
        prod.setName(product.getName());
        prod.setQuantityOnHand(product.getQuantityOnHand());
        prod.setPrice(product.getPrice());
        prod.setDescription(product.getDescription());
        
        em.merge(prod);
    }
     
    public void removeProduct(Product product){

        Product prod = em.find(Product.class, product.getId());
        em.remove(prod);
    }
    
    public void addUser(User user){
        em.merge(user);
    }
    
    public void updateUser(User user){
        em.merge(user);
    }
}
