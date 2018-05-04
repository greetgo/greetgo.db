
create or replace procedure __SET_CHANGER__(id__ number) is
begin
  DBMS_SESSION.set_context( 'clientcontext', '__GG_CHANGER_ID__', to_char(id__) );
end ;

;;

create or replace function __GET_CHANGER__ return number is
begin
  return to_number (SYS_CONTEXT ('clientcontext', '__GG_CHANGER_ID__'));
exception when VALUE_ERROR then return null ;
end ;
