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
		 * � nesse m�todo que recebo um evento que me diz se houve uma vit�ria
		 * ou uma derrota.
		 */
		tabuleiro.registrarObservadorTab(e -> {
			SwingUtilities.invokeLater(() -> {
				if(e.booleanValue()) {
					JOptionPane.showMessageDialog(this,"Parab�ns! Voc� Ganhou!!!");
				}else {
					JOptionPane.showMessageDialog(this,"Voc� perdeu!");
				}
				
				tabuleiro.reiniciarJogo();
			});
				
		});		
		
	}

}
	