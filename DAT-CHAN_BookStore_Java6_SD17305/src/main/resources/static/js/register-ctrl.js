// AngularJS controller
app.controller("registerCtrl", function ($scope, $http, $window, $rootScope) {
  $rootScope.jwtToken = [];
  $rootScope.formSubmitted = false;
  $scope.message = "";
  const url = "http://localhost:8080/";

  $scope.formData = {}; // Dữ liệu từ form đăng nhập
  $scope.formData.birthday = new Date("04/21/2003");

  $scope.register = function () {
    $rootScope.formSubmitted = true;
    console.log("valid", frmRegister.$valid);
    if (
      $scope.frmRegister.$valid &&
      $scope.formData.password === $scope.formData.confirmPassword
    ) {
      var registerData = {
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
      console.log("Data: ", registerData);
      $http
        .post(url + "api/register", registerData)
        .then(function (response) {
          // Xử lý phản hồi từ API nếu cần
          console.log("Success", response);
          Swal.fire({
            icon: "success",
            title: "Thành công",
            text: "Đăng ký thành công",
            showConfirmButton: true,
            timer: 5000,
          }).then(function () {
            $scope.formData = {};
            $window.location.href = "#!register";
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
          console.log("Lỗi khi đăng ký:", error.data);
        });
    }
  };
});
