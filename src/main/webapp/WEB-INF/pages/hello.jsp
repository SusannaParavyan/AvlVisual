<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>


<html>
<head>
    <title>Avl-tree</title>
</head>
<body>

<c:if test="${exception!=null}">
    <div>${exception}</div>
</c:if>
<form action="/clear" method="get">
    <input type="submit" value="Clear tree">
</form>

<form action="/insert" method="get">
    <br>
    <label for="valueToAdd">Value: </label>
    <input type="number" id="valueToAdd" name="valueToAdd">
    <input type="submit" value="Add">
</form>
<form action="/delete" method="get">
    <br>
    <label for="valueToDelete">Value: </label>
    <input type="number" id="valueToDelete" name="valueToDelete">
    <input type="submit" value="Delete">
</form>

<div>
    <img src="/images/color_gray.png"> - Old node
    <br>
    <img src="/images/color_green.png"> - New node
    <br>
    <img src="/images/color_yellow.png"> - Modified node
    <br>
</div>
<br>

<h1>
    AVL Tree:
</h1>
<h2>
    ${representation}
</h2>
</body>

</html>