-- do NOT run as postgres, DO run as normal user using familyphotos_admin role ONLY
-- as in: $ psql -f familyphotos.sql familyphotos familyphotos_admin --password

drop table pictures;
drop table users cascade;
drop table user_roles;
drop table groups;
drop table tokens;

CREATE TABLE pictures (
    id integer NOT NULL,
    description character varying(320),
    summary character varying(60),
    date character varying(14),
    event character varying(40),
    place character varying(30),
    owner character varying(30),
    viewgroup character varying(80) DEFAULT 'private'::character varying,
    md5 character(32),
    mediumblob bytea,
    smallblob bytea
);

ALTER TABLE ONLY pictures
    ADD CONSTRAINT pictures_pkey PRIMARY KEY (id);

CREATE SEQUENCE pictures_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 32000
    CACHE 1;

ALTER SEQUENCE pictures_id_seq OWNED BY pictures.id;

ALTER TABLE ONLY pictures ALTER COLUMN id SET DEFAULT nextval('pictures_id_seq'::regclass);

ALTER TABLE ONLY pictures
    ADD CONSTRAINT pictures_md5_key UNIQUE (md5);

CREATE TABLE users (
    user_name character varying(30) NOT NULL,
    user_pass character varying(64) NOT NULL,
    email character varying(64)
);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_name);

ALTER TABLE ONLY users
    ADD CONSTRAINT users_email_key UNIQUE (email);

CREATE TABLE user_roles (
    user_name character varying(30) NOT NULL,
    role_name character varying(15) NOT NULL
);

ALTER TABLE ONLY user_roles
    ADD CONSTRAINT user_roles_user_name_fkey FOREIGN KEY (user_name) REFERENCES users(user_name) ON UPDATE CASCADE ON DELETE CASCADE;


CREATE TABLE groups (
    name character varying(15) NOT NULL,
    owner character varying(30) NOT NULL,
    member character varying(30) NOT NULL
);

CREATE TABLE tokens (
    user_name character varying(30) NOT NULL,
    timestamp timestamp NOT NULL,
    enc_token character varying(64) NOT NULL
);

ALTER TABLE ONLY tokens
    ADD CONSTRAINT tokens_user_name_fkey FOREIGN KEY (user_name) REFERENCES users(user_name) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE ONLY tokens
    ADD CONSTRAINT tokens_user_name_key UNIQUE (user_name);

-- user_pass is encrypted using org.apache.commons.codec.digest.DigestUtils.sha256Hex(password)
-- plain text password for admin here is 'demo' 
insert into users (user_name,user_pass) values ('admin','2a97516c354b68848cdbd8f54a226a0a55b21ed138e207ad6c5cbb9c00aa5aea');
insert into user_roles (user_name,role_name) values ('admin','admin');


