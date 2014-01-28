<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%><%
response.sendRedirect("http://" + request.getHeader("host") + request.getContextPath() + "/index.htm");
%>