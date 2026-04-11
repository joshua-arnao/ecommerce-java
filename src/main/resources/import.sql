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
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Nike Air Zoom Pegasus 40', 'Versatile running shoe', 129.99, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', 1, 1);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Adidas Predator Accuracy', 'Football boot with superior grip', 149.99, 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=400', 2, 2);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Nike LeBron XX', 'Basketball shoe', 199.99, 'https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=400', 1, 3);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Under Armour HOVR Phantom', 'Training shoe', 119.99, 'https://images.unsplash.com/photo-1539185441755-769473a23570?w=400', 4, 4);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Puma RS-X', 'Lifestyle sneaker', 89.99, 'https://images.unsplash.com/photo-1584735175315-9d5df23be620?w=400', 3, 5);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Adidas Ultraboost 23', 'Premium running shoe', 179.99, 'https://images.unsplash.com/photo-1587563871167-1ee9c731aefb?w=400', 2, 1);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('New Balance Fresh Foam 1080v13', 'Maximum cushioning', 159.99, 'https://images.unsplash.com/photo-1556906781-9a412961d28b?w=400', 5, 1);
INSERT INTO products (name, description, price, picture_url, product_brand_id, product_type_id) VALUES ('Nike Mercurial Vapor 15', 'Speed football boot', 229.99, 'https://images.unsplash.com/photo-1575537302964-96cd47c06b1b?w=400', 1, 2);

-- Users
INSERT INTO users (username, password) VALUES ('admin@correo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');