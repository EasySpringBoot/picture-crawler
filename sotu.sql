CREATE SCHEMA `sotu`
  DEFAULT CHARACTER SET utf8;

ALTER TABLE `sotu`.`image`
  ADD INDEX `idx_category` (`category` ASC),
  ADD UNIQUE INDEX `uk_url` (`url` ASC);

SHOW TABLES;
DESC image;
DESC search_key_word;

SELECT count(*)
FROM image;
SELECT *
FROM image;
SELECT *
FROM search_key_word
WHERE key_word = '';
SELECT count(*)
FROM search_key_word;
DELETE FROM search_key_word
WHERE key_word = '';

SELECT *
FROM search_key_word;

SELECT *
FROM image
WHERE source_type = 1;


UPDATE image
SET category = concat('干货集中营福利', curdate()) where source_type=1;


SELECT
  @mb := round((sum(DATA_LENGTH) + sum(INDEX_LENGTH)) / (1024 * 1024), 2),
  concat(@mb, 'MB')
FROM information_schema.tables
WHERE table_schema = 'sotu';










