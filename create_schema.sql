set FOREIGN_KEY_CHECKS = 0;
drop table if exists RawMaterials;
drop table if exists Orders;
drop table if exists Pallet;
drop table if exists Recipe;
drop table if exists Customer;
drop table if exists Orders;
drop table if exists ProductQuantity;
drop table if exists Cookie;


create table Customer(
customerId INTEGER auto_increment,
name VARCHAR(30),
address VARCHAR(45),

primary key(customerId)
);

create table Orders(
orderId INTEGER auto_increment,
orderDate DATEtime,

customerId int,

primary key(orderId),
foreign key(customerId) references Customer(customerId)
);

create table Pallet(
palletId INTEGER auto_increment,
blocked boolean,
productionDate Datetime,
deliveryDate Datetime,
maxAmount int,

cookie VARCHAR(30),
orderId int,
primary key(palletId),
foreign key(orderId) references Orders(orderId),
foreign key(cookie) references Cookie(name)

);

create table ProductQuantity(
numPallets int,
orderId int,
cookieName VARCHAR(30),
primary key(orderId, cookieName),
foreign key(orderId) references Orders(orderId),
foreign key(cookieName) REFeRENCES Cookie(name)
);

create table Cookie(
name VARCHAR(30),
primary key(name)
);

create table RawMaterials(
name VARCHAR(30),
currentAmountInStock int,
lastDelAmount int,
lastDelDate datetime,
unit VARCHAR(10),
primary key(name)

);


create table Recipe(
amount int,
cookie VARCHAR(30),
raw_material VARCHAR(30),
unit VARCHAR(20),
primary key(cookie, raw_material),
foreign key(cookie) references Cookie(name),
foreign key(raw_material) references RawMaterials(name)
);
set FOREIGN_KEY_CHECKS = 1;