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

package afc.extraction.commune;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch.ge.afc.ofs.commune.CommuneCritere;
import ch.ge.afc.ofs.commune.ICommuneSuisse;
import ch.ge.afc.ofs.commune.biz.IGestionCommune;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class ExtractionNumHistorisationCommune {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
		        new String[] {"beans_lieuofs.xml"});
		CommuneCritere critere = new CommuneCritere();
		IGestionCommune gestionnaire = (IGestionCommune)context.getBean("gestionCommune");
		List<ICommuneSuisse> communes = gestionnaire.rechercher(critere);
		Collections.sort(communes, new Comparator<ICommuneSuisse>(){
			public int compare(ICommuneSuisse com1, ICommuneSuisse com2) {
				return com1.getNumeroOFS() - com2.getNumeroOFS();
			}
		});
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("NumeroHistorisationCommune.sql"),"Windows-1252"));
		NumeroHistorisationCommuneWriter commWriter = new NumeroHistorisationRefonteSQLWriter();
		for (ICommuneSuisse commune : communes) {
			String numHistStr = commWriter.ecrireNumero(commune);
			if (null != numHistStr) {
				writer.append(numHistStr);
			}
		}
		writer.close();
		
	}

	
	private static interface NumeroHistorisationCommuneWriter {
		String ecrireNumero(ICommuneSuisse commune);
	}
	
	private static class DescriptionTexteWriter implements NumeroHistorisationCommuneWriter {

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
	
	private static class NumeroHistorisationRefonteSQLWriter implements NumeroHistorisationCommuneWriter {

		public NumeroHistorisationRefonteSQLWriter() {
		}
		
		private void ecrireNumero(StringBuilder builder, int noOFS, int noHistorisation) {
			builder.append("UPDATE LFI_T_COMMUNE\n");
			builder.append("SET LOC_N_NUM_HIST = ").append(noHistorisation).append("\n");
			builder.append("WHERE COM_N_ID IN (SELECT LOC_N_ID FROM LFI_T_LIEU_FISCAL WHERE LOC_N_NO_OFS = ").append(noOFS).append(") \n");
			builder.append("/\n\n");
		}
		
		
		@Override
		public String ecrireNumero(ICommuneSuisse commune) {
			if (null == commune.getRadiation()) {
				StringBuilder builder = new StringBuilder();
				ecrireNumero(builder,commune.getNumeroOFS(),commune.getId().intValue());
				return builder.toString();
			}
			return null;
		}
		
	}
	
}
