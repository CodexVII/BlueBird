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
    
    private HtmlDataTable datatableAdmin;
        
    private HtmlDataTable datatableUser;
    
    private HtmlDataTable datatableCart;
    
 // <editor-fold desc="Setter and Getter Garbage.">

    @Inject
    UserEJB usr;
    
    public List<Product> getAllProducts() {
        return usr.getAllProducts();
    }
    
    public HtmlDataTable getDatatableAdmin() {
        return datatableAdmin;
    }

    public void setDatatableAdmin(HtmlDataTable datatableAdmin) {
        this.datatableAdmin = datatableAdmin;
    }

    public HtmlDataTable getDatatableUser() {
        return datatableUser;
    }

    public HtmlDataTable getDatatableCart() {
        return datatableCart;
    }

    public void setDatatableCart(HtmlDataTable datatableCart) {
        this.datatableCart = datatableCart;
    }

    public void setDatatableUser(HtmlDataTable datatableUser) {
        this.datatableUser = datatableUser;
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
    
    public void addProductToShoppingList(Product p){
        this.shoppingList.add(p);
    }
    
    public void removeProductFromShoppingList(Product p){
        this.shoppingList.remove(p);
    }
    
    public void removeFromBasket(ActionEvent ev) throws IOException{
        if (ev.getSource() != null && ev.getSource() instanceof HtmlCommandButton) {
            HtmlCommandButton button = (HtmlCommandButton) ev.getSource();
            int currentId = Integer.parseInt(button.getLabel());
            Product p = (Product) datatableCart.getRowData();
            p.setId(currentId);
            removeProductFromShoppingList(p);
        }
    }
    
    public void addToBasket(ActionEvent ev) throws IOException{
        if (ev.getSource() != null && ev.getSource() instanceof HtmlCommandButton) {
            HtmlCommandButton button = (HtmlCommandButton) ev.getSource();
            int currentId = Integer.parseInt(button.getLabel());
            Product p = (Product) datatableUser.getRowData();
            p.setId(currentId);
            if(!shoppingList.contains(p)){
                System.out.println("Adding item to basket Item " + p.getName());
                addProductToShoppingList(p); 
            }
            else
                System.out.println("Item already present " + p.getName());
        }
    }
    
     public void changeItem(ActionEvent ev) throws IOException{
        if (ev.getSource() != null && ev.getSource() instanceof HtmlCommandButton) {
            HtmlCommandButton button = (HtmlCommandButton) ev.getSource();
            int currentId = Integer.parseInt(button.getLabel());
            Product p = (Product) datatableAdmin.getRowData();
            p.setId(currentId);
            //Code to edit item
        }
    }
    
}
