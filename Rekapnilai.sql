-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 21, 2023 at 03:36 PM
-- Server version: 10.4.25-MariaDB
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rekapnilai`
--

-- --------------------------------------------------------

--
-- Table structure for table `siswa`
--

CREATE TABLE `siswa` (
  `NIS` varchar(10) NOT NULL,
  `Nama` varchar(20) NOT NULL,
  `Indonesia` char(5) NOT NULL,
  `Inggris` char(5) NOT NULL,
  `IPA` char(5) NOT NULL,
  `Matematika` char(5) NOT NULL,
  `Note` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `siswa`
--

INSERT INTO `siswa` (`NIS`, `Nama`, `Indonesia`, `Inggris`, `IPA`, `Matematika`, `Note`) VALUES
('A11', 'Dimas', 'A', 'A', 'A', 'A', '2023-12-21'),
('A12', 'Ferdiansyah', 'A', 'A', 'A', 'A', '2023-12-21'),
('A13', 'Bella', 'A', 'A', 'A', 'A', '2023-12-21'),
('A14', 'Nasywa', 'A', 'A', 'A', 'A', '2023-12-21'),
('A15', 'Aminudin', 'A', 'AB', 'B', 'A', '2023-12-21'),
('A16', 'Deriansyah', 'B', 'AB', 'AB', 'A', '2023-12-21'),
('A17', 'Des Djaja', 'B', 'B', 'AB', 'B', '2023-12-21'),
('A18', 'Erpin', 'B', 'B', 'B', 'B', '2023-12-21'),
('A19', 'Ilham', 'C', 'BC', 'C', 'D', '2023-12-21'),
('A20', 'Levi', 'B', 'BC', 'AB', 'D', '2023-12-21'),
('A21', 'Ridwan', 'BC', 'B', 'A', 'AB', '2023-12-21');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`NIS`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
