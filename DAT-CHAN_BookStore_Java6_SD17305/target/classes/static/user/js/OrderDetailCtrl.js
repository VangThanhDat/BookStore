app.controller("OrderDetailCtrl", function ($scope, $http, $routeParams, $rootScope) {
    $rootScope.jwtToken = [];
    if (sessionStorage.getItem("access_token")) {
        $rootScope.jwtToken = sessionStorage.getItem("access_token");
    }
    console.log("token", sessionStorage.getItem("access_token"));
    var orderId = $routeParams.orderId;
    $rootScope.itemsOrderDetail = [];
    $rootScope.getDetail = function () {
        
        var getDetailApiUrl = "http://localhost:8080/restApi/order/"+orderId ;

        $http.get(getDetailApiUrl).then(function (response) {
            console.log("(OrderDetail) Data", response.data);
            $rootScope.itemsOrderDetail = response.data;
            console.log("itemsOrderDetail", $rootScope.itemsOrderDetail);
        }).catch(function (error){
            confirm("error:",error);
        });
    };

    

    $rootScope.getDetail();

});