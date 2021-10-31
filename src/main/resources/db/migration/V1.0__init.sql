CREATE TABLE public.users (
	user_name varchar(255) NOT NULL primary key,
	password varchar(255) NOT NULL,
	mail varchar(255) not null UNIQUE,
	name varchar(255) not null
);

CREATE TABLE public.permissions (
    id int8 NOT NULL primary key,
	name varchar(255) NOT NULL UNIQUE
);

CREATE TABLE public.user_permissions (
    id bigserial NOT NULL primary key,
	"user_name" varchar(255) NOT NULL,
	"permission_id" int8 NOT NULL,
	CONSTRAINT user_permission_user FOREIGN KEY (user_name) REFERENCES users(user_name),
	CONSTRAINT user_permission_permission FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

CREATE TABLE public.user_transactions (
    id bigserial NOT NULL primary key,
	"user_name" varchar(255) NOT NULL,
    params jsonb null,
    restaurants jsonb null,
    created_at timestamp NULL DEFAULT now(),
	CONSTRAINT user_transaction_user FOREIGN KEY (user_name) REFERENCES users(user_name)
);
