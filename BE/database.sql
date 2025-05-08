-- Tạo cơ sở dữ liệu SteamZoneMekongDatabase
CREATE DATABASE SteamZoneMekongDatabase;
-- Sử dụng cơ sở dữ liệu SteamZoneMekongDatabase
USE SteamZoneMekongDatabase;

-- Tạo bảng Departments (Phòng ban)
CREATE TABLE Departments (
    DepartmentID INT PRIMARY KEY AUTO_INCREMENT,
    DepartmentName NVARCHAR(100) NOT NULL,
    IsActive BIT NOT NULL
);

-- Tạo bảng Positions (Chức vụ)
CREATE TABLE Positions (
    PositionID INT PRIMARY KEY AUTO_INCREMENT,
    PositionName NVARCHAR(100) NOT NULL,
    DepartmentID INT NOT NULL, -- Thêm cột này
    FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID)
);

-- Tạo bảng Accounts
CREATE TABLE Accounts(
	AccountID INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(20) UNIQUE, 
    Email VARCHAR(50) UNIQUE,
    Password TEXT
);

-- Tạo bảng Employees (Nhân viên)
CREATE TABLE Employees (
    EmployeeID INT PRIMARY KEY AUTO_INCREMENT,
    PositionID INT NOT NULL,
    FullName NVARCHAR(100) NOT NULL,
    Gender BIT NOT NULL,
    BirthDate DATE NOT NULL,
    Email VARCHAR(255) NOT NULL UNIQUE,
    PhoneNumber VARCHAR(12) NOT NULL UNIQUE,
    IDCardNumber VARCHAR(12) NOT NULL UNIQUE,
    Address NVARCHAR(255),
    AvatarURL LONGTEXT,
    IsActive BIT NOT NULL,
    FOREIGN KEY (PositionID) REFERENCES Positions(PositionID)
);

-- Tạo bảng LeaveRecords (Nghỉ phép)
CREATE TABLE LeaveRecords (
    LeaveRecordID INT PRIMARY KEY AUTO_INCREMENT, -- Đổi tên từ AttendanceID
    EmployeeID INT NOT NULL,
    Quantity FLOAT NOT NULL,
    FromDate DATETIME NOT NULL,
    ToDate DATETIME NOT NULL,
    IsAccept BIT DEFAULT 1 NOT NULL, 
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID),
    UNIQUE (EmployeeID, FromDate, ToDate)
);

-- Tạo bảng Overtimes (Lịch làm thêm giờ)
CREATE TABLE Overtimes (
    OvertimeID INT PRIMARY KEY AUTO_INCREMENT,
    OvertimeDate DATE NOT NULL,
    StartTime TIME NOT NULL,
    EndTime TIME NOT NULL,
    HourlyRate INT NOT NULL
);

-- Tạo bảng OvertimeRecords (Bản ghi làm thêm giờ của nhân viên)
CREATE TABLE OvertimeRecords (
    RecordID INT PRIMARY KEY AUTO_INCREMENT,
    EmployeeID INT NOT NULL,
    OvertimeID INT NOT NULL,
    FOREIGN KEY (OvertimeID) REFERENCES Overtimes(OvertimeID),
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID),
    UNIQUE (EmployeeID, OvertimeID) -- Đổi ScheduleID thành OvertimeID
);

-- Tạo bảng Contracts (Hợp đồng lao động)
CREATE TABLE Contracts (
    ContractID INT PRIMARY KEY AUTO_INCREMENT,
    StartDate DATETIME NOT NULL,
    EndDate DATETIME NOT NULL,
    SigningDate DATETIME NOT NULL,
    IsActive BIT NOT NULL,
    Salary BIGINT NOT NULL,
    EmployeeID INT NOT NULL,
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);

-- Tạo bảng Allowances (Phụ cấp)
CREATE TABLE Allowances (
    AllowanceID INT PRIMARY KEY AUTO_INCREMENT,
    AllowanceName NVARCHAR(100) NOT NULL,
    Amount BIGINT NOT NULL
);

-- Tạo bảng EmployeeAllowances (Phụ cấp cho nhân viên)
CREATE TABLE EmployeeAllowances (
    EmployeeAllowanceID INT PRIMARY KEY AUTO_INCREMENT,
    AllowanceID INT,
    EmployeeID INT,
    StartDate DATETIME NOT NULL,
    EndDate DATETIME NOT NULL,
    Amount BIGINT,
    FOREIGN KEY (AllowanceID) REFERENCES Allowances(AllowanceID),
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID),
    UNIQUE (AllowanceID, EmployeeID)
);

-- Tạo bảng EmployeeFingers
CREATE TABLE EmployeeFingers(
	FingerID INT AUTO_INCREMENT PRIMARY KEY,
    FingerCode TEXT,
    EmployeeID INT,
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID)
);

