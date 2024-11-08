CREATE TABLE IF NOT EXISTS users (
 `id` bigint not null auto_increment,
 `uuid` varchar(255) not null,
 `created_date` varchar(20) not null,
 primary key (id)
);


CREATE TABLE IF NOT EXISTS Influencer(
`id` bigint not null auto_increment,
`name` varchar(255) not null,
`image` text null,
`describe` varchar(255) not null,
`voiceFile` text null,
`json` text null,
primary key (id)
);

drop table Influencer;


CREATE TABLE IF NOT EXISTS mark(
`id` bigint not null auto_increment,
`uuid` varchar(255) not null,
`file` text null,
`ko_text` varchar(255) not null,
`en_text` text null,
`is_save` boolean,
primary key (id)
);

drop table mark;