<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Student Details</title>
</head>
<body>
    <h1>Student Details</h1>

    <c:choose>
        <c:when test="${not empty student}">
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
        </c:when>
        <c:otherwise>
            <p>No student found with the given ID.</p>
        </c:otherwise>
    </c:choose>

    <br>
    <a href="/">Go Back to Landing Page</a>
</body>
</html>
