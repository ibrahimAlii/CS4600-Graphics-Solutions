// Returns a 3x3 transformation matrix as an array of 9 values in column-major order.
// The transformation first applies scale, then rotation, and finally translation.
// The given rotation value is in degrees.
function GetTransform( positionX, positionY, rotation, scale )
{

	//console.log("scale: " + scale);
	console.log("rotation: " + rotation);

	return Array(
		 scale, Math.sin(rotation), 0,
		 Math.sin(rotation), scale, 0, 
		 positionX, positionY, 1 );
}

// Returns a 3x3 transformation matrix as an array of 9 values in column-major order.
// The arguments are transformation matrices in the same format.
// The returned transformation first applies trans1 and then trans2.
function ApplyTransform( trans1, trans2 )
{
	
	console.log("trans1: " + trans1);
	console.log("trans2: " + trans2);

	var scaleX = trans1[0] + trans2[0];
	var scaleY = trans1[4] + trans2[4];
	var roataionX = trans1[1] + trans2[1];
	var rotationY = trans1[3] + trans2[3];
	var translationX = trans1[6] + trans2[6];
	var translationY = trans1[7] + trans2[7];


	return Array(
		trans1[0], Math.sin(roataionX), 0,
		Math.sin(rotationY), trans2[4], 0,
		translationX, translationY, 1 
		)
}
