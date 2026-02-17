<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Student Details</title>
</head>
<body>
    <h1>Operation Successful!</h1>

    <table border="1">
        <tr>
            <th>Roll Number</th>
            <th>Name</th>
            <th>Standard</th>
            <th>School Name</th>
        </tr>
        <tr>
            <td>${student.rollNo}</td>
            <td>${student.name}</td>
            <td>${student.standard}</td>
            <td>${student.schoolName}</td>
        </tr>
    </table>

    <br>
    <a href="/">Go Back to Landing Page</a>
</body>
</html>
