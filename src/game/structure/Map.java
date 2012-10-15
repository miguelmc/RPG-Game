package game.structure;

import game.Main;
import game.entities.Block;
import game.entities.Entity;
import game.entities.NPC;
import game.entities.Object;
import game.entities.Portal;
import game.entities.Tile;
import game.entities.item.Bundle;
import game.entities.superentities.Monster;
import game.entities.superentities.Player;
import game.entities.superentities.Spawner;
import game.util.TextureManager;
import game.util.XMLParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.lwjgl.util.Dimension;
import org.lwjgl.util.Point;
import org.newdawn.slick.opengl.Texture;

public class Map extends GameObject{
	
	private String NAME;
	private Point offset = new Point(0, 0);
	private Dimension size = new Dimension();
	private Player player;
	private Slot[][] matrix;
	private TextureManager textureManager = new TextureManager();
	private List<Spawner> spawners = new ArrayList<Spawner>();
	
	
	public final static int VIEW_LIMIT = 4;
	
	public Map(int id){
		this(id, null);
	}
	
	public Map(int id, TextureManager texManager){
		super(id);
		parseMap();
		loadTextures(texManager);
	}
	
	private void loadTextures(TextureManager prevTexManager) {
		
		//TODO load items of monsters
		//TODO recycle equipped items, and inventory
		
		List<Slot> slots = getAllSlots();
		
		for(Slot slot: slots){
			List<Entity> entities = slot.getAll();
			for(Entity entity: entities){
				if(prevTexManager != null){
					Texture tex = prevTexManager.get(entity.id());
					if(tex != null){
						textureManager.recycle(entity.id(), tex);
					}
				}else{
					if(!entity.isInvisible())
						textureManager.add(entity.id());
				}
				entity.setTexture(textureManager.get(entity.id()));
			}
			
			Monster monster = (Monster) slot.get(Slot.MONSTER);
			List<Integer> monsterDrops = new ArrayList<Integer>();
			if(monster != null){
				monsterDrops.addAll(monster.getDropsID());
			}
			
			for(Integer id: monsterDrops){
				if(prevTexManager != null){
					Texture tex = prevTexManager.get(id);
					if(tex != null){
						textureManager.recycle(id, tex);
					}
				}else{
					textureManager.add(id);
				}
			}
			
		}
		
	}
		
