importClass(Packages.org.lwjgl.util.Point);
var size = 3+step;
for(var i=0; i<size; i++)
	{
	for(var j=0; j<size; j++)
		{
			if(i==0 || j==0 || i==size-1 || j==size-1)
				sm.play(new Point(i-size/2, j-size/2));//sm.attack(new Array(new Point(i-size/2, j<size/2)))
		}
	}
if(step == 6)
	sm.stop();


