UPDATE ConfigValue Set VALUE = VALUE || ',authorizationHost'
where KEYID = -742267445322295959
ANd NODEID = 0;

-- Configuration SQL-Script created by OCM
-- Owner    : SBT
-- Hierarchy: DATACENTER

INSERT INTO Configuration(SECTIONID, NODEID, SECTIONNAME, OWNER, DESCRIPTION, PARENTID, ACCESSMODE)
  VALUES(169367029131519202, (SELECT nodeId FROM DataCenter WHERE defDC=1), 
         'FeeCalculationTimeout', 'SBT', 
         'FeeCalculationTimeout', null, null);

INSERT INTO ConfigParam(KEYID, ACCESSMODE, SECTIONID, KEYNAME, ACCESSTYPE)
  VALUES(7662191457729262718, 6, 169367029131519202, 'feeCalculationTimeout', null);
INSERT INTO ConfigValue(KEYID, NODEID, VALUE, DESCRIPTION)
  VALUES(7662191457729262718, 0, 
         '1', 'Time for Fee Reset');