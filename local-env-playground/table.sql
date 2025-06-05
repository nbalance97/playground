create table user(
    id bigint AUTO_INCREMENT primary key,
    name VARCHAR(255),
    grade VARCHAR(255)
);

INSERT INTO user (name, grade)
SELECT
    CONCAT('user', n) AS name,
    CASE
        WHEN n % 3 = 0 THEN 'A'
        WHEN n % 3 = 1 THEN 'B'
        ELSE 'C'
        END AS grade
FROM (
         SELECT ROW_NUMBER() OVER () AS n
         FROM information_schema.columns
         LIMIT 100
     ) AS numbers;
