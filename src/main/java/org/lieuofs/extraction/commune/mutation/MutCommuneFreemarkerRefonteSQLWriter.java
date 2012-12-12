package org.lieuofs.extraction.commune.mutation;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.lieuofs.ILieuOFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import org.lieuofs.commune.ICommuneSuisse;
import org.lieuofs.commune.IMutationCommune;
import org.lieuofs.commune.TypeMutationCommune;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class MutCommuneFreemarkerRefonteSQLWriter implements
		MutationCommuneWriter {

	final Logger logger = LoggerFactory.getLogger(MutCommuneFreemarkerRefonteSQLWriter.class);

	@Resource
	private Configuration configFreemarker;
	
	private String nomScript;
	private final DateFormat fmtDtd = new SimpleDateFormat("yyyyMMdd");
	private final DateFormat fmtOracle = new SimpleDateFormat("dd.MM.yyyy");

	
	public void setConfigFreemarker(Configuration configFreemarker) {
		this.configFreemarker = configFreemarker;
	}

	public MutCommuneFreemarkerRefonteSQLWriter() {
		super();
	}

	
	
	public void setNomScript(String nomScript) {
		this.nomScript = nomScript;
	}

	private Map<Object,Object> creerModele() {
		Map<Object,Object> modele = new HashMap<Object,Object>();
		modele.put("nomScript", nomScript);
		return modele;
	}
	
	private void ecrireEvtModifCommune(StringBuilder builder, Date date, int noOFS, String description) throws IOException, TemplateException {
			Map<Object,Object> modele = creerModele();
			modele.put("description", description.replaceAll("'", "''"));
			modele.put("date",fmtDtd.format(date));
			modele.put("noofs",String.valueOf(noOFS));
			builder.append(FreeMarkerTemplateUtils.processTemplateIntoString(configFreemarker.getTemplate("mutation/evtModification.ftl"),modele));
	}
	
	private void ecrireEvtCreationCommune(StringBuilder builder, Date date) throws IOException, TemplateException {
		Map<Object,Object> modele = creerModele();
		modele.put("description", "Création");
		modele.put("date",fmtDtd.format(date));
		builder.append(FreeMarkerTemplateUtils.processTemplateIntoString(configFreemarker.getTemplate("mutation/evtCreation.ftl"),modele));
	}
	
	private void ecrireDesactivationLieuFiscal(StringBuilder builder, Date date, int noOFS, String remarques) throws IOException, TemplateException {
			Map<Object,Object> modele = creerModele();
			modele.put("description", remarques.replaceAll("'", "''"));
			modele.put("date",fmtOracle.format(date));
			modele.put("noofs",String.valueOf(noOFS));
			builder.append(FreeMarkerTemplateUtils.processTemplateIntoString(configFreemarker.getTemplate("mutation/desactivationLieuFiscal.ftl"),modele));
	}
	
	private void ecrireEvtDesactivation(StringBuilder builder, Date date, int noOFS) throws IOException, TemplateException {
		ecrireEvtModifCommune(builder,date,noOFS,"Désactivation");
	}
	
	private void desactiverCommune(StringBuilder builder, Date date, ICommuneSuisse commune, IMutationCommune mutation) throws IOException, TemplateException {
		ecrireEvtDesactivation(builder,date,commune.getNumeroOFS());
		ecrireDesactivationLieuFiscal(builder,date,commune.getNumeroOFS(),mutation.getDescription());
	}
		
	private void ecrireLieuFiscalCree(StringBuilder builder, ILieuOFS lieu, Date dateCreation, String remarques) throws IOException, TemplateException {
		Map<Object,Object> modele = creerModele();
		modele.put("description", remarques.replaceAll("'", "''"));
		modele.put("date",fmtOracle.format(dateCreation));
		modele.put("noofs",String.valueOf(lieu.getNumeroOFS()));
		modele.put("nom", lieu.getNom().replaceAll("'", "''"));
		builder.append(FreeMarkerTemplateUtils.processTemplateIntoString(configFreemarker.getTemplate("mutation/creationLieuFiscal.ftl"),modele));
	}
	
	private void ecrireCommuneCree(StringBuilder builder, ICommuneSuisse commune) throws IOException, TemplateException {
		Map<Object,Object> modele = creerModele();
		modele.put("codeiso2canton", commune.getCanton().getCodeIso2());
		builder.append(FreeMarkerTemplateUtils.processTemplateIntoString(configFreemarker.getTemplate("mutation/creationCommune.ftl"),modele));
	}
	
	private String ecrireFusion(IMutationCommune mutation) throws IOException, TemplateException {
		Calendar cal = Calendar.getInstance();
		StringBuilder builder = new StringBuilder();
		builder.append(getBandeau(mutation.getDescription()));
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

	private void ecrireAbsorption(StringBuilder builder, Date date, int noOFS, List<ICommuneSuisse> communeAbsorbee) throws IOException, TemplateException {
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
	
	private String ecrireInclusion(IMutationCommune mutation) throws IOException, TemplateException {
		Calendar cal = Calendar.getInstance();
		StringBuilder builder = new StringBuilder();
		builder.append(getBandeau(mutation.getDescription()));
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
	
	
	private String ecrireChgtNom(IMutationCommune mutation) throws IOException, TemplateException {
		Calendar cal = Calendar.getInstance();
		StringBuilder builder = new StringBuilder();
		builder.append(getBandeau(mutation.getDescription()));
		cal.setTime(mutation.getDateEffet());
		ICommuneSuisse commune = mutation.getCommunesCibles().get(0);
		ecrireEvtModifCommune(builder,cal.getTime(),commune.getNumeroOFS(),mutation.getDescription());
		
		Map<Object,Object> modele = creerModele();
		modele.put("noofs",String.valueOf(commune.getNumeroOFS()));
		modele.put("nouveaunom", commune.getNom().replaceAll("'", "''"));
		builder.append(FreeMarkerTemplateUtils.processTemplateIntoString(configFreemarker.getTemplate("mutation/chgtNomLieuFiscal.ftl"),modele));
		return builder.toString();
	}
	
	
	@Override
	public String ecrireMutation(IMutationCommune mutation) {
		try {
		if (TypeMutationCommune.FUSION.equals(mutation.getType())) return ecrireFusion(mutation);
		if (TypeMutationCommune.INCLUSION.equals(mutation.getType())) return ecrireInclusion(mutation);
		if (TypeMutationCommune.CHANGEMENT_NOM.equals(mutation.getType())) return ecrireChgtNom(mutation);
		if (TypeMutationCommune.CHANGEMENT_DISTRICT.equals(mutation.getType())) return null;
        if (TypeMutationCommune.ECHANGE_TERRITOIRE.equals(mutation.getType())) return null;
		throw new UnsupportedOperationException("Le type de mutation " + mutation.getType() + " n'est pas supporté !!");
		} catch (IOException ioex) {
			logger.error("Exception mutation commune", ioex);
		} catch (TemplateException texc) {
			logger.error("Exception mutation commune", texc);
		}
		return null;
	}

	
}
