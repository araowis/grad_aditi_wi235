<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Student Portal</title>
</head>
<body>
    <h1>Student Creation and Updation Portal</h1>

    <h2>Register Student</h2>
    <form action="/insert-student" method="POST">
        Roll Number: <input type="number" name="studentId" required><br>
        Name: <input type="text" name="studentName" required><br>
        Standard: <input type="number" name="studentStandard"><br>
        School Name: <input type="text" name="studentSchoolName"><br>
        <br>
        <input type="submit" value="INSERT">
        <input type="reset" value="RESET">
    </form>

    <h2>Get Student Details</h2>
    <form action="/get-student" method="GET">
        Roll Number: <input type="number" name="studentId" required><br>
        <br>
        <input type="submit" value="GET STUDENT">
    </form>

    <h2>Update Student</h2>
    <form action="/update-student" method="POST">
        Roll Number: <input type="number" name="studentId" required><br>
        Name: <input type="text" name="studentName"><br>
        Standard: <input type="number" name="studentStandard"><br>
        School Name: <input type="text" name="studentSchoolName"><br>
        <br>
        <input type="submit" value="UPDATE">
    </form>
</body>
</html>
