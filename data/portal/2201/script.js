importClass(Packages.org.lwjgl.util.Point);
if(pm.getMapID() == 0)
{
	if(pm.playerFacingDir() == pm.UP)
		pm.setMap(1);
}else if(pm.getMapID() == 1)
{
	if(pm.playerFacingDir() == pm.DOWN)
		pm.setMap(0);
}else if(pm.getMapID() == 0008)
{
	pm.setMap(0, new Point(13, 8));
}