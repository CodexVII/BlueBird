/*  
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package information_tracking;

import ejb.UserEJB;
import entity.Product;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

/**
 *
 * @author Gearoid
 */
@Named(value = "products")
@SessionScoped
public class products implements Serializable {
    /**
     * Creates a new instance of products
     */
    
    private List<Product> shoppingList;
  
    
 // <editor-fold desc="Setter and Getter Garbage.">

    @Inject
    UserEJB usr;
    
    public List<Product> getAllProducts() {
        return usr.getAllProducts();
    }

    public List<Product> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<Product> shoppingList) {
        this.shoppingList = shoppingList;
    }
    
    
// </editor-fold>
    
    public products() {
        shoppingList = new ArrayList<Product>();

    }
    
    public void addProductRow(int index, String name, String des, int stock, double price){
        
    }
    
    
    public void removeFromBasket(Product p){
        this.shoppingList.remove(p);
    }
    
    public void addToBasket(Product p){
        if(!shoppingList.contains(p)){
            System.out.println("Adding item to basket Item " + p.getName());
            this.shoppingList.add(p);
        }
        else{
            System.out.println("Item already present " + p.getName());
        }
    }
    
     public void changeItem(Product p) {
        
    }
    
}
