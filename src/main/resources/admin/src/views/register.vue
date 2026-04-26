<template>
	<div>
		<div class="register-container" :style="{'backgroundImage': indexBgUrl?`url(${$base.url + indexBgUrl})`:''}">
			<el-form v-if="pageFlag=='register'" ref="ruleForm" class="rgs-form animate__animated animate__" :model="ruleForm" :rules="rules">
				<div class="rgs-form2">
					<div class="title">基于SpringBoot和Shiro的企业考勤与薪资系统设计与实现</div>
					<el-form-item class="list-item" v-if="tableName=='yuangong'">
						<div class="lable" :class="changeRules('gonghao')?'required':''">工号：</div>
						<el-input  v-model="ruleForm.gonghao" :readonly="ro.gonghao" autocomplete="off" placeholder="工号"  type="text"  />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yuangong'">
						<div class="lable" :class="changeRules('mima')?'required':''">密码：</div>
						<el-input  v-model="ruleForm.mima" :readonly="ro.mima" autocomplete="off" placeholder="密码"  type="password"  />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yuangong'">
						<div class="lable" :class="changeRules('mima')?'required':''">确认密码：</div>
						<el-input  v-model="ruleForm.mima2" autocomplete="off" placeholder="确认密码" type="password" :readonly="ro.mima" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yuangong'">
						<div class="lable" :class="changeRules('xingming')?'required':''">姓名：</div>
						<el-input  v-model="ruleForm.xingming" :readonly="ro.xingming" autocomplete="off" placeholder="姓名"  type="text"  />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yuangong'">
						<div class="lable" :class="changeRules('xingbie')?'required':''">性别：</div>
						<el-select filterable v-model="ruleForm.xingbie" placeholder="请选择性别" :disabled="ro.xingbie">
							<el-option
								v-for="(item,index) in yuangongxingbieOptions"
								v-bind:key="index"
								:label="item"
								:value="item">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yuangong'">
						<div class="lable" :class="changeRules('shouji')?'required':''">手机：</div>
						<el-input  v-model="ruleForm.shouji" :readonly="ro.shouji" autocomplete="off" placeholder="手机"  type="text"  />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yuangong'">
						<div class="lable" :class="changeRules('shenfenzheng')?'required':''">身份证：</div>
						<el-input  v-model="ruleForm.shenfenzheng" :readonly="ro.shenfenzheng" autocomplete="off" placeholder="身份证"  type="text"  />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yuangong'">
						<div class="lable" :class="changeRules('touxiang')?'required':''">头像：</div>
						<file-upload
							tip="点击上传头像"
							action="file/upload"
							:limit="3"
							:multiple="true"
							:fileUrls="ruleForm.touxiang?ruleForm.touxiang:''"
							@change="yuangongtouxiangUploadChange"
						></file-upload>
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='renshiguanliyuan'">
						<div class="lable" :class="changeRules('guanlizhanghao')?'required':''">管理账号：</div>
						<el-input  v-model="ruleForm.guanlizhanghao" :readonly="ro.guanlizhanghao" autocomplete="off" placeholder="管理账号"  type="text"  />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='renshiguanliyuan'">
						<div class="lable" :class="changeRules('mima')?'required':''">密码：</div>
						<el-input  v-model="ruleForm.mima" :readonly="ro.mima" autocomplete="off" placeholder="密码"  type="password"  />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='renshiguanliyuan'">
						<div class="lable" :class="changeRules('mima')?'required':''">确认密码：</div>
						<el-input  v-model="ruleForm.mima2" autocomplete="off" placeholder="确认密码" type="password" :readonly="ro.mima" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='renshiguanliyuan'">
						<div class="lable" :class="changeRules('guanlixingming')?'required':''">管理姓名：</div>
						<el-input  v-model="ruleForm.guanlixingming" :readonly="ro.guanlixingming" autocomplete="off" placeholder="管理姓名"  type="text"  />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='renshiguanliyuan'">
						<div class="lable" :class="changeRules('xingbie')?'required':''">性别：</div>
						<el-select filterable v-model="ruleForm.xingbie" placeholder="请选择性别" :disabled="ro.xingbie">
							<el-option
								v-for="(item,index) in renshiguanliyuanxingbieOptions"
								v-bind:key="index"
								:label="item"
								:value="item">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='renshiguanliyuan'">
						<div class="lable" :class="changeRules('lianxidianhua')?'required':''">联系电话：</div>
						<el-input  v-model="ruleForm.lianxidianhua" :readonly="ro.lianxidianhua" autocomplete="off" placeholder="联系电话"  type="text"  />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='renshiguanliyuan'">
						<div class="lable" :class="changeRules('shenfenzheng')?'required':''">身份证：</div>
						<el-input  v-model="ruleForm.shenfenzheng" :readonly="ro.shenfenzheng" autocomplete="off" placeholder="身份证"  type="text"  />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='renshiguanliyuan'">
						<div class="lable" :class="changeRules('touxiang')?'required':''">头像：</div>
						<file-upload
							tip="点击上传头像"
							action="file/upload"
							:limit="3"
							:multiple="true"
							:fileUrls="ruleForm.touxiang?ruleForm.touxiang:''"
							@change="renshiguanliyuantouxiangUploadChange"
						></file-upload>
					</el-form-item>
					<div class="register-btn">
						<div class="register-btn1">
							<button type="button" class="r-btn" @click="login()">注册</button>
						</div>
						<div class="register-btn2">
							<div class="r-login" @click="close()">已有账号，直接登录</div>
						</div>
					</div>
				</div>
				<div class="idea-box1">Hello~</div>
				<div class="idea-box2">欢迎您注册系统</div>
			</el-form>
		</div>
	</div>
