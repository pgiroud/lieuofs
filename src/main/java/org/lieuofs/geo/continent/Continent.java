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

package org.lieuofs.geo.continent;

import java.util.HashMap;
import java.util.Map;

/**
 * Représente un continent tel que défini par l'OFS.
 * 
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
public enum Continent {
	
	EUROPE(1l,"Europe","Europa","Europa","Europe"),
	AFRICA(2l,"Afrique","Afrika","Africa","Africa"),
	AMERICA(3l,"Amérique","Amerika","America","America"),
	ASIA(4l,"Asie","Asien","Asia","Asia"),
	OCEANIA(5l,"Océanie","Ozeanien","Oceania","Oceania"),
	ANTARCTICA(6l,"Antarctique","Antarktis","Antartide","Antarctica");

	private static Map<Long,Continent> mapParId = new HashMap<Long,Continent>();
	
	static {
		for(Continent continent : values()) {
			mapParId.put(continent.getId(), continent);
		}
	}
	
	public static Continent getParId(Long id) {
		return mapParId.get(id);
	}

	private final Long id;
	private final String nomFR;
	private final String nomDE;
	private final String nomIT;
	private final String nomEN;
	
	private Continent(Long id,String nomFR, String nomDE, String nomIT, String nomEN) {
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
