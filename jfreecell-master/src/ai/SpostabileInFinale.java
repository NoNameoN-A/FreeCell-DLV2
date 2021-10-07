package ai;

import freecell.Colonna;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("spostabileInFinale") 
public class SpostabileInFinale 
{
	private Colonna colonna;
	
	@Param(0)
	private int idCarta;
	@Param(1)
	private int idCellaFinale;


public SpostabileInFinale() {}
	
	public SpostabileInFinale(int id,int idFinish)
	{
		this.idCarta=id;
		this.idCellaFinale=idFinish;
	}

	public int getIdCarta() {
		return idCarta;
	}

	public void setIdCarta(int cardId) {
		this.idCarta = cardId;
	}
	
	public Colonna getColonna() {
		return colonna;
	}

	public void setColonna(Colonna column) {
		this.colonna = column;
	}

	public int getIdCellaFinale() {
		return idCellaFinale;
	}

	public void setIdCellaFinale(int idFinish) {
		this.idCellaFinale = idFinish;
	}

}
