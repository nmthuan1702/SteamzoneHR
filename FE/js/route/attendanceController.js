app.controller('attendanceController',function($scope, $http){
    $scope.employees = [
        { id: 1, phong: "Kế toán", chucvu: "Nhân viên", hoten: "Nguyễn Văn A", gioitinh: "Nam", ngaysinh: "1990-01-01", email: "a.nguyen@example.com", sdt: "0912345678", cccd: "123456789", diachi: "Hà Nội", trangthai: "Kích hoạt", imageUrl: "images/employee1.jpg" },
        { id: 2, phong: "Nhân sự", chucvu: "Trưởng phòng", hoten: "Trần Thị B", gioitinh: "Nữ", ngaysinh: "1988-02-01", email: "b.tran@example.com", sdt: "0912345679", cccd: "987654321", diachi: "TP HCM", trangthai: "Khóa", imageUrl: "images/employee2.jpg" },
        { id: 3, phong: "IT", chucvu: "Nhân viên", hoten: "Lê Văn C", gioitinh: "Nam", ngaysinh: "1992-03-05", email: "c.le@example.com", sdt: "0912345680", cccd: "234567890", diachi: "Đà Nẵng", trangthai: "Kích hoạt", imageUrl: "images/employee3.jpg" },
        { id: 4, phong: "Marketing", chucvu: "Nhân viên", hoten: "Phạm Thị D", gioitinh: "Nữ", ngaysinh: "1994-04-10", email: "d.pham@example.com", sdt: "0912345681", cccd: "345678901", diachi: "Hải Phòng", trangthai: "Khóa", imageUrl: "images/employee4.jpg" },
        { id: 5, phong: "Kinh doanh", chucvu: "Nhân viên", hoten: "Trần Văn E", gioitinh: "Nam", ngaysinh: "1985-05-15", email: "e.tran@example.com", sdt: "0912345682", cccd: "456789012", diachi: "Cần Thơ", trangthai: "Kích hoạt", imageUrl: "images/employee5.jpg" },
        { id: 6, phong: "Kế toán", chucvu: "Trưởng phòng", hoten: "Nguyễn Thị F", gioitinh: "Nữ", ngaysinh: "1987-06-20", email: "f.nguyen@example.com", sdt: "0912345683", cccd: "567890123", diachi: "Hà Nội", trangthai: "Kích hoạt", imageUrl: "images/employee6.jpg" },
        { id: 7, phong: "IT", chucvu: "Nhân viên", hoten: "Bùi Văn G", gioitinh: "Nam", ngaysinh: "1991-07-25", email: "g.bui@example.com", sdt: "0912345684", cccd: "678901234", diachi: "TP HCM", trangthai: "Khóa", imageUrl: "images/employee7.jpg" },
        { id: 8, phong: "Marketing", chucvu: "Nhân viên", hoten: "Vũ Thị H", gioitinh: "Nữ", ngaysinh: "1993-08-30", email: "h.vu@example.com", sdt: "0912345685", cccd: "789012345", diachi: "Đà Nẵng", trangthai: "Kích hoạt", imageUrl: "images/employee8.jpg" },
        { id: 9, phong: "Nhân sự", chucvu: "Nhân viên", hoten: "Hoàng Văn I", gioitinh: "Nam", ngaysinh: "1995-09-01", email: "i.hoang@example.com", sdt: "0912345686", cccd: "890123456", diachi: "Hải Phòng", trangthai: "Khóa", imageUrl: "images/employee9.jpg" },
        { id: 10, phong: "Kinh doanh", chucvu: "Nhân viên", hoten: "Đặng Thị J", gioitinh: "Nữ", ngaysinh: "1990-10-10", email: "j.dang@example.com", sdt: "0912345687", cccd: "901234567", diachi: "Cần Thơ", trangthai: "Kích hoạt", imageUrl: "images/employee10.jpg" },
        { id: 11, phong: "IT", chucvu: "Nhân viên", hoten: "Ngô Văn K", gioitinh: "Nam", ngaysinh: "1988-11-11", email: "k.ngo@example.com", sdt: "0912345688", cccd: "012345678", diachi: "Hà Nội", trangthai: "Khóa", imageUrl: "images/employee11.jpg" },
        { id: 12, phong: "Kế toán", chucvu: "Nhân viên", hoten: "Phạm Thị L", gioitinh: "Nữ", ngaysinh: "1989-12-12", email: "l.pham@example.com", sdt: "0912345689", cccd: "123456780", diachi: "TP HCM", trangthai: "Kích hoạt", imageUrl: "images/employee12.jpg" },
        { id: 13, phong: "Nhân sự", chucvu: "Nhân viên", hoten: "Võ Văn M", gioitinh: "Nam", ngaysinh: "1993-01-01", email: "m.vo@example.com", sdt: "0912345690", cccd: "234567891", diachi: "Đà Nẵng", trangthai: "Kích hoạt", imageUrl: "images/employee13.jpg" },
        { id: 14, phong: "Marketing", chucvu: "Trưởng phòng", hoten: "Đỗ Thị N", gioitinh: "Nữ", ngaysinh: "1986-02-02", email: "n.do@example.com", sdt: "0912345691", cccd: "345678902", diachi: "Hải Phòng", trangthai: "Khóa", imageUrl: "images/employee14.jpg" },
        { id: 15, phong: "Kinh doanh", chucvu: "Nhân viên", hoten: "Trần Văn O", gioitinh: "Nam", ngaysinh: "1990-03-03", email: "o.tran@example.com", sdt: "0912345692", cccd: "456789013", diachi: "Cần Thơ", trangthai: "Kích hoạt", imageUrl: "images/employee15.jpg" },
        { id: 16, phong: "IT", chucvu: "Nhân viên", hoten: "Bùi Thị P", gioitinh: "Nữ", ngaysinh: "1991-04-04", email: "p.bui@example.com", sdt: "0912345693", cccd: "567890124", diachi: "Hà Nội", trangthai: "Kích hoạt", imageUrl: "images/employee16.jpg" },
        { id: 17, phong: "Kế toán", chucvu: "Nhân viên", hoten: "Nguyễn Văn Q", gioitinh: "Nam", ngaysinh: "1989-05-05", email: "q.nguyen@example.com", sdt: "0912345694", cccd: "678901235", diachi: "TP HCM", trangthai: "Khóa", imageUrl: "images/employee17.jpg" },
        { id: 18, phong: "Nhân sự", chucvu: "Nhân viên", hoten: "Trần Thị R", gioitinh: "Nữ", ngaysinh: "1990-06-06", email: "r.tran@example.com", sdt: "0912345695", cccd: "789012346", diachi: "Đà Nẵng", trangthai: "Kích hoạt", imageUrl: "images/employee18.jpg" },
        { id: 19, phong: "Marketing", chucvu: "Nhân viên", hoten: "Lê Văn S", gioitinh: "Nam", ngaysinh: "1992-07-07", email: "s.le@example.com", sdt: "0912345696", cccd: "890123457", diachi: "Hải Phòng", trangthai: "Khóa", imageUrl: "images/employee19.jpg" }
    ];

    $scope.selectAll = false; // Biến để theo dõi trạng thái của checkbox chọn tất cả

    $scope.toggleSelectAll = function () {
        // Lặp qua tất cả nhân viên và thiết lập trạng thái selected theo giá trị của selectAll
        angular.forEach($scope.employees, function (employee) {
            employee.selected = $scope.selectAll;
        });
    };
    
    // Khởi tạo DataTable sau khi controller đã load
    $(document).ready(function () {
        $('#employees').DataTable(); // Khởi tạo DataTable cho lịch làm thêm giờ
    });

    // Hàm mở modal cập nhật
    $scope.openUpdateModal = function (schedule) {
        $scope.selectedSchedule = angular.copy(schedule); // Lưu thông tin vào biến để chỉnh sửa
    };

    // Hàm cập nhật lịch
    $scope.updateSchedule = function () {
        // Cập nhật dữ liệu logic ở đây
        console.log('Cập nhật lịch:', $scope.selectedSchedule);
        // Đóng modal
        $('#updateModal').modal('hide');
    };
});