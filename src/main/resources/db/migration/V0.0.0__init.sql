CREATE TABLE product (
  id SERIAL PRIMARY KEY,
  name character varying(255) NOT NULL UNIQUE,
  price numeric (15, 2) NOT NULL
);

CREATE TABLE orders (
  id SERIAL PRIMARY KEY,
  email character varying(255) NOT NULL,
  order_date timestamp NOT NULL,
  order_amount numeric (15, 2) NOT NULL
);

CREATE TABLE order_product(
  order_id SERIAL,
  product_id SERIAL,
  PRIMARY KEY(order_id, product_id),
  constraint fk_order_product_product foreign key (product_id) REFERENCES product(id),
  constraint fk_order_product_order foreign key (order_id) REFERENCES orders(id)
);

CREATE INDEX product_name_idx ON product(name);
CREATE INDEX order_id_idx ON order_product(order_id);