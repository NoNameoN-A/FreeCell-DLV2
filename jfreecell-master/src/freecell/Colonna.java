package freecell;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import javax.swing.JPanel;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("colonna")
public class Colonna extends JPanel implements CardSource, CardDestination {
	private static final long serialVersionUID = 225348828532472406L;
	
	private LinkedList<Card> cards;
	private boolean selected;
	
	@Param(0)
	private int idCarta;
	@Param(1)
	private int posizioneCarta;
	@Param(2)
	private int idColonna;
	
	public Colonna() {} 
	
	public Colonna(int id) 
	{
		super();
		
		cards = new LinkedList<Card>();
		selected = false;
		this.idColonna=id;
		
		updateSize();
	}
	
	public Colonna(int card,int pos,int id) 
	{
		//super();
		
		this.idCarta = card;
		this.posizioneCarta=pos;
		this.idColonna=id;
		//updateSize();
		//System.out.println(column);
	}
	
	
	public LinkedList<Card> getCards() {
		return cards;
	}

	public void setCards(LinkedList<Card> cards) {
		this.cards = cards;
	}

	public Card remove() {
		Card ret = cards.removeLast();
		updateSize();
		repaint();
		
		return ret;
	}
	
	public Card removeI(int index) {
		Card ret = cards.remove(index);
		updateSize();
		repaint();
		
		return ret;
	}
	
	public Card peek() {
		return cards.peekLast();  //ritorna l'ultimo elemento della lista
	}
	
	public boolean canRemove() {
		return !cards.isEmpty();
	}
	
	public void select() {
		selected = true;
		repaint();
	}
	
	public void unselect() {
		selected = false;
		repaint();
	}
	
	public void add(Card card) {
		if (!canAdd(card)) {
			throw new IllegalArgumentException();
		}
		
		cards.add(card);
		
		updateSize();
		repaint();
	}
	
	public boolean canAdd(Card card) 
	{
		if (cards.isEmpty()) {
			return true;
		}
		else
		{
			Card bottom = cards.peekLast(); //mi salvo l'ultima carta della lista
			if (bottom.getColor() != card.getColor() &&
					bottom.getRank() == card.getRank() + 1) { //System.out.println("id colonna: "+column);
				return true;}
			else
				return false;
		}
	}
	
	public void initAdd(Card card) 
	{
		cards.add(card);
		
		updateSize();
		repaint();
	}
	
	
	public void reset() {
		cards.clear();
		selected = false;
		
		updateSize();
		repaint();
	}
	
	public void paintComponent(Graphics g) 
	{
		g.setColor(FreeCell.BACKGROUND_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int i;
		for (i = 0; i < cards.size(); i++) {
			Card c = cards.get(i);
			c.drawGraphics(g, new Point(0, i * 30));
		}
		
		if (selected) {
			g.setColor(FreeCell.SELECTED_COLOR);
			g.drawRect(0, (i - 1) * 30, 79, 119);
			g.drawRect(1, ((i - 1) * 30) + 1, 77, 117);
		}
	}
	
	private void updateSize() 
	{
		int height = cards.size() == 0 ? 120 : 120 + ((cards.size() - 1) * 30);
		Dimension size = new Dimension(80, height);
		setSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
	}
	
	public static boolean canGoUnder(Card top, Card bottom) {
		return ((top.getColor() != bottom.getColor()) && (top.getRank() == bottom.getRank() + 1));
	}
	
	public int getIdCarta() {
		return idCarta;
	}


	public void setIdCarta(int card) {
		this.idCarta = card;
	}


	public int getPosizioneCarta() {
		return posizioneCarta;
	}


	public void setPosizioneCarta(int position) {
		this.posizioneCarta = position;
	}


	public int getIdColonna() { 
		return idColonna;
	}


	public void setIdColonna(int column) {
		this.idColonna = column;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
