package br.com.jefferson.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class Tabuleiro implements CampoObserver {

	private final int qntLinhas;
	private final int qntColunas;
	private final int quantidadeMinas;

	

	private final List<Campo> campos = new ArrayList<Campo>();
	
	private final List<Consumer<Boolean>> observadoresTab = new ArrayList<>();
	
	public void forEachPersonalizado(Consumer<Campo> function) {
		
		campos.forEach(function);
	}
	
	
	public void registrarObservadorTab(Consumer<Boolean> observadortab) {
		
		observadoresTab.add(observadortab);
	}
	
	
	private void notificarObservadorTab(boolean resultado) {
		
		observadoresTab.stream().forEach(o -> o.accept(resultado));
	}
	
	
	public Tabuleiro(int quantidadeLinhas, int quantidadeColunas, int quantidadeMinas) {

		this.qntLinhas = quantidadeLinhas;
		this.qntColunas = quantidadeColunas;
		this.quantidadeMinas = quantidadeMinas;

		gerarCampos();
		associarVizinhos();
		gerarMinasAleatoriamente();

	}

	public void abrirCampoEscolhido(int linha, int coluna) {

		Predicate<Campo> predicado =
				c -> c.getLinha() == linha && c.getColuna() == coluna;

		campos.parallelStream()
				.filter(predicado)
				.findFirst()
				.ifPresent(c -> c.abrirCampo());

	}

	
	
	public void alterarMarcacaoCampoEscolhido(int linha, int coluna) {

		Predicate<Campo> predicado = c -> c.getLinha() == linha &&
										c.getColuna() == coluna;

		campos.parallelStream().filter(predicado)
								.findFirst()
								.ifPresent(c -> c.alternarMarcacao());

	}

	private void gerarCampos() {
		for (int linha = 0; linha < qntLinhas; linha++) {
			for (int coluna = 0; coluna < qntColunas; coluna++) {
				Campo campo = new Campo(linha, coluna);
				campo.registryObserver(this);
				campos.add(campo);
			}
		}

	}

	private void associarVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}

	}

	private void gerarMinasAleatoriamente() {

		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();

		do {			
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minarCampo();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas < quantidadeMinas);

	}

	private boolean conseguiuGanharJogo() {
		Predicate<Campo> naoExplodiu = n -> n.objetivoAlcancado();
		boolean vitoria = campos.stream().allMatch(naoExplodiu);
		return vitoria;
	}

	public void reiniciarJogo() {
		campos.forEach(c -> c.reiniciarCampos());
		gerarMinasAleatoriamente();
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if (evento == CampoEvento.EXPLODIR){
			mostrarMinasNoFinal();
			notificarObservadorTab(false);
		} else if(conseguiuGanharJogo()) {
			notificarObservadorTab(true);
		}
		
	}
	
	private void mostrarMinasNoFinal() {

		campos.stream()
				.filter(c-> c.isMinado())
				.filter(c-> !c.isMarcado())
				.forEach(c -> c.setEstaAberto(true));
	}
	
	public int getQntLinhas() {
		return qntLinhas;
	}


	public int getQntColunas() {
		return qntColunas;
	}
	
}

