package probus.jam.entity;

import java.util.Comparator;

public class YOrderComparator implements Comparator<Entity>
{
	public int compare(Entity e1, Entity e2)
	{
		if (e1 instanceof Beam) {
			if (e2 instanceof Beam) {
				return 0;
			}
			else {
				return 1;
			}
		}
		else if (e2 instanceof Beam) {
			return -1;
		}
		
		if(e1.y < e2.y)
			return -1;
		
		if(e1.y > e2.y)
			return 1;
		
		return 0;
	}
}