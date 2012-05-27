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

package afc.extraction.etatpays;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import ch.ge.afc.ofs.geo.territoire.biz.EtatTerritoireCritere;
import ch.ge.afc.ofs.geo.territoire.biz.dao.EtatTerritoireDao;
import ch.ge.afc.ofs.geo.territoire.biz.dao.EtatTerritoirePersistant;
import ch.ge.afc.ofs.util.InfosONUetISO3166;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class ExtractionEtat {

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
		        new String[] {"beans_lieuofs.xml"});
		EtatTerritoireCritere critere = new EtatTerritoireCritere();
		critere.setEstEtat(Boolean.TRUE);
		// critere.setValide(Boolean.TRUE);
				
		EtatTerritoireDao dao = (EtatTerritoireDao)context.getBean("etatTerritoireDao");
		Set<EtatTerritoirePersistant> etats = dao.rechercher(critere);
		
		EtatWriter etatWriter = new CsvPlatEtatWriter();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Etat.txt"),"UTF-8"));
		
		List<EtatTerritoirePersistant> listeTriee = new ArrayList<EtatTerritoirePersistant>(etats);
		Collections.sort(listeTriee, new Comparator<EtatTerritoirePersistant>() {

			@Override
			public int compare(EtatTerritoirePersistant o1,
					EtatTerritoirePersistant o2) {
				//return o1.getFormeCourte("fr").compareTo(o2.getFormeCourte("fr"));
				return o1.getNumeroOFS() - o2.getNumeroOFS();
			}
			
		});
		
		
		for (EtatTerritoirePersistant etat : listeTriee) {
			String etatStr = etatWriter.ecrireEtat(etat);
			if (null != etatStr) {
				writer.append(etatStr);
			}
		}
		writer.close();
	}

	private static interface EtatWriter {
		String ecrireEtat(EtatTerritoirePersistant etat);
	}
	
	private static class RemarqueSQLWriter implements EtatWriter {

		@Override
		public String ecrireEtat(EtatTerritoirePersistant etat) {
			String remarques = etat.getRemarque("fr");
			if (StringUtils.hasText(remarques)) {
				StringBuilder builder = new StringBuilder();
				builder.append("----------------------------------------------------------------\n");
				builder.append("-- Ajout remarques pour ").append(etat.getFormeCourte("fr")).append("\n");
				builder.append("----------------------------------------------------------------\n\n");
				builder.append("INSERT INTO EVM_T_EVT_METIER e (EVM_N_ID,EVM_C_DOMAINE, EVM_N_TYPE_OBJ,EVM_N_TYPE,\n");
				builder.append("\tEVM_N_OBJET_ID, EVM_N_DATE, EVM_N_DATE_JOURNAL, EVM_N_HEURE_JOURNAL, EVM_C_UTILISATEUR,\n");
				builder.append("\tEVM_C_COMMENTAIRE)\n");		
				builder.append("SELECT EVM_S_EVT_METIER.nextval,\n");		
				builder.append("\t'AFC.LIEUFISCAL',\n");	
				builder.append("\t2,\n");		
				builder.append("\t2,\n");		
				builder.append("\tLOC_N_ID,\n");		
				builder.append("\tTO_NUMBER(TO_CHAR(SYSDATE, 'YYYYMMDD')),\n");		
				builder.append("\tTO_NUMBER(TO_CHAR(SYSDATE, 'YYYYMMDD')),\n");		
				builder.append("\tTO_NUMBER(TO_CHAR(SYSDATE, 'HH24MISS')),\n");		
				builder.append("\t'Script R909',\n");		
				builder.append("\t'Ajout remarques depuis fichier OFS'\n");		
				builder.append("FROM LFI_T_LIEU_FISCAL\n");		
				builder.append("WHERE LOC_N_NO_OFS = ").append(etat.getNumeroOFS()).append("\n");		
				builder.append("/\n");		
				builder.append("-- Conditions de validation : 0 ou 1 ligne ajoutée\n\n");		
				
				builder.append("UPDATE LFI_T_LIEU_FISCAL");
				builder.append("\n");
				builder.append("SET LOC_C_REMARQUES = '");
				builder.append(remarques.replace("'", "''"));
				builder.append("'\n");
				builder.append("WHERE LOC_N_NO_OFS = ").append(etat.getNumeroOFS()).append("\n");
				builder.append("/\n");
				builder.append("-- Condition de validation : 0 ou 1 ligne mise à jour");
				builder.append("\n\n");
				return builder.toString();
			}
			else return null;
		}
		
	}
	
	
	private static class CsvPlatEtatWriter implements EtatWriter {
		
		private final static String SEPARATEUR = ";"; 
		
		public String ecrireEtat(EtatTerritoirePersistant etat) {
			StringBuilder builder = new StringBuilder();
			// Numéro OFS
			builder.append(etat.getNumeroOFS());
			builder.append(SEPARATEUR);
			// Forme courte
			builder.append(etat.getFormeCourte("fr"));
			builder.append(SEPARATEUR);
			// Validité
			builder.append(etat.isValide() ? 1 : 0);
			builder.append(SEPARATEUR);
//			// Remarques en français
//			String remarques = etat.getRemarque("fr");
//			if (null == remarques) remarques = "";
//			builder.append(remarques);
//			builder.append(SEPARATEUR);
			InfosONUetISO3166 infos = etat.getInfosISO();
			if (null != infos) {
				// Alpha 2
				builder.append(infos.getCodeIsoAlpha2());
				builder.append(SEPARATEUR);
				// Alpha 3
				builder.append(infos.getCodeIsoAlpha3());
				builder.append(SEPARATEUR);
				// num ONU
				if (0 < infos.getCodeNumeriqueONU()) builder.append(infos.getCodeNumeriqueONU());
			} else {
				builder.append(SEPARATEUR);
				builder.append(SEPARATEUR);
			}
			builder.append("\n");
			return builder.toString();
		}
	}
	
	private static class XsdListeRecapISWriter implements EtatWriter {
		
		private static final String[] langues = new String[]{"fr","de","it","en"};
		
		public String ecrireEtat(EtatTerritoirePersistant etat) {
			String marge = "\t\t\t";
			InfosONUetISO3166 infos = etat.getInfosISO();
			if (null == infos) return null;
			StringBuilder builder = new StringBuilder();
			builder.append(marge);
			builder.append("<xs:enumeration value=\"");
			builder.append(infos.getCodeIsoAlpha2());
			builder.append("\">");
			builder.append("\n");
			builder.append(marge);
			builder.append("\t");
			builder.append("<xs:annotation>");
			builder.append("\n");
			for (String langue : langues) {
				builder.append(marge);
				builder.append("\t\t");
				builder.append("<xs:documentation xml:lang=\"");
				builder.append(langue);
				builder.append("\">");
				builder.append(etat.getFormeCourte(langue));
				builder.append("</xs:documentation>");
				builder.append("\n");
			}
			builder.append(marge);
			builder.append("\t");
			builder.append("</xs:annotation>");
			builder.append("\n");
			builder.append(marge);
			builder.append("</xs:enumeration>");
			builder.append("\n");
			return builder.toString();
		}
	}
	
}
