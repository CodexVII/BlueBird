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
 * @author Alan Noonan, Ian Lodovica, Dylan O'Connor Desmond, Gearoid Cremin, Trevor McSweeney
 *
 * All the methods to allow admin users to interact with the database
 */
@Stateless
@LocalBean
public class AdminEJB {
    @PersistenceContext
    EntityManager em;

    /**
     * Allows for adding a product to the database.
     *
     * @param product the product to be added to the database
     */
    public void addProduct(Product product) {
        em.merge(product);
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product the product to be updated in the database
     */
    public void updateProduct(Product product) {
        // Find product
        Product prod = em.find(Product.class, product.getId());

        prod.setName(product.getName());
        prod.setQuantityOnHand(product.getQuantityOnHand());
        prod.setPrice(product.getPrice());
        prod.setDescription(product.getDescription());

        // Merge to database
        em.merge(prod);
    }

    /**
     * Removes a product from the database.
     *
     * @param product the product to be removed the database
     */
    public void removeProduct(Product product){
        // Find and remove prduct from database
        Product prod = em.find(Product.class, product.getId());
        em.remove(prod);
    }

    /**
     * Adds a user to the database.
     *
     * @param user the user to be added to the database
     */
    public void addUser(User user){
        em.merge(user);
    }

    /**
     * Updates an existing user in the database.
     *
     * @param user the user to be updated in the database
     */
    public void updateUser(User user){
        em.merge(user);
    }
}
