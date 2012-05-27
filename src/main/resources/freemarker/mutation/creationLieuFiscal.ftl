INSERT INTO LFI_T_LIEU_FISCAL(LOC_N_ID,LOC_N_NO_OFS,LOC_C_NOM,LOC_N_TYPE,LOC_D_DEBUT_VALIDITE,LOC_C_REMARQUES)
SELECT LFI_S_LIEU_FISCAL.nextval,${noofs},'${nom}',3,to_date('${date}','dd.mm.yyyy'),'${description}'
FROM dual
/
-- Conditions de validation : 1 ligne ajoutée

