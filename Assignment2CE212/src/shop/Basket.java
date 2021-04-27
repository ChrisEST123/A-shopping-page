package shop;

import java.text.DecimalFormat;
import java.util.*;

public class Basket {

    Collection<Product> items;
    ShopDB db;
    public Map<String, Integer> quantities = new HashMap();

    public Basket() {
        db = ShopDB.getSingleton();
        items = new ArrayList<Product>();
        for (Product product : db.getAllProducts() ) {
            quantities.put(product.title, 0);
        }
    }

    /**
     *
     * @return Collection of Product items that are stored in the basket
     *
     * Each item is a product object - need to be clear about that...
     *
     * When we come to list the Basket contents, it will be much more
     * convenient to have all the product details as items in this way
     * in order to calculate that product totals etc.
     *
     */
    public Collection<Product> getItems() {
        return items;
    }

    /**
     * empty the basket - the basket should contain no items after calling this method
     */
    public void clearBasket() {
        items.clear();
        quantities.clear();
        for (Product product : db.getAllProducts() ) {
            quantities.put(product.title, 0);
        }
    }

    public void removeItem(String title, String pid){
        quantities.put(title, quantities.get(title) - 1);
        for(Iterator<Product> i = items.iterator(); i.hasNext();){
            if(pid.equals(i.next().PID)){
                i.remove();
                break;
            }
        }
    }

    /**
     *
     *  Adds an item specified by its product code to the shopping basket
     *
     * @param pid - the product code
     */
    public void addItem(String pid) {

        // need to look the product name up in the
        // database to allow this kind of item adding...

        addItem( db.getProduct( pid ) );

    }

    public void addItem(Product p) {
        // ensure that we don't add any nulls to the item list
        if (p != null ) {
            items.add( p );
        }
    }

    /**
     *
     * @return the total value of items in the basket in pence
     */
    public int getTotal() {
        int total = 0;
        // iterate over the set of products...
        for (Product e: items) {
            total += e.price;
        }

        // return the total
        return total;
    }

    /**
     *
     * @return the total value of items in the basket as
     * a pounds and pence String with exactly two decimal places - hence
     * suitable for inclusion as a total in a web page
     */
    public String getTotalString() {
        int total = getTotal();
        if(total == 0){
            return "";
        }
        double realTotal = total * 0.01;

        DecimalFormat df = new DecimalFormat("#.00");
        String totalChanged = df.format(realTotal);

        return "£" + totalChanged;
    }

    public void quantitiesPut(String title, int curQuant){
        quantities.put(title, curQuant + 1);
    }

    /*public void addItemQuant(String pid, String title, int curQuant){
        addItem( db.getProduct( pid ) );

        quantities.put(title, curQuant + 1);
    }*/
}
