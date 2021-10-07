package freecell;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ai.SpostabileDaScartoAColonna;
import ai.SpostabileDaScartoAFinale;
import ai.SpostabileInScarto;
import ai.SpostabileInColonna;
import ai.SpostabileInFinale;
import ai.MuoviDaCellaDiScartoAColonna;
import ai.MuoviDaCellaDiScartoAFinale;
import ai.SpostaInScarto;
import ai.SpostaInColonna;
import ai.SpostaInFinale;
import freecell.Card.Suit;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv.desktop.DLVDesktopService;



public class FreeCell extends JFrame implements MouseListener {	

	private static final long serialVersionUID = -4727296451255399519L;

	public static final Color BACKGROUND_COLOR = new Color(61, 145, 64);
	public static final Color SELECTED_COLOR = Color.blue;
	public static final Color CELL_OUTLINE_COLOR = new Color(0, 255, 127);

	private Deck deck;
	private CellaDiScarto[] celleDiScarto;
	private CellaFinale[] celleFinali;
	private Colonna[] colonne;
	private static Handler handler;

	private Card appenaSpostata;
	private int nContemporanee=0;

	private int level=1;

	private String res;

	Button bstop;
	Button brallenta;
	Button bvelocizza;
	boolean first=true;
	boolean assi=true,centro,fine;
	boolean stop=false,rallenta=true,velocizza=false,colonneVuote=false;
	int velocita = 1000;
	
	ASPMapper m;

	private static String liberaAssi="encodings/liberaAssi";
	private static String riempiFinali="encodings/riempiFinali";
	public InputProgram facts;
	


