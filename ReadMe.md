# Create MySQL DB with table

```CREATE DATABASE APIDevelopSpringBoot;
use APIDevelopSpringBoot;
CREATE TABLE LibraryDemo(
book_name varchar(50),
id varchar(50),
isbn varchar(50),
aisle int,
author varchar(50), PRIMARY KEY (id)
);

INSERT INTO LibraryDemo(book_name, id, isbn, aisle, author)
values("Appium", "fdsefr343", "fdsefr3", "43", "Mrkonja");```
