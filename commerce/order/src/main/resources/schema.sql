CREATE SCHEMA IF NOT EXISTS order_schema;

CREATE TABLE IF NOT EXISTS order_schema.orders (
    order_id UUID PRIMARY KEY,
    order_state VARCHAR(10),
    shopping_cart_id UUID,
    username VARCHAR(100),
    payment_id UUID,
    delivery_id UUID,
    delivery_weight DOUBLE PRECISION,
    delivery_volume DOUBLE PRECISION,
    fragile BOOLEAN,
    total_price DOUBLE PRECISION,
    delivery_price DOUBLE PRECISION,
    product_price DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS order_schema.order_products (
    order_id UUID REFERENCES order_schema.orders(order_id) ON DELETE CASCADE,
    product_id UUID,
    quantity INT,
    PRIMARY KEY(order_id, product_id)
);