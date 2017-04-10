/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.Product;
import entity.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author keita
 */
@Stateless
@LocalBean
public class UserEJB {
    @PersistenceContext
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    

    public List<Product> getAllProducts(){
        Query q = em.createNamedQuery("Product.findAll", Product.class);
        return (List<Product>)q.getResultList();
    }
    
    public List<User> getAllUsers(){
        Query q = em.createNamedQuery("User.findAll", User.class);
        return (List<User>)q.getResultList();
    }
    
    public void purchaseProduct(Product product, int amount) {
        Product prod = em.find(Product.class, product.getName());
        prod.setQuantityOnHand(prod.getQuantityOnHand()-amount);
        em.merge(prod);
    }
    
    public void updateUser(User user){
        User u = em.find(User.class, user.getId());
        
        u.setStatusMessage(user.getStatusMessage());
        
        em.merge(u);
    }
    
    public void updateProduct(Product product) {
        Product prod = em.find(Product.class, product.getId());
        
        prod.setName(product.getName());
        prod.setQuantityOnHand(product.getQuantityOnHand());
        prod.setPrice(product.getPrice());
        prod.setDescription(product.getDescription());
        
        em.merge(prod);
    }
}
