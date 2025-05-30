insert into user (id, email,first_name,last_name,roles,password) values (1,'yusuke@yuyuhakusho.com','Yusuke','Urameshi', 'USER', '{bcrypt}$2a$10$k9njnRGaefKGhAvVQdW./O9ZG/794jyZbhcMFkALD3xiwKji.Op3i');
insert into user (id, email,first_name,last_name,roles,password) values (2,'hiei@yuyuhakusho.com','Hiei','Dragon', 'USER', '{bcrypt}$2a$10$k9njnRGaefKGhAvVQdW./O9ZG/794jyZbhcMFkALD3xiwKji.Op3i');
insert into profile (id, description, name) values (1, 'Manages everything', 'Admin');
insert into user_profile (id, user_id, profile_id) values (1,1,1);
insert into user_profile (id, user_id, profile_id) values (2,2,1);