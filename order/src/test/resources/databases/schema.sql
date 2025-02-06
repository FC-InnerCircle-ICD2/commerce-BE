CREATE TABLE order_statistics
(
    id                  BIGINT PRIMARY KEY,
    product_id          BIGINT    NOT NULL,
    category_id         BIGINT    NOT NULL,
    total_order         INT       NOT NULL,
    total_quantity_sold INT       NOT NULL,
    last_ordered_at     TIMESTAMP NOT NULL
);