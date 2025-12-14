-- This script creates the database and tables for the UPGRADED CarShowcase Hub project.
-- It includes users, showrooms, and links cars to showrooms.

-- Drop the database if it exists to start fresh, then create it.
DROP DATABASE IF EXISTS `car_showcase_db`;
CREATE DATABASE IF NOT EXISTS `car_showcase_db`;
USE `car_showcase_db`;

-- 1. Table: showrooms
-- Stores showroom information. Must be created before users and cars.
CREATE TABLE IF NOT EXISTS `showrooms` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `location` VARCHAR(255) NOT NULL,
    INDEX `idx_showroom_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Table: users
-- Stores login info for all users (Common Users and Owners).
-- Assumes ONE owner per showroom.
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL, -- Will store encrypted (BCrypt) password
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `role` ENUM('ROLE_USER', 'ROLE_OWNER') NOT NULL,
    `showroom_id` BIGINT NULL UNIQUE, -- An owner is linked to one showroom

    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`),

    FOREIGN KEY (`showroom_id`) REFERENCES `showrooms`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Table: cars (MODIFIED)
-- Now includes a foreign key to 'showrooms'
CREATE TABLE IF NOT EXISTS `cars` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `brand` VARCHAR(100) NOT NULL,
    `model` VARCHAR(100) NOT NULL,
    `year` INT NOT NULL,
    `price` DECIMAL(12, 2) NOT NULL,
    `description` TEXT,
    `image_url` VARCHAR(2048),
    `showroom_id` BIGINT NOT NULL,     -- The car now BELONGS to a showroom

    INDEX `idx_brand_model` (`brand`, `model`),
    INDEX `idx_cars_showroom` (`showroom_id`),
    FOREIGN KEY (`showroom_id`) REFERENCES `showrooms`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Table: car_specifications
