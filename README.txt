Welcome to the barebones image resizer!

Here is the *old* url pattern:

IMAGESHOST/resize/WIDTH/HEIGHT/STARTX-STARTY-ENDX-ENDY/IMAGESRCPATH


The images host is something like http://images.eonline.com
The width and height are obviously the dimensions of the image.
The start and end numbers are to allow you to crop.  These numbers are not required.

example: http://images.eonline.com/resize/66/66/0-0-293-293/eol_images/Entire_Site/20090311/293.romijn.oconnell.lc.031109.jpg


Here;s the revised pattern:

http:[host]/[orignal_image_host]/[image_path]/[file_name].[scaling_instructions],[cropping_instructions].[file_extension]
  where:
  host = the host this code will be running on.
  original_image_host = host server serving the original image we're processing.
  image_path = path to the image on the original server
  scaling instructions = [width]-[height] resize (e.g. 90_90)
  cropping instructions = [x_origin]-[y_origin]-[x_offset]-[y_offset]
       (e.g. 100-100-193-193 => "start at position 100,100 on the image and crop it at position 293,293)
  file extension= ".jpg",etc



Currently only Jpgs are supported. We're all about future growth.