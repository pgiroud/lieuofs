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
package ch.ge.afc.ofs;

import java.util.HashMap;
import java.util.Map;

public enum TypeMutation implements Identifiable {
	
	PREMIERE_SAISIE(20l,"Première saisie commune / district","1ère saisie COM/DIST"),
	CREATION(21l,"Création commune/district","Création COM/DIST"),
	CHGT_NOM_DIST(22l,"Changement de nom du district","Changement de nom DIST"),
	CHGT_NOM_COMM(23l,"Changement de nom de la commune","Changement de nom COM"),
	RATT(24l,"Rattachement à un autre district/canton","Nouveau DIST/CT"),
	MODIF_TERR(26l,"Modification du territoire de la commune","Modification du territoire COM"),
	RENUMEROTATION_FORMELLE(27l,"Renumérotation formelle de la commune / du district","Renumérotation COM/DIST"),
	RADIATION(29l,"Radiation commune/district","Radiation COM/DIST"),
	ANNULATION(30l,"Annulation de la mutation","Annulation de la mutation");
	
	
	private static Map<Long,TypeMutation> mapParId = new HashMap<Long,TypeMutation>();
	
	static {
		for(TypeMutation typeMutation : values()) {
			mapParId.put(typeMutation.getId(), typeMutation);
		}
	}
	
	public static TypeMutation getParId(Long id) {
		return mapParId.get(id);
	}

	private final Long id;
	private final String nom;
	private final String nomCourt;
	
	private TypeMutation(Long id, String nom, String nomCourt) {
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
