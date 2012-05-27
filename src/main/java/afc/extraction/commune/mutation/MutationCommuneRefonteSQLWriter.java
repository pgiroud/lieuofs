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

package afc.extraction.commune.mutation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.ge.afc.ofs.ILieuOFS;
import ch.ge.afc.ofs.commune.ICommuneSuisse;
import ch.ge.afc.ofs.commune.IMutationCommune;
import ch.ge.afc.ofs.commune.TypeMutationCommune;

/**
 * @author <a href="mailto:patrick.giroud@etat.ge.ch">Patrick Giroud</a>
 *
 */
class MutationCommuneRefonteSQLWriter implements MutationCommuneWriter {

	private String nomScript;
	private final DateFormat fmtDtd = new SimpleDateFormat("yyyyMMdd");
	private final DateFormat fmtOracle = new SimpleDateFormat("dd.MM.yyyy");
	
	
	public void setNomScript(String nomScript) {
		this.nomScript = nomScript;
	}

	private void ecrireEvtModifCommune(StringBuilder builder, Date date, int noOFS, String description) {
		builder.append("INSERT INTO EVM_T_EVT_METIER e (EVM_N_ID,EVM_C_DOMAINE, EVM_N_TYPE_OBJ,EVM_N_TYPE,\n");
		builder.append("\tEVM_N_OBJET_ID, EVM_N_DATE, EVM_N_DATE_JOURNAL, EVM_N_HEURE_JOURNAL, EVM_C_UTILISATEUR,\n");
		builder.append("\tEVM_C_COMMENTAIRE)\n");
		builder.append("SELECT EVM_S_EVT_METIER.nextval,\n");
		builder.append("\t'AFC.LIEUFISCAL',\n");
		builder.append("\t4,\n");
		builder.append("\t2,\n");
		builder.append("\tLOC_N_ID,\n");
		builder.append("\t").append(fmtDtd.format(date)).append(",\n");
		builder.append("\tTO_NUMBER(TO_CHAR(SYSDATE, 'YYYYMMDD')),\n");
		builder.append("\tTO_NUMBER(TO_CHAR(SYSDATE, 'HH24MISS')),\n");
		builder.append("\t'").append(nomScript).append("',\n");
		builder.append("\t'").append(description).append("'\n");
		builder.append("FROM LFI_T_LIEU_FISCAL\n");
		builder.append("WHERE LOC_N_NO_OFS = ").append(noOFS).append("\n");
		builder.append("\tAND LOC_C_STATUT = 'A'\n");
		builder.append("/\n-- Conditions de validation : 1 ligne ajoutée\n\n");
	}
	
	private void ecrireEvtDesactivation(StringBuilder builder, Date date, int noOFS) {
		ecrireEvtModifCommune(builder,date,noOFS,"Désactivation");
	}
	
	
	private void ecrireDesactivationLieuFiscal(StringBuilder builder, Date date, int noOFS, String remarques) {
		builder.append("UPDATE LFI_T_LIEU_FISCAL\n");
		builder.append("SET LOC_C_STATUT = 'I',\n");
		builder.append("\tLOC_D_FIN_VALIDITE = to_date('").append(fmtOracle.format(date)).append("','dd.mm.yyyy'),\n");
		builder.append("\tLOC_C_REMARQUES = '").append(remarques.replaceAll("'", "''")).append("'\n");
		builder.append("WHERE LOC_N_NO_OFS = ").append(noOFS).append("\n");
		builder.append("/\n-- Conditions de validation : 1 ligne modifiée\n\n");
	}
	
	private void ecrireCreationFromDual(StringBuilder builder) {
		builder.append("FROM dual\n");
		builder.append("/\n");
		builder.append("-- Conditions de validation : 1 ligne ajoutée\n\n");
	}
	
	
	
	private void ecrireCommuneCree(StringBuilder builder, ICommuneSuisse commune) {
		builder.append("INSERT INTO LFI_T_COMMUNE(COM_N_ID,COM_CTN_N_ID)\n");
		builder.append("SELECT LFI_S_LIEU_FISCAL.currval,CTN_N_ID\n");
		builder.append("FROM LFI_T_CANTON\n");
		builder.append("WHERE CTN_C_ISO_2 = '").append(commune.getCanton().getCodeIso2()).append("'\n");
		builder.append("/\n");
		builder.append("-- Conditions de validation : 1 ligne ajoutée\n\n");
	}
	
	private void ecrireLieuFiscalCree(StringBuilder builder, ILieuOFS lieu, Date dateCreation, String remarques) {
		builder.append("INSERT INTO LFI_T_LIEU_FISCAL(LOC_N_ID,LOC_N_NO_OFS,LOC_C_NOM,LOC_N_TYPE,LOC_D_DEBUT_VALIDITE,LOC_C_REMARQUES)\n");
		builder.append("SELECT LFI_S_LIEU_FISCAL.nextval,").append(lieu.getNumeroOFS())
		.append(",'").append(lieu.getNom()).append("',3").append(",to_date('").append(fmtOracle.format(dateCreation)).append("','dd.mm.yyyy'),'")
		.append(remarques.replaceAll("'", "''"))
		.append("'\n");
		ecrireCreationFromDual(builder);
	}
	
