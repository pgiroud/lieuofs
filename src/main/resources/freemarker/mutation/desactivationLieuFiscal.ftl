UPDATE LFI_T_LIEU_FISCAL
SET LOC_C_STATUT = 'I',
	LOC_D_FIN_VALIDITE = to_date('${date}','dd.mm.yyyy'),
	LOC_C_REMARQUES = '${description}'
WHERE LOC_N_NO_OFS = ${noofs}
/
-- Conditions de validation : 1 ligne modifiée

