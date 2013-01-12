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
package org.lieuofs.district.biz;

import org.lieuofs.district.IDistrict;
import org.lieuofs.district.IMutationDistrict;
import org.lieuofs.district.TypeMutationDistrict;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MutationDistrict implements IMutationDistrict {

    private TypeMutationDistrict type;
    private int numero;

    private List<IDistrict> origines = new ArrayList<IDistrict>();
    private List<IDistrict> cibles = new ArrayList<IDistrict>();

    private Date dateEffet;

    public MutationDistrict(TypeMutationDistrict type) {
        this.type = type;
    }

    @Override
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public TypeMutationDistrict getType() {
        return type;
    }

    @Override
    public List<IDistrict> getOrigines() {
        return origines;
    }

    public void ajouterDistrictOrigine(District district) {
        origines.add(district);
    }


    @Override
    public List<IDistrict> getCibles() {
        return cibles;
    }


    public void ajouterDistrictCible(District district) {
        cibles.add(district);
    }

    @Override
    public String getDescription() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Date getDateEffet() {
        return new Date(dateEffet.getTime());
    }

    public void setDateEffet(Date dateEffet) {
        this.dateEffet = dateEffet;
    }
}
