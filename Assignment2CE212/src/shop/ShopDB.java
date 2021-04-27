
package shop;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator;

public class ShopDB {

    Connection con;
    static int nOrders = 0;
    static ShopDB singleton;

    public ShopDB() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            System.out.println("loaded class");
            con = DriverManager.getConnection("jdbc:hsqldb:file:\\tomcat\\webapps\\ass2\\shopdb", "sa", "");
            System.out.println("created con");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public static ShopDB getSingleton() {
        if (singleton == null) {
            singleton = new ShopDB();
        }
        return singleton;
    }

    public ResultSet getProducts() {
        try {
            Statement s = con.createStatement();
            System.out.println("Created statement");
            ResultSet rs = s.executeQuery("Select * from Product");
            System.out.println("Returning result set...");
            return rs;
        }
        catch(Exception e) {
            System.out.println( "Exception in getProducts(): " + e );
            return null;
        }
    }

    public Collection<Product> getAllProducts() {
        return getProductCollection("Select * from Product");
    }

    public Product getProduct(String pid) {
        try {
            // re-use the getProductCollection method
            // even though we only expect to get a single Product Object
            String query = "Select * from Product where PID = '" + pid + "'";
            Collection<Product> c = getProductCollection( query );
            Iterator<Product> i = c.iterator();
            return i.next();
        }
        catch(Exception e) {
            // unable to find the product matching that pid
            return null;
        }
    }

    public Collection<Product> getProductCollection(String query) {
        LinkedList<Product> list = new LinkedList<Product>();
        try {
            Statement s = con.createStatement();

            ResultSet rs = s.executeQuery(query);
            while ( rs.next() ) {
                Product product = new Product(
                        rs.getString("PID"),
                        rs.getString("Artist"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getInt("price"),
                        rs.getString("thumbnail"),
                        rs.getString("fullimage")
                );
                list.add( product );
            }
            return list;
        }
        catch(Exception e) {
            System.out.println( "Exception in getProductCollection(): " + e );
            return null;
        }
    }

    public void order(Basket basket , String customerName) {
        try {
            // create a unique order id
            String orderId = System.currentTimeMillis() + ":" + nOrders++;

            // iterate over the basket of contents ...

            Collection<Product> items = basket.getItems();
            String[] usedIDs = new String[items.size()];
            Iterator<Product> i = basket.getItems().iterator();
            while (i.hasNext()) {
                Product product = i.next();
                int quantity = basket.quantities.get(product.title);
                // and place the order for each one
                for(int j = 0; j < usedIDs.length; j++){
                    if(usedIDs[j] == null){
                        usedIDs[j] = product.PID;
                        order(con, product, orderId, quantity, customerName);
                        break;
                    } else if(usedIDs[j].equals(product.PID)){
                        break;
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void order(Connection con, Product p, String orderId, int quantity,  String customerName) throws Exception {
        String newName = "'%" + customerName + "%'";
        String newOrderId = "'%" + orderId + "%'";
        String newPID = "'%" + p.PID + "%'";
        try {
            PreparedStatement s = con.prepareStatement("insert into orders " +
                    "values ("+ newPID + ", "+ newOrderId +", " + newName + ", " + quantity + ", " + p.price + ")");
            System.out.println("Created statement");
            s.executeUpdate();
            System.out.println("Inserting order...");
        }
        catch(Exception e) {
            System.out.println( "Exception in order(): " + e );
        }
    }


}
