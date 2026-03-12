<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Welcome Page</title>
</head>
<body>
    <h3>Hello, welcome to a Spring Boot JSP application!</h3>
    <!-- <%= request.getParameter("user") %> -->
    <!-- <h3> We see you are: </h3> -->
    <!-- <%= session.getAttribute("un") %> -->
     Welcome, ${ user }
    <p>The current server time is: <%= new java.util.Date() %></p>
</body>
</html>
