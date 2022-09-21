CREATE DATABASE test;

USE test;

DROP TABLE hibernate_sequence;
DROP TABLE user;
DROP TABLE users;

CREATE TABLE users(
	`id` INT(11) NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(45) NOT NULL,
    `password` VARCHAR(45) NOT NULL,
    
    PRIMARY KEY(`id`)
);

# 테이블 목록 보여주기
SHOW TABLES;

# 해당 테이블의 필드 정보 보여주기
DESC users;

INSERT INTO users (user_id, password) VALUES ("test", "test");
INSERT INTO users (user_id, password) VALUES ("test2", "test2");
INSERT INTO users (user_id, password) VALUES ("test10", "test10");
SELECT * FROM users;
SELECT * FROM hibernate_sequence;



# users의 id, userId만 선택
# https://dev.mysql.com/doc/refman/8.0/en/select.html
SELECT id, user_id FROM user;

# expr도 가능
SELECT 1+1;

# id가 1인 userId
SELECT user_id FROM users WHERE id=1;

# id를 역으로 정렬해 출력
SELECT * FROM users ORDER BY id DESC;

# LIMIT 갯수 만큼 출력
SELECT * FROM users ORDER BY id DESC LIMIT 1;

# safe mode 끄기
SET SQL_SAFE_UPDATES = 0;

# safe mod 켜기
SET SQL_SAFE_UPDATES = 1;

# update test2 test2 -> test2 test3
# 이건 safe mod 있으면 작동 안함
UPDATE users SET password = 'test3' WHERE user_id = 'test2';

# 이건 safe mod 있어도 작동함 
UPDATE users SET password = 'test4' WHERE id = 1;

# id가 2인 유저 삭제
DELETE FROM users WHERE id = 2;