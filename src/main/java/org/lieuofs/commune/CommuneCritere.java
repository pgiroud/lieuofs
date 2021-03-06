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
package org.lieuofs.commune;

import java.util.Date;
import java.util.EnumSet;

import org.lieuofs.Identifiable;

public class CommuneCritere {
	
	private String codeCanton;
	private Identifiable district;
	
	private Date dateValiditeAvant;
    private Date dateValiditeApres;
	private EnumSet<TypeCommune> type = EnumSet.of(TypeCommune.COMMUNE_POLITIQUE);
	
	
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
	 * @return the district
	 */
	public Identifiable getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(Identifiable district) {
		this.district = district;
	}

	public void setDateValidite(Date date) {
        dateValiditeAvant = date;
        dateValiditeApres = date;
	}

    public void setDateValiditeAvant(Date dateValiditeAvant) {
        this.dateValiditeAvant = dateValiditeAvant;
    }

    public void setDateValiditeApres(Date dateValiditeApres) {
        this.dateValiditeApres = dateValiditeApres;
    }

    /**
	 * @return the dateValidite
	 */
	public Date getDateValiditeAvant() {
		return dateValiditeAvant;
	}

    /**
     * @return the dateValidite
     */
    public Date getDateValiditeApres() {
        return dateValiditeApres;
    }

    public void setType(TypeCommune premier, TypeCommune... reste) {
		type = EnumSet.of(premier,reste);
	}

	/**
	 * @return the type
	 */
	public EnumSet<TypeCommune> getType() {
		return type;
	}

	
}
