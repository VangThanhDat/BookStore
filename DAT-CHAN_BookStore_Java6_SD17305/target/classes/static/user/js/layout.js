var app = angular.module("myapp", ["ngRoute"]);
app.config(function ($routeProvider, $httpProvider) {
  $routeProvider
    .when("/", {
      templateUrl: "/user/home.html",
      controller: "product",
    })
    .when("/blog", {
      templateUrl: "/user/blog.html",
    })
    .when("/category", {
      templateUrl: "/user/category.html",
      controller: "product",
    })
    .when("/contact", {
      templateUrl: "/user/contact.html",
    })
    .when("/cart", {
      templateUrl: "/user/cart.html",
      controller: "cartCtrl",
    })
    .when("/checkout", {
      templateUrl: "/user/checkout.html",
      controller: "cartCtrl",
    })
    .when("/product/:productId", {
      templateUrl: "/user/product_detail.html",
      controller: "productDetailCtrl",
    })
    .when("/order", {
      templateUrl: "/user/order.html",
      controller: "OrderCtrl",
    })
    .when("/order/:orderId", {
      templateUrl: "/user/orderDetail.html",
      controller: "OrderDetailCtrl",
    })
    .when("/favorite", {
      templateUrl: "/user/favorite.html",
      controller: "FavoriteCtrl",
    })
    .otherwise({
      redirectTo: "/",
    });

  $httpProvider.interceptors.push("AuthInterceptor");
});

app.factory("AuthInterceptor", function ($q, $window, $rootScope, $injector) {
  var retrying = false;
  var $http;

  return {
    responseError: function (rejection) {
      if (rejection.status === 401 && !retrying) {
        retrying = true;
        var refreshToken = sessionStorage.getItem("refresh_token");
        console.log("refresh", refreshToken);

        if (refreshToken) {
          console.log("có refresh token");
          var config = {
            headers: {
              Authorization: "Bearer " + refreshToken,
              "Content-Type": "application/json",
            },
          };
          $http = $http || $injector.get("$http"); // Lazy-load $http service
          return $http
            .post(
              "http://localhost:8080/api/getNewToken",
              {
                token: refreshToken,
              },
              config
            )
            .then(function (response) {
              // Update token in localStorage
              console.log("response", response);
              sessionStorage.setItem("access_token", response.data.token);
              setTimeout(function () {
                $window.location.reload();
              }, 1000);
              // Retry the original request
              return $http(rejection.config);
            })
            .catch(function (error) {
              // Handle error during token refresh if needed
              // Redirect to login page
              console.log("lỗi không nhận được token", error);

              return $q.reject(rejection);
            })
            .finally(function () {
              retrying = false;
            });
        } else {
          // Redirect to login page
          console.log("lỗi ko có refresh token");
          Swal.fire({
            icon: "warning",
            title: "Cảnh báo",
            text: "Phiên đăng nhập hết hạn vui lòng đăng nhập để tiêp tục.",
            showConfirmButton: true,
            timer: 5000,
          }).then(function () {
            sessionStorage.removeItem("name");
            sessionStorage.removeItem("access_token");
            $window.location.href = "http://localhost:8080/auth#!/";
          });
        }
      } else if (rejection.status === 403) {
        // Redirect to the login page
        $window.location.href = "http://localhost:8080/auth#!/";
      }
      return $q.reject(rejection);
    },
  };
});

