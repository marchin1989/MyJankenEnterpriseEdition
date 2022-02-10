-- players table
CREATE TABLE IF NOT EXISTS `janken`.`players`(
  `id` CHAR(36) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

-- jankens table
CREATE TABLE IF NOT EXISTS `janken`.`jankens`(
  `id` CHAR(36) NOT NULL,
  `played_at` DATETIME(6) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `played_at_idx` (`played_at` ASC)
);

-- janken_details table
CREATE TABLE IF NOT EXISTS `janken`.`janken_details`(
  `id` CHAR(36) NOT NULL,
  `janken_id` CHAR(36) NOT NULL,
  `player_id` CHAR(36) NOT NULL,
  `hand` INT UNSIGNED NOT NULL,
  `result` INT UNSIGNED NOT NULL,
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
