app.controller("OrderCtrl", function ($scope, $rootScope, $http) {
  $rootScope.jwtToken = [];
  if (sessionStorage.getItem("access_token")) {
    $rootScope.jwtToken = sessionStorage.getItem("access_token");
  }
  console.log("token", sessionStorage.getItem("access_token"));

  $rootScope.itemsAll = [];
  $rootScope.itemsWait = [];
  $rootScope.itemsAccept = [];
  $rootScope.itemsShip = [];
  $rootScope.itemsCancel = [];

  $scope.order = {
    getItemOrder() {

      //All Order
      var getAllOrderURL = "http://localhost:8080/restApi/allorder";
      $http.get(getAllOrderURL).then((resp) => {
        console.log("(All Order) data", resp.data);
        $rootScope.itemsAll = resp.data;
        console.log("itemsAll", $rootScope.itemsAll);
      }).catch((err) => {
        console.log("(All Order data) Error", err);
      });
      // .catch(angular.noop);

      // Wait Order
      var getOrderWaitURL = "http://localhost:8080/restApi/orderwait";
      $http.get(getOrderWaitURL).then((resp) => {
        console.log("OrderWaitData", resp.data);
        $rootScope.itemsWait = resp.data;
        console.log("itemsWait", $rootScope.itemsWait);
      }).catch((err) => {
        console.log("(Wait Order data) Error", err);
      });
      // .catch(angular.noop);

      //Accept Order
      var getOrderAcceptURL = "http://localhost:8080/restApi/orderaccept";
      $http.get(getOrderAcceptURL).then((resp) => {
        console.log("OrderAcceptData", resp.data);
        $rootScope.itemsAccept = resp.data;
        console.log("itemsAccept", $rootScope.itemsAccept);
      }).catch((err) => {
        console.log("(Accept Order data) Error", err);
      });
      // .catch(angular.noop);

      // OrderShip
      var getOrderShipURL = "http://localhost:8080/restApi/ordership";
      $http.get(getOrderShipURL).then((resp) => {
        console.log("OrderShipData", resp.data);
        $rootScope.itemsShip = resp.data;
        console.log("itemsShip", $rootScope.itemsShip);
      }).catch((err) => {
        console.log("(Ship Order data) Error", err);
      });
      // .catch(angular.noop);

      // OrderCancel
      var getOrderCancelURL = "http://localhost:8080/restApi/ordercancel";
      $http.get(getOrderCancelURL).then((resp) => {
        console.log("OrderCancelData", resp.data);
        $rootScope.itemsCancel = resp.data;
        console.log("itemsCancel", $rootScope.itemsCancel);
      }).catch((err) => {
        console.log("(Cancel Order data) Error", err);
      });
      // .catch(angular.noop);
    }
  };

  $scope.order.getItemOrder();
});
