var app = angular.module("userApp", ["ngRoute"]);
app.controller("homeCtrl", function ($scope, $http, $window, $rootScope) {
  $rootScope.jwtToken = [];
  const url = "http://localhost:8080/";
  $scope.name = sessionStorage.getItem("name");

  $scope.logout = function () {
    $http
      .post(url + "api/logout")
      .then(function (response) {
        console.log("Đăng xuất thành công:", response);
        $scope.message = response.data.message;
        $window.location.href = "http://localhost:8080/auth#!/login";
        // Thực hiện các thao tác khác sau khi đăng xuất (ví dụ: chuyển hướng đến trang đăng nhập)
      })
      .catch(function (error) {
        console.log("Lỗi khi đăng xuất:", error);
      });
  };
});
