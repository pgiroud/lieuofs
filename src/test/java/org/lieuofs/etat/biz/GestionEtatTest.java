package org.lieuofs.etat.biz;

import java.util.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lieuofs.etat.EtatCritere;
import org.lieuofs.etat.IEtat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.lieuofs.ContexteTest.INSTANCE;

public class GestionEtatTest {

	private IGestionEtat gestionnaire;

	@BeforeEach
	public void contexte() {
		gestionnaire = INSTANCE.construireGestionEtat();
	}

	@Test
	public void tousLesEtats() {
		Set<IEtat> etats = gestionnaire.rechercher(new EtatCritere());
		int taille = etats.size();
		List<String> descriptions = new ArrayList<String>();
		for (IEtat etat : etats) {
			String description = etat.getNumeroOFS() + "\t" + etat.getNom();
			descriptions.add(description);
		}
		Collections.sort(descriptions);
		assertThat(200 > taille).isTrue();
	}
	
	@Test
	public void etatNonMembreONU() {
		EtatCritere critere = new EtatCritere();
		critere.setMembreONU(Boolean.FALSE);
		Set<IEtat> etats = gestionnaire.rechercher(critere);
		// Nombre etats = 6 (Palestine, Vatican, Kosovo, Taïwan, Sahara Occidental, Îles Cook)
		assertThat(etats).hasSize(6);

	}
	
	@Test
	public void etatNonReconnuSuisse() {
		EtatCritere critere = new EtatCritere();
		critere.setReconnuSuisse(Boolean.FALSE);
		Set<IEtat> etats = gestionnaire.rechercher(critere);
		// Nombre etats = 4 (Suisse, Palestine, Taïwan, Sahara occidental)
		assertThat(etats).hasSize(4);
	}
	
	@Test
	public void etatNonMembreONUReconnuParLaSuisse() {
		EtatCritere critere = new EtatCritere();
		critere.setReconnuSuisse(Boolean.TRUE);
		critere.setMembreONU(Boolean.FALSE);
		Set<IEtat> etats = gestionnaire.rechercher(critere);
		// 3 états non membres de l'ONU reconnu par la Suisse : le Vatican, le Kosovo, Îles Cook
		assertThat(etats).hasSize(3);

	}
	
	@Test
	public void rechercheParCodeIso2() {
		EtatCritere critere = new EtatCritere();
		critere.setValideEtInvalide();
		critere.setCodeIsoAlpha2("fr");
		Set<IEtat> etats = gestionnaire.rechercher(critere);
		// 1 état : la France
		assertThat(etats).hasSize(1);
		assertThat(etats.iterator().next().getFormeCourte("fr")).isEqualTo("France");
	}
	
	@Test
	public void rechercheEtatReconnuSuisse() {
		EtatCritere critere = new EtatCritere();
		critere.setReconnuSuisse(Boolean.TRUE);
		Set<IEtat> etats = gestionnaire.rechercher(critere);
		List<String> descriptions = new ArrayList<String>();
		for (IEtat etat : etats) {
			String description = etat.getNumeroOFS() + "\t" + etat.getNom();
			descriptions.add(description);
		}
		Collections.sort(descriptions);
		assertThat(193 <= etats.size()).isTrue();
	}

    @Test
    public void nonReconnaissanceKosovoFin2007() {
        EtatCritere critere = new EtatCritere();
        Calendar cal = Calendar.getInstance();
        cal.set(2007,Calendar.DECEMBER,31);
        critere.setReconnuSuisseALaDate(cal.getTime());
        Set<IEtat> etats = gestionnaire.rechercher(critere);
        assertThat(extractProperty("numeroOFS").from(etats)).doesNotContain(8256);

    }

    @Test
    public void reconnaissanceKosovoFin2008() {
        EtatCritere critere = new EtatCritere();
        Calendar cal = Calendar.getInstance();
        cal.set(2008,Calendar.DECEMBER,31);
        critere.setReconnuSuisseALaDate(cal.getTime());
        Set<IEtat> etats = gestionnaire.rechercher(critere);
        assertThat(extractProperty("numeroOFS").from(etats)).contains(8256);
    }

}
