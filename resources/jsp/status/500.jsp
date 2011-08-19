<pre name="code" class="core">
<%@ page isErrorPage="true" import="java.io.*"%>

<div id="stacktrace" style="display:none;">
<pre>
<%
// if there is an exception
if (exception != null) {
// print the stack trace hidden in the HTML source code for debug
exception.printStackTrace(new PrintWriter(out));
}
%>
</pre>
</div>
</pre>

