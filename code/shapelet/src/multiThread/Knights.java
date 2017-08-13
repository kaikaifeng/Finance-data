package multiThread;

import java.util.EnumSet;
import java.util.HashMap;

public interface Knights {
	public enum Name { 
		Roland("Roland", 1),
		Rinaldo("Rinaldo", 2),
		Turpin("Turpin", 3),
		Malagigi("Malagigi", 4),
		Ogier("Ogier", 5),
		Solomon("Solomon", 6),
		Astolpho("Astolpho", 7),
		DukeOfFavaria("DukeOfFavaria", 8),
		Fierambras("Fierambras", 9),
		Florismart("Florismart", 10),
		Oliver("Oliver", 11),
		Ganelon("Ganelon", 12);
		
		private String name;
		private int position;
		private static final HashMap<Integer, Name> names = new HashMap<Integer, Name>();
		
		static{
			for (Name name : EnumSet.allOf(Name.class)) {
				names.put(name.position, name);
			}
		}
		
		private Name(String name, int position){
			this.name = name;
			this.position = position;
		}
		
		public String getName() {
			return name;
		}
		
		public int getPosition() {
			return position;
		}
		
		public Name getKnight(int number){
			return names.get(new Integer(number));
		}
	}
}
