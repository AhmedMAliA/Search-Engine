<?xml version="1.0" encoding="windows-1256" ?>
<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1256" />
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="resources/css/style.css" />
</head>
<body background="resources/css/images/body.jpg">

	<div class="smallContainer">
		
			<form action="Result" method="get">
			<center>
				<input type="text" name="search" value="${search}"
					class="smallInput" align="middle" /> <input type="submit"
					value="search" class="smallButton" />
					<br />
					
					
					
			
		</center>
		<input type="checkbox" name="soundex" id="optionDatabase" value="useDB" class="squaredTwo" /><label style="font-size: x-large; margin-right: 10px; color: navy;">Soundex</label> 
    <input type="checkbox" name="bigram" id="optionEmail" value="sendEmail" class="roundedTwo"/><label style="font-size: x-large; margin-right: 10px; color: navy;">Bigram</label>
		</form>
	</div>
	<% if(request.getParameterValues("bigram")!=null){%>
	<div class="cardView">
	<label style="font-size: 40px; margin-left: 20px; margin-right: 20px; text-align: justify;">Did you mean</label><br />
	<c:forEach var="book" items="${mean}">
		
				<a href="Result?search=${book}" style="font-size: 40px; margin-left: 20px; margin-right: 20px; text-align: justify;"  >${book}
				</a><br />
				<br />
		</c:forEach>
	</div>

 
 <%}else{ %>
	<div class="hrefContainer">

		<<c:forEach var="book" items="${list}">
		
				<a href="${book}" style="font-size: 40px; margin-left: 20px; margin-right: 20px; text-align: justify;" >${book}
				</a><br />
				<br />
		</c:forEach>

	</div>
	<%} %>
</body>
</html>