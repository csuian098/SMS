<template>
	<div>
		<div class="login-container" :style="{'backgroundImage': indexBgUrl?`url(${$base.url + indexBgUrl})`:''}">
			<el-form class="login_form animate__animated animate__">
				<div class="login_form2">
					<div class="title-container">基于SpringBoot和Shiro的企业考勤与薪资系统设计与实现登录</div>
					<div v-if="loginType==1" class="list-item">
						<div class="lable">
							账号
						</div>
						<input placeholder="请输入账号" name="username" type="text" v-model="rulesForm.username">
					</div>
					<div v-if="loginType==1" class="list-item">
						<div class="lable">
							密码
						</div>
						<div class="password-box">
							<input placeholder="请输入密码" name="password" :type="showPassword?'text':'password'" v-model="rulesForm.password">
							<span class="icon iconfont" :class="showPassword?'icon-liulan13':'icon-liulan17'" @click="showPassword=!showPassword"></span>
						</div>
					</div>

					<div class="list-item select" v-if="roles.length>1&&loginType<=2">
						<div class="lable">
							角色
						</div>
						<el-select v-model="rulesForm.role" placeholder="请选择角色">
							<el-option v-if="loginType==1||(loginType==2&&item.role!='users')" v-for="item in roles" :key="item.roleName" :label="item.roleName" :value="item.roleName" />
						</el-select>
					</div>

		
					<div class="login-btn">
						<div class="login-btn1">
							<el-button v-if="loginType==1||loginType==3||loginType==4" type="primary" @click="login()" class="loginInBt">登录</el-button>
						</div>
						<div class="login-btn2">
							<el-button type="primary" @click="register('yuangong')" class="register">
								注册员工							</el-button>
						</div>
						<div class="login-btn3">
						</div>
					</div>
				</div>
				<div class="idea-box1">Hello~</div>
				<div class="idea-box2">欢迎您登录系统</div>
			</el-form>
		</div>
	</div>
</template>
<script>
	import 'animate.css'
	import menu from "@/utils/menu";
	export default {
		data() {
			return {
				verifyCheck2: false,
				flag: false,
				baseUrl:this.$base.url,
				loginType: 1,
				rulesForm: {
					username: "",
					password: "",
					role: "",
				},
				menus: [],
				roles: [],
				tableName: "",
				showPassword: false,
				indexBgUrl: '',
			};
		},
		mounted() {
			this.getMenu();

		},
		created() {
			this.$http.get('config/info?name=bLoginBackgroundImg',).then(rs=>{this.indexBgUrl = rs.data.data?rs.data.data.value:''})
		},
		destroyed() {
		},
		components: {
		},
		methods: {
			getMenu() {
				let params = {
					page: 1,
					limit: 1,
					sort: 'id',
				}

				this.$http({
					url: "menu/list",
					method: "get",
					params: params
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.menus = JSON.parse(data.data.list[0].menujson);
						for (let i = 0; i < this.menus.length; i++) {
							if (this.menus[i].hasBackLogin=='是') {
								this.roles.push(this.menus[i])
							}
						}
						this.$storage.set("menus", this.menus);
					}
				})
			},

			//注册
			register(tableName){
				this.$storage.set("loginTable", tableName);
				this.$router.push({path:'/register',query:{pageFlag:'register'}})
			},
			// 登陆
			login() {
				if(this.loginType==1) {

					if (!this.rulesForm.username) {
						this.$message.error("请输入用户名");
						return;
					}
					if (!this.rulesForm.password) {
						this.$message.error("请输入密码");
						return;
					}
					if(this.roles.length>1) {
						if (!this.rulesForm.role) {
							this.$message.error("请选择角色");
							return;
						}
					
						for (let i = 0; i < this.roles.length; i++) {
							if (this.roles[i].roleName == this.rulesForm.role) {
								this.tableName = this.roles[i].tableName;
							}
						}
					} else {
						this.tableName = this.roles[0].tableName;
						this.rulesForm.role = this.roles[0].roleName;
					}
				}
		
				this.loginPost()
			},
			loginPost() {
				this.$http({
					url: `${this.tableName}/login?username=${this.rulesForm.username}&password=${this.rulesForm.password}`,
					method: "post"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.$storage.set("Token", data.token);
						this.$storage.set("role", this.rulesForm.role);
						this.$storage.set("sessionTable", this.tableName);
						this.$storage.set("adminName", this.rulesForm.username);
						this.$nextTick(()=>{
							this.$http({
								url: this.tableName + '/session',
								method: "get"
							}).then(({
								data
							}) => {
								if (data && data.code === 0) {
									if(this.tableName == 'yuangong') {
										this.$storage.set('headportrait',data.data.touxiang)
									}
									if(this.tableName == 'renshiguanliyuan') {
										this.$storage.set('headportrait',data.data.touxiang)
									}
									if(this.tableName == 'users') {
										this.$storage.set('headportrait',data.data.image)
									}
									this.$storage.set('userForm',JSON.stringify(data.data))
									this.$storage.set('userid',data.data.id);
								} else {
									let message = this.$message
									message.error(data.msg);
								}
								this.$router.replace({ path: "/" });
							});
						})
					}
					else {
						this.$message.error(data.msg);
					}
				});
			},
		}
	}
</script>

