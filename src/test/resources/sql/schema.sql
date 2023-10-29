drop table if exists product;

CREATE TABLE IF NOT EXISTS product (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        category_id INT NOT NULL,
                                        description VARCHAR(5000) NOT NULL,
                                        expiration_date DATE,
                                        price DOUBLE NOT NULL,
                                        product_name VARCHAR(50) NOT NULL,
                                        state VARCHAR(255)
);

