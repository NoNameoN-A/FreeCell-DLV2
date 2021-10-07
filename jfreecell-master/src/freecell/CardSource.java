package freecell;

public interface CardSource {
	public Card remove();
	public Card peek();
	public boolean canRemove();
	/*public void select();
	public void unselect();*/
}
