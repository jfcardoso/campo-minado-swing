package br.com.jefferson.model;

@FunctionalInterface
public interface CampoObserver {
	
	public void eventoOcorreu(Campo campo, CampoEvento evento);

}
