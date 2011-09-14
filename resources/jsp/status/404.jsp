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
   	<div class="wrapper"><p><b>404: Not Found.</b></p>
       	<div class="innerwrapper">
         <ul>
       	<!-- TEXT GOES HERE! -->

           <p><%= messageString %></p>

<!-- End Text-->
         </ul>
<br/>&nbsp;<br/>
<b>Questions?</b>
      <ul>
        Join the discussion or ask a question <a href="http://groups.google.com/group/ptg-putty-users">here</a>
      </ul>

           </div>
       </div>
   </div>
</body>
</html>
