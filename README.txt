Welcome (back) to the barebones image resizer!

This started out in 2006 as a "written in 1 hour" servlet for EOL that was meant to be a placeholder until a "real"
server-side image manipulation program could be purchased. It's still in use :)

It was briefly refactored to a separate app in 2009 in an attempt to make it easier to swap-in a better image engine
 than the standard Graphics2D it's using. That's still on the road map The remains of that project are here:




This latest version is a quick refactor of the above project with some added local file caching available. It also
  deprecates the *old* url-pattern based crop and resize instructions in favor of param based instructions. We did
  this to make it as easy as possible to develop,diagnose, and extend. The intent is to have any sort of CDN or cache-friendly
   URLs get re-written at the proxy level. This also conveniently forces a client to use a proxy to avoid slamming the resizer with
   a bunch of requests for the same image. When in doubt, abstract!

Anyway,  Here is the *old* url pattern:

IMAGESHOST/resize/WIDTH/HEIGHT/STARTX-STARTY-ENDX-ENDY/IMAGESRCPATH


For old images the image host is http://images.eonline.com
The width and height are obviously the dimensions of the image.
The start and end numbers are to allow you to crop.  These numbers are not required.

example: http://images.eonline.com/resize/66/66/0-0-293-293/eol_images/Entire_Site/20090311/293.romijn.oconnell.lc.031109.jpg


Here's the *new* pattern:

http:[host]/[orignal_image_host]/[image_path]/[file_name]?width=90&hight=90&x1=0&y1=0&x2=293&y2=293
  where:
  host = the host this code will be running on.
  original_image_host = host server serving the original image we're processing.
  image_path = path to the image on the original server
  width = resize the above image to this width (in pixels)
  height = resize the above image to this height (in pixels)
  x1,y1 = crop point start coordinates. The upper left corner of the image is at coordinates 0,0
  x2,y2, = crop point end coordinates. basically, "count this many pixels over from the starting pixels (x1,x2).


