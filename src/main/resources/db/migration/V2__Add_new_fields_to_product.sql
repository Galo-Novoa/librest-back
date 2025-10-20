-- Crear tabla de categorías
CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

-- Agregar nuevos campos a producto
ALTER TABLE product 
ADD COLUMN rating DOUBLE PRECISION DEFAULT 0.0,
ADD COLUMN publisher VARCHAR(255) DEFAULT 'admin',
ADD COLUMN date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN sale INTEGER DEFAULT 0,
ADD COLUMN category_id BIGINT,
ADD CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id);

-- Insertar categorías por defecto
INSERT INTO category (name, description) VALUES 
('Electrónicos', 'Teléfonos, computadoras, tablets y más'),
('Hogar', 'Muebles, decoración y artículos para el hogar'),
('Deportes', 'Equipamiento deportivo y ropa de ejercicio'),
('Moda', 'Ropa, calzado y accesorios'),
('Libros', 'Libros, novelas y material educativo'),
('Juguetes', 'Juguetes para niños y juegos'),
('Salud', 'Productos de salud y belleza'),
('Automotriz', 'Repuestos y accesorios para vehículos'),
('Mascotas', 'Alimentos y accesorios para mascotas'),
('Herramientas', 'Herramientas y equipamiento');