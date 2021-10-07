package ai;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;


@Id("spostaInFinale")
public class SpostaInFinale 
{
	@Param(0)
	private int idCarta;
	@Param(1)
	private int idCellaFinale;
	
	public SpostaInFinale() {}
	
	public SpostaInFinale(int idCarta,int cellaFinale)
	{
		this.idCarta=idCarta;
		this.idCellaFinale=cellaFinale;
	}

	public int getIdCellaFinale() {
		return idCellaFinale;
	}

	public void setIdCellaFinale(int cellaFinale) {
		this.idCellaFinale = cellaFinale;
	}
	
	public int getIdCarta() {
		return idCarta;
	}

	public void setIdCarta(int idCarta) {
		this.idCarta = idCarta;
	}
	
	public String toString() {
		String s = "";
		s += "spostaInFinale(" + idCarta + "," + idCellaFinale + ")";
		return s;
	}

}
