package freecell;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import freecell.Card.Suit;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("cellaFinale")
public class CellaFinale extends JPanel implements CardDestination {
	private static final long serialVersionUID = 1502461858912278004L;
	
	private Card topC;
	
	@Param(0)
	private int idCarta;
	@Param(1) 
	private String seme;
	@Param(2)
	private int idCella;
	
	private Suit suit;
	
	public CellaFinale() {}
	
	public CellaFinale(int tC,String su,int id) 
	{
		this.idCarta=tC;
		this.seme=su;
		this.idCella=id; 
	}

	public CellaFinale(int tC,Suit su,int id) 
	{
		super();

		Dimension size = new Dimension(80, 120);
		setSize(size);
		setPreferredSize(size);
		setMinimumSize(size);
	
		this.idCarta=tC;
		this.suit=su;
		this.idCella=id;  
	}
	
	public String getSeme() {
		return seme;
	}

	public void setSeme(String suitDlv) {
		this.seme = suitDlv;
	}

	public int getIdCarta() {
		return idCarta;
	}


	public void setIdCarta(int top) {
		this.idCarta = top;
	}


	public void add(Card card) 
	{
		if (!canAdd(card)) {
			throw new IllegalArgumentException();
		}
		
		//if (suit == null) {
			suit = card.getSuit();
		//}
		
		topC = card;
		idCarta=card.getId();
		
		
		String s="ciao";
		if(card.getSuit()==Suit.FIORI)
			s="FIORI";
		else if((card.getSuit()==Suit.QUADRI))
			s="QUADRI";
		else if((card.getSuit()==Suit.CUORI))
			s="CUORI";
		else if((card.getSuit()==Suit.PICCHE))
			s="PICCHE";
		
		seme=s;
		
		repaint();
	}
	
	public boolean canAdd(Card card) 
	{
		int currentRank = (topC == null) ? 0 : topC.getRank();
		if(currentRank==0 && card.getRank()==1) return true;
		if (card.getRank() == currentRank + 1) 
		{ 
			if (suit == null || card.getSuit() == suit) 
			{
				return true;
			}
		}
		return false;
	}
	
	public Card getTopCard() {
		return topC;
	}
	
	public boolean isComplete() {
		return topC == null ? false : topC.getRank() == 13;
	}
	
	public void reset() {
		topC = null;
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		if (topC == null) {
			g.setColor(FreeCell.BACKGROUND_COLOR);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(FreeCell.CELL_OUTLINE_COLOR);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		}
		else {
			topC.drawGraphics(g, new Point(0, 0));
		}
	}

	public Card getTopC() {
		return topC;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public int getIdCella() {
		return idCella;
	}
	
	public void setIdCella(int id) {
		this.idCella = id;
	}

	public Suit getSu() {
		return suit;
	}

	public void setSu(Suit su) {
		this.suit = su;
	}

	
	
	
}
