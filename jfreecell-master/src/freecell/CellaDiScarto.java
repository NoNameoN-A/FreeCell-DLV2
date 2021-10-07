package freecell;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.NoSuchElementException;

import javax.swing.JPanel;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("cellaDiScarto") 
public class CellaDiScarto extends JPanel implements CardSource, CardDestination {
	private static final long serialVersionUID = 4656851991098138209L;
	
	private Card carta;
	@Param(0)
	private int idCarta;
	@Param(1)
	private int idCellaDiScarto;
	
	private boolean selected;
	
	public CellaDiScarto() {}
	
	public CellaDiScarto(int idCarta,int idCellaDiScarto) 
	{
		super();
		
		Dimension size = new Dimension(80, 120);
		setSize(size);
		setPreferredSize(size);
		setMinimumSize(size);
		
		this.idCarta=idCarta;
		this.idCellaDiScarto=idCellaDiScarto;
		
		selected = false;
	}
	
	
	


	public Card remove() 
	{
		if (carta == null) {
			throw new NoSuchElementException();
		}
		
		Card ret = carta;
		carta = null;
		
		idCarta=53;
		
		repaint();
		
		return ret;
	}
	
	public Card peek() {
		return carta;
	}
	
	public boolean canRemove() {
		return carta != null;
	}
	
	public void select() {
		selected = true;
		repaint();
	}
	
	public void unselect() {
		selected = false;
		repaint();
	}
	
	public void add(Card carta)
	{
		if (this.carta != null) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		this.carta = carta;
		this.idCarta=carta.getId();
		repaint();
	}
	
	public boolean canAdd(Card carta) {
		return this.carta == null;
	}


	public void reset()
	{
		carta = null;
		//selected = false;
		
		repaint();
	}
	
	public void paintComponent(Graphics g) 
	{
		if (carta == null) {
			g.setColor(FreeCell.BACKGROUND_COLOR);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(FreeCell.CELL_OUTLINE_COLOR);
			g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		}
		else {
			carta.drawGraphics(g, new Point(0, 0));
			
			/*if (selected) {
				g.setColor(FreeCell.SELECTED_COLOR);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
			}*/
		}
	}
	
	public Card getCarta() {
		return carta;
	}

	public void setCarta(Card carta) {
		this.carta = carta;
	}

	public int getIdCarta() {
		return idCarta;
	}

	public void setIdCarta(int idCarta) {
		this.idCarta = idCarta;
	}
	
	public int getIdCellaDiScarto() {
		return idCellaDiScarto;
	}

	public void setIdCellaDiScarto(int idCellaDiScarto) {
		this.idCellaDiScarto = idCellaDiScarto;
	}


}