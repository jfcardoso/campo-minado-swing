package br.com.jefferson.view;

import javax.swing.JFrame;

import br.com.jefferson.model.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame{
	
	public TelaPrincipal() {
		
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 20);
		
		PainelTabuleiro painel = new PainelTabuleiro(tabuleiro);
		
		this.add(painel);
		
		setTitle("Campo Minado 2.0");
		setSize(690,438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);	
		
	}
	
	public static void main(String[] args) {
		
		new TelaPrincipal();
	}

}
