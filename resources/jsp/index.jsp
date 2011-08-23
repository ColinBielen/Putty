<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Putty: On-the-Fly Image Utilities</title>

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

<p><b>Usage:</b>
<p class="urls">http://<%= request.getServerName()+":"+request.getServerPort()+ request.getContextPath() %>/V1/images.eonline.com/eol_images/Entire_Site/2011522/reg_634.kim.k.lc.062211.jpg?width=200&height=200&x1=0&y1=0&x2=293&y2=293</p>
<p class="instructions">
Where:<br/><br/>


<b>V1</b> = API version <br/>
<b>images.eonline.com</b> = server containing the original image <br/>
<b>eol_images/Entire_Site/2011522/reg_634.kim.k.lc.062211.jpg</b> = path and file name of original image on the site.<br/>
<b>width</b> = resize the above image to this width (in pixels) <br/>
<b>height</b> = resize the above image to this height (in pixels)  <br/>
<b>x1,y1</b> = crop point start coordinates. The upper left corner of the image is at coordinates 0,0 <br/>
<b>x2,y2</b> = crop point end coordinates. basically, "count this many pixels over from the starting pixels (x1,x2)".<br/>


</p> 
</p>

<p><b>Legacy URL Structure:</b></p>
<p class="urls">
http://<%= request.getServerName()+":"+request.getServerPort()+ request.getContextPath() %>/resize/66/66/0-0-293-293/eol_images/Entire_Site/20090311/293.romijn.oconnell.lc.031109.jpg</p>
<p class="instructions">

Where:<br/><br/>

<b>/66/66</b> = width & height resize values.<br/>
<b>0-0-293-293</b> = crop arguments.<br/>
<b>/20090311/293.romijn.oconnell.lc.031109.jpg</b> = image path on "images.eonline.com"<br/>
</p>
</p>
<p>Legacy urls are provided for older code pointing at images.eonline.com.</p>
</div>
</div>
</div>
</body>
</html>
