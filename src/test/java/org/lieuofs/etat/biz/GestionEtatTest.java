package org.lieuofs.etat.biz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.lieuofs.etat.EtatCritere;
import org.lieuofs.etat.IEtat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/beans_lieuofs.xml")
public class GestionEtatTest {

	@Resource(name = "gestionEtat")
	private IGestionEtat gestionnaire;

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
		assertTrue("Nombre etats < 200",200 > taille);
	}
	
	@Test
	public void etatNonMembreONU() {
		EtatCritere critere = new EtatCritere();
		critere.setMembreONU(Boolean.FALSE);
		Set<IEtat> etats = gestionnaire.rechercher(critere);
		int taille = etats.size();
		assertTrue("Nombre etats = 5 (Palestine, Vatican, Kosovo, Taïwan, Sahara Occidental)", 5 == taille);
	}
	
	@Test
	public void etatNonReconnuSuisse() {
		EtatCritere critere = new EtatCritere();
		critere.setReconnuSuisse(Boolean.FALSE);
		Set<IEtat> etats = gestionnaire.rechercher(critere);
		int taille = etats.size();
		assertTrue("Nombre etats = 4 (Suisse, Palestine, Taïwan, Sahara occidental)", 4 == taille);
	}
	
	@Test
	public void etatNonMembreONUReconnuParLaSuisse() {
		EtatCritere critere = new EtatCritere();
		critere.setReconnuSuisse(Boolean.TRUE);
		critere.setMembreONU(Boolean.FALSE);
		Set<IEtat> etats = gestionnaire.rechercher(critere);
		assertEquals("2 états non membres de l'ONU reconnu par la Suisse : le Vatican et le Kosovo", 2, etats.size());
	}
	
	@Test
	public void rechercheParCodeIso2() {
		EtatCritere critere = new EtatCritere();
		critere.setValideEtInvalide();
		critere.setCodeIsoAlpha2("fr");
		Set<IEtat> etats = gestionnaire.rechercher(critere);
		assertEquals("1 état : la France", 1, etats.size());
		assertEquals("Nom", "France", etats.iterator().next().getFormeCourte("fr"));
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
		assertTrue("Nbre états reconnus", 193 <= etats.size());
	}
}
