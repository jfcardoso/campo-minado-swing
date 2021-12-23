package br.com.jefferson.model;

import java.util.ArrayList;
import java.util.List;




public class Campo {

	private int linha;

	private int coluna;

	// já inicializam "FALSE" como padrão.
	private boolean estaMinado;
	private boolean estaAberto;
	private boolean estaMarcado;

	// Auto-relacionamento= relação 1:n consigo mesmo
	private List<Campo> vizinhos = new ArrayList<>();
	
	private List <CampoObserver> observers = new ArrayList<>();
	
		
	public void registryObserver(CampoObserver observador) {
		
		observers.add(observador);
	}
	
	private void notifyObserver(CampoEvento evento) {
		observers.stream()
				.forEach(o -> o.eventoOcorreu(this, evento));
		
	}

	Campo(int linha, int coluna) {
		this.coluna = coluna;
		this.linha = linha;
	}

	/*
	 * testa se o campo é vizinho do campo marcado, tanto diagonalmente, quanto na
	 * mesma linha ou coluna do campo marcado.
	 */
	public boolean adicionarVizinho(Campo candidatoVizinho) {

		boolean linhaDiferente = this.linha != candidatoVizinho.linha;
		boolean coluniaDiferente = this.coluna != candidatoVizinho.coluna;

		boolean linhaDiagonal = linhaDiferente && coluniaDiferente;

		int deltaLinha = Math.abs(this.linha - candidatoVizinho.linha);
		int deltaColuna = Math.abs(this.coluna - candidatoVizinho.coluna);

		/*
		 * se a soma for 1 é vizinho e está na mesma linha ou coluna, se der 2 é vizinho
		 * e está na diagonal.
		 */
		int deltaGeral = deltaColuna + deltaLinha;

		if (deltaGeral == 1 && !linhaDiagonal) {
			vizinhos.add(candidatoVizinho);
			return true;
		} else if (deltaGeral == 2 && linhaDiagonal) {
			vizinhos.add(candidatoVizinho);
			return true;
		} else {
			// não é um campo vizinho
			return false;
		}
	}

	/*
	 * marca ou desmarca o campo como campo minado. Uma vez marcado, o campo não
	 * pode ser aberto.
	 */
	public void alternarMarcacao() {
		if (!estaAberto) {
			estaMarcado = !estaMarcado;
			
			if (estaMarcado) {
				notifyObserver(CampoEvento.MARCAR);
			} else {
				notifyObserver(CampoEvento.DESMARCAR);
			}
		}
	}

	public boolean abrirCampo() {

		// se não estiver aberto e não estiver marcado, a função abre o campo
		if (!estaAberto && !estaMarcado) {
			if (estaMinado) {
				notifyObserver(CampoEvento.EXPLODIR);
				return true;
			}
			
			setEstaAberto(true);
						
			if (expandirVizinhos()) {
				vizinhos.forEach(v -> v.abrirCampo());
			}

			return true;

		} else {
			return false;
		}

	}

	public boolean expandirVizinhos() {

		return vizinhos.stream().noneMatch(vizinho -> vizinho.estaMinado);
	}

	// permite minar um campo
	public void minarCampo() {

		estaMinado = true;
	}

	/*
	 * ao clicar em um campo qualquer, essa funçao testa se o objetivo final foi
	 * alcançado. Ou o campo foi aberto com sucesso, ou ele foi protegido (marcado
	 * como perigoso) com sucesso.
	 */
	public boolean objetivoAlcancado() {

		boolean campoAberto = !estaMinado && estaAberto;
		boolean campoProtegido = estaMinado && estaMarcado;

		return campoAberto || campoProtegido;

	}

	/*
	 * este método mostra quantas minas tem ativas na vizinhança
	 */
	public int mostrarMinasNaVizinhanca() {
		return (int) vizinhos.stream().filter(v -> v.estaMinado).count();
	}

	/*
	 * reset das variáveis para o default inicial do jogo
	 */
	public void reiniciarCampos() {
		estaAberto = false;
		estaMarcado = false;
		estaMinado = false;
		notifyObserver(CampoEvento.REINICIAR);
	}

	

	public boolean isMarcado() {
		return estaMarcado;
	}

	public boolean isMinado() {
		return estaMinado;
	}

	public boolean isAberto() {
		return estaAberto;
	}
	
	
	void setEstaAberto(boolean aberto) {
		this.estaAberto = aberto;
		
		if (aberto) {
			notifyObserver(CampoEvento.ABRIR);
		}
	}

	
	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

}
