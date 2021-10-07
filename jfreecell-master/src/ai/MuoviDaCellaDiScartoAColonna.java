package ai;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("muoviDaCellaDiScartoAColonna")
public class MuoviDaCellaDiScartoAColonna 
{
	@Param(0)
	private int idCellaDiScarto;
	@Param(1)
	private int idColonna;
	
	public MuoviDaCellaDiScartoAColonna() {}
	
	public MuoviDaCellaDiScartoAColonna(int id,int f)
	{
		this.idCellaDiScarto=id;
		this.idColonna=f;
	}
	
	public int getIdCellaDiScarto() {
		return idCellaDiScarto;
	}

	public void setIdCellaDiScarto(int cefrom) {
		this.idCellaDiScarto = cefrom;
	}

	public int getIdColonna() {
		return idColonna;
	}

	public void setIdColonna(int coTo) {
		this.idColonna = coTo;
	}

	public String toString() {
		String s = "";
		s += "muoviDaCellaDiScartoAColonna(" + idCellaDiScarto + "," + idColonna + ")";
		return s;
	}
}