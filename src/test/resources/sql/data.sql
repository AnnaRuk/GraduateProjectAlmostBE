insert into account(id,address, city, date_of_birth, email, first_name, gender, hash_password, last_name, phone, postcode, role, state)
values (1,'Berlinstrasse7','Berlin','1998-03-08','sedakovsergey1@gmail.com','Sergey','MALE','$2a$04$YFDmGcB9H317JRzplPdezOll9xdkNyJtfX.eU/dNGlT4.aHho63nW','Sedakov','+4912451219','45129','USER','CONFIRMED');

insert into account(id,address, city, date_of_birth, email, first_name, gender, hash_password, last_name, phone, postcode, role, state)
values (2,'gerlichstrasse5','Dusseldorf','1997-05-12','anna@gmail.com','Anna','FEMALE','Qwerty999!','Bieliaieva','+498746416','46549','USER','CONFIRMED');

insert into account(id,address, city, date_of_birth, email, first_name, gender, hash_password, last_name, phone, postcode, role, state)
values (3,'petrovka5','Koln','1947-07-15','semenova1@gmail.com','larisa','FEMALE','Qwerty999!','semenovna','+484151651','74441','USER','CONFIRMED');

insert into account(id,address, city, date_of_birth, email, first_name, gender, hash_password, last_name, phone, postcode, role, state)
values (4,'nikolaystris15','Dusseldorf','1987-07-01','kolyan25@gmail.com','Nikolay','MALE','Kolyan1!','Nikolaenko','+49484894','11115','USER','CONFIRMED');

insert into account(id,address, city, date_of_birth, email, first_name, gender, hash_password, last_name, phone, postcode, role, state)
values (5,'petrovska74','Essen','1977-11-12','irina15@gmail.com','irina','FEMALE','Qwerty999!','borisova','+4944564','55549','USER','CONFIRMED');



insert into kindergarten(id,address, capacity, city, description, free_places, link_img, postcode, title, manager_id)
values (1,'berlinstrasse',25,'Berlin','forest kindergarten',10,'img','45126','hi',1);

insert into kindergarten(id,address, capacity, city, description, free_places, link_img, postcode, title, manager_id)
values (2,'strasse50',50,'Munchen','very god kindergarten',1,'img','12349','hi',1);

insert into kindergarten(id,address, capacity, city, description, free_places, link_img, postcode, title, manager_id)
values (3,'fake',500,'Berlin','fake kindergarten',1,'img','45641','hi',1);

insert into favorites(user_id, kindergarten_id)
values (2,1);
insert into favorites(user_id, kindergarten_id)
values (2,2);
insert into favorites(user_id, kindergarten_id)
values (3,2);
