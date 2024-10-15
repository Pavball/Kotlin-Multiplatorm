-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3307
-- Generation Time: Sep 12, 2024 at 08:34 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `popitdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

CREATE TABLE `address` (
  `addressId` int(11) NOT NULL,
  `street_no` varchar(100) NOT NULL,
  `userId` int(11) DEFAULT NULL,
  `locationId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `address`
--

INSERT INTO `address` (`addressId`, `street_no`, `userId`, `locationId`) VALUES
(6, 'Bribirska 3', 9, 6),
(7, 'Bribirska 3', 10, 7);

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `adminId` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `phone` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`adminId`, `name`, `surname`, `email`, `password`, `phone`) VALUES
(3, 'Testni', 'Test', '1', '1', '095333122');

-- --------------------------------------------------------

--
-- Table structure for table `country`
--

CREATE TABLE `country` (
  `countryId` int(11) NOT NULL,
  `countryName` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `country`
--

INSERT INTO `country` (`countryId`, `countryName`) VALUES
(1, 'Afghanistan'),
(2, 'Aland Islands'),
(3, 'Albania'),
(4, 'Algeria'),
(5, 'American Samoa'),
(6, 'Andorra'),
(7, 'Angola'),
(8, 'Anguilla'),
(9, 'Antarctica'),
(10, 'Antigua and Barbuda'),
(11, 'Argentina'),
(12, 'Armenia'),
(13, 'Aruba'),
(14, 'Australia'),
(15, 'Austria'),
(16, 'Azerbaijan'),
(17, 'Bahamas'),
(18, 'Bahrain'),
(19, 'Bangladesh'),
(20, 'Barbados'),
(21, 'Belarus'),
(22, 'Belgium'),
(23, 'Belize'),
(24, 'Benin'),
(25, 'Bermuda'),
(26, 'Bhutan'),
(27, 'Bolivia'),
(28, 'Bonaire'),
(29, 'Bosnia and Herzegovina'),
(30, 'Botswana'),
(31, 'Bouvet Island'),
(32, 'Brazil'),
(33, 'Brunei Darussalam'),
(34, 'Bulgaria'),
(35, 'Burkina Faso'),
(36, 'Burundi'),
(37, 'Cambodia'),
(38, 'Cameroon'),
(39, 'Canada'),
(40, 'Cape Verde'),
(41, 'Cayman Islands'),
(42, 'Central African Republic'),
(43, 'Chad'),
(44, 'Chile'),
(45, 'China'),
(46, 'Christmas Island'),
(47, 'Cocos (Keeling) Islands'),
(48, 'Colombia'),
(49, 'Comoros'),
(50, 'Congo'),
(51, 'Cook Islands'),
(52, 'Costa Rica'),
(53, 'Cote D\'Ivoire'),
(54, 'Croatia'),
(55, 'Cuba'),
(56, 'Curacao'),
(57, 'Cyprus'),
(58, 'Czech Republic'),
(59, 'Denmark'),
(60, 'Djibouti'),
(61, 'Dominica'),
(62, 'Dominican Republic'),
(63, 'Ecuador'),
(64, 'Egypt'),
(65, 'El Salvador'),
(66, 'Equatorial Guinea'),
(67, 'Eritrea'),
(68, 'Estonia'),
(69, 'Ethiopia'),
(70, 'Falkland Islands'),
(71, 'Faroe Islands'),
(72, 'Fiji'),
(73, 'Finland'),
(74, 'France'),
(75, 'Gabon'),
(76, 'Gambia'),
(77, 'Georgia'),
(78, 'Germany'),
(79, 'Ghana'),
(80, 'Gibraltar'),
(81, 'Greece'),
(82, 'Greenland'),
(83, 'Grenada'),
(84, 'Guadeloupe'),
(85, 'Guam'),
(86, 'Guatemala'),
(87, 'Guernsey'),
(88, 'Guinea'),
(89, 'Guinea-Bissau'),
(90, 'Guyana'),
(91, 'Haiti'),
(92, 'Holy See (Vatican City State)'),
(93, 'Honduras'),
(94, 'Hong Kong'),
(95, 'Hungary'),
(96, 'Iceland'),
(97, 'India'),
(98, 'Indonesia'),
(99, 'Iran'),
(100, 'Iraq'),
(101, 'Ireland'),
(102, 'Isle of Man'),
(103, 'Israel'),
(104, 'Italy'),
(105, 'Jamaica'),
(106, 'Japan'),
(107, 'Jersey'),
(108, 'Jordan'),
(109, 'Kazakhstan'),
(110, 'Kenya'),
(111, 'Kiribati'),
(112, 'Korea South'),
(113, 'Korea North'),
(114, 'Kosovo'),
(115, 'Kuwait'),
(116, 'Kyrgyzstan'),
(117, 'Laos'),
(118, 'Latvia'),
(119, 'Lebanon'),
(120, 'Lesotho'),
(121, 'Liberia'),
(122, 'Libyan Arab Jamahiriya'),
(123, 'Liechtenstein'),
(124, 'Lithuania'),
(125, 'Luxembourg'),
(126, 'Macao'),
(127, 'Macedonia'),
(128, 'Madagascar'),
(129, 'Malawi'),
(130, 'Malaysia'),
(131, 'Maldives'),
(132, 'Mali'),
(133, 'Malta'),
(134, 'Marshall Islands'),
(135, 'Martinique'),
(136, 'Mauritania'),
(137, 'Mauritius'),
(138, 'Mayotte'),
(139, 'Mexico'),
(140, 'Micronesia'),
(141, 'Moldova'),
(142, 'Monaco'),
(143, 'Mongolia'),
(144, 'Montenegro'),
(145, 'Montserrat'),
(146, 'Morocco'),
(147, 'Mozambique'),
(148, 'Myanmar'),
(149, 'Namibia'),
(150, 'Nauru'),
(151, 'Nepal'),
(152, 'Netherlands'),
(153, 'Netherlands Antilles'),
(154, 'New Caledonia'),
(155, 'New Zealand'),
(156, 'Nicaragua'),
(157, 'Niger'),
(158, 'Nigeria'),
(159, 'Niue'),
(160, 'Norfolk Island'),
(161, 'Northern Mariana Islands'),
(162, 'Norway'),
(163, 'Oman'),
(164, 'Pakistan'),
(165, 'Palau'),
(166, 'Palestine'),
(167, 'Panama'),
(168, 'Papua New Guinea'),
(169, 'Paraguay'),
(170, 'Peru'),
(171, 'Philippines'),
(172, 'Pitcairn'),
(173, 'Poland'),
(174, 'Portugal'),
(175, 'Puerto Rico'),
(176, 'Qatar'),
(177, 'Reunion'),
(178, 'Romania'),
(179, 'Russia'),
(180, 'Rwanda'),
(181, 'Saint Barthelemy'),
(182, 'Saint Helena'),
(183, 'Saint Kitts and Nevis'),
(184, 'Saint Lucia'),
(185, 'Saint Martin'),
(186, 'Saint Pierre'),
(187, 'Saint Vincent'),
(188, 'Samoa'),
(189, 'San Marino'),
(190, 'Sao Tome and Principe'),
(191, 'Saudi Arabia'),
(192, 'Senegal'),
(193, 'Serbia'),
(194, 'Montenegro'),
(195, 'Seychelles'),
(196, 'Sierra Leone'),
(197, 'Singapore'),
(198, 'Sint Maarten'),
(199, 'Slovakia'),
(200, 'Slovenia'),
(201, 'Solomon Islands'),
(202, 'Somalia'),
(203, 'South Africa'),
(204, 'South Georgia'),
(205, 'South Sudan'),
(206, 'Spain'),
(207, 'Sri Lanka'),
(208, 'Sudan'),
(209, 'Suriname'),
(210, 'Svalbard and Jan Mayen'),
(211, 'Swaziland'),
(212, 'Sweden'),
(213, 'Switzerland'),
(214, 'Syrian Arab Republic'),
(215, 'Taiwan'),
(216, 'Tajikistan'),
(217, 'Tanzania'),
(218, 'Thailand'),
(219, 'Timor-Leste'),
(220, 'Togo'),
(221, 'Tokelau'),
(222, 'Tonga'),
(223, 'Trinidad and Tobago'),
(224, 'Tunisia'),
(225, 'Turkey'),
(226, 'Turkmenistan'),
(227, 'Turks and Caicos Islands'),
(228, 'Tuvalu'),
(229, 'Uganda'),
(230, 'Ukraine'),
(231, 'United Arab Emirates'),
(232, 'United Kingdom'),
(233, 'United States'),
(234, 'Uruguay'),
(235, 'Uzbekistan'),
(236, 'Vanuatu'),
(237, 'Venezuela'),
(238, 'Viet Nam'),
(239, 'Virgin Islands, British'),
(240, 'Virgin Islands, U.s.'),
(241, 'Wallis and Futuna'),
(242, 'Western Sahara'),
(243, 'Yemen'),
(244, 'Zambia'),
(245, 'Zimbabwe');

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `employeeId` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `phone` varchar(45) NOT NULL,
  `password` varchar(300) NOT NULL,
  `workPositionId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`employeeId`, `name`, `surname`, `email`, `phone`, `password`, `workPositionId`) VALUES
(8, 'Ivan', 'Matkovic', 'imatkovic@popit.hr', '095431248', '1', 1),
(9, 'Zaposlenik', 'Zaposlenice', '1', '413414314', '1', 3);

-- --------------------------------------------------------

--
-- Table structure for table `employeeservice`
--

CREATE TABLE `employeeservice` (
  `employeeServiceId` int(11) NOT NULL,
  `serviceId` int(11) DEFAULT NULL,
  `employeeId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employeeservice`
--

INSERT INTO `employeeservice` (`employeeServiceId`, `serviceId`, `employeeId`) VALUES
(1, 2, 9),
(2, 3, 8);

-- --------------------------------------------------------

--
-- Table structure for table `location`
--

CREATE TABLE `location` (
  `locationId` int(11) NOT NULL,
  `city` varchar(100) NOT NULL,
  `postalCode` varchar(45) NOT NULL,
  `countryId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `location`
--

INSERT INTO `location` (`locationId`, `city`, `postalCode`, `countryId`) VALUES
(6, 'Zagreb', '10000', 54),
(7, 'Zagreb', '10000', 54);

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `orderId` int(11) NOT NULL,
  `orderDate` date NOT NULL,
  `orderStatus` varchar(45) NOT NULL,
  `totalAmount` varchar(45) NOT NULL,
  `userId` int(11) DEFAULT NULL,
  `createdByAdminId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order`
--

INSERT INTO `order` (`orderId`, `orderDate`, `orderStatus`, `totalAmount`, `userId`, `createdByAdminId`) VALUES
(10, '2024-08-11', 'Completed', '230', 9, NULL),
(12, '2024-08-14', 'Completed', '160', 9, NULL),
(13, '2024-08-21', 'U obradi', '70', 9, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `orderdetail`
--

CREATE TABLE `orderdetail` (
  `orderDetailId` int(11) NOT NULL,
  `orderQuantity` int(11) NOT NULL,
  `unitPrice` int(11) NOT NULL,
  `productId` int(11) DEFAULT NULL,
  `orderId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orderdetail`
--

INSERT INTO `orderdetail` (`orderDetailId`, `orderQuantity`, `unitPrice`, `productId`, `orderId`) VALUES
(9, 1, 70, 4, 10),
(10, 1, 160, 6, 10),
(11, 1, 160, 6, 12),
(12, 1, 70, 4, 13);

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `productId` int(11) NOT NULL,
  `productName` varchar(45) NOT NULL,
  `productDesc` varchar(200) DEFAULT NULL,
  `productPrice` double(16,2) DEFAULT NULL,
  `productStock` int(11) NOT NULL,
  `productImage` varchar(256) DEFAULT NULL,
  `categoryId` int(11) DEFAULT NULL,
  `createdByAdminId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`productId`, `productName`, `productDesc`, `productPrice`, `productStock`, `productImage`, `categoryId`, `createdByAdminId`) VALUES
(1, 'Nvidia RTX 4090', 'Nvidia RTX 4090 ROG Graphics Card', 2000.00, 0, 'https://www.bitworks.io/wp-content/uploads/2024/03/69697ed27af1f07588e64b418c2af2fc.jpg', 1, NULL),
(2, 'Nvidia RTX 4080', 'Nvidia RTX 4080 ROG Graphics Card', 1800.00, 0, 'https://m.media-amazon.com/images/I/71m9FJCubXL.jpg', 1, NULL),
(3, 'Nvidia RTX 4070', 'Nvidia RTX 4070 ROG Graphics Card', 700.00, 0, 'https://m.media-amazon.com/images/I/51pXI8c5VkL._AC_UF894,1000_QL80_.jpg', 1, NULL),
(4, 'RX 580', 'AMD Radeon RX 580 Graphics Card', 70.00, 0, 'https://static.gigabyte.com/StaticFile/Image/Global/c192a8ecacac1badc7ca5389d41cef36/Product/24296/Png', 1, NULL),
(5, 'Intel I9-14900K', 'Procesor Intel Core i9-14900K (24C/32T, up to 6.0GHz, 36MB, LGA1700)', 699.00, 1, 'https://www.links.hr/content/images/thumbs/019/0190136_procesor-intel-core-i9-14900k-box-s-1700-3-2ghz-36mb-cache-bez-hladnjaka-010501054.jpg', 2, NULL),
(6, 'Intel I5-12400F', 'Procesor Intel Core i5-12400F (6C/12T, up to 4.4GHz, 18MB, LGA1700)', 180.00, 5, 'https://www.instar-informatika.hr/slike/velike/procesor-intel-core-i5-12400f-43ghz-12mb-inp-000220_1.jpg', 2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `productcategory`
--

CREATE TABLE `productcategory` (
  `categoryId` int(11) NOT NULL,
  `categoryName` varchar(45) NOT NULL,
  `categoryDesc` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `productcategory`
--

INSERT INTO `productcategory` (`categoryId`, `categoryName`, `categoryDesc`) VALUES
(1, 'Graphics Card', 'A graphics card is a computer expansion card that generates a feed of graphics output to a display device such as a monitor.'),
(2, 'CPU', 'A central processing unit (CPU), also called a central processor, main processor, or just processor, is the most important processor in a given computer.');

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

CREATE TABLE `service` (
  `serviceId` int(11) NOT NULL,
  `serviceName` varchar(45) NOT NULL,
  `serviceDesc` varchar(200) DEFAULT NULL,
  `servicePrice` int(10) NOT NULL,
  `serviceTypeId` int(11) DEFAULT NULL,
  `createdByAdminId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `service`
--

INSERT INTO `service` (`serviceId`, `serviceName`, `serviceDesc`, `servicePrice`, `serviceTypeId`, `createdByAdminId`) VALUES
(2, 'Pregled uređaja', 'Pregledavamo Vam uređaj kako bi pronašli potencijalni problem!', 25, 1, NULL),
(3, 'Testni tip 2', 'Ovo je testni tip za neki test, dodavanje nekog servisa u web shop.', 999, 2, NULL),
(4, 'Testni tip 3', 'Ovo je testni tip 3 za testiranje grid-a u servisima', 150, 3, NULL),
(5, 'Testni tip 4', 'Ovo je testni tip 4 za testiranje grid-a u servisima', 10, 4, NULL),
(7, 'Test', 'test', 20, 2, 3);

-- --------------------------------------------------------

--
-- Table structure for table `servicerequest`
--

CREATE TABLE `servicerequest` (
  `requestId` int(11) NOT NULL,
  `requestDate` date NOT NULL,
  `requestStatus` varchar(45) NOT NULL,
  `problemDesc` varchar(200) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `createdByAdminId` int(11) DEFAULT NULL,
  `serviceId` int(11) DEFAULT NULL,
  `employeeId` int(11) DEFAULT NULL,
  `communication` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `servicerequest`
--

INSERT INTO `servicerequest` (`requestId`, `requestDate`, `requestStatus`, `problemDesc`, `userId`, `createdByAdminId`, `serviceId`, `employeeId`, `communication`) VALUES
(2, '2024-08-07', 'Completed', 'Dobar dan, moj uređaj ne radi, hvala doviđenja!', 9, NULL, 2, 9, '\n3: Ovo je test\nMarco: Ovo je opet test\n3: Ovo  je test broj 3\n3: Test poruka\nTestni: Test Poruka\nMarco: da\nMarco: \nMarco: da\nMarco: dada\nMarco: dadadad\nMarco: dadada\nMarco: 1\nMarco: \nMarco: 32\nMarco: 3232323\nTestni: est\nTestni: Dobar dan\nTestni: test\nTestni: test\nTestni: 123\nTestni: 123\nTestni: test\nTestni: poruka\nTestni: ovo je zadnja poruka\nTestni: Je li moguce\nMarco: Test\nTestni: Ovo je poruka\nTestni: Moje ime je\nTestni: Test poruka\nAntonela: test\nZaposlenik: test\nZaposlenik: test\nZaposlenik: test\nZaposlenik: Hvala Vam na javljanju! Service je zatvoren!'),
(3, '2024-08-07', 'Pending', 'test 123 ovo je testna poruka', 9, NULL, 3, 8, 'Opis problema: test 123 ovo je testna poruka\nTestni: Je li\nFilip : test'),
(4, '2024-08-19', 'Pending', 'ovo je test', 9, NULL, 4, NULL, 'Opis problema: ovo je test'),
(5, '2024-08-19', 'Pending', 'ovo je isto test', 9, NULL, 3, 8, 'Opis problema: ovo je isto test'),
(6, '2024-08-21', 'Pending', 'Uređaj mi je u kvaru!', 9, NULL, 2, 9, 'Opis problema: Uređaj mi je u kvaru!\nZaposlenik: Popraviti ćemo to!');

-- --------------------------------------------------------

--
-- Table structure for table `servicetype`
--

CREATE TABLE `servicetype` (
  `typeId` int(11) NOT NULL,
  `typeName` varchar(45) NOT NULL,
  `typeDesc` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `servicetype`
--

INSERT INTO `servicetype` (`typeId`, `typeName`, `typeDesc`) VALUES
(1, 'Pregled uređaja', 'Detaljni pregled uređaja'),
(2, 'Test tip 2', 'Ovo je testni tip za opis nekog servisnog tipa.'),
(3, 'Test tip 3', 'Testni tip 3'),
(4, 'Test tip 4', 'Testni tip 4');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userId` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `pass` varchar(300) NOT NULL,
  `phoneNumber` varchar(45) NOT NULL,
  `createdByAdminId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userId`, `name`, `surname`, `email`, `pass`, `phoneNumber`, `createdByAdminId`) VALUES
(9, 'Marco', 'Matijević', 'marcomat090@gmail.com', '$2y$10$Pk7qb8ngxMCVSbIwf77one8v57oc9opYbda6dMjXCK.W5ZMRJK3Yu', '0953914931', NULL),
(10, 'Marco', 'Matijević', 'marcomat090@gmail.com', '$2y$10$gXyWMi96zPgx0ff/r4kYjOKyge4sj8I/DJ4sj76QMZS5ljKTLiWQy', '095-3914-931', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `workposition`
--

CREATE TABLE `workposition` (
  `positionId` int(11) NOT NULL,
  `positionName` varchar(45) NOT NULL,
  `positionDesc` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `workposition`
--

INSERT INTO `workposition` (`positionId`, `positionName`, `positionDesc`) VALUES
(1, 'IT Support Technician', 'Provides technical support for software and hardware issues, resolves user inquiries, and manages helpdesk tickets.'),
(2, 'Network Administrator', 'Responsible for managing and maintaining the company’s network infrastructure, including routers, switches, and firewalls.'),
(3, 'System Administrator', 'Manages and configures server systems, ensures system stability, security, and backup procedures.'),
(4, 'Field Service Engineer', 'Performs on-site troubleshooting, installation, and maintenance of IT equipment at client locations.'),
(5, 'IT Consultant', 'Provides strategic IT advice to clients, helps with technology planning, and oversees IT projects.'),
(6, 'Cybersecurity Specialist', 'Focuses on protecting the organization’s networks and systems from cyber threats and ensures compliance with security protocols.'),
(7, 'Database Administrator', 'Manages and optimizes database systems, ensures data integrity, and handles data backup and recovery processes.'),
(8, 'Technical Account Manager', 'Works closely with clients to understand their IT needs, ensures successful implementation of IT solutions, and maintains client satisfaction.');

-- --------------------------------------------------------

--
-- Table structure for table `workposition_has_employee`
--

CREATE TABLE `workposition_has_employee` (
  `positionId` int(11) NOT NULL,
  `employeeId` int(11) NOT NULL,
  `dateFrom` date DEFAULT NULL,
  `dateUntil` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `address`
--
ALTER TABLE `address`
  ADD PRIMARY KEY (`addressId`),
  ADD KEY `userId` (`userId`),
  ADD KEY `locationId` (`locationId`);

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`adminId`);

--
-- Indexes for table `country`
--
ALTER TABLE `country`
  ADD PRIMARY KEY (`countryId`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`employeeId`),
  ADD KEY `workPositionId` (`workPositionId`);

--
-- Indexes for table `employeeservice`
--
ALTER TABLE `employeeservice`
  ADD PRIMARY KEY (`employeeServiceId`),
  ADD KEY `serviceId` (`serviceId`),
  ADD KEY `employeeId` (`employeeId`);

--
-- Indexes for table `location`
--
ALTER TABLE `location`
  ADD PRIMARY KEY (`locationId`),
  ADD KEY `countryId` (`countryId`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`orderId`),
  ADD KEY `userId` (`userId`),
  ADD KEY `createdByAdminId` (`createdByAdminId`);

--
-- Indexes for table `orderdetail`
--
ALTER TABLE `orderdetail`
  ADD PRIMARY KEY (`orderDetailId`),
  ADD KEY `productId` (`productId`),
  ADD KEY `orderId` (`orderId`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`productId`),
  ADD KEY `categoryId` (`categoryId`),
  ADD KEY `createdByAdminId` (`createdByAdminId`);

--
-- Indexes for table `productcategory`
--
ALTER TABLE `productcategory`
  ADD PRIMARY KEY (`categoryId`);

--
-- Indexes for table `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`serviceId`),
  ADD KEY `serviceTypeId` (`serviceTypeId`),
  ADD KEY `createdByAdminId` (`createdByAdminId`);

--
-- Indexes for table `servicerequest`
--
ALTER TABLE `servicerequest`
  ADD PRIMARY KEY (`requestId`),
  ADD KEY `userId` (`userId`),
  ADD KEY `createdByAdminId` (`createdByAdminId`),
  ADD KEY `serviceId` (`serviceId`),
  ADD KEY `employeeId` (`employeeId`);

--
-- Indexes for table `servicetype`
--
ALTER TABLE `servicetype`
  ADD PRIMARY KEY (`typeId`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userId`),
  ADD KEY `createdByAdminId` (`createdByAdminId`);

--
-- Indexes for table `workposition`
--
ALTER TABLE `workposition`
  ADD PRIMARY KEY (`positionId`);

--
-- Indexes for table `workposition_has_employee`
--
ALTER TABLE `workposition_has_employee`
  ADD PRIMARY KEY (`positionId`,`employeeId`),
  ADD KEY `employeeId` (`employeeId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `address`
--
ALTER TABLE `address`
  MODIFY `addressId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `adminId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `country`
--
ALTER TABLE `country`
  MODIFY `countryId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=246;

--
-- AUTO_INCREMENT for table `employee`
--
ALTER TABLE `employee`
  MODIFY `employeeId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `employeeservice`
--
ALTER TABLE `employeeservice`
  MODIFY `employeeServiceId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `location`
--
ALTER TABLE `location`
  MODIFY `locationId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `orderId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `orderdetail`
--
ALTER TABLE `orderdetail`
  MODIFY `orderDetailId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `productId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `productcategory`
--
ALTER TABLE `productcategory`
  MODIFY `categoryId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `service`
--
ALTER TABLE `service`
  MODIFY `serviceId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `servicerequest`
--
ALTER TABLE `servicerequest`
  MODIFY `requestId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `servicetype`
--
ALTER TABLE `servicetype`
  MODIFY `typeId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `workposition`
--
ALTER TABLE `workposition`
  MODIFY `positionId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `address`
--
ALTER TABLE `address`
  ADD CONSTRAINT `address_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`),
  ADD CONSTRAINT `address_ibfk_2` FOREIGN KEY (`locationId`) REFERENCES `location` (`locationId`);

--
-- Constraints for table `employee`
--
ALTER TABLE `employee`
  ADD CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`workPositionId`) REFERENCES `workposition` (`positionId`);

--
-- Constraints for table `employeeservice`
--
ALTER TABLE `employeeservice`
  ADD CONSTRAINT `employeeservice_ibfk_1` FOREIGN KEY (`serviceId`) REFERENCES `service` (`serviceId`),
  ADD CONSTRAINT `employeeservice_ibfk_2` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`employeeId`);

--
-- Constraints for table `location`
--
ALTER TABLE `location`
  ADD CONSTRAINT `location_ibfk_1` FOREIGN KEY (`countryId`) REFERENCES `country` (`countryId`);

--
-- Constraints for table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`),
  ADD CONSTRAINT `order_ibfk_2` FOREIGN KEY (`createdByAdminId`) REFERENCES `admin` (`adminId`);

--
-- Constraints for table `orderdetail`
--
ALTER TABLE `orderdetail`
  ADD CONSTRAINT `orderdetail_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `product` (`productId`),
  ADD CONSTRAINT `orderdetail_ibfk_2` FOREIGN KEY (`orderId`) REFERENCES `order` (`orderId`);

--
-- Constraints for table `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `product_ibfk_1` FOREIGN KEY (`categoryId`) REFERENCES `productcategory` (`categoryId`),
  ADD CONSTRAINT `product_ibfk_2` FOREIGN KEY (`createdByAdminId`) REFERENCES `admin` (`adminId`);

--
-- Constraints for table `service`
--
ALTER TABLE `service`
  ADD CONSTRAINT `service_ibfk_1` FOREIGN KEY (`serviceTypeId`) REFERENCES `servicetype` (`typeId`),
  ADD CONSTRAINT `service_ibfk_2` FOREIGN KEY (`createdByAdminId`) REFERENCES `admin` (`adminId`);

--
-- Constraints for table `servicerequest`
--
ALTER TABLE `servicerequest`
  ADD CONSTRAINT `servicerequest_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`),
  ADD CONSTRAINT `servicerequest_ibfk_2` FOREIGN KEY (`createdByAdminId`) REFERENCES `admin` (`adminId`),
  ADD CONSTRAINT `servicerequest_ibfk_3` FOREIGN KEY (`serviceId`) REFERENCES `service` (`serviceId`),
  ADD CONSTRAINT `servicerequest_ibfk_4` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`employeeId`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`createdByAdminId`) REFERENCES `admin` (`adminId`);

--
-- Constraints for table `workposition_has_employee`
--
ALTER TABLE `workposition_has_employee`
  ADD CONSTRAINT `workposition_has_employee_ibfk_1` FOREIGN KEY (`positionId`) REFERENCES `workposition` (`positionId`),
  ADD CONSTRAINT `workposition_has_employee_ibfk_2` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`employeeId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
