<!DOCTYPE HTML>
<%
  String messageString = "";
  if(request.getAttribute("message") != null) {
    messageString = String.valueOf(request.getAttribute("message"));
  }
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Putty: Resource Not Found</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/putty.css" />
</head>

<body>
	<div class="header">
   	<span class="logo ptg" title="PTG Platform Technology Group"></span>
       <span class="logo nbcu" title="NBCUniversal"></span>
   </div>
   <div class="content">
   	<div class="wrapper"><p>404: Not Found.</p>
       	<div class="innerwrapper">

       	<!-- TEXT GOES HERE! -->

           <p><%= messageString %></p>

<!-- End Text-->

           </div>
       </div>
   </div>
</body>
</html>
