-- Brands
INSERT INTO brands (name) VALUES ('Nike');
INSERT INTO brands (name) VALUES ('Adidas');
INSERT INTO brands (name) VALUES ('Puma');
INSERT INTO brands (name) VALUES ('Under Armour');
INSERT INTO brands (name) VALUES ('New Balance');

-- Types
INSERT INTO types (name) VALUES ('Running');
INSERT INTO types (name) VALUES ('Football');
INSERT INTO types (name) VALUES ('Basketball');
INSERT INTO types (name) VALUES ('Training');
INSERT INTO types (name) VALUES ('Lifestyle');

-- Products
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Nike Air Zoom Pegasus 40', 'Versatile running shoe with responsive cushioning', 129.99, 'https://static.nike.com/pegasus40.jpg', 1, 1);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Adidas Predator Accuracy', 'Football boot with superior grip and control', 149.99, 'https://assets.adidas.com/predator.jpg', 2, 2);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Nike LeBron XX', 'Basketball shoe with impact protection technology', 199.99, 'https://static.nike.com/lebronxx.jpg', 1, 3);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Under Armour HOVR Phantom', 'Training shoe with zero-gravity feel cushioning', 119.99, 'https://assets.underarmour.com/hovr.jpg', 4, 4);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Puma RS-X', 'Lifestyle sneaker with retro running design', 89.99, 'https://images.puma.com/rsx.jpg', 3, 5);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Adidas Ultraboost 23', 'Premium running shoe with Boost midsole technology', 179.99, 'https://assets.adidas.com/ultraboost23.jpg', 2, 1);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('New Balance Fresh Foam 1080v13', 'Maximum cushioning running shoe for long distances', 159.99, 'https://images.newbalance.com/1080v13.jpg', 5, 1);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Nike Mercurial Vapor 15', 'Speed-focused football boot for fast players', 229.99, 'https://static.nike.com/mercurial15.jpg', 1, 2);

-- Users
INSERT INTO users (username, password) VALUES ('admin@correo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');