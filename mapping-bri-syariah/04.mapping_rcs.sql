-- RC internal MX  diteruskan ke endpoint channel
insert into rc_mappings (source_rc_id,target_rc_id)
values 

((select id from endpoint_rcs where endpoint_id is null  and rc='00'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='00')),

((select id from endpoint_rcs where endpoint_id is null  and rc='01'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='01')),

((select id from endpoint_rcs where endpoint_id is null  and rc='ZZ'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='89')),

((select id from endpoint_rcs where endpoint_id is null  and rc='SU'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='91'))
/

-- bils0 BHP FREKUENSI
insert into rc_mappings (source_rc_id,target_rc_id)
values 
((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='bils0') and rc='08'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='89')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='bils0') and rc='09'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='87')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='bils0') and rc='10'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='bils0') and rc='11'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='89')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='bils0') and rc='25'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='bils0') and rc='26'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='bils0') and rc='50'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='01')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='bils0') and rc='66'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='01')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='bils0') and rc='97'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B8')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='bils0') and rc='13'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='01'))
/


-- pap 
insert into rc_mappings (source_rc_id,target_rc_id)
values 
((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='pap') and rc='25'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='pap') and rc='13'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='pap') and rc='50'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='pap') and rc='26'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='pap') and rc='97'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B8'))
/

-- per01
insert into rc_mappings (source_rc_id,target_rc_id)
values 
((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='per01') and rc='25'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='per01') and rc='13'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='per01') and rc='50'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='per01') and rc='26'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='per01') and rc='97'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B8'))
/

-- skor01
insert into rc_mappings (source_rc_id,target_rc_id)
values 
((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='skor01') and rc='25'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='skor01') and rc='13'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='skor01') and rc='50'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='skor01') and rc='26'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='skor01') and rc='97'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B8'))
/

-- unar
insert into rc_mappings (source_rc_id,target_rc_id)
values 
((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='unar') and rc='25'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='unar') and rc='13'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='unar') and rc='50'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='unar') and rc='26'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='unar') and rc='97'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B8'))
/

-- iar
insert into rc_mappings (source_rc_id,target_rc_id)
values 
((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='iar') and rc='25'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='iar') and rc='13'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='iar') and rc='50'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='iar') and rc='26'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='iar') and rc='97'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B8'))
/

-- ikrap
insert into rc_mappings (source_rc_id,target_rc_id)
values 
((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='ikrap') and rc='25'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='ikrap') and rc='13'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='ikrap') and rc='50'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='ikrap') and rc='26'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='ikrap') and rc='97'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B8'))
/

-- reor01
insert into rc_mappings (source_rc_id,target_rc_id)
values 
((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='reor01') and rc='25'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='reor01') and rc='13'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='reor01') and rc='50'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='reor01') and rc='26'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B5')),

((select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='reor01') and rc='97'),
(select id from endpoint_rcs where endpoint_id=(select id from endpoints where code='chws4') and rc='B8'))
/




	