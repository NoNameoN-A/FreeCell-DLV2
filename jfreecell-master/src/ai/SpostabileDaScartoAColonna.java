package ai;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("spostabileDaScartoAColonna")
public class SpostabileDaScartoAColonna 
{
		@Param(0)
		private int idCellaDiScarto;
		@Param(1)
		private int idColonna;
		
		public SpostabileDaScartoAColonna() {}
		
		public SpostabileDaScartoAColonna(int idCellaDiScarto,int idColonna)
		{
			this.idCellaDiScarto=idCellaDiScarto;
			this.idColonna=idColonna;
		}


		public int getIdCellaDiScarto() {
			return idCellaDiScarto;
		}

		public void setIdCellaDiScarto(int idCellaDiScarto) {
			this.idCellaDiScarto = idCellaDiScarto;
		}

		public int getIdColonna() {
			return idColonna;
		}

		public void setIdColonna(int idColonna) {
			this.idColonna = idColonna;
		}

		
	
}
