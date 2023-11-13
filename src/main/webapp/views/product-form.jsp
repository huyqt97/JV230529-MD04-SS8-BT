
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product Form</title>
</head>
<body>
<h2>Product Form</h2>
<form action="product" method="post">
    <input type="hidden" name="action" value="${product != null ? 'update' : 'add'}">
    <input type="hidden" name="id" value="${product != null ? product.id : ''}">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" value="${product != null ? product.name : ''}" required><br>
    <label for="price">Price:</label>
    <input type="text" id="price" name="price" value="${ product.price}" required><br>
    <label for="description">Description:</label>
    <textarea id ="description" name="description">${product != null ? product.description : ''}</textarea><br>
    <label for="producer">Manufacturer:</label>
    <input type="text" id="producer" name="producer" value="${product != null ? product.producer : ''}" required><br>
    <input type="submit" value="${product != null ? 'Update' : 'Add'}">
</form>
<br>
<a href="product?action=list">Back to Product List</a>
</body>
</html>
