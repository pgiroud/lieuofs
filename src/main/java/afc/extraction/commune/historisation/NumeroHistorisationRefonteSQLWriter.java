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

package afc.extraction.commune.historisation;

import ch.ge.afc.ofs.commune.ICommuneSuisse;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public class NumeroHistorisationRefonteSQLWriter implements
		NumeroHistorisationCommuneWriter {

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