app.controller(
  "myctrl",
  function ($scope, $rootScope, $http, $window, apiService) {
    const url = "http://localhost:8080/";
    // $scope.loginWithGoogle = function (id, email) {
    //   console.log("id", id, "email", email);
    //   var loginGoogleData = {
    //     id: id,
    //     email: email,
    //   };
    //   $http
    //     .post(url + "api/oauth2/user/login", loginGoogleData)
    //     .then(function (response) {
    //       // Xử lý phản hồi từ API nếu cần
    //       $scope.message = response.data.message;
    //       $scope.token = response.data.token;
    //       $rootScope.jwtToken = response.data.token;
    //       $rootScope.refreshToken = response.data.refreshToken;
    //       sessionStorage.setItem("access_token", $rootScope.jwtToken);
    //       sessionStorage.setItem("refresh_token", $rootScope.refreshToken);
    //       console.log($rootScope.jwtToken);
    //       console.log("Đăng nhập thành công:", response);
    //       $scope.name = response.data.name;
    //       sessionStorage.setItem("name", $scope.name);
    //       console.log($scope.name);
    //       Swal.fire({
    //         icon: "success",
    //         title: "Thành công",
    //         text: "Đăng nhập thành công",
    //         showConfirmButton: true,
    //         timer: 5000,
    //       }).then(function () {
    //         $scope.checkLogin();
    //       });

    //       // $window.location.href = "http://localhost:8080/home/index";
    //     })
    //     .catch(function (error) {
    //       // Xử lý lỗi nếu có
    //       $scope.message = error.data.message;
    //       Swal.fire({
    //         icon: "error",
    //         title: "Lỗi",
    //         text: error.data.message,
    //         showConfirmButton: true,
    //         timer: 5000,
    //       });
    //       console.log("Lỗi khi đăng nhập:", error.data);
    //     });
    // };
    // $scope.checkLogin = function () {
    //   var config = {
    //     headers: {
    //       Authorization: "Bearer " + $rootScope.jwtToken,
    //       "Content-Type": "application/json",
    //     },
    //   };
    //   $http
    //     .post(url + "api/login/authorization", null, config)
    //     .then(function (response) {
    //       console.log("Kiểm tra thành công:", response);
    //       $scope.roles = response.data.roles;
    //       console.log("roles", $scope.roles);

    //       // Kiểm tra nếu không tìm thấy role "admin" trong mảng $scope.roles
    //       if (!$scope.roles || !$scope.roles.includes("ADMIN")) {
    //         // Nếu không có role "admin", điều hướng đến trang user
    //         $window.location.href = "http://localhost:8080/";
    //       } else {
    //         // Nếu có role "admin", điều hướng đến trang admin
    //         $window.location.href = "http://localhost:8080/admin/index";
    //       }
    //     })
    //     .catch(function (error) {
    //       console.log("Kiểm tra không thành công:", error);
    //       // Xử lý lỗi nếu cần thiết
    //     });
    // };
    // var id = $location.search().id;
    // var email = $location.search().email;
    // console.log("id", id, "email", email);
    // if (id && email) {
    //   console.log("Get user google");
    //   $scope.loginWithGoogle(id, email);
    // }
    apiService.setAuthorizationHeader();

    $scope.isAuthenticated = function () {
      console.log("isAuthenticated", isAuthenticated);
      return apiService.getJwtToken() !== null;
    };
    $rootScope.jwtToken = [];
    // Khởi tạo giá trị ban đầu cho $scope.name
    $rootScope.name = "Tài khoản";
    $rootScope.sanpham = {
      items: [],
      getItemCart() {
        var getCartURL = "http://localhost:8080/rest/cart";
        $http
          .get(getCartURL)
          .then((resp) => {
            this.items = resp.data;
          })
          .catch(angular.noop);
      },
      get count() {
        return this.items
          .map((item) => item.quantityPurchased)
          .reduce((total, qty) => (total += qty), 0);
      },
    };
    $rootScope.sanpham.getItemCart();
    // Kiểm tra nếu có sessionStorage và giá trị name đã được lưu trữ thì gán giá trị từ sessionStorage cho $scope.name
    if (sessionStorage.getItem("name")) {
      $rootScope.name = sessionStorage.getItem("name");
    }

    $scope.logout = function () {
      $http
        .post(url + "api/logout")
        .then(function (response) {
          console.log("Đăng xuất thành công:", response);
          $scope.message = response.data.message;
          sessionStorage.removeItem("name");
          sessionStorage.removeItem("access_token");
          sessionStorage.removeItem("refresh_token");
          $rootScope.name = "Tài khoản";
          Swal.fire({
            icon: "success",
            title: "Thành công",
            text: "Đăng xuất thành công",
            timer: 3000,
            showConfirmButton: false,
          }).then(function () {
            $window.location.href = "http://localhost:8080/auth#!/login";
          });

          // Thực hiện các thao tác khác sau khi đăng xuất (ví dụ: chuyển hướng đến trang đăng nhập)
        })
        .catch(function (error) {
          console.log("Lỗi khi đăng xuất:", error);
        });
    };
  }
);

