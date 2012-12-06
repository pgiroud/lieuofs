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
package org.lieuofs.district;

import java.util.HashMap;
import java.util.Map;

import org.lieuofs.Identifiable;

public enum TypeDistrict implements Identifiable {
	
	DISTRICT(15l,"District","District"),
	CANTON(16l,"Canton sans districts","Canton"),
	TERRITOIRE(17l,"Territoire non attribué à un district","Territoire hors DIST");

	private static Map<Long,TypeDistrict> mapParId = new HashMap<Long,TypeDistrict>();
	
	static {
		for (TypeDistrict type : values()) {
			mapParId.put(type.getId(), type);
		}
	}
	
	public static TypeDistrict getParId(Long id) {
		return mapParId.get(id);
	}

	private final Long id;
	private final String nom;
	private final String nomCourt;
	
	private TypeDistrict(Long id, String nom, String nomCourt) {
		this.id = id;
		this.nom = nom;
		this.nomCourt = nomCourt;
	}

	/* (non-Javadoc)
	 * @see org.lieuofs.Identifiable#getId()
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
