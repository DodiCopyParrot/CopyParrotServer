CREATE TABLE IF NOT EXISTS users (
                                     `id` bigint not null auto_increment,
                                     `uuid` varchar(255) not null,
                                     `created_date` varchar(20) not null,
                                     `influencer` bigint null,
                                     primary key (id)
);


drop table users;


CREATE TABLE IF NOT EXISTS influencer(
                                         `id` bigint not null auto_increment,
                                         `name` varchar(255) not null,
                                         `image` text null,
                                         `context` varchar(255) not null,
                                         `voice_file` text null default null,
                                         `json` text null,
                                         primary key (id)
);

INSERT INTO parrot_db.influencer (id, name, image, context, voice_file, json) VALUES (1, 'Barack Obama', 'hhttps://api.dopaminedefense.team/file/image/a100fb2b-1cb4-4542-81d6-b0e3d54f91ab_Obama.jpg', '따뜻하고 설득력 있는 어조', null, 's3://voice-cloning-zero-shot/65a562d0-a0f4-4dcd-9542-f371316fb8b8/original/manifest.json');
INSERT INTO parrot_db.influencer (id, name, image, context, voice_file, json) VALUES (2, 'Elon Musk', 'https://api.dopaminedefense.team/file/image/45c1a054-ccc6-46d8-b56c-1b21ae270716_ElonMusk.png', '남아공 출신 특유의 억양 도전적이며 논리적', 'ElonMusk.mp3', 's3://voice-cloning-zero-shot/65a562d0-a0f4-4dcd-9542-f371316fb8b8/original/manifest.json');



CREATE TABLE IF NOT EXISTS mark(
                                   `id` bigint not null auto_increment,
                                   `uuid` varchar(255) not null,
                                   `file` text null,
                                   `ko_text` varchar(255) not null,
                                   `en_text` text null,
                                   `is_save` boolean,
                                   `influencer_id` bigint null,
                                   `created_date` varchar(20) not null,
                                   primary key (id)
);


drop table mark;



select mark.id, mark.uuid, mark.file, mark.ko_text, mark.en_text, i.name, i.image
from mark
         left join influencer i on mark.influencer_id = i.id
where uuid = '014B5C20-35DE-46B0-A9C0-BDDB1FE8DEA3' and is_save = true;