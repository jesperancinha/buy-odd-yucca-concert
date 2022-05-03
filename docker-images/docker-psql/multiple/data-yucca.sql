insert into ticket.car_parking (id, parking_number)
values ('E3BB8287-8F4F-477B-AFDF-44D78665A08C', 1);
insert into ticket.car_parking (id, parking_number)
values ('23745A3C-9426-4E92-A940-89E07C2ED24D', 2);
insert into ticket.car_parking (id, parking_number)
values ('27728F31-244A-4469-81AC-86B332E7BD1B', 3);
insert into ticket.car_parking (id, parking_number)
values ('0892A233-4973-4477-A6DA-91E44E1386D3', 4);
insert into ticket.car_parking (id, parking_number)
values ('61BF1E61-A7C2-4D6E-951A-8E97BD4E4FB3', 5);

insert into ticket.concert_day(id, name, description, concert_date)
values ('5359A368-CA49-4027-BC25-F375E3EA2463', 'Jamala', '', now());
insert into ticket.concert_day(id, name, description, concert_date)
values ('2E4522B1-D9FF-4B2B-9FFA-052CBAD9D5F2', 'Kalush Orchestra', '', now());

insert into ticket.drink(id, name, width, height, shape, volume, price)
values ('2377198D-9E41-4134-8E89-ABD66FE0C59B', 'Varenukha', 10, 10, 10, 10, 10);
insert into ticket.drink(id, name, width, height, shape, volume, price)
values ('B2A5E349-76E7-4CD6-8105-308D1BC94953', 'Uzvar', 10, 10, 10, 10, 10);

insert into ticket.meal(id, coupon , box_type, discount, price, processed)
values ('59B97053-37CF-4FAF-AB50-E77CEF8E8CC8', gen_random_uuid(), 'XS', 10, 10, false);
insert into ticket.meal(id, coupon , box_type, discount, price, processed)
values ('4581DECF-7740-44E8-8B2C-B7EC0FEE31C3', gen_random_uuid(), 'XS', 10, 10, false);
