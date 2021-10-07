package ai;

import freecell.Colonna;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("spostaInScarto")
public class SpostaInScarto
{
	
	private Colonna idColonna;
	@Param(0)
	private int idCarta;
	@Param(1)
	private int idCella;

	public SpostaInScarto() {}
		
	public SpostaInScarto(int idCarta,int idCella)
	{
		//super(cards);
		this.idCella=idCella;
		this.idCarta=idCarta;
	}
	
	public SpostaInScarto(Colonna idColonna)
	{
		this.idColonna=idColonna;
	}
	
	public int getIdCarta() {
		return idCarta;
	}
	public void setIdCarta(int ca) {
		this.idCarta = ca;
	}
	
	public Colonna getIdColonna() {
		return idColonna;
	}

	public void setIdColonna(Colonna column) {
		this.idColonna = column;
	}

	public void setIdCella(int ce) {
		this.idCella = ce;
	}
	public int getIdCella() {
		return idCella;
	}
	
	public String toString() {
		String s = "";
		s += "spostaInScarto(" + idCarta + "," + idCella + ")";
		return s;
	}
	
}
