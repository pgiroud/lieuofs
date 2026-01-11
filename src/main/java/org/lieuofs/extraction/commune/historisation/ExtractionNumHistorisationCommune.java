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

package org.lieuofs.extraction.commune.historisation;

import java.io.*;

import java.util.Comparator;
import java.util.List;

import org.lieuofs.extraction.commune.ConstructeurContexteGestionCommune;


import org.lieuofs.commune.CommuneCritere;
import org.lieuofs.commune.ICommuneSuisse;
import org.lieuofs.commune.biz.IGestionCommune;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class ExtractionNumHistorisationCommune {

	private final IGestionCommune gestionnaire;
	private final NumeroHistorisationCommuneWriter redacteur;

	public ExtractionNumHistorisationCommune() throws IOException {
		gestionnaire = new ConstructeurContexteGestionCommune().construireGestionCommune();
		redacteur = construireRedacteur();
	}

	private List<ICommuneSuisse> obtenirCommunes() {
		CommuneCritere critere = new CommuneCritere();
		List<ICommuneSuisse> communes = gestionnaire.rechercher(critere);
		communes.sort(new Comparator<ICommuneSuisse>() {
			public int compare(ICommuneSuisse com1, ICommuneSuisse com2) {
				return com1.getNumeroOFS() - com2.getNumeroOFS();
			}
		});
		return communes;
	}

	public void extraire(Writer fluxSortantCaractere) throws IOException {
		obtenirCommunes().stream().map(redacteur::ecrireNumero)
				.filter(String::isBlank)
				.forEach(s -> ecrire(s,fluxSortantCaractere));
	}

	private void ecrire(String chaîne, Writer fluxSortantCaractere) {
		try {
			fluxSortantCaractere.append(chaîne);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private NumeroHistorisationCommuneWriter construireRedacteur() {
		return new NumeroHistorisationRefonteSQLWriter();
	}


	/**
	 * @param args
	 */
	static void main(String[] args) throws IOException {
		ExtractionNumHistorisationCommune extracteur = new ExtractionNumHistorisationCommune();
		try (OutputStream fluxSortantBinaire = new FileOutputStream("NumeroHistorisationCommune.sql");
			 Writer fluxSortantCaractere = new OutputStreamWriter(fluxSortantBinaire,"Windows-1252");
			 Writer fluxSortantCaractereAvecTampon = new BufferedWriter(fluxSortantCaractere);) {
			extracteur.extraire(fluxSortantCaractereAvecTampon);
		}
	}
	
}
