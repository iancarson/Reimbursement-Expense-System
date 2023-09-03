/*
-- users
INSERT INTO user(id,username,first_name,last_name,password,email,address) VALUES (1,'acrenwelge','Andrew','Crenwelge', 'admin' ,'andrewcrenwelge@gmail.com', '11830 Sunrise Valley Dr Apt 545');
INSERT INTO user(id,username,first_name,last_name,password,email,address) VALUES (2,'bcrenwelge','Blake' ,'Crenwelge', 'blake' ,'blakecrenwelge@gmail.com' , '1945 Blueridge Ave');
INSERT INTO user(id,username,first_name,last_name,password,email,address) VALUES (3,'screnwelge','Seth'  ,'Crenwelge', 'seth'  ,'sethcrenwelge@gmail.com'  , '203 Sunridge Heights');
INSERT INTO user(id,username,first_name,last_name,password,email,address) VALUES (4,'ncrenwelge','Nicole','Crenwelge', 'nicole','nicolecrenwelge@gmail.com', '11830 Sunrise Valley Dr Apt 545');

-- reimbursements
INSERT INTO reimbursement(user_id, is_approved) VALUES (1, false);
INSERT INTO reimbursement(user_id, is_approved) VALUES (1, true);

-- expenses
INSERT INTO expense(reimbursement_id, amount, description) VALUES (1, 1.99, 'some description');
INSERT INTO expense(reimbursement_id, amount, description) VALUES (1, 5.31, 'some description');
INSERT INTO expense(reimbursement_id, amount, description) VALUES (1, 9.76, 'some description');
*/
