DROP PROCEDURE IF EXISTS generateSundayAndSaturday;
DELIMITER //
CREATE PROCEDURE generateSundayAndSaturday ()
BEGIN
    DECLARE _now DATE DEFAULT '0000-00-00'; 
    DECLARE _endYear DATE DEFAULT '0000-00-00'; 
	 SELECT now() into _now from dual;   
    SET _now = '2018-01-01'; 
    SET _endYear = '2018-12-31';
    while _now < _endYear DO
  if (select DAYOFWEEK(_now) = 7) THEN -- Saturday
    insert into kabani.holiday_details_master (date_of_holiday,desc_of_holiday,name_of_holiday,type_of_holiday) values(_now,'saturday holiday','saturday holiday','weekend');
    SET _now =  DATE_ADD(_now, INTERVAL 1 DAY);
  ELSEIF (select DAYOFWEEK(_now) = 1) THEN -- Sunday
        insert into kabani.holiday_details_master (date_of_holiday,desc_of_holiday,name_of_holiday,type_of_holiday) values(_now,'sunday holiday','sunday holiday','weekend');
    SET _now =  DATE_ADD(_now, INTERVAL 6 DAY);
  ELSE
    SET _now =  DATE_ADD(_now, INTERVAL 1 DAY);
  END IF;
END WHILE;
END //