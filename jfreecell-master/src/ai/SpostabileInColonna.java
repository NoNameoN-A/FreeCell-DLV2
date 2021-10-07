package ai;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("spostabileInColonna")
public class SpostabileInColonna 
{
	@Param(0)
	private int idCarta;
	@Param(1)
	private int idColonna;
	
	public SpostabileInColonna() {}
	
	public SpostabileInColonna(int id,int idc)
	{
		this.idCarta=id;
		this.idColonna=idc;
	}

	public int getIdColonna() {
		return idColonna;
	}

	public void setIdColonna(int colId) {
		this.idColonna = colId;
	}

	public int getIdCarta() {
		return idCarta;
	}

	public void setIdCarta(int cardId) {
		this.idCarta = cardId;
	}
}