</template>

<script>
	import 'animate.css'
export default {
	data() {
		return {
			ruleForm: {
			},
			forgetForm: {},
            pageFlag : '',
			tableName:"",
			rules: {},
			ro: {},
            yuangongxingbieOptions: [],
            renshiguanliyuanxingbieOptions: [],
			indexBgUrl: '',
		};
	},
	mounted(){
		this.pageFlag = this.$route.query.pageFlag
		if(this.$route.query.pageFlag=='register'){

			let table = this.$storage.get("loginTable");
			this.tableName = table;
			if(this.tableName=='yuangong'){
				this.ruleForm = {
					gonghao: '',
					mima: '',
					xingming: '',
					xingbie: '',
					shouji: '',
					shenfenzheng: '',
					touxiang: '',
					qingjiatianshu: '0',
					zhiwei: '实习生',
				}
				this.ro = {
					gonghao: false,
					mima: false,
					xingming: false,
					xingbie: false,
					shouji: false,
					shenfenzheng: false,
					touxiang: false,
					qingjiatianshu: true,
					zhiwei: true,
				}
			}
			if(this.tableName=='renshiguanliyuan'){
				this.ruleForm = {
					guanlizhanghao: '',
					mima: '',
					guanlixingming: '',
					xingbie: '',
					lianxidianhua: '',
					shenfenzheng: '',
					touxiang: '',
				}
				this.ro = {
					guanlizhanghao: false,
					mima: false,
					guanlixingming: false,
					xingbie: false,
					lianxidianhua: false,
					shenfenzheng: false,
					touxiang: false,
				}
			}
			if ('yuangong' == this.tableName) {
				this.rules.gonghao = [{ required: true, message: '请输入工号', trigger: 'blur' }]
			}
			if ('yuangong' == this.tableName) {
				this.rules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }]
			}
			if ('yuangong' == this.tableName) {
				this.rules.xingming = [{ required: true, message: '请输入姓名', trigger: 'blur' }]
			}
			if ('yuangong' == this.tableName) {
				this.rules.shouji = [{ required: true, message: '请输入手机', trigger: 'blur' }]
			}
			if ('yuangong' == this.tableName) {
				this.rules.shenfenzheng = [{ required: true, message: '请输入身份证', trigger: 'blur' }]
			}
			if ('renshiguanliyuan' == this.tableName) {
				this.rules.guanlizhanghao = [{ required: true, message: '请输入管理账号', trigger: 'blur' }]
			}
			if ('renshiguanliyuan' == this.tableName) {
				this.rules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }]
			}
			if ('renshiguanliyuan' == this.tableName) {
				this.rules.guanlixingming = [{ required: true, message: '请输入管理姓名', trigger: 'blur' }]
			}
			this.yuangongxingbieOptions = "男,女".split(',')
			this.renshiguanliyuanxingbieOptions = "男,女".split(',')
		}
	},
	created() {
		this.$http.get('config/info?name=bRegisterBackgroundImg',).then(rs=>{this.indexBgUrl = rs.data.data?rs.data.data.value:''})
	},
	destroyed() {
		  	},
	methods: {
		changeRules(name){
			if(this.rules[name]){
				return true
			}
			return false
		},
		// 获取uuid
		getUUID () {
			return new Date().getTime();
		},
		close(){
			this.$router.push({ path: "/login" });
		},
        yuangongtouxiangUploadChange(fileUrls) {
            this.ruleForm.touxiang = fileUrls;
        },
        renshiguanliyuantouxiangUploadChange(fileUrls) {
            this.ruleForm.touxiang = fileUrls;
        },

        // 多级联动参数


		// 注册
		login() {
			var url=this.tableName+"/register";
			if((!this.ruleForm.gonghao) && `yuangong` == this.tableName){
				this.$message.error(`工号不能为空`);
				return
			}
			if((!this.ruleForm.mima) && `yuangong` == this.tableName){
				this.$message.error(`密码不能为空`);
				return
			}
			if((this.ruleForm.mima!=this.ruleForm.mima2) && `yuangong` == this.tableName){
				this.$message.error(`两次密码输入不一致`);
				return
			}
			if((!this.ruleForm.xingming) && `yuangong` == this.tableName){
				this.$message.error(`姓名不能为空`);
				return
			}
			if((!this.ruleForm.shouji) && `yuangong` == this.tableName){
				this.$message.error(`手机不能为空`);
				return
			}
			if((!this.ruleForm.shenfenzheng) && `yuangong` == this.tableName){
				this.$message.error(`身份证不能为空`);
				return
			}
			if(`yuangong` == this.tableName && this.ruleForm.shouji &&(!this.$validate.isMobile(this.ruleForm.shouji))){
				this.$message.error(`手机应输入手机格式`);
				return
			}
			if(`yuangong` == this.tableName && this.ruleForm.shenfenzheng &&(!this.$validate.checkIdCard(this.ruleForm.shenfenzheng))){
				this.$message.error(`身份证应输入身份证格式`);
				return
			}
            if(this.ruleForm.touxiang!=null) {
                this.ruleForm.touxiang = this.ruleForm.touxiang.replace(new RegExp(this.$base.url,"g"),"");
            }
			if((!this.ruleForm.guanlizhanghao) && `renshiguanliyuan` == this.tableName){
				this.$message.error(`管理账号不能为空`);
				return
			}
			if((!this.ruleForm.mima) && `renshiguanliyuan` == this.tableName){
				this.$message.error(`密码不能为空`);
				return
			}
			if((this.ruleForm.mima!=this.ruleForm.mima2) && `renshiguanliyuan` == this.tableName){
				this.$message.error(`两次密码输入不一致`);
				return
			}
			if((!this.ruleForm.guanlixingming) && `renshiguanliyuan` == this.tableName){
				this.$message.error(`管理姓名不能为空`);
				return
			}
			if(`renshiguanliyuan` == this.tableName && this.ruleForm.lianxidianhua &&(!this.$validate.isMobile(this.ruleForm.lianxidianhua))){
				this.$message.error(`联系电话应输入手机格式`);
				return
			}
			if(`renshiguanliyuan` == this.tableName && this.ruleForm.shenfenzheng &&(!this.$validate.checkIdCard(this.ruleForm.shenfenzheng))){
				this.$message.error(`身份证应输入身份证格式`);
				return
			}
            if(this.ruleForm.touxiang!=null) {
                this.ruleForm.touxiang = this.ruleForm.touxiang.replace(new RegExp(this.$base.url,"g"),"");
            }
			this.$http({
				url: url,
				method: "post",
				data:this.ruleForm
			}).then(({ data }) => {
				if (data && data.code === 0) {
					this.$message({
						message: "注册成功",
						type: "success",
						duration: 1500,
						onClose: () => {
							this.$router.replace({ path: "/login" });
						}
					});
				} else {
					this.$message.error(data.msg);
				}
			});
		},
	}
};
</script>

