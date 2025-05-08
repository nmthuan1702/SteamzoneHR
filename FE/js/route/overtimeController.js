app.controller('overtimeController', function($scope, $http) {
    $scope.overtimeSchedules = [
        {
            ScheduleID: 1,
            ScheduleDate: "2024-10-01T00:00:00",
            StartTime: "18:00:00",
            EndTime: "22:00:00",
            HourlyRate: 200000,
            Status: "Đã tạo" 
        },
        {
            ScheduleID: 2,
            ScheduleDate: "2024-10-05T00:00:00",
            StartTime: "19:00:00",
            EndTime: "23:00:00",
            HourlyRate: 250000,
            Status: "Đã hủy"
        },
        {
            ScheduleID: 3,
            ScheduleDate: "2024-10-10T00:00:00",
            StartTime: "17:30:00",
            EndTime: "21:30:00",
            HourlyRate: 220000,
            Status: "Đã tạo"
        },
        {
            ScheduleID: 4,
            ScheduleDate: "2024-10-12T00:00:00",
            StartTime: "20:00:00",
            EndTime: "00:00:00",
            HourlyRate: 240000,
            Status: "Đã tạo"
        },
        {
            ScheduleID: 5,
            ScheduleDate: "2024-10-15T00:00:00",
            StartTime: "18:00:00",
            EndTime: "21:00:00",
            HourlyRate: 230000,
            Status: "Đã hủy"
        },
        {
            ScheduleID: 1,
            ScheduleDate: "2024-10-01T00:00:00",
            StartTime: "18:00:00",
            EndTime: "22:00:00",
            HourlyRate: 200000,
            Status: "Đã tạo"
        },
        {
            ScheduleID: 2,
            ScheduleDate: "2024-10-05T00:00:00",
            StartTime: "19:00:00",
            EndTime: "23:00:00",
            HourlyRate: 250000,
            Status: "Đã tạo"
        },
        {
            ScheduleID: 3,
            ScheduleDate: "2024-10-10T00:00:00",
            StartTime: "17:30:00",
            EndTime: "21:30:00",
            HourlyRate: 220000,
            Status: "Đã tạo"
        },
        {
            ScheduleID: 4,
            ScheduleDate: "2024-10-12T00:00:00",
            StartTime: "20:00:00",
            EndTime: "00:00:00",
            HourlyRate: 240000,
            Status: "Đã hủy"
        },
        {
            ScheduleID: 5,
            ScheduleDate: "2024-10-15T00:00:00",
            StartTime: "18:00:00",
            EndTime: "21:00:00",
            HourlyRate: 230000,
            Status: "Đã hủy"
        },
        {
            ScheduleID: 1,
            ScheduleDate: "2024-10-01T00:00:00",
            StartTime: "18:00:00",
            EndTime: "22:00:00",
            HourlyRate: 200000,
            Status: "Đã tạo" // Thay đổi trạng thái mặc định
        },
        {
            ScheduleID: 2,
            ScheduleDate: "2024-10-05T00:00:00",
            StartTime: "19:00:00",
            EndTime: "23:00:00",
            HourlyRate: 250000,
            Status: "Đã tạo"
        },
        {
            ScheduleID: 3,
            ScheduleDate: "2024-10-10T00:00:00",
            StartTime: "17:30:00",
            EndTime: "21:30:00",
            HourlyRate: 220000,
            Status: "Đã tạo"
        },
        {
            ScheduleID: 4,
            ScheduleDate: "2024-10-12T00:00:00",
            StartTime: "20:00:00",
            EndTime: "00:00:00",
            HourlyRate: 240000,
            Status: "Đã tạo"
        },
        {
            ScheduleID: 5,
            ScheduleDate: "2024-10-15T00:00:00",
            StartTime: "18:00:00",
            EndTime: "21:00:00",
            HourlyRate: 230000,
            Status: "Đã hủy"
        }
        ,
        {
            ScheduleID: 1,
            ScheduleDate: "2024-10-01T00:00:00",
            StartTime: "18:00:00",
            EndTime: "22:00:00",
            HourlyRate: 200000,
            Status: "Đã tạo" // Thay đổi trạng thái mặc định
        },
        {
            ScheduleID: 2,
            ScheduleDate: "2024-10-05T00:00:00",
            StartTime: "19:00:00",
            EndTime: "23:00:00",
            HourlyRate: 250000,
            Status: "Đã hủy"
        },
        {
            ScheduleID: 3,
            ScheduleDate: "2024-10-10T00:00:00",
            StartTime: "17:30:00",
            EndTime: "21:30:00",
            HourlyRate: 220000,
            Status: "Đã tạo"
        },
        {
            ScheduleID: 4,
            ScheduleDate: "2024-10-12T00:00:00",
            StartTime: "20:00:00",
            EndTime: "00:00:00",
            HourlyRate: 240000,
            Status: "Đã tạo"
        },
        {
            ScheduleID: 5,
            ScheduleDate: "2024-10-15T00:00:00",
            StartTime: "18:00:00",
            EndTime: "21:00:00",
            HourlyRate: 230000,
            Status: "Đã hủy"
        }

    ];

    $scope.goToSchedule = function(schedule) {
        // Lưu schedule vào localStorage hoặc sessionStorage
        localStorage.setItem('selectedSchedule', JSON.stringify(schedule));
        
        // Chuyển hướng tới overtime-schedule.html
        window.location.href = 'overtime-schedule.html';
      };

    // Hàm toggleStatus để thay đổi trạng thái
    $scope.toggleStatus = function(overtimeSchedule) {
        // Kiểm tra trạng thái hiện tại và cập nhật
        if (overtimeSchedule.Status === 'Đã tạo') {
            overtimeSchedule.Status = 'Đã hủy';
        } else {
            overtimeSchedule.Status = 'Đã tạo';
        }
    };
});
