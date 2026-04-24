-- INSERTS para tipos de usuarios, usuarios, marcas, tipos de zapatillas, zapatillas y stock de zapatillas
INSERT INTO type_users (name) VALUES ('customer');
INSERT INTO type_users (name) VALUES ('admin');

INSERT INTO users (name, email, password, id_type) VALUES
('Admin', 'admin@example.com', 'admin123', 2),
('Pedro', 'pedro@example.com', 'pedro123', default);

INSERT INTO brands (name) VALUES ('Nike');
INSERT INTO brands (name) VALUES ('Adidas');
INSERT INTO brands (name) VALUES ('Puma');
INSERT INTO brands (name) VALUES ('New Balance');

INSERT INTO type_sneakers (name) VALUES ('Fútbol');
INSERT INTO type_sneakers (name) VALUES ('Casual');
INSERT INTO type_sneakers (name) VALUES ('Running');
INSERT INTO type_sneakers (name) VALUES ('Baloncesto');

-- Nike
INSERT INTO sneakers (name, description, price, filePath, id_brand, id_type_sneaker)
VALUES
('Nike Air Force 1', 'Zapatillas clásicas urbanas', 110.00, 'airforce1.jpg',1, 2),
('Nike Air Max 97', 'Diseño inspirado en tren bala con cámara de aire', 170.00, 'airmax97.png',1, 2),
('Nike LeBron XXIII', 'Zapatillas de baloncesto de alto rendimiento', 199.99, 'lebronxxiii.png',1, 4),
('Nike Pegasus 41', 'Zapatillas de running versátiles', 120.00, 'pegasus41.png',1, 3),
('Nike Mercurial Superfly 10', 'Botas de fútbol de perfil alto multisuperficie', 94.99, 'mercurialsuperfly10.png',1, 1);

-- Adidas
INSERT INTO sneakers (name, description, price, filePath, id_brand, id_type_sneaker)
VALUES
('Adidas Superstar', 'Clásicas con puntera de goma', 100.00, 'superstar.png',2, 2),
('Adidas Samba OG', 'Estilo clásico y urbano', 120.00, 'sambaog.png',2, 2),
('Adidas Adizero Adios Pro 4', 'Zapatillas de running exclusivas', 250.00, 'adizeroadiospro4.png',2, 3),
('Adidas Harden Volume 9', 'Zapatillas de baloncesto de alto rendimiento', 160.00, 'hardenvolume9.png',2, 4),
('Adidas F50 Sparkfusion League', 'Botas de fútbol de césped natural y artificial', 95.00, 'f50sparkfusionleague.png',2, 1);

-- Puma
INSERT INTO sneakers (name, description, price, filePath, id_brand, id_type_sneaker)
VALUES
('Puma x Rick And Morty MB.05', 'El dúo animado favorito de Melo vuelve para una nueva edición de la colaboración más salvaje del multiverso: PUMA x RICK AND MORTY.', 135.00, 'mb05.png',3, 4),
('Puma Palermo Leather', 'Zapatillas clásicas urbanas', 90.00, 'palermoleather.png',3, 2),
('Puma RS-X', 'Diseño chunky moderno', 120.00, 'rsx.png',3, 2),
('Puma Future 9 Ultimate', 'Todo lo demás se puede enseñar. ¿La creatividad? La tienes o no la tienes. Da rienda suelta a tus habilidades como creador de jugadas', 240, 'future9ultimate.png',3, 1),
('Puma NITRO™ Elite 4', 'Cuando la rapidez no es suficiente, vas más allá. Las Deviate NITRO™ Elite 4 están diseñadas para los días de competición, esos momentos', 100.00, 'nitroelite4.png',3, 2);

-- New Balance
INSERT INTO sneakers (name, description, price, filePath, id_brand, id_type_sneaker)
VALUES
('New Balance 550', 'Modelo retro de baloncesto', 150.00, 'newbalance550.png',4, 2),
('New Balance 574', 'Clásico cómodo para diario', 95.00, 'newbalance574.png',4, 2);

INSERT INTO stocks (id_sneaker, size, quantity)
VALUES
(1, 8.0, 10), (1, 8.5, 10),
(2, 8.0, 10), (2, 8.5, 10),
(3, 8.0, 10), (3, 8.5, 10),
(4, 8.0, 10), (4, 8.5, 10),
(5, 8.0, 10), (5, 8.5, 10),

(6, 8.0, 10), (6, 8.5, 10),
(7, 8.0, 10), (7, 8.5, 10),
(8, 8.0, 10), (8, 8.5, 10),
(9, 8.0, 10), (9, 8.5, 10),
(10, 8.0, 10), (10, 8.5, 10),

(11, 8.0, 10), (11, 8.5, 10),
(12, 8.0, 10), (12, 8.5, 10),
(13, 8.0, 10), (13, 8.5, 10),
(14, 8.0, 10), (14, 8.5, 10),
(15, 8.0, 10), (15, 8.5, 10),

(16, 8.0, 10), (16, 8.5, 10),
(17, 8.0, 10), (17, 8.5, 10);