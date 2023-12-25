// AngularJS controller
app.controller("changeInfoCtrl", function ($scope, $http, $window, $rootScope) {
  $rootScope.jwtToken = [];
  $rootScope.formSubmitted = false;
  $scope.message = "";
  const url = "http://localhost:8080/";
  if (sessionStorage.getItem("access_token")) {
    $rootScope.jwtToken = sessionStorage.getItem("access_token");
    console.log("token", sessionStorage.getItem("access_token"));
    var config = {
      headers: {
        Authorization: "Bearer " + $rootScope.jwtToken,
        "Content-Type": "application/json",
      },
    };

    $scope.formData = []; // Dữ liệu từ form đăng nhập
    if ($scope.formData) {
      $http
        .get(url + "api/get-user-info", config)
        .then(function (response) {
          console.log("Success ", response);
          $scope.formData = response.data;
          $scope.formData.birthday = new Date(response.data.birthday);
          $scope.formData.username = response.data.id;
        })
        .catch(function (error) {
          console.log("Lỗi lấy dữ liệu: ", error);
        });
    }
    $scope.change = function () {
      $rootScope.formSubmitted = true;
      console.log("valid", frmChange.$valid);
      if ($scope.frmChange.$valid) {
        var changeData = {
          id: $scope.formData.username,
          password: $scope.formData.password,
          fullname: $scope.formData.fullname,
          email: $scope.formData.email,
          gender: $scope.formData.gender === "true" ? true : false,
          birthday: $scope.formData.birthday,
          phone: $scope.formData.phone,
          address: $scope.formData.address,
          active: true,
        };

        // Gửi yêu cầu POST đến API
        console.log("Data: ", changeData);
        $http
          .post(url + "api/change-info", changeData, config)
          .then(function (response) {
            // Xử lý phản hồi từ API nếu cần
            console.log("Success", response);
            Swal.fire({
              icon: "success",
              title: "Thành công",
              text: "Thay đổi thông tin thành công",
              showConfirmButton: true,
              timer: 5000,
            }).then(function () {
              $window.location.href = "#!change-info";
            });
            // $window.location.href = "http://localhost:8080/home/index";
          })
          .catch(function (error) {
            // Xử lý lỗi nếu có
            Swal.fire({
              icon: "error",
              title: "Lỗi",
              text: error.data.message,
              showConfirmButton: true,
              timer: 5000,
            });
            console.log("Lỗi khi thay đổi thông tin:", error.data);
            console.log("Status ", error.status);
            console.log(error.status === 401);
            if (error.status === 401) {
              $window.location.href =
                "http://localhost:8080/auth#!/login?message=Phiên đăng nhập hết hạn";
              sessionStorage.removeItem("access_token");
            }
          });
      }
    };
  } else {
    $window.location.href =
      "/auth#!/login?message=Vui lòng đăng nhập để sử dụng chức năng";
  }
});
