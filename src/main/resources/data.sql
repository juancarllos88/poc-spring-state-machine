insert IGNORE into product(id, name, price, automatic_shipping, quantity_in_stock)
values(1, 'Caneca do Cebolinha', 50.0, true, 25);

insert IGNORE into cart(id, created_at, shipping_price, state)
values(1, '2022-05-27', 15.5, 'EMPTY');
