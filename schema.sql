CREATE TYPE action_type AS ENUM ('r' , 'w' , 'd');


CREATE TABLE IF NOT EXISTS user_data (

    userId         serial PRIMARY KEY,
    login           varchar(255) UNIQUE NOT NULL,
    password        varchar(255)    NOT NULL,
    enabled         boolean         NOT NULL,
    firstName       varchar(255)    NOT NULL,
    lastName        varchar(255)    NOT NULL,
    email           varchar(255)    NOT NULL,
    created         timestamp       NOT NULL,
    lastLogin       timestamp       NOT NULL
);



CREATE TABLE IF NOT EXISTS role
(
    roleId           serial              PRIMARY KEY,
    name             varchar(255) UNIQUE NOT NULL,
    displayName      varchar(255)        NOT NULL,
    description      varchar(255)        NOT NULL
);

CREATE TABLE IF NOT EXISTS permission
(
    permissionId     serial               PRIMARY KEY,
    name             varchar(255) UNIQUE  NOT NULL,
    description      varchar(255)
);

CREATE TABLE IF NOT EXISTS user_role
(
    userRoleId      serial  PRIMARY KEY,
    userId          integer REFERENCES User,
    roleId           integer REFERENCES Role
);

CREATE TABLE  IF NOT EXISTS  role_permission
(
    rolePermissionId  serial  PRIMARY KEY,
    roleId            integer REFERENCES Role,
    permissionId      integer REFERENCES Permission
);