<style lang="scss" scoped>
.register-container {
	position: relative;
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
	.rgs-form {
		.rgs-form2 {
		padding: 0;
		margin: 20px 0 0;
		background: none;
		width: 100%;
		}
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
		.title {
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
			padding: 0 0 0 130px;
			margin: 0 auto 15px;
			background: #fff;
			width: 100%;
			position: relative;
			height: auto;
			/deep/ .el-form-item__content {
				display: block;
			}
			.lable {
				padding: 0 10px 0 0;
				color: #333;
				left: -130px;
				width: 130px;
				font-size: 16px;
				line-height: 44px;
				position: absolute !important;
				text-align: right;
			}
			.el-input {
				width: 100%;
			}
			.el-input /deep/ .el-input__inner {
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
			.el-input /deep/ .el-input__inner:focus {
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
			.el-input-number {
				width: 100%;
			}
			.el-input-number /deep/ .el-input__inner {
				text-align: center;
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
			.el-input-number /deep/ .el-input__inner:focus {
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
			.el-input-number /deep/ .el-input-number__decrease {
				display: none;
			}
			.el-input-number /deep/ .el-input-number__increase {
				display: none;
			}
			.el-select {
				width: 100%;
			}
			.el-select /deep/ .el-input__inner {
				border-radius: 0px;
				padding: 0 10px;
				color: #666;
				background: none;
				width: 100%;
				font-size: 16px;
				border-color: #d1d1d1;
				border-width: 0 0 0px;
				border-style: solid;
				height: 44px;
			}
			.el-select /deep/ .el-input__inner:focus {
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
			.el-date-editor {
				width: 100%;
			}
			.el-date-editor /deep/ .el-input__inner {
				border-radius: 0px;
				padding: 0 10px 0 30px;
				color: #666;
				background: none;
				width: 100%;
				font-size: 16px;
				border-color: #d1d1d1;
				border-width: 0 0 0px;
				border-style: solid;
				height: 44px;
			}
			.el-date-editor /deep/ .el-input__inner:focus {
				border-radius: 0px;
				padding: 0 10px 0 30px;
				color: #1c6ab2;
				background: none;
				width: 100%;
				font-size: 16px;
				border-color: #4c7bf5;
				border-width: 0 0 0px;
				border-style: solid;
				height: 44px;
			}
			.el-date-editor.el-input {
				width: 100%;
			}
			/deep/ .el-upload--picture-card {
				background: transparent;
				border: 0;
				border-radius: 0;
				width: auto;
				height: auto;
				line-height: initial;
				vertical-align: middle;
			}
			/deep/ .upload .upload-img {
				border: 1px solid #ddd;
				cursor: pointer;
				border-radius: 4px;
				margin: 10px 0 0;
				color: #999;
				width: 90px;
				font-size: 26px;
				line-height: 60px;
				text-align: center;
				height: 60px;
			}
			/deep/ .el-upload-list .el-upload-list__item {
				border: 1px solid #ddd;
				cursor: pointer;
				border-radius: 4px;
				margin: 10px 0 0;
				color: #999;
				width: 90px;
				font-size: 26px;
				line-height: 60px;
				text-align: center;
				height: 60px;
			}
			/deep/ .el-upload .el-icon-plus {
				border: 1px solid #ddd;
				cursor: pointer;
				border-radius: 4px;
				margin: 10px 0 0;
				color: #999;
				width: 90px;
				font-size: 26px;
				line-height: 60px;
				text-align: center;
				height: 60px;
			}
			/deep/ .el-upload__tip {
				color: #666;
				font-size: 16px;
			}
			/deep/ .el-input__inner::placeholder {
				color: #666;
				font-size: 16px;
			}
			.required {
				position: relative;
			}
			.required::after{
				color: red;
				left: 120px;
				position: absolute;
				content: "*";
			}
			.editor {
				width: 100%;
				height: auto;
			}
			.editor>.avatar-uploader {
				line-height: 0;
				height: 0;
			}
		}
		.list-item.email {
			input {
				border-radius: 0px;
				padding: 0 10px;
				color: #333;
				flex: 1;
				background: none;
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
			input::placeholder {
				color: #666;
				font-size: 16px;
			}
			button {
				border: 0;
				cursor: pointer;
				padding: 0 0px;
				margin: 0;
				color: #333;
				font-size: 16px;
				border-color: #d1d1d1;
				border-radius: 0 0px 0px 0;
				box-shadow: none;
				outline: none;
				background: none;
				width: 130px;
				border-width: 0 0 0px;
				border-style: solid;
				height: 44px;
			}
			button:hover {
				color: #4c7bf5;
				border-color: #4c7bf5;
			}
		}
		.register-btn {
			width: 100%;
		}
		.register-btn1 {
			padding: 0;
			width: 100%;
		}
		.register-btn2 {
			z-index: 1;
			width: 100%;
			order: 5;
		}
		.r-btn {
			border: 0;
			cursor: pointer;
			border-radius: 0px;
			padding: 0 24px;
			margin: 10px 0;
			outline: none;
			color: #fff;
			background: #1c6ab2;
			width: 100%;
			font-size: 22px;
			height: 50px;
		}
		.r-btn:hover {
			opacity: 0.5;
		}
		.r-login {
			cursor: pointer;
			padding: 5px;
			color: #095dac;
			background: #aaceec30;
			display: inline-block;
			width: 100%;
			font-size: 16px;
			line-height: 2;
			text-align: center;
		}
		.r-login:hover {
			opacity: 0.8;
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

	::-webkit-scrollbar {
	  display: none;
	}
</style>
