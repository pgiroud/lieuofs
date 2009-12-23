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

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

public class MutationCommuneCritere {
	private TypeMutationCommune type;
	private String codeCanton;
	private Date dateDebut;
	private Date dateFin;
	private Set<TypeMutationCommune> typeAExclure = EnumSet.of(TypeMutationCommune.RENUMEROTATION_FORMELLE);
	
	/**
	 * @return the type
	 */
	public TypeMutationCommune getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(TypeMutationCommune type) {
		this.type = type;
	}
	/**
	 * @return the codeCanton
	 */
	public String getCodeCanton() {
		return codeCanton;
	}
	/**
	 * @param codeCanton the codeCanton to set
	 */
	public void setCodeCanton(String codeCanton) {
		this.codeCanton = codeCanton;
	}
	/**
	 * @return the dateDebut
	 */
	public Date getDateDebut() {
		return dateDebut;
	}
	/**
	 * @param dateDebut the dateDebut to set
	 */
	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}
	
	/**
	 * @return the dateFin
	 */
	public Date getDateFin() {
		return dateFin;
	}
	/**
	 * @param dateFin the dateFin to set
	 */
	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}
	
	/**
	 * @return the typeAExclure
	 */
	public Set<TypeMutationCommune> getTypeAExclure() {
		return typeAExclure;
	}
	
	/**
	 * @param typeAExclure the typeAExclure to set
	 */
	public void setTypeAExclure(TypeMutationCommune permierTypeAExclure, TypeMutationCommune... typeAExclureSuivant) {
		this.typeAExclure = EnumSet.of(permierTypeAExclure, typeAExclureSuivant);
	}
	
	
}