-- Thêm dữ liệu mẫu cho bảng Departments (Phòng ban)
INSERT INTO Departments (DepartmentName, IsActive)
VALUES 
('Công nghệ thông tin', 1);

-- Thêm dữ liệu mẫu cho bảng Positions (Chức vụ)
INSERT INTO Positions (PositionName, DepartmentID)
VALUES 
('Thực tập sinh', 1);


-- Thêm dữ liệu mẫu cho bảng Accounts
INSERT INTO Accounts (Username, Email, Password)
VALUES
('admin', 'loan@steamzonemekong.com', 'rmIdSUWjEDqLB2LEaSmGKvxNJYtoy4Gz'),
('nhiemlt', 'lytinhnhiem@gmail.com', 'rmIdSUWjEDqLB2LEaSmGKvxNJYtoy4Gz');

-- Thêm dữ liệu mẫu cho bảng Employees (Nhân viên)
INSERT INTO Employees (PositionID, FullName, Gender, BirthDate, Email, PhoneNumber, IDCardNumber, Address, AvatarURL, IsActive)
VALUES
(1, 'Lý Tính Nhiệm', 1, '2004-02-08', 'lytinhniem@gmail.com', '0787833283', '123456789012', 'FPT Polytechnic Cần Thơ', '/images/lytinhniem.jpg', 1),
(1, 'Nguyễn Minh Thuận', 1, '2004-02-17', 'nguyenminhthuan@gmail.com', '0838644462', '987654321012', 'FPT Polytechnic Cần Thơ', '/images/nguyenminhthuan.jpg', 1),
(1, 'Lê Minh Khôi', 1, '2004-10-02', 'leminhkhoi@gmail.com', '0357149033', '112233445566', 'FPT Polytechnic Cần Thơ', '/images/leminhkhoi.jpg', 1),
(1, 'Võ Thị Thảo Nguyên', 0, '2004-03-10', 'vothithaonguyen@gmail.com', '0375613426', '223344556677', 'FPT Polytechnic Cần Thơ', '/images/vothithaonguyen.jpg', 1),
(1, 'Nguyễn Hồ Vũ', 1, '2004-05-25', 'nguyenhovu@gmail.com', '0764900159', '334455667788', 'FPT Polytechnic Cần Thơ', '/images/nguyenhovu.jpg', 1),
(1, 'Lưu Thành Nghĩa', 1, '2004-11-04', 'luthanhnghia@gmail.com', '0342400497', '445566778899', 'FPT Polytechnic Cần Thơ', '/images/luthanhnghia.jpg', 1);


-- Thêm dữ liệu mẫu cho bảng LeaveRecords (Nghỉ phép)
INSERT INTO LeaveRecords (EmployeeID, Quantity, FromDate, ToDate)
VALUES
(5, 1, '2024-10-05 08:00:00', '2024-10-05 17:00:00'),
(6, 1, '2024-10-05 08:00:00', '2024-10-05 17:00:00'),
(2, 1, '2024-10-08 08:00:00', '2024-10-08 17:00:00'),
(2, 0.5, '2024-10-17 08:00:00', '2024-10-17 12:00:00'),
(5, 0.5, '2024-10-28 08:00:00', '2024-10-17 12:00:00'),
(3, 1, '2024-10-28 08:00:00', '2024-10-28 17:00:00'),
(6, 0.5, '2024-11-01 13:00:00', '2024-11-01 17:00:00'),
(1, 0.5, '2024-11-04 08:00:00', '2024-11-04 12:00:00'),
(2, 0.5, '2024-11-04 08:00:00', '2024-11-04 12:00:00'),
(2, 0.5, '2024-11-08 13:00:00', '2024-11-08 17:00:00'),
(6, 1, '2024-11-11 08:00:00', '2024-11-11 17:00:00'),
(1, 0.5, '2024-11-13 08:00:00', '2024-11-13 12:00:00'),
(2, 0.5, '2024-11-15 13:00:00', '2024-11-15 17:00:00'),
(4, 0.5, '2024-11-15 13:00:00', '2024-11-15 17:00:00'),
(2, 1, '2024-11-16 08:00:00', '2024-11-16 17:00:00');

-- Thêm dữ liệu mẫu cho bảng Contracts (Hợp đồng lao động)
INSERT INTO Contracts (StartDate, EndDate, SigningDate, IsActive, Salary, EmployeeID)
VALUES
('2024-09-30', '2024-11-30', '2024-09-09', 1, 0, 1),
('2024-09-30', '2024-11-30', '2024-09-09', 1, 0, 2),
('2024-09-30', '2024-11-30', '2024-09-09', 1, 0, 3),
('2024-09-30', '2024-11-30', '2024-09-09', 1, 0, 4),
('2024-09-30', '2024-11-30', '2024-09-09', 1, 0, 5),
('2024-09-30', '2024-11-30', '2024-09-09', 1, 0, 6);

