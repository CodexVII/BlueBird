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
    public class ShoppingItem{
        private Product p;
        private int quantity;

        public ShoppingItem(Product p, int quantity)
        {
            this.p = p;
            this.quantity = quantity;
        }        
        public Product getP() {
            return p;
        }

        public void setP(Product p) {
            this.p = p;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        
        
    }
    private List<ShoppingItem> shoppingList;
    
    private Boolean administrator;
    
 // <editor-fold desc="Setter and Getter Garbage.">

    @Inject
    UserEJB usr;
    
    public List<ShoppingItem> getAllProducts() {
        List<Product> ejbProds = usr.getAllProducts();
        List<ShoppingItem> s1 = new ArrayList<ShoppingItem>();
        for(int i =0; i < ejbProds.size(); i++){
            s1.add(new ShoppingItem(ejbProds.get(i),0));
        }
        return s1;
        
    }

    public List<ShoppingItem> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<ShoppingItem> shoppingList) {
        this.shoppingList = shoppingList;
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

    
// </editor-fold>
    
    public products() {
        this.shoppingList = new ArrayList<ShoppingItem>();
        this.administrator = true;
    }
    
    public void addProductRow( String name, String des, int stock, double price){
        
    }
    
    public void removeFromBasket(ShoppingItem p){
        this.shoppingList.remove(p);
    }
    
    public void addToBasket(ShoppingItem p){
        if(!shoppingList.contains(p)){
            System.out.println("Adding "+ p.quantity +" of item to basket: " + p.p.getName());
            this.shoppingList.add(p);
        }
        else{
            System.out.println("Item already present " + p.p.getName());
        }
    }
    
     public void changeItem(Product p) {
        System.out.println("EJB will change: " + p.getName());
    }
     
    public double getShoppingTotal(){
        double total=0.0;
        for(int i =0; i<this.shoppingList.size();i++){
            total+= this.shoppingList.get(i).p.getPrice() * this.shoppingList.get(i).quantity;
        }
        return total;
    }
    
}