app.controller("product", function ($scope, $http, $rootScope, $window) {
  $rootScope.jwtToken = [];
  if (sessionStorage.getItem("access_token")) {
    $rootScope.jwtToken = sessionStorage.getItem("access_token");
  }
  console.log("token", sessionStorage.getItem("access_token"));
  // var config = {
  //   headers: {
  //     Authorization:
  //       "Bearer " +
  //       "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXlkdCIsInJvbGVzIjpbIkFETUlOIiwiVVNFUiJdLCJpYXQiOjE2OTE0MTI3NDgsImV4cCI6MTY5MjcwODc0OH0.VEvrF1pn6NIEXw567Nrau2-j96w02iz5Kf7SUrMPMpo",
  //     "Content-Type": "application/json",
  //   },
  // };
  // var configUser = {
  //   headers: {
  //     Authorization: "Bearer " + $rootScope.jwtToken,
  //     "Content-Type": "application/json",
  //   },
  // };
  
  //Load dữ liệu sản phẩm--------------------
  $scope.products = [];
  $scope.sum = 0;
  $scope.getTours = function () {
    var getToursApiUrl = "http://localhost:8080/api/products";
    $http
      .get(getToursApiUrl)
      .then(function (response) {
        $scope.products = response.data;
        $scope.initialProducts = angular.copy(response.data); // Lưu trữ bản sao
        $scope.sum = (Math.ceil($scope.products.length / 6) - 1) * 6;
        console.log("SUM", $scope.sum);
        console.log("Success getTours:", response.data);
      })
      .catch(function (error) {
        console.error("Error fetching tour products:", error);
      });
  };
  //END--------------------------
  
  //Thêm vào giỏ hàng
  $scope.addCart = function (id) {
    $http
      .get(`http://localhost:8080/rest/addCart/${id}`)
      .then((resp) => {
        $rootScope.sanpham.getItemCart();
        console.log("Thêm giỏ hàng:", resp.data);
        Swal.fire({
          icon: "success",
          title: "Thêm vào giỏ hàng thành công",
          showConfirmButton: false,
          timer: 1500,
        });
      })
      .catch((error) => {
        console.log("Status ", error.status);
        if (error.status === 401) {
          Swal.fire({
            icon: "error",
            title:
              "Thêm vào giỏ hàng thất bại, vui lòng đăng nhập để sử dụng tính năng",
            showConfirmButton: false,
            timer: 1500,
          }).then(function () {
            $window.location.href = "http://localhost:8080/auth#!/login";
          });
        }
      });
  };
  //END--------------------------
  
  // Khởi tạo danh sách các danh mục
  $scope.categories = [];

  $scope.getCategory = function () {
    var getCategoryApiUrl = "http://localhost:8080/api/category";
    $http
      .get(getCategoryApiUrl)
      .then(function (response) {
        $scope.categories = response.data;
        $scope.sum = (Math.ceil($scope.products.length / 6) - 1) * 6;
        console.log("Success getCategory:", response);
      })
      .catch(function (error) {
        console.error("Error fetching categories:", error);
      });
  };
  $scope.getCategory();
  //END--------------------------

  // Lấy sản phẩm theo danh mục ------------------------
  $scope.products = [];
  $scope.getProductsByCategory = function (categoryId) {
    var url = "http://localhost:8080/api/category/" + categoryId + "/products";
    $http
      .get(url)
      .then(function (response) {
        $scope.products = response.data;
        $scope.sum = (Math.ceil($scope.products.length / 6) - 1) * 6;
      })
      .catch(function (error) {
        console.error("Lỗi khi lấy sản phẩm:", error);
      });
  };

  var categoryId = null;
  $scope.getProductsByCategory(categoryId);
  // Kết thúc -------------------------

  //Tìm kiếm sản phẩm
  $scope.searchProducts = function () {
    var searchApiUrl =
      "http://localhost:8080/api/products/search/" + $scope.searchQuery;
    $http
      .get(searchApiUrl)
      .then(function (response) {
        $scope.products = response.data;
        $scope.sum = (Math.ceil($scope.products.length / 6) - 1) * 6;
      })
      .catch(function (error) {
        console.error("Lỗi khi tìm kiếm sản phẩm:", error);
      });
    // Đặt lại trạng thái ban đầu
    $scope.searchQuery = "";
    $scope.sortOption = "1";
    $scope.sortProducts();
  };
  // Kết thúc -------------------------

  // Xử lý hiển thị thông báo khi không tìm thấy sản phẩm
  $scope.$watch("products", function (newProducts) {
    $scope.showNoProductsMessage = newProducts.length === 0;
    $scope.sum = (Math.ceil($scope.products.length / 6) - 1) * 6;
  });
  // Kết thúc -------------------------

  //Sắp xếp tăng dần giảm dần
  $scope.sortOption = "1";
  $scope.sortProducts = function () {
    if ($scope.sortOption === "2") {
      $scope.sortByPriceAsc();
      $scope.begin = 0;
    } else if ($scope.sortOption === "3") {
      $scope.sortByPriceDesc();
      $scope.begin = 0;
    } else if ($scope.sortOption === "4") {
      $scope.sortByProductNameAsc();
      $scope.begin = 0;
    } else if ($scope.sortOption === "5") {
      $scope.sortByProductNameDesc();
      $scope.begin = 0;
    } else {
      $scope.products = angular.copy($scope.initialProducts);
      $scope.begin = 0;
    }
  };
  $scope.sortByPriceAsc = function () {
    $scope.products.sort(function (a, b) {
      return a.price - b.price;
    });
  };

  $scope.sortByPriceDesc = function () {
    $scope.products.sort(function (a, b) {
      return b.price - a.price;
    });
  };
  $scope.sortByProductNameAsc = function () {
    $scope.products.sort(function (a, b) {
      return a.name.localeCompare(b.name);
    });
  };

  $scope.sortByProductNameDesc = function () {
    $scope.products.sort(function (a, b) {
      return b.name.localeCompare(a.name);
    });
  };
  //------------------
  
  //Phân trang----------------------------
  $scope.begin = 0;
  $scope.first = function () {
    $scope.begin = 0;
  };
  $scope.prev = function () {
    if ($scope.begin > 0) {
      $scope.begin -= 6;
    }
  };
  $scope.next = function () {
    if ($scope.begin < $scope.sum) {
      $scope.begin += 6;
    }
  };
  $scope.last = function () {
    $scope.begin = $scope.sum;
  };
  $scope.getTours();
  $scope.updateProductsByCategory = function (categoryId) {
    $scope.getProductsByCategory(categoryId);
    $scope.begin = 0;
    // Reset lại trang khi chọn danh mục mới
  };
  $scope.getPageNumbers = function () {
    var totalPages = Math.ceil($scope.products.length / 6);
    var pageNumbers = [];

    for (var i = 1; i <= totalPages; i++) {
      pageNumbers.push(i);
    }

    return pageNumbers;
  };

  $scope.goToPage = function (pageNumber) {
    $scope.begin = (pageNumber - 1) * 6;
  };
  // Kết thúc -------------------------
});

