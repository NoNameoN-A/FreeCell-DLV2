package ai;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("spostaInColonna")
public class SpostaInColonna 
{
	@Param(0)
	private int idCarta;
	@Param(1)
	private int idColonna;
	
	public SpostaInColonna() {}
	
	public SpostaInColonna(int id,int f)
	{
		this.idCarta=id;
		this.idColonna=f;
	}

	public int getIdCarta() {
		return idCarta;
	}

	public void setIdCarta(int caId) {
		this.idCarta = caId;
	}

	public int getIdColonna() {
		return idColonna;
	}

	public void setIdColonna(int coId) {
		this.idColonna = coId;
	}
	
	public String toString() {
		String s = "";
		s += "spostaInColonna(" + idCarta + "," + idColonna + ")";
		return s;
	}
}