<?xml version="1.0" encoding="windows-1256" ?>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1256" />
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="resources/css/style.css" />
</head>
<body background="resources/css/images/body.jpg">

	<div class="container">

    <center><img src="resources/css/images/final.png" alt="ahmed" />	</center>
	<form action="Search" method="post">
	<center><input type="text" name="search" class="input" align="middle"/></center>
	 <center><input type="checkbox" name="soundex" id="optionDatabase" value="useDB" style="width: 30px;height: 22px; margin-top: 10px" /><label style="font-size: x-large; margin-right: 10px; color: navy;">Soundex</label> 
    <input type="checkbox" name="bigram" id="optionEmail" value="sendEmail"style="width: 30px;height: 22px; margin-top: 10px"/><label style="font-size: x-large; margin-right: 10px; color: navy;">Bigram</label></center>
	<center><input type="submit" value="search" name="button" class = "button"  /></center>
       
     </form>
	</div>
	
</body>
</html>