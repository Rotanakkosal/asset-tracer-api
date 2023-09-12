-------------------------------------------------------------------
-- Select and Search Role
create
or replace function get_all_roles_func(_page int, _size int, _name varchar(255))
    RETURNS setof role

as
$$
begin

    if
_name = '' or _name IS NULL then
        RETURN QUERY
SELECT *
FROM role
ORDER BY name
OFFSET _size * (_page-1) LIMIT _size;

else
        RETURN QUERY
SELECT *
FROM role
WHERE name ilike _name || '%'
ORDER BY name
OFFSET _size * (_page-1) LIMIT _size;
end if;

end
$$
language plpgsql;