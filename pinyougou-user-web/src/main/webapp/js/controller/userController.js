 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){



	//注册
	$scope.reg=function(){
		if($scope.entity.password!=$scope.password) {
			$scope.entity.password="";
			$scope.password="";

			alert("两次输入的密码不一致，请重新输入");
			return ;
		}
		if($scope.code==null){
			alert("请输入验证码");
			return;
		}

		userService.add( $scope.entity,$scope.code ).success(
			function(response){
				alert(response.message);
			}
		);
	}
	//发送验证码
	$scope.sendCode=function () {
		if($scope.entity.phone==null||$scope.entity.phone==""){
			alert("请输入手机号");
			return;
		}
		userService.sendCode($scope.entity.phone).success(
			function (response) {
				alert(response.message);
			}
		)
	}
});	
