package br.com.jefferson.view;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.jefferson.model.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {
	
	public PainelTabuleiro(Tabuleiro tabuleiro) {
		
		setLayout(new GridLayout(
			tabuleiro.getQntLinhas(),tabuleiro.getQntColunas()));
		
		//c -> objeto do tipo campo
		tabuleiro.forEachPersonalizado(c -> add(new BotaoCampo(c)));
		
		/*
		 * é nesse método que recebo um evento que me diz se houve uma vitória
		 * ou uma derrota.
		 */
		tabuleiro.registrarObservadorTab(e -> {
			SwingUtilities.invokeLater(() -> {
				if(e.booleanValue()) {
					JOptionPane.showMessageDialog(this,"Parabéns! Você Ganhou!!!");
				}else {
					JOptionPane.showMessageDialog(this,"Você perdeu!");
				}
				
				tabuleiro.reiniciarJogo();
			});
				
		});		
		
	}

}
	