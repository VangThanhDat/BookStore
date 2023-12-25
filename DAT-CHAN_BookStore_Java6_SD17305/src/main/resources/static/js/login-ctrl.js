// AngularJS controller
app.controller(
  "loginCtrl",
  function ($scope, $http, $window, $rootScope, $routeParams) {
    if ($routeParams.message) {
      Swal.fire({
        icon: "error",
        title: "Lỗi",
        text: $routeParams.message,
        showConfirmButton: true,
        timer: 5000,
      });
    }
    $rootScope.jwtToken = [];
    $rootScope.refreshToken = [];
    $rootScope.formSubmitted = false;
    const url = "http://localhost:8080/";

    $scope.formData = {}; // Dữ liệu từ form đăng nhập

    $scope.login = function () {
      $rootScope.formSubmitted = true;
      console.log("valid", frmLogin.$valid);
      if ($scope.frmLogin.$valid) {
        var loginData = {
          id: $scope.formData.username,
          password: $scope.formData.password,
          rememberMe: $scope.formData.rememberMe,
        };

        // Gửi yêu cầu POST đến API
        console.log("Data: ", loginData);
        $http
          .post(url + "api/login", loginData)
          .then(function (response) {
            // Xử lý phản hồi từ API nếu cần
            $scope.message = response.data.message;
            $scope.token = response.data.token;
            $rootScope.jwtToken = response.data.token;
            $rootScope.refreshToken = response.data.refreshToken;
            sessionStorage.setItem("access_token", $rootScope.jwtToken);
            sessionStorage.setItem("refresh_token", $rootScope.refreshToken);
            console.log($rootScope.jwtToken);
            console.log("Đăng nhập thành công:", response);
            $scope.name = response.data.name;
            sessionStorage.setItem("name", $scope.name);
            console.log($scope.name);
            Swal.fire({
              icon: "success",
              title: "Thành công",
              text: "Đăng nhập thành công",
              showConfirmButton: true,
              timer: 5000,
            }).then(function () {
              $scope.checkLogin();
            });

            // $window.location.href = "http://localhost:8080/home/index";
          })
          .catch(function (error) {
            // Xử lý lỗi nếu có
            $scope.message = error.data.message;
            Swal.fire({
              icon: "error",
              title: "Lỗi",
              text: error.data.message,
              showConfirmButton: true,
              timer: 5000,
            });
            console.log("Lỗi khi đăng nhập:", error.data);
          });
      }
    };
    $scope.checkLogin = function () {
      var config = {
        headers: {
          Authorization: "Bearer " + $rootScope.jwtToken,
          "Content-Type": "application/json",
        },
      };
      $http
        .post(url + "api/login/authorization", null, config)
        .then(function (response) {
          console.log("Kiểm tra thành công:", response);
          $scope.roles = response.data.roles;
          console.log("roles", $scope.roles);

          // Kiểm tra nếu không tìm thấy role "admin" trong mảng $scope.roles
          if (!$scope.roles || !$scope.roles.includes("ADMIN")) {
            // Nếu không có role "admin", điều hướng đến trang user
            $window.location.href = "http://localhost:8080/";
          } else {
            // Nếu có role "admin", điều hướng đến trang admin
            $window.location.href = "http://localhost:8080/admin/index";
          }
        })
        .catch(function (error) {
          console.log("Kiểm tra không thành công:", error);
          // Xử lý lỗi nếu cần thiết
        });
    };

    $scope.logout = function () {
      $http
        .post(url + "api/logout")
        .then(function (response) {
          console.log("Đăng xuất thành công:", response);
          $scope.message = response.data.message;
          // $window.location.href = "/auth/login/form";
          // Thực hiện các thao tác khác sau khi đăng xuất (ví dụ: chuyển hướng đến trang đăng nhập)
        })
        .catch(function (error) {
          console.log("Lỗi khi đăng xuất:", error);
        });
    };
  }
);
