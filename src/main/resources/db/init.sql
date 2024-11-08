

CREATE TABLE IF NOT EXISTS products (
    `id` bigint not null auto_increment,
    `category` varchar(100) not null,
    `name` varchar(100) not null,
    `price` bigint not null,
    `total_count` bigint not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS products_history (
                                        `id` bigint not null auto_increment,
                                        `product_id` bigint not null,
                                        `count` bigint not null,
                                        `created_date` varchar(100) not null,
                                        primary key (id)
);


INSERT INTO products (category, name, price, total_count) VALUES ('FOOD', 'chicken', 20000, 100);

