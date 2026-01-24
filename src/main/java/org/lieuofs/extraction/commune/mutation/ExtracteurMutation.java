/*
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

package org.lieuofs.extraction.commune.mutation;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.lieuofs.commune.IMutationCommune;
import org.lieuofs.commune.MutationCommuneCritere;

import org.lieuofs.commune.biz.IGestionCommune;
import org.lieuofs.extraction.commune.ConstructeurContexteGestionCommune;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class ExtracteurMutation {

	private final IGestionCommune gestionnaire;
	private final MutationCommuneWriter redacteur;

	public ExtracteurMutation(MutationCommuneWriter redacteur) throws IOException {
		gestionnaire = new ConstructeurContexteGestionCommune().construireGestionCommune();
		this.redacteur = redacteur;
	}



	public void extraireMutation(Date depuis, Writer fluxSortantCaractere) throws IOException {
		extraireMutation(depuis,null,fluxSortantCaractere);
	}

	private List<IMutationCommune> obtenirMutation(Date depuis, Date jusqua) {
		MutationCommuneCritere critere = new MutationCommuneCritere();
		if (null != depuis) {
			critere.setDateDebut(depuis);
		}
		if (null != jusqua) {
			critere.setDateFin(jusqua);
		}
		return gestionnaire.rechercherMutation(critere);
	}

	private void ecrire(String chaine, Writer fluxSortantCaractere) {
		try {
			fluxSortantCaractere.append(chaine);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private static boolean isNotBlank(String str) {
		return null != str && !str.isBlank();
	}

	public void extraireMutation(Date depuis, Date jusqua, Writer fluxSortantCaractere) {
		obtenirMutation(depuis,jusqua).stream()
				.map(redacteur::ecrireMutation)
				.filter(ExtracteurMutation::isNotBlank)
				.forEach(s -> ecrire(s,fluxSortantCaractere));
	}

	private static void sortie(Calendar cal, MutationCommuneWriter redacteur, String nomFichierProduit) throws IOException {
		ExtracteurMutation extracteur = new ExtracteurMutation(redacteur);
		String codage = Arrays.stream(nomFichierProduit.split("\\.")).toList().getLast().equalsIgnoreCase("sql") ?
				"Windows-1252" : "UTF-8";
		try (OutputStream fluxSortantBinaire = new FileOutputStream(nomFichierProduit);
			 Writer fluxSortantCaractere = new OutputStreamWriter(fluxSortantBinaire,codage);
			 Writer fluxSortantCaractereAvecTampon = new BufferedWriter(fluxSortantCaractere);
		) {
			extracteur.extraireMutation(cal.getTime(), fluxSortantCaractereAvecTampon);
		}
	}

	static void main(String[] args) throws IOException {
		Calendar cal = Calendar.getInstance();
		cal.set(2025, Calendar.JANUARY,2);
		sortie(cal,new MutCommuneFreemarkerRefonteSQLWriter("Script 2603"),"MutationCommune2026.sql");
		sortie(cal,new DescriptionTexteWriter(),"MutationCommune2026.txt");
	}
	
	
}
