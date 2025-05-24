<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Staff" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Staff List</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 { color: #333; }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .search-form {
            margin-bottom: 20px;
        }
        .search-form input[type="text"], .search-form select, .search-form button {
            padding: 8px;
            margin-right: 10px;
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
        .status-active {
            color: green;
            font-weight: bold;
        }
        .status-inactive {
            color: red;
            font-weight: bold;
        }
        .status-select {
            padding: 4px;
            border-radius: 4px;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
    <h1>Staff List</h1>

    <%-- Display messages from servlet --%>
    <% String errorMessage = (String) request.getSession().getAttribute("errorMessage");
       String successMessage = (String) request.getSession().getAttribute("successMessage");
       if (errorMessage != null) {
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

    <div class="search-form">
        <form action="listStaff" method="get">
            <input type="text" name="query" placeholder="Search term">
            <select name="searchType">
                <option value="name">Name</option>
                <option value="position">Position</option>
                <option value="status">Status</option>
            </select>
            <button type="submit">Search</button>
        </form>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Position</th>
                <th>Address</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <%-- Retrieve staff list from request attribute --%>
            <% List<Staff> staffList = (List<Staff>) request.getAttribute("staffList");
               if (staffList != null && !staffList.isEmpty()) {
                   for (Staff staff : staffList) {
            %>
                       <tr>
                           <td><%= staff.getStaffID() %></td>
                           <td><%= staff.getName() %></td>
                           <td><%= staff.getEmail() %></td>
                           <td><%= staff.getPosition() %></td>
                           <td><%= staff.getAddress() %></td>
                           <td>
                               <form action="updateStaffStatus" method="post" style="margin: 0;">
                                   <input type="hidden" name="staffID" value="<%= staff.getStaffID() %>">
                                   <select name="status" class="status-select" onchange="this.form.submit()">
                                       <option value="active" <%= "active".equals(staff.getStatus()) ? "selected" : "" %>>Active</option>
                                       <option value="inactive" <%= "inactive".equals(staff.getStatus()) ? "selected" : "" %>>Inactive</option>
                                   </select>
                               </form>
                           </td>
                           <td>
                               <a href="editStaff?id=<%= staff.getStaffID() %>">Edit</a> |
                               <a href="deleteStaff?id=<%= staff.getStaffID() %>" onclick="return confirm('Are you sure you want to delete this staff member?');">Delete</a>
                           </td>
                       </tr>
            <%      }
               } else {
            %>
                   <tr>
                       <td colspan="7">No staff members found.</td>
                   </tr>
            <%  }
            %>
        </tbody>
    </table>

    <br/>
    <a href="createStaff.jsp">Add New Staff</a>
    <br/>
    <a href="landing.jsp">Back to Dashboard</a>

</body>
</html> 