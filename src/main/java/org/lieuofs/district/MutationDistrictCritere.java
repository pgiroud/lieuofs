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


import java.util.Date;

public class MutationDistrictCritere {
    private TypeMutationDistrict type;
    private String codeCanton;
    private Date dateDebut;
    private Date dateFin;

    public TypeMutationDistrict getType() {
        return type;
    }

    public void setType(TypeMutationDistrict type) {
        this.type = type;
    }

    public String getCodeCanton() {
        return codeCanton;
    }

    public void setCodeCanton(String codeCanton) {
        this.codeCanton = codeCanton;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
}
