-- Tabel pengguna (user)
CREATE TABLE pengguna (
    id INTEGER PRIMARY KEY,
    name VARCHAR(25),
    username VARCHAR(25) UNIQUE,
	password VARCHAR (10),
    bio VARCHAR(40)
	
	
);

insert into pengguna (id, name, username, password, bio) values (01, 'salwa', 'salwafz', '123', 'tetap semangat' );

insert into pengguna (id, name, username, password, bio) values (02, 'bejo', 'bejoTejo', '123', 'gak semangat' );
