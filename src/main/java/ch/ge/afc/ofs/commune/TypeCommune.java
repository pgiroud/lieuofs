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
package ch.ge.afc.ofs.commune;

import java.util.HashMap;
import java.util.Map;

import ch.ge.afc.ofs.Identifiable;

public enum TypeCommune implements Identifiable {
	
	COMMUNE_POLITIQUE(11l,"Commune politique","Commune"),
	TERRITOIRE_HORS_COMM(12l,"Territoire non attribué à une commune","Territoire hors COM"),
	PARTIE_LAC(13l,"Partie cantonale de lac","Partie cant. de lac");
	
	private static Map<Long,TypeCommune> mapParId = new HashMap<Long,TypeCommune>();
	
	static {
		for(TypeCommune typeCommune : values()) {
			mapParId.put(typeCommune.getId(), typeCommune);
		}
	}
	
	public static TypeCommune getParId(Long id) {
		return mapParId.get(id);
	}

	private final Long id;
	private final String nom;
	private final String nomCourt;
	
	private TypeCommune(Long id, String nom, String nomCourt) {
		this.id = id;
		this.nom = nom;
		this.nomCourt = nomCourt;
	}

	/* (non-Javadoc)
	 * @see ch.ge.afc.ofs.Identifiable#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @return the nomCourt
	 */
	public String getNomCourt() {
		return nomCourt;
	}

	
	
}
