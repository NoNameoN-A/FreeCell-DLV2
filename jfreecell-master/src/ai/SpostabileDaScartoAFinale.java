package ai;
import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("spostabileDaScartoAFinale")
public class SpostabileDaScartoAFinale 
{
		@Param(0)
		private int idCarta;
		@Param(1)
		private int idCellaFinale;
		
		public SpostabileDaScartoAFinale() {}
		
		public SpostabileDaScartoAFinale(int idCarta,int idCella)
		{
			this.idCarta=idCarta;
			this.idCellaFinale=idCella;
		}

		public int getIdCarta() {
			return idCarta;
		}

		public void setIdCarta(int idCarta) {
			this.idCarta = idCarta;
		}

		public int getIdCellaFinale() {
			return idCellaFinale;
		}

		public void setIdCellaFinale(int idCella) {
			this.idCellaFinale = idCella;
		}

		
	
}
