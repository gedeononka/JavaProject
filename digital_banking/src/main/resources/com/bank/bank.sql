-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 20, 2022 at 08:05 PM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bank`
--
CREATE DATABASE IF NOT EXISTS `bank` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `bank`;

-- --------------------------------------------------------

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
CREATE TABLE `clients` (
  `id_clt` bigint(30) NOT NULL,
  `nomPrenom` varchar(30) NOT NULL,
  `date_n` date DEFAULT NULL,
  `tel` varchar(30) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `adr` longtext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `clients`------------------------------------------

--
-- Table structure for table `comptes`
--

DROP TABLE IF EXISTS `comptes`;
CREATE TABLE `comptes` (
  `num_c` bigint(30) NOT NULL,
  `type_c` varchar(30) NOT NULL,
  `solde_c` double NOT NULL,
  `id_c` bigint(30) NOT NULL,
  `etat` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- Table `users`
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
                         `id_user` BIGINT(30) NOT NULL AUTO_INCREMENT,
                         `username` VARCHAR(50) NOT NULL UNIQUE,
                         `password` VARCHAR(255) NOT NULL,
                         `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `comptes`
--

-- --------------------------------------------------------

--
-- Table structure for table `operations`
--

DROP TABLE IF EXISTS `operations`;
CREATE TABLE `operations` (
  `id_op` int(11) NOT NULL,
  `nom_op` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `operations`
--

INSERT INTO `operations` (`id_op`, `nom_op`) VALUES(1, 'Depot');
INSERT INTO `operations` (`id_op`, `nom_op`) VALUES(2, 'Retrait');


-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
CREATE TABLE `transactions` (
  `num_op` bigint(20) NOT NULL,
  `type_op` int(11) NOT NULL,
  `date_op` datetime NOT NULL,
  `mnt_op` float NOT NULL,
  `num_c_em` bigint(20) NOT NULL,
  `num_c_ben` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactions`
--

-- Indexes for dumped tables
--

--
-- Indexes for table `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`id_clt`);

--
-- Indexes for table `comptes`
--
ALTER TABLE `comptes`
  ADD PRIMARY KEY (`num_c`),
  ADD KEY `id_c` (`id_c`);

--
-- Indexes for table `operations`
--
ALTER TABLE `operations`
  ADD PRIMARY KEY (`id_op`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`num_op`),
  ADD KEY `num_c_ben` (`num_c_ben`),
  ADD KEY `num_c_em` (`num_c_em`),
  ADD KEY `type_op` (`type_op`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `comptes`
--
ALTER TABLE `comptes`
  MODIFY `num_c` bigint(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=0;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `num_op` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=0;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`num_c_ben`) REFERENCES `comptes` (`num_c`),
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`num_c_em`) REFERENCES `comptes` (`num_c`),
  ADD CONSTRAINT `transactions_ibfk_3` FOREIGN KEY (`type_op`) REFERENCES `operations` (`id_op`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
