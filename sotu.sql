CREATE SCHEMA `sotu` DEFAULT CHARACTER SET utf8 ;

ALTER TABLE `sotu`.`image`
  ADD INDEX `idx_category` (`category` ASC),
  ADD UNIQUE INDEX `uk_url` (`url` ASC);

show TABLES ;
desc image;
desc search_key_word;

SELECT count(*) from image;
SELECT * FROM image;
SELECT * FROM search_key_word where key_word = '';
SELECT count(*) FROM search_key_word;
delete from search_key_word WHERE key_word = '';

SELECT * FROM search_key_word;








