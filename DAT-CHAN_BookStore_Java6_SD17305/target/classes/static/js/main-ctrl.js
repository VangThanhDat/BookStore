var app = angular.module("authApp", ["ngRoute"]);
app.config(function ($routeProvider) {
  $routeProvider
    .when("/", {
      templateUrl: "/frmAuth/login.html",
    })
    .when("/login", {
      templateUrl: "/frmAuth/login.html",
    })

    .when("/register", {
      templateUrl: "/frmAuth/register.html",
    })

    .when("/change-info", {
      templateUrl: "/frmAuth/change-info.html",
    })

    .when("/forgot-password", {
      templateUrl: "/frmAuth/forgot-password.html",
    })

    .otherwise({
      redirectTo: "/",
    });
});
app.controller("mainCtrl", function ($scope) {
  console.log("Main ctrl called");
});
