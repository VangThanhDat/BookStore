app.controller("FavoriteCtrl", function ($scope, $http, $rootScope) {
    $rootScope.jwtToken = [];
    if (sessionStorage.getItem("access_token")) {
        $rootScope.jwtToken = sessionStorage.getItem("access_token");
    }
    console.log("token", sessionStorage.getItem("access_token"));

    $scope.itemslist = [];

    $scope.like = {
        getLikeList() {

            //All favorites of users
            var getLikeListURL = "http://localhost:8080/restApi/like";
            $http.get(getLikeListURL).then((resp) => {
                console.log("(All Order) data", resp.data);
                $scope.itemslist = resp.data;
                console.log("itemsLikeList", $scope.itemslist);
            }).catch((err) => {
                console.log("(like list data) Error", err);
            });
            // .catch(angular.noop);

        }
    };
    //Begin add cart
    $scope.addCart = function (id) {
        $http
            .get(`http://localhost:8080/rest/addCart/${id}`)
            .then((resp) => {
                $rootScope.sanpham.getItemCart()
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
    //End ndadd cart

    //Begin category list
    $scope.categories = [];
    $scope.getCategory = function () {
        var getCategoryApiUrl = "http://localhost:8080/api/category";
        $http
            .get(getCategoryApiUrl)
            .then(function (response) {
                $scope.categories = response.data;
                console.log("Success getCategory:", response);
            })
            .catch(function (error) {
                console.error("Error fetching categories:", error);
            });
    };
    $scope.getCategory();

    $scope.getProductsByCategory = function (categoryId) {
        var url = "http://localhost:8080/restApi/like/" + categoryId + "/products";
        $http
          .get(url)
          .then(function (response) {
            $scope.itemslist = response.data;
            console.log("itemsLikeList", $scope.itemslist);
          })
          .catch(function (error) {
            console.error("Lỗi khi lấy sản phẩm:", error);
          });
      };

      $scope.updateProductsByCategory = function (categoryId) {
        $scope.getProductsByCategory(categoryId);
      };
    //End category list

    $scope.searchProducts = function () {
        var searchApiUrl =
            "http://localhost:8080/restApi/like/search/" + $scope.searchQuery;
        $http
            .get(searchApiUrl)
            .then(function (response) {
                $scope.itemslist = response.data;
                console.log("itemsLikeList(Search)", $scope.itemslist);
            })
            .catch(function (error) {
                console.error("Lỗi khi tìm kiếm sản phẩm:", error);
            });
        $scope.searchQuery = "";
    };

    $scope.like.getLikeList();
});