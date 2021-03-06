import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class JogoDeDomino{
	/*Arrays e iteradores necessarios para as maos, mesa e banca*/
	public ArrayList <PedraDomino> maoJ = new ArrayList<PedraDomino> ();
	public ArrayList <PedraDomino> maoC = new ArrayList<PedraDomino> ();
	public ArrayList <PedraDomino> mesa =  new ArrayList<PedraDomino> ();
	public ArrayList <PedraDomino> banca =  new ArrayList<PedraDomino> ();
	public Iterator<PedraDomino> itJ, itC, itM;
	
	public int inc; // variavel para incrementar quantas vezes o computador foi a banca 
	public Random aleatorio = new Random();
	private Scanner input;
	public int randomInt;
	public int numP; // numero de pecas
	public int player = 0;// 0 - Comp   1 - jogador
	
	
	/*Construtor do jogo de domino que inicia um jogo contra o jogador - jog*/
	public JogoDeDomino (String jog, int n){
		int numP = 0;
		
		for (int i = 0; i<= n+1; i++){
			numP+=i;
		}
		
		//aqui e criada a banca usando a funcao todas da classe PedraDomino
		banca = PedraDomino.todas(n);
		
		// valor aleatorio para saber qual a primeira mao a ser criada 
		randomInt = aleatorio.nextInt(2); 
		
		/* cria as maos por ordem diferente de acordo com o valor aleatorio gerado*/
		if (randomInt == 0){
			for(int i = 1; i <= n+1; i++){
				randomInt = aleatorio.nextInt(numP);
				maoC.add(banca.get(randomInt));
				banca.remove(randomInt);
				numP-=1;
			}
			for(int j = 1; j <= n+1; j++){
				randomInt = aleatorio.nextInt(numP);
				maoJ.add(banca.get(randomInt));
				banca.remove(randomInt);
				numP-=1;
			}
		}
		else{
			for(int i = 1; i <= n+1; i++){
				randomInt = aleatorio.nextInt(numP);
				maoJ.add(banca.get(randomInt));
				banca.remove(randomInt);
				numP-=1;
			}
			for(int j = 1; j <= n+1; j++){
				randomInt = aleatorio.nextInt(numP);
				maoC.add(banca.get(randomInt));
				banca.remove(randomInt);
				numP-=1;
			}
		}
		System.out.println("O jogo foi iniciado, o jogador com a maior peca dupla iniciara a mesa.");
		
		iniciaMesa(maoJ, maoC, jog, n);
	}
	
	
	// algoritmo para calcular o numero de pecas de acordo com n
	public int calcNumP(int n){
		int numP = 0;

		for (int i = 0; i<= n+1; i++){
			numP+=i;
		}
		return numP;
	}
	
	//verifica qual dos jogadores tem a peca dupla maior e coloca-a na mesa
	public void iniciaMesa(ArrayList<PedraDomino> mj, ArrayList<PedraDomino> mc, String jog, int n){
		
		// sao criadas as pecas duplas mais baixas para comparar com as pecas das maos para saber qual a maior
		PedraDomino maiorJ = new PedraDomino(0, 0);
		PedraDomino maiorC = new PedraDomino(0, 0);
		
		/* Caso seja encontrada uma peca maior que a anterior que esta guardada na variavel o valor
		 *  antigo e substituido pelo novo */
		for (PedraDomino i : mj){
			if (i.eDuplo() && i.getLadoDir() > maiorJ.getLadoDir()){
				maiorJ = i;
			}
		}
		for (PedraDomino j : mc){
			if (j.eDuplo() && j.getLadoDir() > maiorC.getLadoDir()){
				maiorC = j;
			}
		}
		
		/*Aqui sao comparados os maiores duplos de cada jogador e e iniciada a mesa com o maior entre
		  os dois.*/
		if(maiorJ.getLadoDir() > maiorC.getLadoDir()){
			mesa.add(maiorJ);
			maoJ.remove(maiorJ);
			
		}
		else if(maiorJ.getLadoDir() < maiorC.getLadoDir()){
			mesa.add(maiorC);
			maoC.remove(maiorC);
		}
		
		
		/*aqui ira ser decidido qual o primeiro a jogar apos iniciar a mesa. caso tenha sido o computador
		 * a iniciar a mesa o proximo a jogar sera o jogador. Caso contrario sera o computador o proximo
		 * a jogar. Caso nenhum deles tenha iniciado a mesa significa que nenhum tinha uma peca dupla,
		 * o que significa que tera de ser removida uma da banca e o proximo a jogar sera escolhido
		 * aleatoriamente.*/
		if(maoC.size() == n){
			player = 1;
			System.out.println("\nA mesa sera iniciada por: Computador.");
		}
		else if (maoJ.size() == n){
			player = 0;
			System.out.println("\nA mesa sera iniciada por: " + jog);
		}
		else{
			player = aleatorio.nextInt(2);
			System.out.println("\nNinguem tem um duplo para jogar, sera removida uma peca da banca.");
			randomInt = aleatorio.nextInt(banca.size());
			mesa.add(banca.get(randomInt));
			banca.remove(banca.get(randomInt));
		}
	}
	
	
	// metodo que devolve uma representacao(String) da banca
	public String mostraBanca(){
		String b = "";
		for (PedraDomino i : banca){
			b += i.pedraToString()+" ";
		}
		return b;
	}
	
	//metodo que devolve a representacao(String) da mesa
	public String mostraMesa(){
		String m = "<->";
		for (PedraDomino i : mesa){
			m += i.pedraToString()+"-";
		}
		m = m.substring(0, m.length()-1);
		m += "<->";
		return m;
	}
	
	//metodo que devolve uma representacao(String) da mao do jogador
	public String mostraPedras(){
		String p = "";
		for (PedraDomino i : maoJ){
			p += i.pedraToString()+" ";
		}
		return p;
	}
	
	//metodo que devolve uma representacao(String) da mao do computador
	public String mostraMaoC(){
		String c = "";
		for (PedraDomino i : maoC){
			c += i.pedraToString()+" ";
		}
		return c;
	}
	
	/*metodo que verifica se foram cumpridas condicoes para terminar o jogo.*/
	public void fimJogo(){
		 /* total da soma dos pontos. estas vars sao utilizada para verificar quem tem menos pontos na 
		   sua mao.*/
		int somaPontosJ = 0;
		int somaPontosC = 0;
		
		/*verifica se a banca esta vazia*/
		if(banca.isEmpty()){
			System.out.println("\nA banca esta vazia.");
			System.out.println("\nGanha quem tiver menos pontos na sua mao.");
		}
		
		/*vai ser feita a contagem do total de pontos de cada mao caso a banca fique vazia para se 
		 decidir quem vai ganhar o jogo */
		if (!maoJ.isEmpty() && !maoC.isEmpty()){
			for(int i = 0;i < maoJ.size();i++){
				somaPontosJ += (maoJ.get(i).getLadoEsq()+maoJ.get(i).getLadoDir());
			}
			for(int i = 0;i < maoC.size();i++){
				somaPontosC += (maoC.get(i).getLadoEsq()+maoC.get(i).getLadoDir());
			}
			System.out.println("\nPontos Jogador: " + somaPontosJ);
			System.out.println("\nPontos Computador: " + somaPontosC);
			if (somaPontosJ > somaPontosC){
				JOptionPane.showMessageDialog(null, "O computador ganhou! Fica para a proxima.");
			}
			else if(somaPontosJ < somaPontosC){
				JOptionPane.showMessageDialog(null, "Parabens!! Ganhou o jogo!");
			}
			else{
				JOptionPane.showMessageDialog(null, "Empate!");
			}
		}
	}
	
	//metodo para realizar a jogada do jogador dada uma pedra x.
	public void jogaJogador(PedraDomino x){
		String extremidade = "";
	    /* a variavel temp e uma variavel temporaria que serve apenas para guardar o valor de um dos
		 * lados da peca antes de fazer os set's para que nao se perca o valor ao fazer o set desse lado*/
		int temp;
		
		/*Iterador da mao do jogador*/
		itJ = maoJ.iterator();
		
		/*aqui e percorrida a mao do jogador para saber se se a peca x que recebe como argumento
		 se encontra na mao*/
		while(itJ.hasNext()){
			PedraDomino j = itJ.next();
			/*se a peca x estiver na mao e entao perguntado ao utilizador se deseja coloca-la no inicio
			 ou no fim*/
			if (j.getLadoEsq() == x.getLadoEsq() && j.getLadoDir() == x.getLadoDir()){
				System.out.println("Deseja colocar a pedra onde? (0 - Inicio    1 - Fim )");
				input = new Scanner(System.in);
				extremidade = input.nextLine();
				break;
			}
		}
		/*De seguida e verificado cada caso(inicio/fim)*/
		if (extremidade.equals("0")){
			/* no primeiro if vai ser verificado se a peca que se deseja jogar tem o numero no seu
			 *  lado esquerdo igual ao numero da ponta esquerda. se se der o caso, a peca tera que ser
			 *   invertida e e de seguida removida da mao e adicionada no inicio da mesa.*/
			if(x.getLadoEsq() == mesa.get(0).getLadoEsq()){
				temp = x.getLadoDir();
				x.setLadoDir(x.getLadoEsq());
				x.setLadoEsq(temp);
				mesa.add(0,x);
				maoJ.remove(x);
				System.out.println("\nVez do computador");
			}
			/* no proximo if vai ser verificado se a peca que se deseja jogar tem o numero no seu
			 *  lado direito igual ao numero da ponta esquerda. se se der o caso, a peca vai ser logo
			 *  adicionada a mesa e removida da mao*/
			else if (x.getLadoDir() == mesa.get(0).getLadoEsq()){
				mesa.add(0,x);
				maoJ.remove(x);
				System.out.println("\nVez do computador");
			}
			/* se a peca escolhida nao puder mesmo ser jogada no inicio e mostrada uma mensagem de erro
			 * ao utilizador*/
			else{
				JOptionPane.showMessageDialog(null, "Jogada Invalida", "Erro", JOptionPane.ERROR_MESSAGE );
			}
		}
		/*vai verificar o caso do fim*/
		else if(extremidade.equals("1")){
			/* no primeiro if vai ser verificado se a peca que se deseja jogar tem o numero no seu
			 *  lado direito igual ao numero da ponta direita. se se der o caso, a peca tera que ser
			 *   invertida e e de seguida removida da mao e adicionada no fim da mesa.*/
			if(x.getLadoDir() == mesa.get(mesa.size()-1).getLadoDir()){
				temp = x.getLadoEsq();
				x.setLadoEsq(x.getLadoDir());
				x.setLadoDir(temp);
				mesa.add(x);
				maoJ.remove(x);
				System.out.println("\nVez do computador.");
			}
			/* no proximo if vai ser verificado se a peca que se deseja jogar tem o numero no seu
			 *  lado esquerdo igual ao numero da ponta direita. Se se der o caso, a peca vai ser logo
			 *  adicionada a mesa e removida da mao*/
			else if (x.getLadoEsq() == mesa.get(mesa.size()-1).getLadoDir()){
				mesa.add(x);
				maoJ.remove(x);
				System.out.println("\nVez do computador");
			}
			/* se a peca escolhida nao puder mesmo ser jogada no fim e mostrada uma mensagem de erro
			 * ao utilizador*/
			else{
				JOptionPane.showMessageDialog(null, "Jogada Invalida", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
		/* caso o utilizador introduza qualquer outra coisa alem de 0 e 1(Inicio e Fim) e mostrada uma 
		 * mensagem a pedir que introduza os valores especificados.*/
		else{
			JOptionPane.showMessageDialog(null, "Cumpra com as indicacoes, ou 0 ou 1.", "Erro", JOptionPane.ERROR_MESSAGE );	
		}
	}
	
	public void jogaComputador(){
		/*esta variavel ira ser utilizada para a contagem do numero de pecas que o computador pode jogar.
		 *  Se se verificar que nao pode jogar nenhuma procedera entao a remocao de uma peca aleatoria
		 *  da banca.*/
		int podeJogC;
		
		/* a variavel temp e uma variavel temporaria que serve apenas para guardar o valor de um dos
		 * lados da peca antes de fazer os set's para que nao se perca o valor ao fazer o set desse lado*/
		int temp; 
		
		// ira servir para comparar o numero de pontos de cada peca da mao
		int pts = 0;
		
		podeJogC = 0;
		int in = mesa.get(0).getLadoEsq();
		int fim = mesa.get(mesa.size()-1).getLadoDir();
		PedraDomino p = null;
		
		/* no proximo for foi feito um pequeno algoritmo para dar alguma "inteligencia" ao computador.
		 * Primeiro sao contadas quantas pecas o computador pode jogar e se tiver mais que duas pecas
		 * que pode jogar vai ser jogada a peca com o maior numero de pontos*/
		for(int i = 0;i < maoC.size();i++){
			if (maoC.get(i).getLadoEsq()==in || maoC.get(i).getLadoDir() == in || maoC.get(i).getLadoEsq()==fim || maoC.get(i).getLadoDir() == fim){
				podeJogC++;
				if (pts < maoC.get(i).getTotalPontos()){
					pts = maoC.get(i).getTotalPontos();
					p = maoC.get(i);
				} 
			}
		}
		
		if(podeJogC > 0){
			/* assim como no metodo jogaJogador, e verificado se a peca que se esta a comparar tem o
			 *  numero no seu lado direito igual ao numero da ponta esquerda. se se der o caso, a peca
			 *  sera de seguida adicionada no inicio da mesa e removida da mao.*/
			if(p.getLadoDir() == mesa.get(0).getLadoEsq()){
				mesa.add(0, p);
				maoC.remove(p);
				System.out.println("\nPeca jogada.");
			}
			/* no proximo if e verificado se a peca que se esta a comparar tem o numero no seu lado
			 *  esquerdo igual ao numero da ponta esquerda. se se der o caso, a peca tera que ser
			 *  invertida e depois adicionada no inicio da mesa e removida da mao.*/
			else if(p.getLadoEsq() == mesa.get(0).getLadoEsq()){
				temp = p.getLadoDir();
				p.setLadoDir(p.getLadoEsq());
				p.setLadoEsq(temp);
				mesa.add(0, p);
				maoC.remove(p);
				System.out.println("\nPeca jogada.");
			}
			/* no proximo if e verificado se a peca que se esta a comparar tem o
			 *  numero no seu lado esquerdo igual ao numero da ponta direita. se se der o caso, a peca
			 *  e de seguida adicionada no inicio da mesa e removida da mao.*/
			else if (p.getLadoEsq() == mesa.get(mesa.size()-1).getLadoDir()){
				mesa.add(p);
				maoC.remove(p);
				System.out.println("\nPeca jogada.");
			}	
			/* no proximo if e verificado se a peca que se esta a comparar tem o numero no seu lado
			 *  direito igual ao numero da ponta direita. se se der o caso, a peca tera que ser
			 *  invertida e depois adicionada no inicio da mesa e removida da mao.*/
			else if (p.getLadoDir() == mesa.get(mesa.size()-1).getLadoDir()){
				temp = p.getLadoDir();
				p.setLadoDir(p.getLadoEsq());
				p.setLadoEsq(temp);
				mesa.add(p);
				maoC.remove(p);
				System.out.println("\nPeca jogada.");
			}
		}
		/* se a variavel podeJog for 0 significa que o computador nao pode jogar nenhuma peca, como tal 
		 * tera que ser removida uma da banca.*/
		else{
			// a variavel inc serve para contar quantas vezes o computador vai buscar uma peca a banca
			inc++;
			
			/*quando o computador nao encontra nenhuma pedra que possa jogar e removida uma peca 
			 * aleatoriamente da banca para colocar na mao do computador e é chamado o metodo jogaComputador
			 * recursivamente para verificar se pode jogar essa mesma peca, caso contrario passa para o jogador*/
			if(inc < 2){
				System.out.println("\nComputador nao pode jogar nenhuma peca. \nIra ser removida uma da banca");
				if (banca.isEmpty()){
					fimJogo();
				}
				randomInt = aleatorio.nextInt(banca.size());
				maoC.add(banca.get(randomInt));
				banca.remove(randomInt);
				jogaComputador();
			}
			else{
				System.out.println("\nPeca removida nao pode ser jogada.");
			}
		}
			
	}
	
}
