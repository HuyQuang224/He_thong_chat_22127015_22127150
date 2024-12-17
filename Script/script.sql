-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               9.1.0 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for userregistrationdb
CREATE DATABASE IF NOT EXISTS `userregistrationdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `userregistrationdb`;

-- Dumping structure for table userregistrationdb.friend_request
CREATE TABLE IF NOT EXISTS `friend_request` (
  `FROM_ID` int NOT NULL,
  `TO_ID` int NOT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `TRY` int DEFAULT NULL,
  PRIMARY KEY (`FROM_ID`,`TO_ID`),
  KEY `TO_ID` (`TO_ID`),
  CONSTRAINT `friend_request_ibfk_1` FOREIGN KEY (`FROM_ID`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `friend_request_ibfk_2` FOREIGN KEY (`TO_ID`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table userregistrationdb.friend_request: ~3 rows (approximately)
INSERT INTO `friend_request` (`FROM_ID`, `TO_ID`, `STATUS`, `TRY`) VALUES
	(1, 3, 'Pending', 1),
	(1, 5, 'Pending', 1),
	(1, 6, 'Pending', 1);

-- Dumping structure for table userregistrationdb.groupchat
CREATE TABLE IF NOT EXISTS `groupchat` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `GROUP_NAME` varchar(255) DEFAULT NULL,
  `CREATED_AT` timestamp NULL DEFAULT NULL,
  `ONLINE` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table userregistrationdb.groupchat: ~5 rows (approximately)
INSERT INTO `groupchat` (`ID`, `GROUP_NAME`, `CREATED_AT`, `ONLINE`) VALUES
	(1, 'Group1', '2022-12-18 15:52:40', 0),
	(2, 'Group2', '2022-12-18 15:52:40', 0),
	(3, 'Group3', '2022-12-18 15:52:40', 0),
	(4, 'clc_02', '2024-12-17 10:56:11', NULL),
	(5, 'hoctap', '2024-12-17 10:56:45', NULL);

-- Dumping structure for table userregistrationdb.groupchat_member
CREATE TABLE IF NOT EXISTS `groupchat_member` (
  `GROUPCHAT_ID` int NOT NULL,
  `MEMBER_ID` int NOT NULL,
  `POSITION` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`GROUPCHAT_ID`,`MEMBER_ID`),
  KEY `MEMBER_ID` (`MEMBER_ID`),
  CONSTRAINT `groupchat_member_ibfk_1` FOREIGN KEY (`GROUPCHAT_ID`) REFERENCES `groupchat` (`ID`),
  CONSTRAINT `groupchat_member_ibfk_2` FOREIGN KEY (`MEMBER_ID`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table userregistrationdb.groupchat_member: ~11 rows (approximately)
INSERT INTO `groupchat_member` (`GROUPCHAT_ID`, `MEMBER_ID`, `POSITION`) VALUES
	(1, 1, 'admin'),
	(1, 2, 'member'),
	(1, 3, 'member'),
	(2, 2, 'admin'),
	(3, 3, 'admin'),
	(4, 1, 'member'),
	(4, 2, 'admin'),
	(4, 4, 'member'),
	(5, 2, 'admin'),
	(5, 3, 'member'),
	(5, 4, 'member');

-- Dumping structure for table userregistrationdb.login_history
CREATE TABLE IF NOT EXISTS `login_history` (
  `LOGIN_ID` int NOT NULL AUTO_INCREMENT,
  `USER_ID` int DEFAULT NULL,
  `LOGIN_TIME` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`LOGIN_ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `login_history_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table userregistrationdb.login_history: ~6 rows (approximately)
INSERT INTO `login_history` (`LOGIN_ID`, `USER_ID`, `LOGIN_TIME`) VALUES
	(1, 1, '2022-12-15 13:30:00'),
	(2, 3, '2022-12-17 16:55:40'),
	(3, 3, '2022-12-17 01:42:05'),
	(4, 1, '2022-12-18 03:17:21'),
	(5, 2, '2022-12-19 08:25:00'),
	(6, 6, '2022-12-20 15:52:40');

-- Dumping structure for table userregistrationdb.message_group
CREATE TABLE IF NOT EXISTS `message_group` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `FROM_USER` int DEFAULT NULL,
  `TO_GROUP` int DEFAULT NULL,
  `TIME_SEND` timestamp NULL DEFAULT NULL,
  `CONTENT` text,
  PRIMARY KEY (`ID`),
  KEY `FROM_USER` (`FROM_USER`),
  KEY `TO_GROUP` (`TO_GROUP`),
  CONSTRAINT `message_group_ibfk_1` FOREIGN KEY (`FROM_USER`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `message_group_ibfk_2` FOREIGN KEY (`TO_GROUP`) REFERENCES `groupchat` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table userregistrationdb.message_group: ~10 rows (approximately)
INSERT INTO `message_group` (`ID`, `FROM_USER`, `TO_GROUP`, `TIME_SEND`, `CONTENT`) VALUES
	(1, 1, 1, '2022-12-18 15:52:40', 'USER1: Còn công việc cuối là cái này nhưng t để nhầm file làm việc, phải là ở uichatcomponent/DetailAccounForm.java'),
	(2, 1, 1, '2022-12-18 15:52:40', 'USER1: Deadline là 9h tối nay, k có thì t làm lun'),
	(3, 2, 1, '2022-12-18 15:52:40', 'USER2: Oki t có lm cái sườn r'),
	(4, 3, 1, '2022-12-18 15:52:40', 'USER3: cần full tất cả các cột ko ông hay mấy cột chính thôi'),
	(5, 3, 3, '2022-12-18 15:52:40', 'USER3: Message6'),
	(6, 1, 1, '2022-12-18 15:52:40', 'USER1: tranh thủ tối nay xong cái này nhé'),
	(7, 2, 2, '2022-12-18 15:52:40', 'USER2: Message8'),
	(8, 2, 4, NULL, 'USER2: chao cac ban'),
	(9, 4, 4, NULL, 'USER4: hello'),
	(10, 1, 4, NULL, 'USER1: hihi');

-- Dumping structure for table userregistrationdb.message_user
CREATE TABLE IF NOT EXISTS `message_user` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `CHATBOX_ID` varchar(255) DEFAULT NULL,
  `FROM_USER` int DEFAULT NULL,
  `TO_USER` int DEFAULT NULL,
  `TIME_SEND` timestamp NULL DEFAULT NULL,
  `CONTENT` text,
  `VISIBLE_ONLY` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FROM_USER` (`FROM_USER`),
  KEY `TO_USER` (`TO_USER`),
  CONSTRAINT `message_user_ibfk_1` FOREIGN KEY (`FROM_USER`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `message_user_ibfk_2` FOREIGN KEY (`TO_USER`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table userregistrationdb.message_user: ~9 rows (approximately)
INSERT INTO `message_user` (`ID`, `CHATBOX_ID`, `FROM_USER`, `TO_USER`, `TIME_SEND`, `CONTENT`, `VISIBLE_ONLY`) VALUES
	(1, '1-3', 1, 3, '2022-12-18 15:52:40', 'USER1: Hôm nay ông thế nào rồi', NULL),
	(2, '2-3', 2, 3, '2022-12-18 15:55:40', 'USER2: Mai nhớ đem tập', NULL),
	(3, '3-4', 4, 3, '2022-12-18 15:52:40', 'USER4: Tập bữa mượn đâu rồi', NULL),
	(4, '1-3', 3, 1, '2022-12-18 15:52:40', 'USER3: Bữa nay thấy không đi học', NULL),
	(5, '2-3', 3, 2, '2022-12-18 15:52:40', 'USER3: Nó đòi t rồi', NULL),
	(6, '4-6', 4, 6, '2022-12-18 15:52:40', 'USER4: Ok', NULL),
	(7, '4-6', 6, 4, '2022-12-18 15:52:40', 'USER6: Bye', NULL),
	(8, '1-9', 9, 1, '2022-12-18 15:52:40', 'USER9: Cuối tuần rảnh không!', NULL),
	(22, NULL, 2, 4, NULL, 'USER2: hello', NULL);

-- Dumping structure for table userregistrationdb.spam_report
CREATE TABLE IF NOT EXISTS `spam_report` (
  `REPORT_ID` int NOT NULL AUTO_INCREMENT,
  `REPORTER_ID` int DEFAULT NULL,
  `REPORTED_USER_ID` int DEFAULT NULL,
  `MESSAGE_ID` bigint DEFAULT NULL,
  `REPORT_REASON` text,
  `CREATED_AT` timestamp NULL DEFAULT NULL,
  `STATUS` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`REPORT_ID`),
  KEY `REPORTER_ID` (`REPORTER_ID`),
  KEY `REPORTED_USER_ID` (`REPORTED_USER_ID`),
  KEY `MESSAGE_ID` (`MESSAGE_ID`),
  CONSTRAINT `spam_report_ibfk_1` FOREIGN KEY (`REPORTER_ID`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `spam_report_ibfk_2` FOREIGN KEY (`REPORTED_USER_ID`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `spam_report_ibfk_3` FOREIGN KEY (`MESSAGE_ID`) REFERENCES `message_user` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table userregistrationdb.spam_report: ~3 rows (approximately)
INSERT INTO `spam_report` (`REPORT_ID`, `REPORTER_ID`, `REPORTED_USER_ID`, `MESSAGE_ID`, `REPORT_REASON`, `CREATED_AT`, `STATUS`) VALUES
	(1, 1, 5, 1, 'Spam', '2022-12-18 16:00:00', 'Pending'),
	(2, 3, 5, 3, 'Offensive content', '2022-12-18 16:05:00', 'Reviewed'),
	(3, 3, 5, 4, 'Spam', '2022-12-18 16:10:00', 'Resolved');

-- Dumping structure for table userregistrationdb.user_account
CREATE TABLE IF NOT EXISTS `user_account` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `FULLNAME` varchar(255) DEFAULT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `DATE_OF_BIRTH` date DEFAULT NULL,
  `GENDER` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `ONLINE` tinyint(1) DEFAULT NULL,
  `CREATED_AT` timestamp NULL DEFAULT NULL,
  `BANNED` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table userregistrationdb.user_account: ~10 rows (approximately)
INSERT INTO `user_account` (`ID`, `USERNAME`, `PASSWORD`, `FULLNAME`, `ADDRESS`, `DATE_OF_BIRTH`, `GENDER`, `EMAIL`, `ONLINE`, `CREATED_AT`, `BANNED`) VALUES
	(1, 'USER1', '123456', 'Nguyễn Mạnh Hùng', 'Địa chỉ 1', '1990-01-14', 'Male', NULL, 0, '2022-12-15 13:30:00', 0),
	(2, 'USER2', '123456', 'Nguyễn Mạnh Thủy', 'Địa chỉ 2', '1994-12-08', 'Female', NULL, 0, '2022-12-17 16:55:40', 0),
	(3, 'USER3', '123456', 'Nguyễn Mạnh Hà', 'Địa chỉ 3', '1998-09-03', 'Female', NULL, 0, '2022-12-19 08:25:00', 0),
	(4, 'USER4', '123456', 'Nguyễn Mạnh Thu', 'Địa chỉ 4', '1998-09-03', 'Female', NULL, 0, '2022-12-18 18:15:10', 0),
	(5, 'USER5', '123456', 'Nguyễn Mạnh Mai', 'Địa chỉ 5', '2003-03-26', 'Female', NULL, 0, '2022-12-20 11:12:00', 0),
	(6, 'USER6', '123456', 'Nguyễn Mạnh Vy', 'Địa chỉ 6', '2000-02-14', 'Female', NULL, 0, '2022-12-17 15:52:40', 0),
	(7, 'USER7', '123456', 'Nguyễn Mạnh Nam', 'Địa chỉ 7', '1991-05-06', 'Male', NULL, 0, '2022-12-22 13:39:28', 0),
	(8, 'USER8', '123456', 'Nguyễn Mạnh An', 'Địa chỉ 8', '1996-08-19', 'Male', NULL, 0, '2022-12-15 13:08:40', 0),
	(9, 'USER9', '123456', 'Nguyễn Mạnh Nguyệt', 'Địa chỉ 9', '2006-01-14', 'Male', NULL, 0, '2022-12-21 10:27:45', 0),
	(10, 'huyQuang', '123456', 'Huynh Quang', '123 Long Thanh', '2000-06-04', 'Female', 'huyquang222004@gmail.com', 0, '2024-12-17 09:09:10', NULL);

-- Dumping structure for table userregistrationdb.user_block
CREATE TABLE IF NOT EXISTS `user_block` (
  `ID` int NOT NULL,
  `BLOCK_ID` int NOT NULL,
  PRIMARY KEY (`ID`,`BLOCK_ID`) USING BTREE,
  KEY `BLOCK_ID` (`BLOCK_ID`) USING BTREE,
  CONSTRAINT `user_block_ibfk_1` FOREIGN KEY (`ID`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `user_block_ibfk_2` FOREIGN KEY (`BLOCK_ID`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table userregistrationdb.user_block: ~0 rows (approximately)

-- Dumping structure for table userregistrationdb.user_friend
CREATE TABLE IF NOT EXISTS `user_friend` (
  `ID` int NOT NULL,
  `FRIEND_ID` int NOT NULL,
  PRIMARY KEY (`ID`,`FRIEND_ID`),
  KEY `FRIEND_ID` (`FRIEND_ID`),
  CONSTRAINT `user_friend_ibfk_1` FOREIGN KEY (`ID`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `user_friend_ibfk_2` FOREIGN KEY (`FRIEND_ID`) REFERENCES `user_account` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table userregistrationdb.user_friend: ~8 rows (approximately)
INSERT INTO `user_friend` (`ID`, `FRIEND_ID`) VALUES
	(2, 1),
	(1, 2),
	(3, 2),
	(4, 2),
	(2, 3),
	(2, 4),
	(6, 4),
	(4, 6);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
