-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               10.4.11-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table java_cms.app_page
CREATE TABLE IF NOT EXISTS `app_page` (
  `id` bigint(20) NOT NULL,
  `is_homepage` tinyint(1) DEFAULT 0,
  `url` varchar(255) DEFAULT NULL,
  `article_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_igbjulel7o24urrdylah0y6q5` (`url`),
  UNIQUE KEY `UK_ogn4i8o9x9yg1hi9ll1n0sbdo` (`title`),
  KEY `FK5o580git86qtguskkgfp6dkv3` (`article_id`),
  CONSTRAINT `FK5o580git86qtguskkgfp6dkv3` FOREIGN KEY (`article_id`) REFERENCES `app_article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dumping data for table java_cms.app_page: ~3 rows (approximately)
/*!40000 ALTER TABLE `app_page` DISABLE KEYS */;
INSERT INTO `app_page` (`id`, `is_homepage`, `url`, `article_id`, `title`) VALUES
	(10, 1, 'first-article', 1, 'First article'),
	(13, 0, 'Second Article', 3, 'Second article'),
	(14, 0, 'third', 4, 'Third Article'),
	(22, 0, 'Fourth', 5, 'Fourth');
/*!40000 ALTER TABLE `app_page` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