CREATE TABLE IF NOT EXISTS `car_specifications` (
    `car_id` BIGINT NOT NULL,
    `specifications_key` VARCHAR(255) NOT NULL,
    `specifications` VARCHAR(255) NOT NULL,

    PRIMARY KEY (`car_id`, `specifications_key`),
    FOREIGN KEY (`car_id`) REFERENCES `cars`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Table: bookings (For Test Drives)
CREATE TABLE IF NOT EXISTS `bookings` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(20),
    `car_id` BIGINT NOT NULL,
    `date` DATETIME NOT NULL, -- The date of the appointment
    `booked_on` timestamp DEFAULT CURRENT_TIMESTAMP,

    INDEX `idx_booking_email` (`email`),
    INDEX `idx_booking_car_id` (`car_id`),
    FOREIGN KEY (`car_id`) REFERENCES `cars`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Table: pre_bookings (For Pre-Orders)
CREATE TABLE IF NOT EXISTS `pre_bookings` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `car_id` BIGINT NOT NULL,
    `payment_status` ENUM('PENDING', 'COMPLETED', 'FAILED') NOT NULL DEFAULT 'PENDING',
    `date` DATETIME NOT NULL, -- The date the pre-booking was made
    `booked_on` timestamp DEFAULT CURRENT_TIMESTAMP,

    INDEX `idx_prebooking_email` (`email`),
    INDEX `idx_prebooking_car_id` (`car_id`),
    FOREIGN KEY (`car_id`) REFERENCES `cars`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --- INSERT UPGRADED SAMPLE DATA ---

-- 1. Create Showrooms
INSERT INTO `showrooms` (name, location) VALUES
('Prestige Motors', 'Mumbai'),
('FutureAuto', 'Delhi');

-- 2. Create Users
-- The password for all users is "password123"
-- This is the BCRYPT hash for "password123": $2a$10$3JSHqed8KE0ONBYnT6AZ2u5s8kT3Umg.DcOR0zBUMr/Af4tHT1d.O
INSERT INTO `users` (username, password, email, role, showroom_id) VALUES
('owner_mumbai', '$2a$10$3JSHqed8KE0ONBYnT6AZ2u5s8kT3Umg.DcOR0zBUMr/Af4tHT1d.O', 'owner.mumbai@example.com', 'ROLE_OWNER', 1),
('owner_delhi', '$2a$10$3JSHqed8KE0ONBYnT6AZ2u5s8kT3Umg.DcOR0zBUMr/Af4tHT1d.O', 'owner.delhi@example.com', 'ROLE_OWNER', 2),
('common_user', '$2a$10$3JSHqed8KE0ONBYnT6AZ2u5s8kT3Umg.DcOR0zBUMr/Af4tHT1d.O', 'user@example.com', 'ROLE_USER', NULL);

-- 3. Insert Cars for Showroom 1 (Prestige Motors, Mumbai)
INSERT INTO `cars` (`brand`, `model`, `price`, `description`, `image_url`, `year`, `showroom_id`) VALUES
('Tesla', 'Model S', 74990.00, 'A full-size all-electric luxury sedan.', 'https://tse4.mm.bing.net/th/id/OIP.7Lm7xvLDp1HrTat8w__k0gHaFj?pid=Api&P=0&h=180', 2024, 1),
('Rivian', 'R1S', 78000.00, 'A powerful all-electric, three-row SUV.', 'https://cdn.motor1.com/images/mgl/xqKbwP/s3/2025-rivian-r1s-first-drive.jpg', 2024, 1),
('Tesla', 'Model 3', 38990.00, 'A best-selling all-electric sedan with a minimalist interior and long-range capability.', 'https://tse4.mm.bing.net/th/id/OIP.zFjy16pImfvgt-m9gEY7UgHaE7?pid=Api&P=0&h=180', 2024, 1),
('Audi', 'A4', 41900.00, 'A luxury compact sedan with a high-tech interior and standard all-wheel drive.', 'https://tse2.mm.bing.net/th/id/OIP.jNPnqaamO_Bi4pu9ZpbrYwHaE8?pid=Api&P=0&h=180', 2024, 1),
('BMW', '3 Series', 44500.00, 'The benchmark for sport sedans, blending performance with luxury.', 'https://tse4.mm.bing.net/th/id/OIP.hZKaJyK8amOYn10zTZ77BwHaE7?pid=Api&P=0&h=180', 2024, 1);

-- 4. Insert Cars for Showroom 2 (FutureAuto, Delhi)
INSERT INTO `cars` (`brand`, `model`, `price`, `description`, `image_url`, `year`, `showroom_id`) VALUES
('Ford', 'Mustang Mach-E', 46995.00, 'An electric compact crossover SUV.', 'https://tse1.mm.bing.net/th/id/OIP.xp-FExbRGhSvhw4pqAn_2wHaFj?pid=Api&P=0&h=180', 2024, 2),
('Toyota', 'Camry', 30100.00, 'A popular and reliable midsize sedan, now available primarily as a hybrid.', 'https://tse3.mm.bing.net/th/id/OIP.oxXJacg2zg3VOFJdiQ421gHaEK?pid=Api&P=0&h=180', 2025, 2),
('Honda', 'Accord', 29045.00, 'A top-rated midsize sedan known for its enjoyable road manners and fuel economy.', 'https://tse4.mm.bing.net/th/id/OIP.P93V-CW9Bdg-KWv4GJ9B6AHaE8?pid=Api&P=0&h=180', 2024, 2),
('Ford', 'Mustang', 33515.00, 'An iconic American muscle car with a new generation, offering V8 or EcoBoost power.', 'https://tse2.mm.bing.net/th/id/OIP.8CviILbj76S_ufwmcC-qZgHaEK?pid=Api&P=0&h=180', 2024, 2),
('Hyundai', 'Elantra', 22775.00, 'A tech-centric compact sedan offering great value and a bold design.', 'https://media.fastestlaps.com/hyundai-elantra-n.jpg', 2024, 2);

-- 5. Insert Specs (car_id corresponds to the IDs above: 1-10)
INSERT INTO `car_specifications` (`car_id`, `specifications_key`, `specifications`) VALUES
(1, 'Range', '405 miles'),
(1, '0-60 mph', '3.1 seconds'),
(1, 'Drivetrain', 'AWD'),
(2, 'Range', '321 miles'),
(2, '0-60 mph', '3.0 seconds'),
(2, 'Seating', '7'),
(3, 'Engine', 'Electric Motor (RWD)'),
(3, 'Range', '272 miles (EPA est.)'),
(3, '0-60 mph', '5.8 seconds'),
(4, 'Engine', '2.0L 4-Cylinder Turbo (40 TFSI)'),
(4, 'Horsepower', '201 hp'),
(4, 'Drivetrain', 'Quattro AWD'),
(5, 'Engine', '2.0L 4-Cylinder Turbo (330i)'),
(5, 'Horsepower', '255 hp'),
(5, 'Transmission', '8-speed automatic'),
(6, 'Range', '310 miles'),
(6, '0-60 mph', '5.1 seconds'),
(6, 'Cargo', '59.7 cu ft'),
(7, 'Engine', '2.5L 4-Cylinder Hybrid'),
(7, 'Horsepower', '225 hp (FWD)'),
(7, 'Transmission', 'ECVT'),
(8, 'Engine', '1.5L Turbo 4-Cylinder'),
(8, 'Horsepower', '192 hp'),
(8, 'Transmission', 'CVT'),
(9, 'Engine', '2.3L EcoBoost 4-Cylinder'),
(9, 'Horsepower', '315 hp'),
(9, 'Transmission', '10-speed automatic'),
(10, 'Engine', '2.0L 4-Cylinder'),
(10, 'Horsepower', '147 hp'),
(10, 'Transmission', 'Smartstream IVT');

-- Quick verification queries
SELECT * FROM cars;
SELECT * FROM users;
SELECT * FROM showrooms;
SELECT * FROM car_specifications;
select * from pre_bookings;
select * from bookings;

truncate table bookings;
truncate table pre_bookings;