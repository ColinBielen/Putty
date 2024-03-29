<%@ page isErrorPage="true" import="java.io.*"%>
<%

	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	if(exception != null) {
		exception.printStackTrace(pw);
	}
	String trace = sw.toString();
	trace.replaceAll("\n","<br/>\n"); %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Putty - Internal Error</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/putty.css" />

</head>

<body>
	<div class="header">
   	<span class="logo ptg" title="PTG Platform Technology Group"></span>
       <span class="logo nbcu" title="NBCUniversal"></span>
   </div>
   <div class="content">
   	<div class="wrapper">
       	<div class="innerwrapper">

       	<!-- TEXT GOES HERE! -->
           <p><b>500: Error</b></p>
           <p class="stacktrace">

            <%= trace %>
           </p>
<!-- End Text-->

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