	public FreeCell() 
	{
		super("FreeCell");
		
		// Crea il gioco
		deck = new Deck();
		celleDiScarto = new CellaDiScarto[4];
		celleFinali = new CellaFinale[4];
		colonne = new Colonna[8];

		// Dichiarazione iniziale
		handler = new DesktopHandler(new DLVDesktopService("lib/dlv.mingw.exe")); 
		facts= new ASPInputProgram();

		// Crea i mazzi
		for (int i = 0; i < 4; i++) 
		{
			// Creo la cella
			celleDiScarto[i] = new CellaDiScarto(53,i);
			try {
				facts.addObjectInput(celleDiScarto[i]);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			// Creo le finished cell con 53(vuoto) e il seme picche, la scelta del seme è irrilevante perché sono vuote 
			celleFinali[i] = new CellaFinale(53,Suit.PICCHE,i);
			CellaFinale fin = new CellaFinale(53,"PICCHE",i);
			
			try {
				facts.addObjectInput(fin); //Aggiungo ai fatti le celle finali
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}		

		for (int i = 0; i < 8; i++) // Creo le colonne
			colonne[i] = new Colonna(i);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	int ret = JOptionPane.showOptionDialog(this, "premi OK per avviare la modalità facile", "Via!", JOptionPane.OK_OPTION, JOptionPane.OK_OPTION, null, null, null);
		if (ret == JOptionPane.OK_OPTION) {

			// Creo effettivamente la grafica del gioco
			createGUI(); // Parte Grafica
			getContentPane().setBackground(BACKGROUND_COLOR);
			start();
			pack(); 
			setVisible(true);

			// Rilevo subito le carte che possono essere mosse
			findMovableCards();

			findSolution();
			}
	}


	JLabel label = new JLabel();

	// Resetta il campo per una nuova partita + Genera livello
	private void start() 
	{
		gbc.gridx = 9;
		gbc.anchor = GridBagConstraints.CENTER;
		
		label.setText("Livello"+" "+level);
		
		this.add(label,gbc);
		// Reset
		for (int i = 0; i < 4; i++) 
		{
			celleDiScarto[i].reset();
			celleFinali[i].reset();
		}

		for (int i = 0; i < 8; i++) 
		{
			colonne[i].reset();
		}
		
		deck.shuffle(); //rimescolare
		
		generaDaFile();// Genera Livello

	}

	//Genera livello
	private void generaDaFile()
	{
		if(level==1)
			res="resources/easy.txt";
		else if(level==2)
			res="resources/medium.txt";
		else
			res="resources/hard.txt";

		char[] carte=new char[168];
		int j=0;

		////////////////////////METTO TUTTO IN UN ARRAY///////////////////////////////////////////////
		try {
			// apre il file in lettura
			FileReader filein = new FileReader(res);

			int next;
			do {
				next = filein.read(); // legge il prossimo carattere

				if (next != -1) { // se non e' finito il file
					char nextc = (char) next;
					// System.out.print(j +"-"+ nextc+ " ");
					carte[j]=nextc; j++;
				}

			} while (next != -1);

			filein.close(); // chiude il file

		} catch (IOException e) {
			System.out.println(e);
		}
		///////////////////////////////////////////////////////////////////////////////////////////////

		int colonna=0;
		int val;
		Suit seme;

		for(int i=0;i<carte.length;)
		{
			if(converti(carte[i])!=0)
			{
				val=converti(carte[i]); i++; // Converte il valore della carta in 1-13
				seme=controllaSeme(carte[i]);i++; // Converte il seme della carta in enum {Clubs, Diamonds, Hearts, Spades}
				Card c=deck.restituisciCarta(val, seme); // Crea la carta in base ai valori acquisiti
				colonne[colonna].initAdd(c); // Metto la carta nella colonna  
				int p=colonne[colonna].getCards().size();
				try {
					facts.addObjectInput(new Colonna(c.getId(),p,colonna)); // Aggiunge ai fatti tutte le colonne
				}
				catch (Exception e) {
					e.printStackTrace();}
				if(colonna==7)
					colonna=0;
				else
					colonna++;
			}
			else i++;
		}

	}

	
	

	int prova=0;
	
	// L'intelligenza parte da qui generando i fatti principali
	private void findNewFacts()
	{
		// *** ProssimiInScala *** 
		rilevaProssimiInScala(); 
		
		// *** Celle di scarto *** 
		for (int i = 0; i < 4; i++) 
		{
			try {
				facts.addObjectInput(celleDiScarto[i]); 
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		// *** Celle finali *** 
		for(int i=0;i<4;i++)
		{
			String s="";
			if(celleFinali[i].getSuit()==Suit.FIORI)
				s="FIORI";
			else if(celleFinali[i].getSuit()==Suit.QUADRI)
				s="QUADRI";
			else if(celleFinali[i].getSuit()==Suit.CUORI)
				s="CUORI";
			else if(celleFinali[i].getSuit()==Suit.PICCHE)
				s="PICCHE";

			try {
				// Viene rilevata come fatto la carta più in alto(getTop) nelle celle finali
				/* *
				 * Assumiamo
				 * CellaFinale[1] contiene carte di Picche da 1 a 7, la carta 7Picche ha id 40
				 * Viene passato come fatto 
				 * finishedCell(40,Picche,1)
				 * */
				facts.addObjectInput(new CellaFinale(celleFinali[i].getIdCarta(),s,i) );  
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		// Genera ad oc
		if(res=="resources/medium2.txt" ) { 
			try {
			}
			catch (Exception e) {
				e.printStackTrace();}}

		// *** Colonne *** 
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<colonne[i].getCards().size();j++)
			{
				Card c=colonne[i].getCards().get(j);
				try {
					facts.addObjectInput(new Colonna(c.getId(),j,i));
				}
				catch (Exception e) {
					e.printStackTrace();}
			}
		}

		cercaStrategia();
	}

	/*
	 * Verifico:
	 * - Se ci sono colonne vuote
	 * - Se ci sono assi nelle colonne
	 * - Se, essendoci meno di 20 carte "sul campo", sono alla fine, altrimenti sono al centro
	 * */
	private void cercaStrategia() {
		
		System.out.println("Cerca strategia");
		assi=false;
		centro=false;
		fine=false;
		colonneVuote=false;
		int ncarte=0;
		
		for(int i=0;i<8;i++) {
			ncarte+=colonne[i].getCards().size(); // Numero di carte nella colonna i-esima
			if(colonne[i].getCards().isEmpty()) { // Se sta gestendo una colonna vuota
				colonneVuote=true; 
			}

			for(int j=0;j<colonne[i].getCards().size();j++) {
				if((!colonne[i].getCards().isEmpty()) && colonne[i].getCards().get(j).getRank()==1 ){ //Scorre tutte le carte della colonna i-esima e verifica se ci sono assi da dover liberare
					assi=true;
					System.out.println("\n***ASSI***");
				}
			}
		}

		// Oltre alle carte in colonna devo verificare se ci sono carte nelle celle di scarto
		for(int i=0;i<4;i++)
			if(celleDiScarto[i].getIdCarta()!=53)
				ncarte++;

		if(!assi) {
			centro=true;
			System.out.println("\n***CENTRO***");
		}
	}

	
	
	// Rileva i fatti "ProssimiInScala" quindi fa riferimento alle carte precedenti alle spostabili e alle carte spostabili delle altre colonne
	private void rilevaProssimiInScala() {
		Card carta;

		for(int i=0;i<8;i++) {
			if((colonne[i].getCards().size()<=1)) {
				break; // Se sono minori o uguali a 1 cambia colonna
			}
			
			// Rilevo i fatti relativi alle carte che ci sono prima di quella spostabile in ogni colonna
			carta=colonne[i].getCards().get(colonne[i].getCards().size()-2);
			
			for(int j=0;j<8;j++)
			{
				if(i!=j)
				{
					if(!colonne[j].getCards().isEmpty()) { // Se non è vuota
						Card bottom = colonne[j].getCards().getLast(); // Prendo l'ultima carta dell'altra colonna
						if (bottom.getColor() != carta.getColor() &&
								bottom.getRank() == carta.getRank() + 1)
						{
							
							try {
								//facts.addObjectInput(new ProssimaInScala(carta.getId()));
							}
							catch (Exception e) {
								e.printStackTrace();}
						}
					}
				}
			}
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void findMovableCards() {
		//	movCe=null; int pos=0; movF=null; int pos2=0;
		nContemporanee=1;
		boolean cellaDiScartoVuota;
		if(!first) {
			facts.clearAll();
			findNewFacts();
			cellaDiScartoVuota=false;
		}
		else cellaDiScartoVuota=true;

		for(int j=0;j<4;j++) {
			Card ca=celleDiScarto[j].getCarta();
			if(ca != appenaSpostata) {
				if(celleDiScarto[j].getIdCarta()==53) 
				{
					cellaDiScartoVuota=true;
					nContemporanee++;
					//break;
				}
				else {
					for(int co=0;co<8;co++) {
						if(colonne[co].canAdd(ca))
							try {
								facts.addObjectInput(new SpostabileDaScartoAColonna(celleDiScarto[j].getIdCellaDiScarto(),co));
							}
						catch (Exception e) {
							e.printStackTrace();}
					}

					for(int h=0;h<4;h++) {
						if(celleFinali[h].canAdd(ca))
							try {
								facts.addObjectInput(new SpostabileDaScartoAFinale(celleDiScarto[j].getIdCarta(),h));
							}
						catch (Exception e) {
							e.printStackTrace();}
					}
				}
			}
			//System.out.println(appenaSpostataNVolte);
		}

		for(int i=0;i<8;i++) {
			if(!colonne[i].getCards().isEmpty()) {
				Card c=colonne[i].getCards().getLast();
				//if(c!=appenaSpostata)
				//{
				//se c'è almeno una cella vuota le ultime carte possono essere spostate li
				if(cellaDiScartoVuota && (c!=appenaSpostata)) {
					try {
						facts.addObjectInput(new SpostabileInScarto(c.getId()));
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				//controllo se la carta può essere inserita nelle finished cell
				for(int j=0;j<4;j++) {
					if(celleFinali[j].canAdd(c) && (c!=appenaSpostata) ) {
						try {
							facts.addObjectInput(new SpostabileInFinale(c.getId(),celleFinali[j].getIdCella()));
							//movF[pos2]=new MovableInF(c.getId(),columns[i]);pos2++;
						}
						catch (Exception e) {
							e.printStackTrace();}
					}

				}


				////////////////////////////// Ultima carta di ogni colonna//////////////	
				Card ultima=colonne[i].getCards().getLast();

				for(int co=0;co<8;co++) {
					if(colonne[co].canAdd(ultima) && co!=i && (ultima!=appenaSpostata)  && (c!=appenaSpostata) )
						try {
							facts.addObjectInput(new SpostabileInColonna(ultima.getId(),co));
						}
					catch (Exception e) {
						e.printStackTrace();}
				}
				////////////////////////////////////////////////////////////////////////////////////////////////////////			

				int spostate=0;
				LinkedList<Card> tmp= new LinkedList<Card>();
				boolean almenoUna=false;
				for(int ca=colonne[i].getCards().size()-1;ca>=0;ca--) {
					if(spostate<=nContemporanee) {
						if(ca!=0) {
							Card card=colonne[i].getCards().get(ca);
							Card bottom = colonne[i].getCards().get(ca-1); 
							if (bottom.getColor() != card.getColor() &&
									card.getRank() == bottom.getRank() - 1){
								tmp.add(card);
								spostate++;
								almenoUna=true;
							}
							else if(almenoUna) {
								tmp.add(card);
								spostate++;
								break;
							}
							else break;
						}
					}
				}


				if(!(tmp.isEmpty())) {
					Card papabile=tmp.getLast();

					for(int co=0;co<8;co++) {
						if(colonne[co].canAdd(papabile) && co!=i &&(papabile!=appenaSpostata) && (c!=appenaSpostata) )
							try {
								facts.addObjectInput(new SpostabileInColonna(papabile.getId(),co));
								//facts.addObjectInput(new FaParteDiUnaScala(papabile.getId()));
							}
						catch (Exception e) {
							e.printStackTrace();}
					}}
			}

		}


	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void findSolution() {
		//Aggiungo i fatti
		handler.addProgram(facts);
		InputProgram encoding= new ASPInputProgram();

		handler = new DesktopHandler(new DLVDesktopService("lib/dlv.mingw.exe"));
		handler.addProgram(facts);
		
		if(assi) {
			encoding.addFilesPath(liberaAssi);
		} else if(centro) {
			encoding.addFilesPath(riempiFinali);
		}
		
		// Ho scelto il file
		handler.addProgram(encoding);
		//
		first=false;

		try {
			ASPMapper.getInstance().registerClass(SpostaInScarto.class); 

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ASPMapper.getInstance().registerClass(SpostaInFinale.class); 

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ASPMapper.getInstance().registerClass(SpostaInColonna.class); 

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ASPMapper.getInstance().registerClass(MuoviDaCellaDiScartoAColonna.class); 

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ASPMapper.getInstance().registerClass(MuoviDaCellaDiScartoAFinale.class); 

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Output o =  handler.startSync();
		AnswerSets answers= (AnswerSets) o;
		System.out.println(facts.getStringOfFilesPaths());
		System.out.println("\nAnswerset: " + answers.getAnswersets() + "\n");
		checkLose(answers.getAnswersets());
		
		// Gestione AnswerSet
		for(AnswerSet a:answers.getAnswersets()) { // per ogni as. il get restituisce un set di as in ordine di ottimalità
			try {
				for(Object obj:a.getAtoms()) {
					
					if(obj instanceof SpostaInScarto)  {
						SpostaInScarto move = (SpostaInScarto) obj;
						System.out.print(move + " ");
						removeCard(move);
						break;
					}
					if(obj instanceof SpostaInFinale)  {
						SpostaInFinale move = (SpostaInFinale) obj;
						System.out.print(move + " ");
						removeCard(move);
						break;
					}
					if(obj instanceof SpostaInColonna)  {
						SpostaInColonna move = (SpostaInColonna) obj;
						System.out.print(move + " ");
						removeCard(move);
						break;
					}
					if(obj instanceof MuoviDaCellaDiScartoAColonna)  {
						MuoviDaCellaDiScartoAColonna move = (MuoviDaCellaDiScartoAColonna) obj;
						System.out.print(move + " ");
						removeCard(move);
						break;
					}
					if(obj instanceof MuoviDaCellaDiScartoAFinale)  {
						MuoviDaCellaDiScartoAFinale move = (MuoviDaCellaDiScartoAFinale) obj;
						System.out.print(move + " ");
						removeCard(move);
						break;
					}
				}
				System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			break;
		}


	}



	private void removeCard(SpostaInFinale moveToF) throws InterruptedException {
		for(int i=0;i<4;i++) {
			if(celleFinali[i].getIdCella()==moveToF.getIdCellaFinale()) {
				for(int j=0;j<8;j++) {
					if(/*!columns[i].getCards().isEmpty() &&*/ !colonne[j].getCards().isEmpty()) {
						if(colonne[j].getCards().getLast().getId()==moveToF.getIdCarta()) {
							appenaSpostata=colonne[j].getCards().getLast();
							celleFinali[i].add(colonne[j].remove());
							break;
						}
					}
				}
				break;
			}
		}
		checkForVictory();

		tempoMossa();

		findMovableCards();
		findSolution();


	}
	private void removeCard(SpostaInScarto moveToC) throws InterruptedException {
		for(int i=0;i<4;i++){
			if(celleDiScarto[i].getIdCellaDiScarto()==moveToC.getIdCella()){
				for(int j=0;j<8;j++){
					if(!colonne[j].getCards().isEmpty()){
						if((!colonne[j].getCards().isEmpty()) && colonne[j].getCards().getLast().getId()==moveToC.getIdCarta()){
							appenaSpostata=colonne[j].getCards().getLast();
							celleDiScarto[i].add(colonne[j].remove());
							repaint();
							break;
						}
					}
				}
				break;
			}
		}


		tempoMossa();

		findMovableCards();
		findSolution();


	}

	private void removeCard(SpostaInColonna moveToCo) throws InterruptedException {
		int column=0;

		for(int i=0;i<8;i++) {
			if(colonne[i].getIdColonna()==moveToCo.getIdColonna()) {
				column=i; break;}  //individuo la colonna su cui spostare la carta
		}

		for(int i=0;i<8;i++) {
			if(i!=column) {
				for(int j=0;j<colonne[i].getCards().size();j++) {
					if(colonne[i].getCards().get(j).getId()==moveToCo.getIdCarta()) { //individuo la carta che devo spostare
						appenaSpostata=colonne[i].getCards().getLast();
						//System.out.println("appena spostata: "+appenaSpostata);
						if(j!=colonne[i].getCards().size()-1) { //se non è l'ultima 
							for(int k=j;k<colonne[i].getCards().size();) { //sposto tutte le  carte a partire da quella 
								System.out.println(colonne[i].getCards().size());
								if(colonne[column].canAdd((colonne[i].getCards().get(j))))
									colonne[column].add(colonne[i].removeI(j));
								else break;

							}

							//break;
						}
						else { //se è l'ultima devo spostare solo quella 
							appenaSpostata=colonne[i].getCards().get(j);
							//System.out.println("appena spostata: "+appenaSpostata);
							colonne[column].add(colonne[i].remove());
							break;	
						}
					}
				}
			}
		}

		tempoMossa();

		findMovableCards();
		findSolution();
	}

	private void removeCard(MuoviDaCellaDiScartoAColonna move) throws InterruptedException{
		for(int i=0;i<4;i++){
			if(celleDiScarto[i].getIdCellaDiScarto()==move.getIdCellaDiScarto()){
				for(int j=0;j<8;j++) {
					if(colonne[j].getIdColonna()==move.getIdColonna()){
						appenaSpostata=celleDiScarto[i].getCarta();
						colonne[j].add(celleDiScarto[i].remove());
						break;
					}
				}
			}
		}

		checkForVictory();

		tempoMossa();

		findMovableCards();
		findSolution();
	}

	// Ogni volta che muove una carta in finish
	private void removeCard(MuoviDaCellaDiScartoAFinale move) throws InterruptedException {
		for(int i=0;i<4;i++) {
			if(celleDiScarto[i].getIdCarta()==move.getIdCarta()) {
				for(int j=0;j<4;j++) {
					if(celleFinali[j].getIdCella()==move.getIdCellaFinale()) {
						appenaSpostata=celleDiScarto[i].getCarta();
						celleFinali[j].add(celleDiScarto[i].remove());
						break;
					}
				}
			}
		}
		//Verifica se ha vinto
		checkForVictory();
		//sleep
		tempoMossa();

		findMovableCards();
		findSolution();
	}

	//------------------GRAFICA-----------------
	GridBagConstraints gbc = new GridBagConstraints();
	private void createGUI() {
		GridBagLayout gbl = new GridBagLayout();
		
		this.setLayout(gbl);

		bstop=new Button("Stoppa");
		bstop.addMouseListener(this);

		brallenta=new Button("Rallenta");
		brallenta.addMouseListener(this);

		bvelocizza=new Button("Velocizza");
		bvelocizza.addMouseListener(this);


		gbc.gridy = 0;
		gbc.insets = new Insets(2, 2, 2, 2);
		for (int i = 0; i < 4; i++) {
			this.add(celleDiScarto[i], gbc);
		}


		this.add(new JPanel() {
			private static final long serialVersionUID = -6234059593255900935L;

			public Color getBackground() {
				return FreeCell.BACKGROUND_COLOR;
			}

			public Dimension getPreferredSize() {
				return new Dimension(80, 120);
			}

		}, gbc);


		for (int i = 0; i < 4; i++) {
			this.add(celleFinali[i], gbc);
		}



		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1;
		for (int i = 0; i < 8; i++) {
			this.add(colonne[i], gbc);
		}
		
		
		
		gbc.anchor = GridBagConstraints.NORTHEAST;
		gbc.gridy = 1;
		gbc.gridx = 8;
		this.add(bstop,gbc);
		gbc.gridx = 9;
		this.add(bvelocizza,gbc);
		gbc.gridx = 10;
		this.add(brallenta,gbc);

	}
	


	// Verifica se ha vinto
	private void checkForVictory() {
		String mode;
		mode="MEDIUM";
		
		if(level==2)
			mode="HARD";

		for (int i = 0; i < 4; i++) {
			if (!celleFinali[i].isComplete()) {
				return;
			}
		}

		if(level>=3){
			int ret = JOptionPane.showOptionDialog(this, "Congratulazione, hai vinto! Vuoi giocare di nuovo?", "Vittoria!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (ret == JOptionPane.YES_OPTION) {
				level=1;
				this.start();

			}
		}

		else{
			rallenta=true;
			velocizza=false;
			int ret = JOptionPane.showOptionDialog(this, "Congratulazione, hai vinto! Vuoi giocare di nuovo "+ mode+ " modalità?", "Vittoria!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (ret == JOptionPane.YES_OPTION) {
				level++;
				this.start();

			}
		}

	}
	
	private void checkLose(List<AnswerSet> answerset) {
		if(answerset.size() == 0) {
			partitaPersa();
		}
	}
	
	private void partitaPersa() {
		int ret = JOptionPane.showOptionDialog(this, "Mi dispiace, hai perso! Vuoi giocare di nuovo?", "Hai Perso!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (ret == JOptionPane.YES_OPTION) {
			level=1;
			this.start();

		}
	}

	// Rallenta la prossima operazione
	public void tempoMossa() throws InterruptedException {
		while(stop) System.out.print(stop);
		Thread.sleep(velocita);
		
		return;
	}

	public void mouseReleased(MouseEvent me) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent me) { 

		if (me.getSource()==bstop) {
			if(stop==false) 
				stop=true;
			else 
				stop=false;
		}
		else if(me.getSource()==brallenta) {
			velocita = velocita + 100;

		}
		else if(me.getSource()==bvelocizza && velocita > 0) {
			velocita = velocita - 100;	
		}
		else {
			int ret = JOptionPane.showOptionDialog(this, "Velocità massima raggiunta!", "Attenzione!", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
		}
	}
	
	public void mouseEntered(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}


	public static void main(String[] args) {
		new FreeCell();
	}

	// Converto il valore del file in enum tipo della carta
		private Suit controllaSeme(char seme) {
			switch(seme){
			case 'C': return Suit.FIORI; 
			case 'D': return Suit.QUADRI;
			case 'H': return Suit.CUORI;
			case 'S': return Suit.PICCHE;
			}
			return null;

		}

		// Converto il valore del file in numero della carta
		private int converti(char v){
			switch (v) {
			case 'A':
				return 1;
			case '2':
				return 2;
			case '3':
				return 3;
			case '4':
				return 4;
			case '5':
				return 5;
			case '6':
				return 6;
			case '7':
				return 7;
			case '8':
				return 8;
			case '9':
				return 9;
			case 'T':
				return 10;
			case 'J':
				return 11;
			case 'Q':
				return 12;
			case 'K':
				return 13;
			}
			return 0;
		}


}
