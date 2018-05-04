
create or replace function __SET_CHANGER__(id__ int8) returns void language plpgsql as
$quote$

declare
begin
  insert into __greetgo_temp_parameters__ (name, value) values ('CHANGER_ID', id__) ;
exception
  when unique_violation then
    update __greetgo_temp_parameters__ set value = id__ where name = 'CHANGER_ID' ;
  when undefined_table then
    execute 'create temporary table __greetgo_temp_parameters__ (name varchar(100) primary key, value int8)' ;
    insert into __greetgo_temp_parameters__ (name, value) values ('CHANGER_ID', id__) ;
end

$quote$

;;

create or replace function __GET_CHANGER__() returns int8 language plpgsql as
$quote$
declare
  ret int8 ;
begin
  select value into ret from __greetgo_temp_parameters__ where name = 'CHANGER_ID' ;
  return ret ;
exception when undefined_table then
  return null ;
end
$quote$