<style lang="scss" scoped>
.login-container {
	min-height: 100vh;
	position: relative;
	background-repeat: no-repeat;
	background-position: center center;
	background-size: cover;
	background: url(http://codegen.caihongy.cn/20250717/b366c49e196e42409a70a6251e461de6.jpg);
	background-repeat: no-repeat !important;
	background-size: cover !important;
	background: url(http://codegen.caihongy.cn/20250717/b366c49e196e42409a70a6251e461de6.jpg);
	display: flex;
	width: 100%;
	min-height: 100vh;
	justify-content: center;
	align-items: center;
	background-position: center bottom;

	.login_form {
		padding: 40px 5% 20px;
		margin: 0 0 0 auto;
		z-index: 1;
		display: flex;
		min-height: 100vh;
		flex-wrap: wrap;
		border-radius: 0;
		flex-direction: column;
		background: #fff;
		width: 40%;
		justify-content: center;
		align-items: flex-end;
		height: auto;
		.login_form2 {
			padding: 0;
			margin: 40px 0 0;
			background: none;
			width: 100%;
		}
		.title-container {
			padding: 0;
			margin: 10px 0 20px 0;
			text-shadow: none;
			color: #333;
			font-weight: 600;
			width: 100%;
			font-size: 24px;
			line-height: 44px;
			text-align: center;
		}
		.list-item {
			border: 1px solid #d6d6d6;
			border-radius: 0px;
			padding: 0 20px;
			margin: 0 auto 25px;
			background: #fff;
			display: flex;
			width: 100%;
			align-items: center;
			.lable {
				padding: 0 5px 0 0;
				color: #333;
				width: 100px;
				font-size: 16px;
				border-color: #d1d1d1;
				border-width: 0 0 0px;
				line-height: 44px;
				border-style: solid;
				text-align: right;
				height: 44px;
			}
			input {
				border-radius: 0px;
				padding: 0 10px;
				color: #333;
				background: none;
				flex: 1;
				width: 100%;
				font-size: 16px;
				border-color: #d1d1d1;
				border-width: 0 0 0px;
				border-style: solid;
				height: 44px;
			}
			input:focus {
				border-radius: 0px;
				padding: 0 10px;
				color: #1c6ab2;
				flex: 1;
				background: none;
				width: 100%;
				font-size: 16px;
				border-color: #4c7bf5;
				border-width: 0 0 0px;
				border-style: solid;
				height: 44px;
			}
			.password-box {
				flex: 1;
				display: flex;
				width: 100%;
				position: relative;
				align-items: center;
				input {
					border-radius: 0px;
					padding: 0 10px;
					color: #333;
					background: none;
					flex: 1;
					width: 100%;
					font-size: 16px;
					border-color: #d1d1d1;
					border-width: 0 0 0px;
					border-style: solid;
					height: 44px;
				}
				input:focus {
					border-radius: 0px;
					padding: 0 10px;
					color: #1c6ab2;
					flex: 1;
					background: none;
					width: 100%;
					font-size: 16px;
					border-color: #4c7bf5;
					border-width: 0 0 0px;
					border-style: solid;
					height: 44px;
				}
				.iconfont {
					cursor: pointer;
					z-index: 1;
					color: #000;
					top: 0;
					font-size: 16px;
					line-height: 44px;
					position: absolute;
					right: 5px;
				}
			}
			input::placeholder {
				color: #666;
				font-size: 16px;
			}
			/deep/ .el-select {
				flex: 1;
				width: 100%;
			}
			/deep/ .el-select .el-input__inner {
				border-radius: 0px;
				padding: 0 10px;
				color: #333;
				background: none;
				width: 100%;
				font-size: 16px;
				border-color: #d1d1d1;
				border-width: 0 0 0px;
				border-style: solid;
				height: 44px;
			}
			/deep/ .el-select .is-focus .el-input__inner {
				border-radius: 0px;
				padding: 0 10px;
				color: #1c6ab2;
				background: none;
				width: 100%;
				font-size: 16px;
				border-color: #4c7bf5;
				border-width: 0 0 0px;
				border-style: solid;
				height: 44px;
			}
			/deep/ .el-select .el-input__inner::placeholder{
				color: #666;
				font-size: 16px;
			}
		}
		.login-btn {
			margin: 50px auto;
			display: flex;
			width: 100%;
			justify-content: space-between;
			align-items: center;
			flex-wrap: wrap;
			.login-btn1 {
				width: 100%;
			}
			.login-btn2 {
				padding: 0;
				z-index: 9;
				width: auto;
				order: 5;
			}
			.login-btn3 {
				z-index: 1;
				width: auto;
				text-align: right;
				order: 5;
			}
			.loginInBt {
				border: 0;
				cursor: pointer;
				border-radius: 0px;
				padding: 0 24px;
				margin: 0 0 20px;
				outline: none;
				color: #fff;
				background: #1c6ab2;
				width: 100%;
				font-size: 22px;
				height: 50px;
			}
			.loginInBt:hover {
				opacity: 0.8;
			}
			.register {
				border: 0px solid #333;
				cursor: pointer;
				border-radius: 0px;
				padding: 0 10px;
				margin: 0 10px 15px 0;
				outline: none;
				color: #1c6ab2;
				background: #aaceec;
				width: auto;
				font-size: 16px;
				height: 36px;
			}
			.register:hover {
				opacity: 0.8;
			}
			.forget {
				border: 0;
				cursor: pointer;
				border-radius: 0px;
				padding: 0 10px;
				margin: 0 0 15px 5px;
				outline: none;
				color: #1c6ab2;
				background: #aaceec;
				width: auto;
				font-size: 15px;
				height: 36px;
			}
			.forget:hover {
				opacity: 0.8;
			}
		}
	}
	.idea-box1 {
		margin: 0 4% 0 0;
		font-weight: 600;
		width: 550px;
		font-size: 42px;
		height: auto;
		order: -2;
	}
	.idea-box2 {
		margin: 0 4% 0 0;
		color: #333;
		width: 550px;
		font-size: 32px;
		height: auto;
		order: -1;
	}
}

</style>
