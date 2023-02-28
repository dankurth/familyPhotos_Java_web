-- do NOT run as postgres, DO run as normal user using familyphotos_admin role ONLY
-- as in: $ psql -f familyphotos.sql familyphotos familyphotos_admin --password

-- drop table pictures;

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

CREATE TABLE user_roles (
    user_name character varying(30) NOT NULL,
    role_name character varying(15) NOT NULL
);

CREATE TABLE users (
    user_name character varying(30) NOT NULL,
    user_pass character varying(64) NOT NULL
);

CREATE TABLE groups (
    name character varying(15) NOT NULL,
    owner character varying(30) NOT NULL,
    member character varying(30) NOT NULL
);

-- user_pass is encrypted using org.apache.commons.codec.digest.DigestUtils.sha256Hex(password)
-- plain text password to use to login for user 'demo' is also 'demo'
insert into users (user_name,user_pass) values ('demo','2a97516c354b68848cdbd8f54a226a0a55b21ed138e207ad6c5cbb9c00aa5aea');
insert into user_roles (user_name, role_name) values ('demo','user');
