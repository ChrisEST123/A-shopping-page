<%@ page import="shop.Product"%>

<jsp:useBean id='db'
             scope='session'
             class='shop.ShopDB' />


<html>

<style>
    <jsp:include page="css/shop_1906001.css"/>
</style>

<head>
<title>My Shop</title>
</head>
<body>
<%
    String pid = request.getParameter("pid");
    Product product = db.getProduct(pid);
    // out.println("pid = " + pid);
    if (product == null) {
        // do something sensible!!!
        out.println( product );
    }
    else {
        %>
        <div align="center">
        <h2 id="titleCombo"> <%= product.title %>  by <%= product.artist %> </h2>
        <img src="<%= product.fullimage %>" />
            <p>Price: <%= product.getPrice() %></p>
        <p id="description"> <%= product.description %> </p>
            <p><a href='<%="basket.jsp?addItem="+product.PID%>'> add to basket</a></p>
            <form action="index.jsp" method="post">
                <input type="hidden" name="goBack">
                <input type="submit" value="Return to Home Page"/>
            </form>
        </div>
        <%
    }
%>
</body>
</html>
