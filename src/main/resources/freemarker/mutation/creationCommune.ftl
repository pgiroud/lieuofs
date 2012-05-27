INSERT INTO LFI_T_COMMUNE(COM_N_ID,COM_CTN_N_ID)
SELECT LFI_S_LIEU_FISCAL.currval,CTN_N_ID
FROM LFI_T_CANTON
WHERE CTN_C_ISO_2 = '${codeiso2canton}'
/
-- Conditions de validation : 1 ligne ajoutée