//productDetailCtrl
app.controller(
  "productDetailCtrl",
  function ($scope, $http, $routeParams, $rootScope, $window) {
    $rootScope.jwtToken = [];
    if (sessionStorage.getItem("access_token")) {
      $rootScope.jwtToken = sessionStorage.getItem("access_token");
    }
    console.log("token", sessionStorage.getItem("access_token"));
    // var config = {
    //   headers: {
    //     Authorization: "Bearer " + $rootScope.jwtToken,
    //     "Content-Type": "application/json",
    //   },
    // };
    // Function to fetch the detail of the selected product
    $scope.getProductDetail = function () {
      var productId = $routeParams.productId;
      var getProductDetailApiUrl =
        "http://localhost:8080/api/products/" + productId;

      $http
        .get(getProductDetailApiUrl)
        .then(function (response) {
          $scope.productDetail = response.data;
        })
        .catch(function (error) {
          console.error("Error fetching product detail:", error);
        });
      $http
        .get("/api/products/" + productId)
        .then(function (response) {
          $scope.productDetail = response.data;

          // Lấy danh sách sản phẩm cùng loại
          $http
            .get("/api/products/related/" + $scope.productDetail.category.id)
            .then(function (response) {
              $scope.relatedProducts = response.data;
            })
            .catch(function (error) {
              console.log("Lỗi khi lấy danh sách sản phẩm cùng loại:", error);
            });
        })
        .catch(function (error) {
          console.log("Lỗi khi lấy thông tin sản phẩm chi tiết:", error);
        });
    };
    
	//Thêm vào giỏ hàng
    $scope.addCart = function (id) {
      $http
        .get(`http://localhost:8080/rest/addCart/${id}`)
        .then((resp) => {
          $rootScope.sanpham.getItemCart();
          console.log(resp.data);
          Swal.fire({
            icon: "success",
            title: "Thêm vào giỏ hàng thành công",
            showConfirmButton: false,
            timer: 1500,
          });
        })
        .catch((error) => {
          console.log("Status ", error.status);
          if (error.status === 401) {
            Swal.fire({
              icon: "error",
              title:
                "Thêm vào giỏ hàng thất bại, vui lòng đăng nhập để sử dụng tính năng",
              showConfirmButton: false,
              timer: 1500,
            }).then(function () {
              $window.location.href = "http://localhost:8080/auth#!/login";
            });
          }
        });
    };
    $scope.getProductDetail();
  }
);
