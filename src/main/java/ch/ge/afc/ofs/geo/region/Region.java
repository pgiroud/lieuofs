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

package ch.ge.afc.ofs.geo.region;

import java.util.HashMap;
import java.util.Map;


/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public enum Region {
	NORTHERN_EUROPE(11l,"Europe septentrionale","11","11","Northern Europe"),
	EASTERN_EUROPE(12l,"Europe orientale","12","12","Eastern Europe"),
	CENTRAL_EUROPE(13l,"Europe centrale","13","13","Central Europe"),
	WESTERN_EUROPE(14l,"Europe occidentale","14","14","Western Europe"),
	SOUTH_WESTERN_EUROPE(15l,"Europe méridionale occidentale","15","15","South-West Europe"),
	SOUTHERN_EUROPE(16l,"Europe méridionale","16","16","Southern Europe"),
	SOUTH_EAST_EUROPE(17l,"Europe méridionale orientale","17","17","South-East Europe"),
	NORTHERN_AFRICA(21l,"Afrique septentrionale","21","21","Northern Africa"),
	EASTERN_AFRICA(22l,"Afrique orientale","22","22","Eastern Africa"),
	CENTRAL_AFRICA(23l,"Afrique centrale","23","23","Central Africa"),
	WESTERN_AFRICA(24l,"Afrique occidentale","24","24","Western Africa"),
	SOUTH_WEST_AFRICA(25l,"Afrique méridionale occidentale","25","25","South-West Africa"),
	SOUTHERN_AFRICA(26l,"Afrique méridionale","26","26","Southern Africa"),
	SOUTH_EAST_AFRICA(27l,"Afrique méridionale orientale","27","27","South-East Africa"),
	NORTHERN_AMERICA(31l,"Amérique du Nord","31","31","Northern America"),
	THE_CARIBBEAN(32l,"Caraïbes","32","32","The Caribbean"),
	CENTRAL_AMERICA(33l,"Amérique centrale","33","33","Central America"),
	NORTHERN_SOUTH_AMERICA(34l,"Amérique du Sud septentrionale","34","34","Northern South-America"),
	MIDDLE_SOUTH_AMERICA(35l,"Amérique du Sud centrale","35","35","Middle South-America"),
	SOUTHERN_SOUTH_AMERICA(36l,"Amérique du Sud méridionale","36","36","Southern South-America"),
	NORTHERN_ASIA(41l,"Asie septentrionale","41","41","Northern Asia"),
	EASTERN_ASIA(42l,"Asie orientale","42","42","Eastern Asia"),
	CENTRAL_ASIA(43l,"Asie centrale","43","43","Central Asia"),
	WESTERN_ASIA(44l,"Asie occidentale","44","44","Western Asia"),
	SOUTH_WEST_ASIA(45l,"Asie méridionale occidentale","45","45","South-West Asia"),
	SOUTHERN_ASIA(46l,"Asie méridionale","46","46","Southern Asia"),
	SOUTH_EAST_ASIA(47l,"Asie méridionale orientale","47","47","South-East Asia"),
	NORTHERN_OCEANIA(51l,"Océanie septentrionale","51","51","Northern Oceania"),
	MIDDLE_OCEANIA(53l,"Océanie centrale","53","53","Middle Oceania"),
	WESTERN_OCEANIA(54l,"Océanie occidentale","54","54","Western Oceania"),
	SOUTHERN_OCEANIA(56l,"Océanie méridionale","56","56","Southern Oceania"),
	ANTARCTICA(61l,"Antarctique","61","61","Antarctica");

	private static Map<Long,Region> mapParId = new HashMap<Long,Region>();
	
	static {
		for(Region region : values()) {
			mapParId.put(region.getId(), region);
		}
	}
	
	public static Region getParId(Long id) {
		return mapParId.get(id);
	}

	private final Long id;
	private final String nomFR;
	private final String nomDE;
	private final String nomIT;
	private final String nomEN;
	
	private Region(Long id,String nomFR, String nomDE, String nomIT, String nomEN) {
		this.id = id;
		this.nomFR = nomFR;
		this.nomDE = nomDE;
		this.nomIT = nomIT;
		this.nomEN = nomEN;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	public String getNom(String langue) {
		String langueMinuscule = langue.toLowerCase();
		if ("fr".equals(langueMinuscule)) {
			return nomFR;
		} else if ("de".equals(langueMinuscule)) {
			return nomDE;
		} else if ("it".equals(langueMinuscule)) {
			return nomIT;
		} else {
			return nomEN;
		}
	}
	
}
