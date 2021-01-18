-- players table
CREATE TABLE IF NOT EXISTS `janken`.`players`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

-- jankens table
CREATE TABLE IF NOT EXISTS `janken`.`jankens`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `played_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
);

-- janken_details table
CREATE TABLE IF NOT EXISTS `janken`.`janken_details`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `janken_id` INT UNSIGNED NOT NULL,
  `player_id` INT UNSIGNED NOT NULL,
  `hand` TINYINT UNSIGNED NOT NULL,
  `result` TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `janken_id_idx` (`janken_id` ASC),
  INDEX `player_id_idx` (`player_id` ASC),
  CONSTRAINT `janken_id`
    FOREIGN KEY (`janken_id`)
    REFERENCES `janken`.`jankens` (`id`),
  CONSTRAINT `player_id`
    FOREIGN KEY (`player_id`)
    REFERENCES `janken`.`players` (`id`)
);

INSERT INTO `janken`.`players` (`id`, `name`)VALUES
(1, 'Alice'),
(2, 'Bob');
