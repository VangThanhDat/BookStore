app.controller("cartCtrl", function ($scope, $http, $rootScope, apiService) {
	$rootScope.jwtToken = [];

	if (sessionStorage.getItem("access_token")) {
		$rootScope.jwtToken = sessionStorage.getItem("access_token");
	}
	console.log("token", sessionStorage.getItem("access_token"));
	//   var config = {
	//     headers: {
	//       Authorization: "Bearer " + $rootScope.jwtToken,
	//       "Content-Type": "application/json",
	//     },
	//   };

	$scope.cart = {
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
				.map(item => item.quantityPurchased)
				.reduce((total, qty) => total += qty, 0);
		},
		get amout() {
			return this.items
				.map(item => item.quantityPurchased * item.product.price)
				.reduce((total, qty) => total += qty, 0);
		},
		remove(id) {
			var removeCartURL = `http://localhost:8080/rest/cart/remove/${id}`;
			var index = this.items.findIndex((item) => item.product.id === id);
			console.log("remove: ", index)
			$http
				.get(removeCartURL)
				.then((resp) => {
					$rootScope.sanpham.getItemCart()
					this.items.splice(index, 1)
					Swal.fire({
						icon: 'success',
						title: 'Đã xoá thành công',
						showConfirmButton: false,
						timer: 1500
					})
				})
				.catch(angular.noop);
		},
		clear() {
			var removeAllCartURL = `http://localhost:8080/rest/cart/removeAll`
			$http
				.get(removeAllCartURL)
				.then((resp) => {
					$rootScope.sanpham.getItemCart()
					this.items = []
					Swal.fire({
						icon: 'success',
						title: 'Đã xoá thành công',
						showConfirmButton: false,
						timer: 1500
					})
				})
				.catch(angular.noop);
		},
		increase(id) {
			var increaseItemURL = `http://localhost:8080/rest/cart/increase/${id}`
			var index = this.items.findIndex((item) => item.id === id);
			$http
				.get(increaseItemURL)
				.then((resp) => {
					$rootScope.sanpham.getItemCart()
					this.items.splice(index, 1, resp.data);
				})
				.catch(angular.noop);
		},
		reduce(id) {
			var reduceItemURL = `http://localhost:8080/rest/cart/reduce/${id}`
			var index = this.items.findIndex((item) => item.id === id);
			$http
				.get(reduceItemURL)
				.then((resp) => {
					$rootScope.sanpham.getItemCart()
					this.items.splice(index, 1, resp.data);
				})
				.catch(angular.noop);
		}
	};
	$scope.order = {
		fullname: {},
		phone: {},
		email: {},
		address:{},
		createDate: new Date(),
		getUserOrder() {
			var getUserOrderURL = "http://localhost:8080/rest/username";
			$http
				.get(getUserOrderURL)
				.then((resp) => {
					this.fullname = resp.data.fullname;
					this.phone = resp.data.phone;
					this.email = resp.data.email;
					this.address = resp.data.address;
				})
		},
		purchase() {
			var createOrderURL = "http://localhost:8080/rest/order/create";
			$scope.formData = {},
				orderData = {
					fullname: $scope.formData.username,
					phone: $scope.formData.numberPhone,
					email: $scope.formData.email,
					createDate: $scope.formData.createDate,
					address: $scope.formData.address,
				};
				console.log("Data: ", orderData);
			$http
				.post(createOrderURL, orderData)
				.then((resp) => {
					Swal.fire({
						icon: 'success',
						title: 'Đặt hàng thành công',
						showConfirmButton: false,
						timer: 1500
					})
				}).catch((error) => {
					Swal.fire({
						icon: 'error',
						title: 'Đặt hàng thất bại',
						showConfirmButton: false,
						timer: 1500
					})
				})
		},
		vnpay() {
			var createOrderURL = "http://localhost:8080/api/vnpay/create";
			$http
				.get(createOrderURL)
				.then((resp) => {
					Swal.fire({
						icon: 'success',
						title: 'Thanh toán thành công',
						showConfirmButton: false,
						timer: 1500
					})
				}).catch((error) => {
					Swal.fire({
						icon: 'error',
						title: 'Đặt hàng thất bại',
						showConfirmButton: false,
						timer: 1500
					})
				})
		},
		checkPayments() {
			if (document.getElementById('payment').checked) {
				var createOrderURL = "http://localhost:8080/rest/order/create";
			$scope.formData = {}
				var orderData = {
					id: "HD999",
					fullname: this.fullname,
					phone: this.numberPhone,
					email: this.email,
					createDate: this.createDate,
					address: this.address,
				};
				console.log("Data: ", orderData);
			$http
				.post(createOrderURL, orderData)
				.then((resp) => {
					Swal.fire({
						icon: 'success',
						title: 'Đặt hàng thành công',
						showConfirmButton: false,
						timer: 1500
					})
					window.location.href = "http://localhost:8080/#!/order";
				}).catch((error) => {
					Swal.fire({
						icon: 'error',
						title: 'Đặt hàng thất bại',
						showConfirmButton: false,
						timer: 1500
					})
				})
			} else if (document.getElementById('vnpay').checked) {
				var createOrderVnpayURL = "http://localhost:8080/api/vnpay/create";
				$http
					.get(createOrderVnpayURL)
					.then((resp) => {
						Swal.fire({
							icon: 'success',
							title: 'Thanh toán thành công',
							showConfirmButton: false,
							timer: 1500
						})
						var url = resp.data.url
						console.log(url)
						window.open(url);
					}).catch((error) => {
						Swal.fire({
							icon: 'error',
							title: 'Đặt hàng thất bại',
							showConfirmButton: false,
							timer: 1500
						})
						console.log(error)
					})
			}
		}
	}
	$scope.cart.getItemCart();
	$scope.order.getUserOrder();
});
