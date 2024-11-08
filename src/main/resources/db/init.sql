CREATE TABLE IF NOT EXISTS users (
 `id` bigint not null auto_increment,
 `uuid` varchar(255) not null,
 `name` varchar(100) not null,
 `created_date` varchar(20) not null,
 primary key (id)
);


CREATE TABLE IF NOT EXISTS Influencer(
`id` bigint not null auto_increment,
`image` text null,
`context` varchar(255) not null,
`json` text null,
primary key (id)
);