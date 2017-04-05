/*  
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package information_tracking;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Gearoid
 */
@Named(value = "products")
@SessionScoped
public class products implements Serializable {
    
    public class Product {
        private double price;
        private int stock;
        private String name;
        private String description;
        private int index;
        private Boolean basketCase;

         // <editor-fold desc="Setter and Getter Garbage.">
        public Boolean getBasketCase() {
            return basketCase;
        }

        public void setBasketCase(Boolean basketCase) {
            this.basketCase = basketCase;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }        
        
        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        // </editor-fold>
        
        public String buttonText(){
            if(!this.basketCase)
                return "Add";
            else
                return "Remove";
        }
        
        public Product(int index, String name, String description, int stock, double price){
            this.price = price;
            this.stock = stock;
            this.description = description;
            this.name = name;
            this.index = index;
            this.basketCase = false;
        }
    }

    /**
     * Creates a new instance of products
     */
    private List<Product> allProducts;
    
    private HtmlDataTable datatableAdmin;
        
    private HtmlDataTable datatableUser;
    
 // <editor-fold desc="Setter and Getter Garbage.">

    public List<Product> getAllProducts() {
        return allProducts;
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

    public void setDatatableUser(HtmlDataTable datatableUser) {
        this.datatableUser = datatableUser;
    }
    
    public void setAllProducts(List<Product> allProducts) {
        this.allProducts = allProducts;
    }
// </editor-fold>
    
    public products() {
        allProducts = new ArrayList<Product>();
        // Just some dummy data to test with
        this.addProductRow(1,"Fight Milk", "Fight Like a Crow!", 1000, 55.99);
        this.addProductRow(2,"Chicken Hut Gravy", "Forged in the fires of Mount Doom", 2, 199.00);
    }
    
    public void addProductRow(int index, String name, String des, int stock, double price){
        this.allProducts.add(new Product(index,name,des,stock,price));
    }
    
    public void addToBasket(ActionEvent ev) throws IOException{
        if (ev.getSource() != null && ev.getSource() instanceof HtmlCommandButton) {
            HtmlCommandButton button = (HtmlCommandButton) ev.getSource();
            int currentRow = Integer.parseInt(button.getLabel());
            Product p = (Product) datatableUser.getRowData();
            if(p.basketCase)
                //Add item to basket
                System.out.println("Removing item from basket Item " + p.name);
            else
                //remove item from basket
                System.out.println("Adding item to basket Item " + p.name);
            p.basketCase = !p.basketCase;
            button.setValue(p.buttonText());
            this.allProducts.set(currentRow-1,p);
        }
    }
    
     public void changeItem(ActionEvent ev) throws IOException{
        if (ev.getSource() != null && ev.getSource() instanceof HtmlCommandButton) {
            HtmlCommandButton button = (HtmlCommandButton) ev.getSource();
            int currentRow = Integer.parseInt(button.getLabel());
            Product p = (Product) datatableAdmin.getRowData();
            System.out.println("Editing Item :" + p.index);
            this.allProducts.set(currentRow-1, p);
            //Code to edit item
        }
    }
    
}
