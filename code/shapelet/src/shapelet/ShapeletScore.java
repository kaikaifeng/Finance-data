package shapelet;

public class ShapeletScore implements Comparable<ShapeletScore>{
	private Shapelet shapelet;
	private double score;
	
	public ShapeletScore(Shapelet shapelet, double score){
		this.shapelet = shapelet;
		this.score = score;
	}
	
	public void setShapelet(Shapelet shapelet) {
		this.shapelet = shapelet;
	}
	
	public Shapelet getShapelet() {
		return shapelet;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public double getScore() {
		return score;
	}

	@Override
	public int compareTo(ShapeletScore o) {
		if(this.score < o.score){
			return -1;
		}
		else if (this.score > o.score) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	@Override
	public String toString(){
		return shapelet.getStringBuffer().append(score).append(",").append(shapelet.getFrom()).toString();
	}
}
