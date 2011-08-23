<%@ page isErrorPage="true" import="java.io.*"%>
<!DOCTYPE HTML>
               <html>
                <head>
               <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
               <title>Putty - On-the-Fly Image Utilities</title>
               <style media="all" type="text/css">
               html, body { width: 100&; min-height: 100%; }
               html, body, hr, p { padding: 0; margin: 0; }
               body { font-size: 14px; font-family: Arial, Helvetica, sans-serif; color: #666;
               background-repeat: repeat;
               background-image: url(/images/bg_textured.jpg);
               background-image: -moz-linear-gradient(top, rgba(0,0,0,0.81) 0%, rgba(0,0,0,0) 100%), url(/images/bg_textured.jpg); /* FF3.6+ */
               background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(0,0,0,0.81)), color-stop(100%,rgba(0,0,0,0))), url(/images/bg_textured.jpg); /* Chrome,Safari4+ */
               background-image: -webkit-linear-gradient(top, rgba(0,0,0,0.81) 0%,rgba(0,0,0,0) 100%), url(/images/bg_textured.jpg); /* Chrome10+,Safari5.1+ */
               background-image: -o-linear-gradient(top, rgba(0,0,0,0.81) 0%,rgba(0,0,0,0) 100%), url(/images/bg_textured.jpg); /* Opera11.10+ */
               background-image: -ms-linear-gradient(top, rgba(0,0,0,0.81) 0%,rgba(0,0,0,0) 100%), url(/images/bg_textured.jpg); /* IE10+ */
               background-image: linear-gradient(top, rgba(0,0,0,0.81) 0%,rgba(0,0,0,0) 100%), url(/images/bg_textured.jpg); /* W3C */ }
               .header, .header .logo { display: block; height: 38px; }
               .header { background: #1c1b1b; *zoom: 1; position: relative;
               		-webkit-box-shadow: 0px 1px 24px 0px rgba(0,0,0,1);
               		-moz-box-shadow: 0px 1px 24px 0px rgba(0,0,0,1);
               		box-shadow: 0px 1px 24px 0px rgba(0,0,0,1);}
               .header .logo { background-position: center center; background-repeat: no-repeat; position: absolute; }
               .header .logo.ptg { background-image: url(/images/logo-ptg.png); width: 136px; top: 0; left: 14px;}
               .header .logo.nbcu { background-image: url(/images/logo-nbcu.png); width: 96px; top: 0; right: 14px;}
               .content { width: 663px; background: #fff; margin: 83px auto;
               			-webkit-border-radius: 28px;
               			-moz-border-radius: 28px;
               			border-radius: 28px;
               			-webkit-box-shadow: 0px 5px 21px 0px rgba(0,0,0,.63);
               			-moz-box-shadow: 0px 5px 21px 0px rgba(0,0,0,.63);
               			box-shadow: 0px 5px 21px 0px rgba(0,0,0,.63); }
               	.content .wrapper { padding: 313px 28px 61px 28px; background: url(/images/logo-putty.png) 31px 69px no-repeat; }
               	.content .innerwrapper { border-top: 1px #eee solid; padding: 29px 7px; }
               	.content .parameters { color: #000; font-size: 24px; font-weight: bold; text-align: center; margin: 11px 0; }
               	.content .urls { background: #eaf5ff; font-size: 12px; color: #001d37; font-family: Courier, monospace; padding: 22px; margin: 17px 0; word-break: break-all; word-wrap: break-word; }
               </style>
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
                           <p>500: Error</p>
                           <tt>
                           <% exception.printStackTrace(new PrintWriter(out)); %>

                           </tt>

<!-- End Text-->

                           </div>
                       </div>
                   </div>
               </body>
               </html>
