// bgImg is the background image to be modified.
// fgImg is the foreground image.
// fgOpac is the opacity of the foreground image.
// fgPos is the position of the foreground image in pixels. It can be negative and (0,0) means the top-left pixels of the foreground and background are aligned.
function composite( bgImg, fgImg, fgOpac, fgPos )
{

	var bgPixels = bgImg.data;
	var fgPixels = fgImg.data;

	var bgWidth = bgImg.width;
	var bgHeight = bgImg.height;
	var fgWidth = fgImg.width;
	var fgheight = fgImg.height;
	
	for (var y = 0; y < fgheight; y++) {
		for (var x = 0; x < fgWidth; x++) {
		var fgRed = y * (fgWidth * 4) + x * 4;
		var fgGreen = fgRed + 1;
		var fgBlue = fgRed + 2;
		var fgAlpha = fgRed + 3;

		var bgRed = (y + fgPos.y) * (bgWidth * 4) + (x + fgPos.x) * 4;
		var bgGreen = bgRed + 1;
		var bgBlue = bgRed + 2;
		var bgAlpha = bgRed + 3;

		// some techniques :

		  // Additive Blending
		// c = alphaForeground * colorForeground + colorBackground  

		  // Difference Blending 
		// c = Math.abs(alphaForefround * colorForeground - colorBackground)

		

		 // Multiply Blending:
		// c = colorForeground * colorBackground

		// c = alphaForeground * (colorForeground * colorBackground) + (1 - alphaForeground) * colorBackground
		
		  // Screen Blending
		// c = 1 - (1 - colorForeground) * (1 - colorBackground)	


		bgPixels[bgRed] = fgOpac * fgPixels[fgRed] + bgPixels[bgRed];
		bgPixels[bgGreen] = fgOpac * fgPixels[fgGreen] + bgPixels[bgGreen];
		bgPixels[bgBlue] = fgOpac * fgPixels[fgBlue] + bgPixels[bgBlue];
		bgPixels[bgAlpha] = fgOpac * fgPixels[fgAlpha] + bgPixels[bgAlpha];


		}
	}

}
