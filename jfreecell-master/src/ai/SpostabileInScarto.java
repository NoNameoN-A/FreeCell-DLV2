package ai;

import freecell.Colonna;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("spostabileInScarto")
public class SpostabileInScarto 
{
	private Colonna idColonna;
	
	@Param(0)
	private int idCarta;
	
	public SpostabileInScarto()
	{
	}
	
	public SpostabileInScarto(int idCarta)
	{
		this.idCarta=idCarta;
	}
	
//	public SpostabileInScarto(int id,Column c)
//	{
//		this.idCarta=id;
//		this.idColonna=c;
//	}

	public Colonna getColumn() {
		return idColonna;
	}

	public void setColumn(Colonna column) {
		this.idColonna = column;
	}

	public int getIdCarta() {
		return idCarta;
	}

	public void setIdCarta(int cardId) {
		this.idCarta = cardId;
	}
}
