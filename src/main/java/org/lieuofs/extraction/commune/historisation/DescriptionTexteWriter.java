package org.lieuofs.extraction.commune.historisation;

import org.lieuofs.commune.ICommuneSuisse;

class DescriptionTexteWriter implements NumeroHistorisationCommuneWriter {

	private ICommuneSuisse communePrecedente;
	
	private void ecrireCommune(ICommuneSuisse commune, StringBuilder builder) {
		builder.append(commune.getNomCourt());
		builder.append(" (").append(commune.getCanton().getNom()).append(" ").append(commune.getId())
		.append(" )");
	}
	
	@Override
	public String ecrireNumero(ICommuneSuisse commune) {
		String texte = null;
		if (null != communePrecedente && communePrecedente.getNumeroOFS() == commune.getNumeroOFS()
				&& !communePrecedente.getNom().equals(commune.getNom())) {
			StringBuilder builder = new StringBuilder();
			builder.append("N° ").append(commune.getNumeroOFS()).append("\t");
			ecrireCommune(communePrecedente,builder);
			builder.append("  ");
			ecrireCommune(commune,builder);
			builder.append("\n");
			texte = builder.toString();
		}
		communePrecedente = commune;
		return texte;
	}

}
