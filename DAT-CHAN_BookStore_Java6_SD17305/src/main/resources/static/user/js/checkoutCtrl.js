app.controller("checkoutCtrl", function ($scope, $http, $rootScope) {
  $rootScope.jwtToken = [];
  if (sessionStorage.getItem("access_token")) {
    $rootScope.jwtToken = sessionStorage.getItem("access_token");
  }
  console.log("token", sessionStorage.getItem("access_token"));
  // var config = {
  // 	headers: {
  // 		Authorization: "Bearer " + $rootScope.jwtToken,
  // 		"Content-Type": "application/json",
  // 	},
  // };
  //   console.log("config", config);
  $scope.order = {
    createDate: new Date(),
  };
  //	$scope.order;
});
