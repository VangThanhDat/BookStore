// AngularJS controller
app.controller(
  "forgotCtrl",
  function ($scope, $http, $window, $rootScope, $routeParams) {
    $rootScope.username = [];
    $rootScope.getForm = true;
    $rootScope.checkForm = false;
    $rootScope.changeForm = false;

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
    $rootScope.formSubmitted = false;
    const url = "http://localhost:8080/";

    $scope.formData = {}; // Dữ liệu từ form đăng nhập

    $scope.getOTP = function () {
      $rootScope.formSubmitted = true;
      console.log("valid", frmForgot.$valid);
      if ($scope.frmForgot.$valid) {
        var forgotData = {
          id: $scope.formData.username,
          email: $scope.formData.email,
        };
        var config = {
          headers: {
            Authorization:
              "Bearer " +
              "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXlkdCIsInJvbGVzIjpbIkFETUlOIiwiVVNFUiIsIkdVRVNUIl0sImlhdCI6MTY5MDk5NDU3NCwiZXhwIjoxNjkxMzU0NTc0fQ.dsM0NumEJX4c_Biu-NdAP0NK6Vm3Rr6riUA4TQJy5kM",
            "Content-Type": "application/json",
          },
        };
        // Gửi yêu cầu POST đến API
        console.log("Data: ", forgotData);
        $http
          .post(url + "api/get-otp", forgotData, config)
          .then(function (response) {
            // Xử lý phản hồi từ API nếu cần
            $scope.message = response.data.message;

            Swal.fire({
              icon: "success",
              title: "Thành công",
              text: response.data.message,
              showConfirmButton: true,
              timer: 5000,
            }).then(function () {});
            $rootScope.getForm = false;
            $rootScope.checkForm = true;
            $rootScope.changeForm = false;
            $rootScope.formSubmitted = false;
            $rootScope.username = response.data.username;
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
            console.log("Lỗi khi lấy OTP", error.data);
          });
      }
    };
    $scope.checkOTP = function () {
      $rootScope.formSubmitted = true;
      console.log("valid", frmOTP.$valid);
      if ($scope.frmOTP.$valid) {
        var forgotData = {
          otp: $scope.formData.otp,
        };
        var config = {
          headers: {
            Authorization:
              "Bearer " +
              "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXlkdCIsInJvbGVzIjpbIkFETUlOIiwiVVNFUiIsIkdVRVNUIl0sImlhdCI6MTY5MDk5NDU3NCwiZXhwIjoxNjkxMzU0NTc0fQ.dsM0NumEJX4c_Biu-NdAP0NK6Vm3Rr6riUA4TQJy5kM",
            "Content-Type": "application/json",
          },
        };
        // Gửi yêu cầu POST đến API
        console.log("Data: ", forgotData);
        $http
          .post(url + "api/check-otp", forgotData, config)
          .then(function (response) {
            // Xử lý phản hồi từ API nếu cần
            $scope.message = response.data.message;
            Swal.fire({
              icon: "success",
              title: "Thành công",
              text: response.data.message,
              showConfirmButton: true,
              timer: 5000,
            }).then(function () {});
            $rootScope.getForm = false;
            $rootScope.checkForm = false;
            $rootScope.changeForm = true;
            $rootScope.formSubmitted = false;
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
            if (error.status == 404) {
              $rootScope.getForm = true;
              $rootScope.checkForm = false;
              $rootScope.changeForm = false;
              $rootScope.formSubmitted = false;
            }
            console.log("Lỗi khi lấy OTP", error.data);
          });
      }
    };
    $scope.change = function () {
      $rootScope.formSubmitted = true;
      console.log("valid", frmChange.$valid);
      if (
        $scope.frmChange.$valid &&
        $scope.formData.password === $scope.formData.confirmPassword
      ) {
        var forgotData = {
          password: $scope.formData.password,
          username: $rootScope.username,
        };
        var config = {
          headers: {
            Authorization:
              "Bearer " +
              "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXlkdCIsInJvbGVzIjpbIkFETUlOIiwiVVNFUiIsIkdVRVNUIl0sImlhdCI6MTY5MDk5NDU3NCwiZXhwIjoxNjkxMzU0NTc0fQ.dsM0NumEJX4c_Biu-NdAP0NK6Vm3Rr6riUA4TQJy5kM",
            "Content-Type": "application/json",
          },
        };
        // Gửi yêu cầu POST đến API
        console.log("Data: ", forgotData);
        $http
          .post(url + "api/change-pass", forgotData, config)
          .then(function (response) {
            // Xử lý phản hồi từ API nếu cần
            $scope.message = response.data.message;
            Swal.fire({
              icon: "success",
              title: "Thành công",
              text: response.data.message,
              showConfirmButton: true,
              timer: 10000,
            }).then(function () {
              $window.location.href = "/auth";
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
            if (error.status == 404) {
              $rootScope.getForm = false;
              $rootScope.checkForm = true;
              $rootScope.changeForm = false;
              $rootScope.formSubmitted = false;
            }
            console.log("Lỗi khi lấy OTP", error.data);
          });
      }
    };
  }
);
