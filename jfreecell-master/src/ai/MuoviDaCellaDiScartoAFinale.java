package ai;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("muoviDaCellaDiScartoAFinale")
public class MuoviDaCellaDiScartoAFinale 
{
	@Param(0)
	private int idCarta;
	@Param(1)
	private int idCellaFinale;
	
	public MuoviDaCellaDiScartoAFinale() {}
	
	public MuoviDaCellaDiScartoAFinale(int id,int f)
	{
		this.idCarta=id;
		this.idCellaFinale=f;
	}
	
	
	public String toString() {
		String s = "";
		s += "muoviDaCellaDiScartoAFinale(" + idCarta + "," + idCellaFinale + ")";
		return s;
	}

	public int getIdCarta() {
		return idCarta;
	}

	public void setIdCarta(int cardFrom1) {
		this.idCarta = cardFrom1;
	}

	public int getIdCellaFinale() { 
		return idCellaFinale;
	}

	public void setIdCellaFinale(int finish1) {
		this.idCellaFinale = finish1;
	}
}