	private void parseMap() {
		
		XMLParser parser = new XMLParser("map/" + hexID() + ".xml");
		
		// Parse map info
		NAME = parser.getAttribute("Map", "name");
		size.setWidth(Integer.parseInt(parser.getAttribute("Map", "width")));
		size.setHeight(Integer.parseInt(parser.getAttribute("Map", "height")));
		
		// init matrix
		matrix = new Slot[size.getWidth()][size.getHeight()];
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[0].length; j++){
				matrix[i][j] = new Slot();
			}
		}
		
		// Parse tiles
		Queue<Integer> tileQueue = new LinkedList<Integer>();
		
		List<java.util.Map<String, String>> tiles = parser.getChildrenAttributes("Map/Tiles");
		for(java.util.Map<String, String> data: tiles){
			int id = Integer.parseInt(data.get("id"), 16);
			int amount = Integer.parseInt(data.get("amount"));
			for(int i=0; i<amount; i++){
				tileQueue.add(id);
			}
		}
		
		for(int i=0; i<size.getHeight(); i++){
			for(int j=0; j<size.getWidth(); j++){
				Tile tile = new Tile(tileQueue.poll());
				matrix[j][i].set(tile);
				tile.modifyPos(new Point(j, i));
			}
		}
		
		//Parse all other entities
		String xmlElements[] = {"Portals", "Monsters", "NPCs", "Objects"};
		
		for(String xmlElement: xmlElements){
			List<java.util.Map<String, String>> entities = parser.getChildrenAttributes("Map/" + xmlElement);
			for(java.util.Map<String, String> data: entities){
				Point position = new Point(Integer.parseInt(data.get("x")), Integer.parseInt(data.get("y")));
				add(Entity.createEntity(Integer.parseInt(data.get("id"), 16)), position);
				if(xmlElement.equals("Monsters")){
					spawners.add(new Spawner((Monster) get(position).get(Slot.MONSTER), 5000));
				}
			}
		}	
	}
	
	public void input() {
		getPlayer().input();
	}
	
	public void update(){
		for(Slot s: getAllSlots()){
			s.update();
		}
		
		for(Spawner spawner: spawners){
			spawner.update();
		}
		
	}
	
	public void render(){
		
		//Do not render slot by slot, but by entity type
		for(int i=0; i<7; i++){
			for(Slot s: getAllSlots()){
				Entity entity = s.get(i);
				if(entity != null)
					entity.render();
			}
		}

		for(int i=0; i<7; i++){
			for(Slot s: getAllSlots()){
				Entity entity = s.get(i);
				if(entity != null)
					entity.midRender();
			}
		}
		
		for(int i=0; i<7; i++){
			for(Slot s: getAllSlots()){
				Entity entity = s.get(i);
				if(entity != null)
					entity.UIRender();
			}
		}
		
	}
	
	public void add(Entity entity, Point position){
				
		entity.modifyPos(new Point(position)); // new Point to prevent it from being modified				
		get(position).set(entity);
		
		if(entity instanceof Object){
			for(Block block: ((Object)entity).getBlocks()){
				add(block, block.position());
			}
		}
				
		if(entity instanceof Player){
			player = (Player)entity;
			centerView();
		}
				
	}
	
	public void moveView(int horizontal, int vertical){
		if(getSize().getWidth() > Main.GRIDSIZE.getWidth())
			offset.setX(offset.getX() + horizontal);
		if(getSize().getHeight() > Main.GRIDSIZE.getHeight())
			offset.setY(offset.getY() + vertical);
	}
	
	public boolean isPlayerAt(Point pos) {
		return player.position().equals(pos);
	}
	
	public void removePlayer(){
		if(hasPlayer()){
			get(getPlayer().position()).remove(Slot.PLAYER);
			player = null;
		}
	}
	
	public boolean isPointInMap(Point p) {
		return p.getX() < size.getWidth() && p.getY() < size.getHeight() && p.getX() >= 0 && p.getY() >= 0; 
	}
	
	public static boolean isPointInGrid(Point p){
		return p.getX()>=0 && p.getY()>=0 && p.getX()<Main.GRIDSIZE.getWidth() && p.getY()<Main.GRIDSIZE.getHeight();
	}
	
	public void resetCamera() {
		moveView(-offset.getX(), -offset.getY());
	}
	
	public void centerView() {
		while(getPlayer().getX() >= Main.GRIDSIZE.getWidth() - Map.VIEW_LIMIT + offset.getX() &&
				getTileAt(new Point(Main.GRIDSIZE.getWidth() + getOffSet().getX(), 0)) != null){
				offset.setX(offset.getX()+1);
		}
		
		while(getPlayer().getY() >= Main.GRIDSIZE.getHeight() - Map.VIEW_LIMIT + offset.getY() &&
				getTileAt(new Point(0, Main.GRIDSIZE.getHeight() + getOffSet().getY())) != null){
				offset.setY(offset.getY()+1);
		}
		
	}
	
	public List<Entity> getStrongEntities(){
		
		List<Entity> strongEntities = new ArrayList<Entity>();
		
		for(Slot s: getAllSlots()){
			for(Entity e: s.getAll()){
				if(e.isStrong())
					strongEntities.add(e);
			}
		}
		
		return strongEntities;
	}

	public List<Entity> getStrongEntitiesAt(Point p){
		List<Entity> strongEntities = new ArrayList<Entity>();

		for (Entity e : get(p).getAll()) {
			if (e.isStrong()) {
				strongEntities.add(e);
			}
		}
			
		return strongEntities;
	}
	
	public List<Slot> getAllSlots(){
		List<Slot> slots = new ArrayList<Slot>();
		
		for(int i=0; i<size.getHeight(); i++){
			for(int j=0; j<size.getWidth(); j++){
				slots.add(matrix[j][i]);
			}
		}
		
		return slots;
	}
	
	public Slot get(Point p){ 
		if(p.getX() >= 0 && p.getX() < matrix.length && p.getY() >= 0 && p.getY() < matrix[0].length)
			return matrix[p.getX()][p.getY()];
		return null;
	}
	
	public NPC getNpcAt(Point p){
		if(!isPointInMap(p))
			return null;
		Entity entity = get(p).get(Slot.NPC);
		if(entity != null)
			return (NPC) entity;
		return null;
	}
	
	public Monster getMonsterAt(Point p){
		if(!isPointInMap(p))
			return null;
		Entity entity = get(p).get(Slot.MONSTER);
		if(entity != null)
			return (Monster) entity; 
		return null;
	}
	
	public Portal getPortalAt(Point p){
		if(!isPointInMap(p))
			return null;
		Entity entity = get(p).get(Slot.PORTAL);
		if(entity != null)
			return (Portal) entity;
		return null;
	}
	
	public Bundle getItemsAt(Point p){
		if(!isPointInMap(p))
			return null;
		Entity entity = get(p).get(Slot.ITEMS);
		if(entity != null)
			return (Bundle) entity;
		return null;
	}
	
	public Tile getTileAt(Point p){
		if(!isPointInMap(p))
			return null;
		Entity entity = get(p).get(Slot.TILE);
		if(entity != null)
			return (Tile) entity;
		return null;
	}
	
	public Object getObjectAt(Point p){
		if(!isPointInMap(p))
			return null;
		Entity entity = get(p).get(Slot.OBJECT);
		if(entity != null)
			return (Object) entity;
		return null;
	}
	
	public boolean hasPlayer(){ return player != null; }
	public boolean hasTileAt(Point p) { return isPointInMap(p); }
	public Player getPlayer(){ return player; }
	
	public Dimension getSize() { return size; }
	public Point getOffSet() { return offset; }
	public String getName(){ return NAME; }
	public TextureManager getTextureManager(){ return textureManager; }
	
	public void remove(Entity entity){ get(entity.position()).remove(entity); }
	
	public List<Entity> getEntitiesAt(Point pos) { return get(pos).getAll(); }
	public boolean hasStrongEntitiesAt(Point p){ return !getStrongEntitiesAt(p).isEmpty(); }

	public Portal getPortalByID(int id) {
		for(Slot s: getAllSlots()){
			Entity portal = s.get(Slot.PORTAL);
			if(portal != null){
				if(portal.id() == id)
					return (Portal)portal;
			}
			
		}
		return null;
	}
	
}
