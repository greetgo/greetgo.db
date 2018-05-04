
create or replace procedure __SET_CHANGER__(id__ varchar2) is
begin
  DBMS_SESSION.set_context( 'clientcontext', '__GG_CHANGER_ID__', id__ );
end ;

;;

create or replace function __GET_CHANGER__ return varchar2 is
begin
  return SYS_CONTEXT ('clientcontext', '__GG_CHANGER_ID__');
end ;
