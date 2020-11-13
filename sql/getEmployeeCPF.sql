select c.BDCPFCOL as CPF from VRH_EMP_TCOLCON c
where
c.BDCODEMP = :enterprise and c.BDCODCOL = :employee