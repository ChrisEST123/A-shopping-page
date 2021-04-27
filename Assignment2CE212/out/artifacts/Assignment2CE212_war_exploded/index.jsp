<%@ page import="shop.Product"%>
<%@ page import="java.sql.SQLOutput" %>

<jsp:useBean id='db'
             scope='session'
             class='shop.ShopDB' />

<%
    boolean titles = false;
    boolean artists = false;
    String titleSearch = request.getParameter("titleSearch");
    String artistSearch = request.getParameter("artistSearch");
    if(titleSearch == null){
        titleSearch = "";
    }
    if(artistSearch == null){
        artistSearch = "";
    }
    if(!titleSearch.equals("")){
        titles = true;
    }
    if(!artistSearch.equals("")){
        artists = true;
    }
%>


<html>
<style>
    <jsp:include page="css/shop.css"/>
</style>

<head>
<title>My Shop</title>
    <script>showAll()</script>
</head>
<body>

<p id="indexP">Search by artist name:</p>

<form id="indexP" action="index.jsp" method="post">
    <input type="text" name="artistSearch" size="20">
    <input type="submit" value="Search" />
</form>

<p id="indexP">Search by product title:</p>
<form id="indexP" action="index.jsp" method="post">
    <input type="text" name="titleSearch" size="20">
    <input type="submit" value="Search"/>
</form>

<table>
    <tr>
        <th> Title </th> <th> Price </th> <th> Picture </th>
    </tr>
    <%
        String temp = "";
        boolean goAll = true;
        boolean check = false;
        boolean artistFail = false;
        boolean titleFail = false;
        String failure = "";
        if(artists == true){
            for (Product product : db.getAllProducts() ) {
                if(artistSearch.equals(product.artist)){
    %>
                    <tr>
                        <td> <%= product.title %> </td>
                        <td> <%= product.price %> </td>
                        <td> <a href = '<%="viewProduct.jsp?pid="+product.PID%>'> <img src="<%= product.thumbnail %>"/> </a> </td>
                    </tr>
                    <%
                    check = true;
                }
            }
            if(check == true){
                goAll = false;
            } else {
                artistFail = true;
                failure = "No artists with the given name exist here(check that your " +
                        "characters are written in correct cases!)";
            }
        } else if(titles == true){
            titleSearch = titleSearch.toLowerCase();
            for (Product product : db.getAllProducts() ) {
                String title = product.title.toLowerCase();
                if(titleSearch.length() > 1 && title.contains(titleSearch)){
                    %>
                    <tr>
                        <td> <%= product.title %> </td>
                        <td> <%= product.price %> </td>
                        <td> <a href = '<%="viewProduct.jsp?pid="+product.PID%>'> <img src="<%= product.thumbnail %>"/> </a> </td>
                    </tr>
                    <%
                check = true;
                }
            }
            if(check == true){
                goAll = false;
            } else {
                titleFail = true;
                failure = "There are no products like this in the database or the search " +
                        "was not specific enough(more than 1 character)";
            }
        }
        if(goAll == true) {
            for (Product product : db.getAllProducts() ) {

            %>
            <tr>
                <td> <%= product.title %> </td>
                <td> <%= product.price %> </td>
                <td> <a href = '<%="viewProduct.jsp?pid="+product.PID%>'> <img src="<%= product.thumbnail %>"/> </a> </td>
            </tr>
            <%
            }
        }
    %>
</table>

<%
    if(titleFail == true || artistFail == true){
%>
    <pre id="Error"> <%=failure%> </pre>
     <%
    }
%>

<form id="indexP" action="index.jsp" method="post">
    <input type="hidden" name="returnNormal">
    <input type="hidden" name="artistSearch" value="<%=temp%>">
    <input type="hidden" name="titleSearch" value="<%=temp%>">
    <input type="submit" value="See all products"/>
</form>

<form id="indexP" action="basket.jsp" method="post">
    <input type="hidden" name="goBasket">
    <input type="submit" value="See Basket"/>
</form>
</body>
</html>
