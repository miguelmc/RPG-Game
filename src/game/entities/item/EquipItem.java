package game.entities.item;

import game.features.Stat;
import game.util.XMLParser;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.Point;

public class EquipItem extends Item{
	
	private java.util.Map<Integer, Integer> stats;
	private EquipType type;

	public EquipItem(int id, Point pos) {
		super(id, pos);
	}
	
	public EquipItem(int id){
		this(id, null);
	}
	
	@Override
	protected void parseItem(XMLParser parser){
		
		super.parseItem(parser);
		
		stats = new HashMap<Integer, Integer>();
		
		type = EquipType.getType(parser.getAttribute("EquipItem", "type"));
		
		Map<String, String> statAttributes = parser.getAttributes("EquipItem/stats");
				
		stats.put(Stat.MAXHP.ID, Integer.parseInt(statAttributes.get("maxHP")));
		stats.put(Stat.MAXMP.ID, Integer.parseInt(statAttributes.get("maxMP")));
		stats.put(Stat.ATK.ID, Integer.parseInt(statAttributes.get("atk")));
		stats.put(Stat.STR.ID, Integer.parseInt(statAttributes.get("str")));
		
	}
	
	public EquipType getType(){
		return type;
	}
	
	public int getStat(int stat){
		return stats.get(stat);
	}

	public boolean equals(Object obj){
		if(!(obj instanceof EquipItem))
			return false;
		return stats.equals(((EquipItem)obj).getStats()) && id() == ((EquipItem)obj).id();
	}
	
	public int hashCode(){
		return id() + 99999 + stats.hashCode();
	}
	
	public Map<Integer, Integer> getStats(){
		return stats;
	}
	
	public enum EquipType{
		
		TOPWEAR("topwear"), BOTTOMWEAR("bottomwear"), HELMET("helmet"), SHOES("shoes"), WEAPON("weapon");
		
		private String name;
		
		private EquipType(String name){
			this.name = name;
		}
		
		public static EquipType getType(String name){
			
			for(EquipType type: EquipType.values()){
				if(type.toString().equals(name))
					return type;
			}
			
			return null;
			
		}
		
		public String toString(){
			return name;
		}
		
	}
	
}
