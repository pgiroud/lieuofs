/**
 * This file is part of LieuOFS.
 *
 * LieuOFS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * LieuOFS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LieuOFS.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.lieuofs.extraction.etatpays;

import java.io.*;
import java.util.*;


import org.lieuofs.geo.territoire.biz.EtatTerritoireCritere;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoireDao;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoireFichierXmlDao;
import org.lieuofs.geo.territoire.biz.dao.EtatTerritoirePersistant;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public final class ExtractionPays {

	private final EtatTerritoireDao dao;

	public ExtractionPays() {
		dao = construireEtatTerritoireDao();
	}

	private EtatTerritoireDao construireEtatTerritoireDao() {
		String nomFichier = "org/lieuofs/geo/territoire/biz/dao/eCH0072.xml";
		EtatTerritoireFichierXmlDao xmlDao = new EtatTerritoireFichierXmlDao(nomFichier);
		return xmlDao;
	}

	private EtatWriter construireRedacteur() {
        return new NumOFSEtatWriter();
	}

	private List<EtatTerritoirePersistant> obtenirEtatTerritoire() {
		EtatTerritoireCritere critere = new EtatTerritoireCritere();
		//critere.setEstEtat(Boolean.FALSE);
		Set<EtatTerritoirePersistant> etats = dao.rechercher(critere);
		List<EtatTerritoirePersistant> listeTriee = new ArrayList<EtatTerritoirePersistant>(etats);
		Collections.sort(listeTriee, new Comparator<EtatTerritoirePersistant>() {

			@Override
			public int compare(EtatTerritoirePersistant o1,
							   EtatTerritoirePersistant o2) {
				//return o1.getFormeCourte("fr").compareTo(o2.getFormeCourte("fr"));
				return o1.getNumeroOFS() - o2.getNumeroOFS();
			}

		});
		return filtre(listeTriee);
	}

	private List<EtatTerritoirePersistant> filtre(List<EtatTerritoirePersistant> listeOriginal) {
		List<EtatTerritoirePersistant> listeFiltree = new ArrayList<EtatTerritoirePersistant>();
		for (EtatTerritoirePersistant etatTerritoire : listeOriginal) {
			// On sélectionne tous les états territoire reconnu il ya moins de 2 ans
			Calendar cal = Calendar.getInstance();
			cal.roll(Calendar.YEAR,-5);
			Date ilYa5an = cal.getTime();
			Date dateReconnaissance = etatTerritoire.getDateReconnaissance();
			if (null != dateReconnaissance
					&& dateReconnaissance.compareTo(ilYa5an) > 0) {
				listeFiltree.add(etatTerritoire);
			}
		}
		return listeFiltree;
	}

	private void ecrire(String chaîne, Writer fluxSortantCaractere) {
		try {
			fluxSortantCaractere.append(chaîne);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	public void extraire(Writer fluxSortantCaratere) {
		EtatWriter redacteur = new NumOFSEtatWriter();
		obtenirEtatTerritoire().stream()
				.map(redacteur::ecrireEtat)
				.filter(String::isBlank)
				.forEach(s -> ecrire(s,fluxSortantCaratere));

	}


	/**
	 * @param args
	 */
	static void main(String[] args) throws IOException {
		ExtractionPays extracteur = new ExtractionPays();

		try (OutputStream fluxSortantBinaire = new FileOutputStream("ExtractionPaysOFSReconnuRecemment.txt");
			 Writer fluxSortantCaractere = new OutputStreamWriter(fluxSortantBinaire,"UTF-8");
			 Writer fluxSortantCaractereAvecTampon = new BufferedWriter(fluxSortantCaractere);
		) {
			extracteur.extraire(fluxSortantCaractereAvecTampon);
		}
	}


    
    
	private static interface EtatWriter {
		String ecrireEtat(EtatTerritoirePersistant etat);
	}

	private static class NumOFSEtatWriter implements EtatWriter {

		private boolean estPremier = true;
		
		@Override
		public String ecrireEtat(EtatTerritoirePersistant etat) {
			String nom = String.valueOf(etat.getFormeCourte("fr"));
			if (estPremier) {
				estPremier = false;
				return nom;
			}
			else return "\n" + nom;
		}
		
	}
}