	private void ecrireEvtCreationCommune(StringBuilder builder, Date date) {
		builder.append("INSERT INTO EVM_T_EVT_METIER e (EVM_N_ID,EVM_C_DOMAINE, EVM_N_TYPE_OBJ,EVM_N_TYPE,\n");
		builder.append("\tEVM_N_OBJET_ID, EVM_N_DATE, EVM_N_DATE_JOURNAL, EVM_N_HEURE_JOURNAL, EVM_C_UTILISATEUR,\n");
		builder.append("\tEVM_C_COMMENTAIRE)\n");
		builder.append("SELECT EVM_S_EVT_METIER.nextval,\n");
		builder.append("\t'AFC.LIEUFISCAL', \n");
		builder.append("\t4,\n");
		builder.append("\t1,\n");
		builder.append("\tLFI_S_LIEU_FISCAL.currval,\n");
		builder.append("\t").append(fmtDtd.format(date)).append(",\n");
		builder.append("\tTO_NUMBER(TO_CHAR(SYSDATE, 'YYYYMMDD')),\n");
		builder.append("\tTO_NUMBER(TO_CHAR(SYSDATE, 'HH24MISS')),\n");
		builder.append("\t'").append(nomScript).append("',\n");
		builder.append("\t'Création'\n");
		ecrireCreationFromDual(builder);
	}
	
	private void desactiverCommune(StringBuilder builder, Date date, ICommuneSuisse commune, IMutationCommune mutation) {
		ecrireEvtDesactivation(builder,date,commune.getNumeroOFS());
		ecrireDesactivationLieuFiscal(builder,date,commune.getNumeroOFS(),mutation.getDescription());
	}
	
	private String ecrireFusion(IMutationCommune mutation) {
		Calendar cal = Calendar.getInstance();
		StringBuilder builder = new StringBuilder();
		// Communes fusionnées
		cal.setTime(mutation.getDateEffet());
		cal.add(Calendar.DATE, -1);
		for (ICommuneSuisse commune : mutation.getCommunesOrigines()) {
			desactiverCommune(builder,cal.getTime(),commune,mutation);
		}
		// Commune née de la fusion
		cal.setTime(mutation.getDateEffet());
		ICommuneSuisse communeCree = mutation.getCommunesCibles().get(0);
		ecrireLieuFiscalCree(builder,communeCree,cal.getTime(),mutation.getDescription());
		ecrireCommuneCree(builder,communeCree);
		ecrireEvtCreationCommune(builder,cal.getTime());
		return builder.toString();
	}
	
	private void ecrireAbsorption(StringBuilder builder, Date date, int noOFS, List<ICommuneSuisse> communeAbsorbee) {
		StringBuilder description = new StringBuilder("Absorption ");
		boolean premier = true;
		for (ICommuneSuisse commune : communeAbsorbee) {
			if (!premier) description.append(", ");
			else {
				premier = false;
			}
			description.append(commune.getNom().replaceAll("'", "''")).append(" (").append(commune.getNumeroOFS()).append(")");
		}
		ecrireEvtModifCommune(builder,date,noOFS,description.toString());
	}
	
	private String ecrireInclusion(IMutationCommune mutation) {
		Calendar cal = Calendar.getInstance();
		StringBuilder builder = new StringBuilder();
		ICommuneSuisse communeAbsorbante = mutation.getCommunesCibles().get(0);
		// Communes incluses
		cal.setTime(mutation.getDateEffet());
		cal.add(Calendar.DATE, -1);
		for (ICommuneSuisse commune : mutation.getCommunesOrigines()) {
			// Attention, on est obligé de faire ce test car la notion de commune
			// à l'OFS est une notion avec historique !
			if (commune.getNumeroOFS() != communeAbsorbante.getNumeroOFS()) {
				desactiverCommune(builder,cal.getTime(),commune,mutation);
			}
		}
		// Commune absorbante
		cal.setTime(mutation.getDateEffet());
		ecrireAbsorption(builder,cal.getTime(),communeAbsorbante.getNumeroOFS(),mutation.getCommunesOrigines());
		return builder.toString();
	}
	
	private String getBandeau(String libelle) {
		StringBuilder builder = new StringBuilder();
		String demarque = "------------------------------------------------------------------------";
		builder.append(demarque).append("\n");
		builder.append("-- ");
		builder.append(libelle);
		builder.append("\n").append(demarque).append("\n\n");
		return builder.toString();
	}
	
	private String ecrireChgtNom(IMutationCommune mutation) {
		Calendar cal = Calendar.getInstance();
		StringBuilder builder = new StringBuilder();
		builder.append(getBandeau(mutation.getDescription()));
		cal.setTime(mutation.getDateEffet());
		ICommuneSuisse commune = mutation.getCommunesCibles().get(0);
		ecrireEvtModifCommune(builder,cal.getTime(),commune.getNumeroOFS(),mutation.getDescription());
		builder.append("UPDATE LFI_T_LIEU_FISCAL\n");
		builder.append("SET LOC_C_NOM = '").append(commune.getNom()).append("'\n");
		builder.append("WHERE LOC_N_NO_OFS = ").append(commune.getNumeroOFS()).append("\n");
		builder.append("/\n");
		builder.append("-- Conditions de validation : 1 ligne mise à jour\n\n");
		return builder.toString();
	}
	
	@Override
	public String ecrireMutation(IMutationCommune mutation) {
		if (TypeMutationCommune.FUSION.equals(mutation.getType())) return ecrireFusion(mutation);
		if (TypeMutationCommune.INCLUSION.equals(mutation.getType())) return ecrireInclusion(mutation);
		if (TypeMutationCommune.CHANGEMENT_NOM.equals(mutation.getType())) return ecrireChgtNom(mutation);
		if (TypeMutationCommune.CHANGEMENT_DISTRICT.equals(mutation.getType())) return null;
		throw new UnsupportedOperationException("Le type de mutation " + mutation.getType() + " n'est pas supporté !!");
	}

}
