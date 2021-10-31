INSERT INTO public.permissions (id,"name") VALUES(1,'create');
INSERT INTO public.permissions (id,"name") VALUES(2,'delete');
INSERT INTO public.permissions (id,"name") VALUES(3,'update');
INSERT INTO public.permissions (id,"name") VALUES(4,'get');

INSERT INTO public.users (user_name, "password", mail, "name") VALUES('jose1234', '827CCB0EEA8A706C4C34A16891F84E7B', 'jose@mail.com', 'jose');
INSERT INTO public.users (user_name, "password", mail, "name") VALUES('maria1234', '827CCB0EEA8A706C4C34A16891F84E7B', 'maria@mail.com', 'maria');
INSERT INTO public.users (user_name, "password", mail, "name") VALUES('pepe1234', '827CCB0EEA8A706C4C34A16891F84E7B', 'pepe@mail.com', 'pepe');

INSERT INTO public.user_permissions (user_name, permission_id) VALUES('jose1234', 1);
INSERT INTO public.user_permissions (user_name, permission_id) VALUES('jose1234', 2);
INSERT INTO public.user_permissions (user_name, permission_id) VALUES('jose1234', 4);

INSERT INTO public.user_permissions (user_name, permission_id) VALUES('maria1234', 2);
INSERT INTO public.user_permissions (user_name, permission_id) VALUES('maria1234', 3);

INSERT INTO public.user_permissions (user_name, permission_id) VALUES('pepe1234', 1);
INSERT INTO public.user_permissions (user_name, permission_id) VALUES('pepe1234', 3);