CREATE SCHEMA `sotu` DEFAULT CHARACTER SET utf8 ;

ALTER TABLE `sotu`.`image`
  ADD INDEX `idx_category` (`category` ASC),
  ADD UNIQUE INDEX `uk_url` (`url` ASC);

show TABLES ;
desc image;

SELECT count(*) from image;







