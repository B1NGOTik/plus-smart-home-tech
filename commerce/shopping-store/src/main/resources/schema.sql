CREATE SCHEMA IF NOT EXISTS store;

CREATE TABLE IF NOT EXISTS store.products(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(64),
    description TEXT,
    image_src TEXT,
    quantity_state TEXT,
    product_state TEXT,
    product_category TEXT,
    price FLOAT
);