<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Staff" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Staff Member</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 { color: #333; }
        form div {
            margin-bottom: 15px;
        }
        label {
            display: inline-block;
            width: 100px;
            margin-bottom: 5px;
        }
        input[type="text"], input[type="email"] {
            width: 250px;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        button {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
        .error-message { color: red; }
        .success-message { color: green; }
    </style>
</head>
<body>
    <h1>Edit Staff Member</h1>

    <%-- Retrieve staff object from request attribute --%>
    <% Staff staff = (Staff) request.getAttribute("staff");
       String errorMessage = (String) request.getSession().getAttribute("errorMessage");
       String successMessage = (String) request.getSession().getAttribute("successMessage");

       if (staff == null) {
    %>
           <p class="error-message">Staff member not found or unable to load details.</p>
           <br/>
           <a href="listStaff">Back to Staff List</a>
    <% } else {
    %>
           <%-- Display messages from servlet --%>
           <% if (errorMessage != null) {
           %>
                  <p class="error-message"><%= errorMessage %></p>
           <% request.getSession().removeAttribute("errorMessage");
              }
              if (successMessage != null) {
           %>
                  <p class="success-message"><%= successMessage %></p>
           <% request.getSession().removeAttribute("successMessage");
              }
           %>

           <form action="editStaff" method="post">
               <input type="hidden" name="staffID" value="<%= staff.getStaffID() %>">
               <div>
                   <label for="name">Name:</label>
                   <input type="text" id="name" name="name" value="<%= staff.getName() %>" required>
               </div>
               <div>
                   <label for="email">Email:</label>
                   <input type="email" id="email" name="email" value="<%= staff.getEmail() %>" required>
               </div>
               <div>
                   <label for="position">Position:</label>
                   <input type="text" id="position" name="position" value="<%= staff.getPosition() %>">
               </div>
               <div>
                   <label for="address">Address:</label>
                   <input type="text" id="address" name="address" value="<%= staff.getAddress() %>">
               </div>
               <div>
                   <button type="submit">Update Staff</button>
               </div>
           </form>

           <br/>
           <a href="listStaff">Cancel and Back to List</a>
    <% } %>

</body>
</html> 