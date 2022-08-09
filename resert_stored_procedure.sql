CREATE PROCEDURE reset ()
BEGIN
INSERT INTO Customer(CustomerId, customerName, customerAdress)
   VALUES (0,'Bjudkakor AB' , 'Ystad'),(0,'Finkakor AB','Helsingborg'),(0,'Gästkakor AB','Hässleholm'),(0,'Kaffebröd AB', 'Landskrona'),
          (0,'Kalaskakor AB', 'Trelleborg'),(0,'Partykakor AB', 'Kristianstad'),(0,'Skånekakor AB', 'Perstorp'),(0,'Småbröd AB', 'Malmö');
          
INSERT INTO Cookie(cookieName)
   VALUES ('Almond delight'),('Amneris'),('Berliner'),('Nut cookie'),('Nut ring'),('Tango');

INSERT INTO RawMaterials (materialName, currentAmountInStock, unit)
values ('Bread crumbs',500000, 'g'),('Butter', 500000, 'g'), ('Chocolate', 500000, 'g'), ('Chopped almonds', 500000, 'g'), ('Cinnamon', 500000, 'g'), ('Egg whites', 500000, 'ml'),
('Eggs', 500000, 'g'), ('Fine-ground nuts', 500000, 'g'), ('Flour', 500000, 'g'), ('Ground, roasted nuts', 500000, 'g'), ('Icing sugar', 500000, 'g'), ('Marzipan', 500000, 'g'), 
('Potato starch', 500000, 'g'), ('Roasted, chopped nuts', 500000, 'g'), ('Sodium bicarbonate', 500000, 'g'), ('Sugar', 500000, 'g'), ('Vanilla sugar', 500000, 'g'), 
('Vanilla', 500000, 'g'), ('Wheat flour', 500000, 'g');

insert into Recipe (amount, cookieName, materialName)
VALUES (400, 'Almond delight', 'butter'),
 (279, 'Almond delight', 'Chopped almonds'),
 (10, 'Almond delight', 'Cinnamon'),
 (400, 'Almond delight', 'Flour'),
 (270, 'Almond delight', 'Sugar');

insert into Recipe (amount, cookieName, materialName)
VALUES (250, 'Amneris', 'Butter'),
 (250, 'Amneris', 'Eggs'),
 (750, 'Amneris', 'Marzipan'),
 (25, 'Amneris', 'Potato starch'),
 (25, 'Amneris', 'Wheat flour');


insert into Recipe (amount, cookieName, materialName)
VALUES (250, 'Berliner', 'Butter'),
 (50, 'Berliner', 'Chocolate'),
 (50, 'Berliner', 'Eggs'),
 (350, 'Berliner', 'Flour'),
 (100, 'Berliner', 'Icing sugar'),
 (5, 'Berliner, Vanilla sugar');


insert into Recipe (amount, cookieName, materialName)
VALUES (125, 'Nut cookie', 'Bread crumbs'),
 (50, 'Nut cookie', 'Chocolate'),
 (350, 'Nut cookie', 'Egg whites'),
 (750, 'Nut cookie', 'Fine-ground nuts'),
 (625, 'Nut cookie', 'Ground-roasted nuts'),
 (375, 'Nut cookie', 'Sugar');


insert into Recipe (amount, cookieName, materialName)
VALUES (450, 'Nut ring', 'Butter'),
 (450, 'Nut ring', 'Flour'),
 (190, 'Nut ring', 'Icing sugar'),
 (225, 'Nut ring', 'Roasted-chopped nuts');


insert into Recipe (amount, cookieName, materialName)
VALUES (200, 'Tango', 'Butter'),
 (300, 'Tango', 'Flour'),
 (4, 'Tango', 'Sodium bicarbonate'),
 (250, 'Tango', 'Sugar'),
 (2, 'Tango', 'Vanilla');
END
