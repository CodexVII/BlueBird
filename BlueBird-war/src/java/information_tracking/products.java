/*  
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package information_tracking;

import ejb.AdminEJB;
import ejb.UserEJB;
import entity.Product;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Comparator;
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
    
    private String searchBy="";
    private String searchName="";
    private int searchPart=0;
    
    private String npName;
    private String npDescription;
    private int npQuantity;
    private double npPrice;
    
    private int sortingOption = 0;
    private boolean sortingDirection = true;
    
 // <editor-fold desc="Setter and Getter Garbage.">

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }
    
    public boolean isSortingDirection() {
        return sortingDirection;
    }

    public void setSortingDirection(boolean sortingDirection) {
        this.sortingDirection = sortingDirection;
    }
    
    public Map<Integer, Integer> getQuantityOfItem() {
        return quantityOfItem;
    }

    public void setQuantityOfItem(Map<Integer, Integer> quantityOfItem) {
        this.quantityOfItem = quantityOfItem;
    }
    
    @Inject
    UserEJB usr;
    
    public void updateProducts() {
        List<Product> allProducts = usr.getAllProducts();
        for(int i =0; i<allProducts.size(); i++){
            if(!this.quantityOfItem.containsKey(allProducts.get(i).getId())){
                this.quantityOfItem.put(allProducts.get(i).getId(), 1);
            }
        }
        this.adminProducts = allProducts;
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

    public List<Product> getUpdatedProducts() {
        this.updateProducts();
        List<Product> filteredProducts = new ArrayList<Product>();
        if(this.searchName == "" && this.searchPart==0){
            this.sortProducts(this.adminProducts,this.sortingOption, this.sortingDirection);
            return this.adminProducts;
        }
        System.out.println("Performing a search operation now");
        for(int i =0; i<this.adminProducts.size();i++){
            if(this.searchName != "" && this.adminProducts.get(i).getName().contains(this.searchName)){
                filteredProducts.add(this.adminProducts.get(i));
            }
            else if(this.searchPart != 0 && this.adminProducts.get(i).getId() == this.searchPart){
                filteredProducts.add(this.adminProducts.get(i));            
            }
        }
        this.searchBy="";
        this.sortProducts(filteredProducts, this.sortingOption, this.sortingDirection);
        return filteredProducts;
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

    public String getNpName() {
        return npName;
    }

    public void setNpName(String npName) {
        this.npName = npName;
    }

    public String getNpDescription() {
        return npDescription;
    }

    public void setNpDescription(String npDescription) {
        this.npDescription = npDescription;
    }

    public int getNpQuantity() {
        return npQuantity;
    }

    public void setNpQuantity(int npQuantity) {
        this.npQuantity = npQuantity;
    }

    public double getNpPrice() {
        return npPrice;
    }

    public void setNpPrice(double npPrice) {
        this.npPrice = npPrice;
    }

    public int getSortingOption() {
        return sortingOption;
    }

    public void setSortingOption(int sortingOption) {
        this.sortingOption = sortingOption;
    }
    
// </editor-fold>
    
    public products() {
        this.shoppingList = new ArrayList<Product>();
        this.administrator = true;
        this.quantityOfItem = new HashMap<Integer, Integer>();
        this.adminProducts = new ArrayList<Product>();
    }
    
    public void addProduct(){
        System.out.println("Adding a new product: " + this.npName);
        Product newProduct = new Product();
        newProduct.setName(npName);
        newProduct.setDescription(npDescription);
        newProduct.setQuantityOnHand(npQuantity);
        newProduct.setPrice(npPrice);
        ad.addProduct(newProduct);
        this.npDescription="";
        this.npName="";
        this.npPrice=0.00;
        this.npQuantity=0;
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
    
    @Inject
    AdminEJB ad;
    
     public void changeItem(Product p) {
        System.out.println("EJB will change: " + p.getName() + " with Id " + p.getId()+ " and description " +p.getDescription());
        ad.updateProduct(p);
        this.updateProducts();
    }
     
    public void removeItem(Product p){
        System.out.println("Removing product # " + p.getId() + " - " + p.getName());
        ad.removeProduct(p);
        this.updateProducts();
    }
    public double getShoppingTotal(){
        return 0.0;
    }
    
    public void sortingOrder(int ord, boolean dir){
        this.sortingOption = ord;
        this.sortingDirection = dir;
    }
    
    public List<Product> reverse(List<Product> list) {
    for(int i = 0, j = list.size() - 1; i < j; i++) {
        list.add(i, list.remove(j));
    }
    return list;
}
    
    public void sortProducts(List<Product> sortThis,int option, boolean descending){
        int negOne = (descending)?(1):(-1);
        int posOne = (descending)?(-1):(1);
        
        switch(option){
                case 0: //Id
                    Collections.sort(sortThis, new Comparator<Product>() {
                        public int compare(Product c1, Product c2) {
                            if (c1.getId() < c2.getId()) return negOne;
                            if (c1.getId() > c2.getId()) return posOne;
                            return 0;
                          }});
                    break;
                case 1: //Name
                    Collections.sort(sortThis, new Comparator<Product>() {
                        public int compare(Product c1, Product c2) {
                            return c1.getName().compareTo(c2.getName());
                          }});
                    if(descending)
                        sortThis = reverse(sortThis);
                    break;
                case 2: //Description?
                    break;
                case 3: //Quantity on Hand
                    Collections.sort(sortThis, new Comparator<Product>() {
                        public int compare(Product c1, Product c2) {
                            if (c1.getQuantityOnHand()< c2.getQuantityOnHand()) return negOne;
                            if (c1.getQuantityOnHand()> c2.getQuantityOnHand()) return posOne;
                            return 0;
                          }});
                    break;
                case 4: //Price
                    Collections.sort(sortThis, new Comparator<Product>() {
                        public int compare(Product c1, Product c2) {
                            if (c1.getPrice() < c2.getPrice()) return negOne;
                            if (c1.getPrice() > c2.getPrice()) return posOne;
                            return 0;
                          }});
                    break;
        }
    }
    
    public String searchName(){
        this.searchName=this.searchBy;
        this.searchPart=0;
        System.out.println("Searching for Name: " + this.searchName);
        return "userProduct";
    }
    
    public String searchId(){
        this.searchPart=Integer.parseInt(this.searchBy);
        this.searchName="";
        System.out.println("Searching for Id: " + this.searchPart);
        return "userProduct";
    }
    
    public String browseAllProducts(){
        this.searchPart=0;
        this.searchName="";
        this.searchBy="";
        return "userProduct";
    }
}
