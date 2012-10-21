if(pm.getMapID() == 0){
	if(pm.playerFacingDir() == pm.UP){
		pm.setMap(0001);
	}
}else if(pm.getMapID() == 1){
	if(pm.playerFacingDir() == pm.DOWN){
		pm.setMap(0000);
	}
}