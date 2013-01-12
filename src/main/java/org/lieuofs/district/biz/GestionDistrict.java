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

import java.util.ArrayList;
import java.util.List;

import org.lieuofs.Identifiable;
import org.lieuofs.Mutable;
import org.lieuofs.TypeMutation;
import org.lieuofs.canton.CantonCritere;
import org.lieuofs.canton.ICanton;
import org.lieuofs.canton.biz.IGestionCanton;
import org.lieuofs.commune.IMutationCommune;
import org.lieuofs.commune.TypeMutationCommune;
import org.lieuofs.commune.biz.MutationCommune;
import org.lieuofs.commune.biz.dao.PersistCommune;
import org.lieuofs.district.*;
import org.lieuofs.district.biz.dao.DistrictOFSDao;
import org.lieuofs.district.biz.dao.PersistDistrict;

public class GestionDistrict implements IGestionDistrict {

	private DistrictOFSDao dao;
	private IGestionCanton gestionnaireCanton;
	
	/**
	 * @param dao the dao to set
	 */
	public void setDao(DistrictOFSDao dao) {
		this.dao = dao;
	}

	/**
	 * @param gestionnaireCanton the gestionnaireCanton to set
	 */
	public void setGestionnaireCanton(IGestionCanton gestionnaireCanton) {
		this.gestionnaireCanton = gestionnaireCanton;
	}

	@Override
	public IDistrict lire(Long id) {
		final PersistDistrict districtPersistant = dao.lire(id);
		District district = new District(districtPersistant);
		district.setCanton(gestionnaireCanton.lire(new Identifiable() {
			@Override
			public Long getId() {
				return districtPersistant.getIdCanton();
			}
		}));
		return district;
	}

	@Override
	public List<IDistrict> rechercher(DistrictCritere critere) {
		return null;
	}

    @Override
    public IMutationDistrict lireMutation(int numero) {
        List<PersistDistrict> districts = dao.getMutation(numero);
        List<PersistDistrict> districtsInscrits = this.getDistrictsInscrits(districts,numero);
        List<PersistDistrict> districtsRadies = this.getDistrictsRadies(districts,numero);
        return creerMutation(numero,districtsInscrits,districtsRadies);
    }

    private List<PersistDistrict> getDistrictsInscrits(List<PersistDistrict> districts, int numMutation) {
        List<PersistDistrict> districtsFiltres = new ArrayList<PersistDistrict>();
        for (PersistDistrict district : districts) {
            Mutable radiation = district.getRadiation();
            if (null != radiation && numMutation == radiation.getNumero()) districtsFiltres.add(district);
        }
        return districtsFiltres;
    }

    private List<PersistDistrict> getDistrictsRadies(List<PersistDistrict> districts, int numMutation) {
        List<PersistDistrict> districtsFiltres = new ArrayList<PersistDistrict>();
        for (PersistDistrict district : districts) {
            Mutable inscription = district.getInscription();
            if (numMutation == inscription.getNumero()) districtsFiltres.add(district);
        }
        return districtsFiltres;
    }



    protected IMutationDistrict creerMutation(int numero, List<PersistDistrict> districtsSource, List<PersistDistrict> districtsCible) {
        TypeMutationDistrict type = this.obtenirTypeMutation(districtsSource, districtsCible);
        MutationDistrict mutation = new MutationDistrict(type);
        mutation.setNumero(numero);
        for (PersistDistrict persistant : districtsSource) {
            mutation.ajouterDistrictOrigine(this.creerDistrict(persistant));
        }
        for (PersistDistrict persistant : districtsCible) {
            mutation.ajouterDistrictCible(this.creerDistrict(persistant));
        }
        mutation.setDateEffet(districtsCible.get(0).getInscription().getDate());
        return mutation;
    }

    protected TypeMutationDistrict obtenirTypeMutation(List<PersistDistrict> districtsSource, List<PersistDistrict> districtsCible) {
        return  obtenirTypeMutation(districtsSource.get(0).getRadiation().getMode(),districtsCible.get(0).getInscription().getMode());
    }

    private TypeMutationDistrict obtenirTypeMutation(TypeMutation typeRadiation, TypeMutation typeInscription) {

        if (TypeMutation.RADIATION.equals(typeRadiation) && TypeMutation.CREATION.equals(typeInscription)) {
            return TypeMutationDistrict.Redefinition_de_districts_dans_le_canton;
        } else if (TypeMutation.RATT.equals(typeRadiation) && TypeMutation.RATT.equals(typeInscription)) {
            return TypeMutationDistrict.Changement_d_appartenance_cantonale_du_district;
        } else if (TypeMutation.CHGT_NOM_DIST.equals(typeRadiation) && TypeMutation.CHGT_NOM_DIST.equals(typeInscription)) {
            return TypeMutationDistrict.Changement_de_nom_du_district;
        } else if (TypeMutation.RENUMEROTATION_FORMELLE.equals(typeRadiation) && TypeMutation.RENUMEROTATION_FORMELLE.equals(typeInscription)) {
            return TypeMutationDistrict.Renumérotation_de_districts;
        } else {
            throw new IllegalArgumentException("Type mutation : radiation = " + typeRadiation + ", inscription = " + typeInscription + " INCONNUE");
        }
    }


    private District creerDistrict(final PersistDistrict persistant) {
        if (null == persistant) return null;
        District district = new District(persistant);
        district.setCanton(gestionnaireCanton.lire(new Identifiable() {
            @Override
            public Long getId() {
                return persistant.getIdCanton();
            }
        }));
        return district;
    }


    @Override
    public List<IMutationDistrict> rechercherMutation(MutationDistrictCritere critere) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
