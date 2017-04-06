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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    private Boolean administrator;
    
    private Map<Integer, Integer> quantityOfItem;

    private List<Product> adminProducts;
    
 // <editor-fold desc="Setter and Getter Garbage.">

    public Map<Integer, Integer> getQuantityOfItem() {
        return quantityOfItem;
    }

    public void setQuantityOfItem(Map<Integer, Integer> quantityOfItem) {
        this.quantityOfItem = quantityOfItem;
    }
    
    @Inject
    UserEJB usr;
    
    public List<Product> getAllProducts() {
        List<Product> allProducts = usr.getAllProducts();
        for(int i =0; i<allProducts.size(); i++){
            if(!this.quantityOfItem.containsKey(allProducts.get(i).getId())){
                this.quantityOfItem.put(allProducts.get(i).getId(), 0);
            }
        }
        return allProducts;
    }

    public List<Product> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<Product> shoppingList) {
        this.shoppingList = shoppingList;
    }

    public List<Product> getAdminProducts() {
        return this.adminProducts;
    }

    public void setAdminProducts(List<Product> adminProducts) {
        this.adminProducts = adminProducts;
    }

    public Boolean getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Boolean administrator) {
        this.administrator = administrator;
    }

    public UserEJB getUsr() {
        return usr;
    }

    public void setUsr(UserEJB usr) {
        this.usr = usr;
    }

    public void updateProducts(){
        this.adminProducts = usr.getAllProducts();
    }
// </editor-fold>
    
    public products() {
        this.shoppingList = new ArrayList<Product>();
        this.administrator = true;
        this.quantityOfItem = new HashMap<Integer, Integer>();
        this.adminProducts = new ArrayList<Product>();
    }
    
    public void addProductRow( String name, String des, int stock, double price){
        
    }
    
    public void removeFromBasket(Product p){
        this.shoppingList.remove(p);
    }
    
    public void addToBasket(Product p){
        if(this.shoppingList.contains(p)){
            this.shoppingList.remove(p);
        }
        System.out.println("Adding " + this.quantityOfItem.get(p.getId()) + " of item to basket: " + p.getName());
        this.shoppingList.add(p);
    }
    
     public void changeItem(Product p) {
        System.out.println("EJB will change: " + p.getName());
    }
     
    public double getShoppingTotal(){
        return 0.0;
    }
    
}
