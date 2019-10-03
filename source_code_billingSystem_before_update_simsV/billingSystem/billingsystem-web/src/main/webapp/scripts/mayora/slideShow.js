var dimages=new Array();
var numImages=4;

  dimages[0]=new Image();
  dimages[0].src="images/web-banner_edc2.jpg";
  dimages[1]=new Image();
  dimages[1].src="images/web_cashback500x160pixel_a.jpg";
  dimages[2]=new Image();
  dimages[2].src="images/web_telkomsel_450menit.jpg";
  dimages[3]=new Image();
  dimages[3].src="images/web_cashback500x160pixel_b.jpg";      
  
  
var curImage=-1;

function swapPicture()
{
  if (document.images)
  {
    var nextImage=curImage+1;
    if (nextImage>=numImages)
      nextImage=0;
    if (dimages[nextImage] && dimages[nextImage].complete)
    {
      var target=0;
      if (document.images.myImage)
        target=document.images.myImage;
      if (document.all && document.getElementById("myImage"))
        target=document.getElementById("myImage");
  
      // make sure target is valid.  It might not be valid
      //   if the page has not finished loading
      if (target)
      {
        target.src=dimages[nextImage].src;
        curImage=nextImage;
      }

      setTimeout("swapPicture()", 3000);

    }
    else
    {
      setTimeout("swapPicture()", 500);
    }
  }
}

setTimeout("swapPicture()", 5000);