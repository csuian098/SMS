<template>
	<div :style='{"padding":"50px 30px 30px"}'>
		<el-form
			:style='{"border":"0px solid #ddd","padding":"30px","boxShadow":"none","borderRadius":"10px","flexWrap":"wrap","background":"#ffffff","display":"flex"}'
			class="add-update-preview"
			ref="ruleForm"
			:model="ruleForm"
			label-width="100%"
		>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}'   v-if="flag=='yuangong'"  label="工号" prop="gonghao">
					<el-input v-model="ruleForm.gonghao" :readonly="ro.gonghao" placeholder="工号" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}'   v-if="flag=='yuangong'"  label="姓名" prop="xingming">
					<el-input v-model="ruleForm.xingming" :readonly="ro.xingming" placeholder="姓名" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}' v-if="flag=='yuangong'"  label="性别" prop="xingbie">
					<el-select filterable v-model="ruleForm.xingbie" :disabled="ro.xingbie" placeholder="请选择性别">
						<el-option
							v-for="(item,index) in yuangongxingbieOptions"
							v-bind:key="index"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}'   v-if="flag=='yuangong'"  label="手机" prop="shouji">
					<el-input v-model="ruleForm.shouji" :readonly="ro.shouji" placeholder="手机" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}'   v-if="flag=='yuangong'"  label="身份证" prop="shenfenzheng">
					<el-input v-model="ruleForm.shenfenzheng" :readonly="ro.shenfenzheng" placeholder="身份证" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}' v-if="flag=='yuangong'" label="头像" prop="touxiang">
					<file-upload
						tip="点击上传头像"
						action="file/upload"
						:limit="1"
						:multiple="false"
						:fileUrls="ruleForm.touxiang?ruleForm.touxiang:''"
						@change="yuangongtouxiangUploadChange"
					></file-upload>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}'   v-if="flag=='yuangong'"  label="请假天数" prop="qingjiatianshu">
					<el-input v-model="ruleForm.qingjiatianshu" :readonly="ro.qingjiatianshu" placeholder="请假天数" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}'   v-if="flag=='yuangong'"  label="职位" prop="zhiwei">
					<el-input v-model="ruleForm.zhiwei" :readonly="ro.zhiwei" placeholder="职位" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}'   v-if="flag=='renshiguanliyuan'"  label="管理账号" prop="guanlizhanghao">
					<el-input v-model="ruleForm.guanlizhanghao" :readonly="ro.guanlizhanghao" placeholder="管理账号" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}'   v-if="flag=='renshiguanliyuan'"  label="管理姓名" prop="guanlixingming">
					<el-input v-model="ruleForm.guanlixingming" :readonly="ro.guanlixingming" placeholder="管理姓名" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}' v-if="flag=='renshiguanliyuan'"  label="性别" prop="xingbie">
					<el-select filterable v-model="ruleForm.xingbie" :disabled="ro.xingbie" placeholder="请选择性别">
						<el-option
							v-for="(item,index) in renshiguanliyuanxingbieOptions"
							v-bind:key="index"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}'   v-if="flag=='renshiguanliyuan'"  label="联系电话" prop="lianxidianhua">
					<el-input v-model="ruleForm.lianxidianhua" :readonly="ro.lianxidianhua" placeholder="联系电话" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}'   v-if="flag=='renshiguanliyuan'"  label="身份证" prop="shenfenzheng">
					<el-input v-model="ruleForm.shenfenzheng" :readonly="ro.shenfenzheng" placeholder="身份证" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}' v-if="flag=='renshiguanliyuan'" label="头像" prop="touxiang">
					<file-upload
						tip="点击上传头像"
						action="file/upload"
						:limit="1"
						:multiple="false"
						:fileUrls="ruleForm.touxiang?ruleForm.touxiang:''"
						@change="renshiguanliyuantouxiangUploadChange"
					></file-upload>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}' v-if="flag=='users'" label="用户名" prop="username">
					<el-input v-model="ruleForm.username" placeholder="用户名"></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(33.33% - 40px)","margin":"0 20px 20px"}' v-if="flag=='users'" label="头像" prop="image">
					<file-upload
						tip="点击上传头像"
						action="file/upload"
						:limit="1"
						:multiple="false"
						:fileUrls="ruleForm.image?ruleForm.image:''"
						@change="usersimageUploadChange"
					></file-upload>
				</el-form-item>
				<el-form-item :style='{"width":"100%","padding":"0 30px","margin":"20px auto 0","justifyContent":"flex-end","display":"flex"}'>
					<el-button class="btn3" type="primary" @click="onUpdateHandler">
						<span class="icon iconfont icon-queren15"></span>
						提交
					</el-button>
				</el-form-item>
		</el-form>
	</div>
</template>
<script>
// 校验引入
	import { 
		isNumber,
		isMobile,
		checkIdCard,
	} from "@/utils/validate";

	export default {
		data() {
			return {
				ruleForm: {},
				flag: '',
				usersFlag: false,
				yuangongxingbieOptions: [],
				renshiguanliyuanxingbieOptions: [],
				ro: {}
			};
		},
		mounted() {
			var table = this.$storage.get("sessionTable");
			this.flag = table;
			this.$http({
				url: `${this.$storage.get("sessionTable")}/session`,
				method: "get"
			}).then(({ data }) => {
				if (data && data.code === 0) {
					if(table == 'yuangong') {
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
					if(table == 'renshiguanliyuan') {
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

					this.ruleForm = data.data;
				} else {
					this.$message.error(data.msg);
				}
			});
			this.yuangongxingbieOptions = "男,女".split(',')
			this.renshiguanliyuanxingbieOptions = "男,女".split(',')
		},
		methods: {
			yuangongtouxiangUploadChange(fileUrls) {
				this.ruleForm.touxiang = fileUrls;
			},
			renshiguanliyuantouxiangUploadChange(fileUrls) {
				this.ruleForm.touxiang = fileUrls;
			},
			usersimageUploadChange(fileUrls) {
				this.ruleForm.image = fileUrls;
			},
			onUpdateHandler() {
				if((!this.ruleForm.gonghao)&& 'yuangong'==this.flag){
					this.$message.error('工号不能为空');
					return
				}
				if((!this.ruleForm.mima)&& 'yuangong'==this.flag){
					this.$message.error('密码不能为空');
					return
				}
				if((!this.ruleForm.xingming)&& 'yuangong'==this.flag){
					this.$message.error('姓名不能为空');
					return
				}
        if((!this.ruleForm.shouji)&& 'yuangong'==this.flag){
          this.$message.error('手机不能为空');
          return
        }
        if((!this.ruleForm.shenfenzheng)&& 'yuangong'==this.flag){
          this.$message.error('身份证不能为空');
          return
        }
				if('yuangong' ==this.flag && this.ruleForm.shouji&&(!isMobile(this.ruleForm.shouji))){
					this.$message.error(`手机应输入手机格式`);
					return
				}
				if('yuangong' ==this.flag && this.ruleForm.shenfenzheng&&(!checkIdCard(this.ruleForm.shenfenzheng))){
					this.$message.error(`身份证应输入身份证格式`);
					return
				}
				if(this.ruleForm.touxiang!=null) {
					this.ruleForm.touxiang = this.ruleForm.touxiang.replace(new RegExp(this.$base.url,"g"),"");
				}
				if('yuangong' ==this.flag && this.ruleForm.qingjiatianshu&&(!isNumber(this.ruleForm.qingjiatianshu))){
					this.$message.error(`请假天数应输入数字`);
					return
				}
				if((!this.ruleForm.guanlizhanghao)&& 'renshiguanliyuan'==this.flag){
					this.$message.error('管理账号不能为空');
					return
				}
				if((!this.ruleForm.mima)&& 'renshiguanliyuan'==this.flag){
					this.$message.error('密码不能为空');
					return
				}
				if((!this.ruleForm.guanlixingming)&& 'renshiguanliyuan'==this.flag){
					this.$message.error('管理姓名不能为空');
					return
				}
				if('renshiguanliyuan' ==this.flag && this.ruleForm.lianxidianhua&&(!isMobile(this.ruleForm.lianxidianhua))){
					this.$message.error(`联系电话应输入手机格式`);
					return
				}
				if('renshiguanliyuan' ==this.flag && this.ruleForm.shenfenzheng&&(!checkIdCard(this.ruleForm.shenfenzheng))){
					this.$message.error(`身份证应输入身份证格式`);
					return
				}
				if(this.ruleForm.touxiang!=null) {
					this.ruleForm.touxiang = this.ruleForm.touxiang.replace(new RegExp(this.$base.url,"g"),"");
				}
				if('users'==this.flag && this.ruleForm.username.trim().length<1) {
					this.$message.error(`用户名不能为空`);
					return	
				}
				if(this.flag=='users'){
					this.ruleForm.image = this.ruleForm.image.replace(new RegExp(this.$base.url,"g"),"")
				}
				this.$http({
					url: `${this.$storage.get("sessionTable")}/update`,
					method: "post",
					data: this.ruleForm
				}).then(({ data }) => {
					if (data && data.code === 0) {
						if(this.flag=='users'){
							this.$storage.set('headportrait',this.ruleForm.image)
						}else {
							if(this.flag == 'yuangong') {
								this.$storage.set('headportrait',this.ruleForm.touxiang)
							}
							if(this.flag == 'renshiguanliyuan') {
								this.$storage.set('headportrait',this.ruleForm.touxiang)
							}
						}
						this.$message({
							message: "修改信息成功",
							type: "success",
							duration: 1500,
							onClose: () => {
								window.location.reload();
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
	.el-date-editor.el-input {
		width: auto;
	}
	
	.add-update-preview .el-form-item /deep/ .el-form-item__label {
				padding: 0 10px;
				color: #666;
				white-space: nowrap;
				font-weight: 500;
				width: 100%;
				font-size: 16px;
				line-height: 40px;
				text-align: left;
			}
	
	.add-update-preview .el-form-item /deep/ .el-form-item__content {
		margin-left: auto !important;
		margin: auto !important;
		display: flex;
		width: 100%;
		justify-content: flex-start;
		align-items: flex-start;
		flex-wrap: wrap;
	}
	
	.add-update-preview .el-input /deep/ .el-input__inner {
				border-radius: 10px;
				padding: 0 12px;
				box-shadow: none;
				outline: none;
				color: #333;
				background: #fafafa;
				width: 100%;
				font-size: 16px;
				border-color: #e8e9eb;
				border-width: 1px;
				border-style: solid;
				height: 50px;
			}
	
	.add-update-preview .el-select /deep/ .el-input__inner {
				border-radius: 10px;
				padding: 0 10px;
				box-shadow: none;
				outline: none;
				color: #333;
				background: #fafafa;
				width: 100%;
				font-size: 16px;
				border-color: #e8e9eb;
				border-width: 1px;
				border-style: solid;
				height: 50px;
			}
	
	.add-update-preview .el-date-editor /deep/ .el-input__inner {
				border-radius: 10px;
				padding: 0 10px 0 30px;
				box-shadow: none;
				outline: none;
				color: #333;
				background: #fafafa;
				width: 100%;
				font-size: 16px;
				border-color: #e8e9eb;
				border-width: 1px;
				border-style: solid;
				height: 50px;
			}
	
	.add-update-preview /deep/ .el-upload--picture-card {
		background: transparent;
		border: 0;
		border-radius: 0;
		width: auto;
		height: auto;
		line-height: initial;
		vertical-align: middle;
	}
	
	.add-update-preview /deep/ .el-upload-list .el-upload-list__item {
				border: 1px solid #e8e9eb;
				cursor: pointer;
				border-radius: 10px;
				color: #999;
				background: #fafafa;
				font-weight: 600;
				width: 80px;
				font-size: 30px;
				line-height: 80px;
				text-align: center;
				height: 80px;
			}
	
	.add-update-preview /deep/ .el-upload .el-icon-plus {
				border: 1px solid #e8e9eb;
				cursor: pointer;
				border-radius: 10px;
				color: #999;
				background: #fafafa;
				font-weight: 600;
				width: 80px;
				font-size: 30px;
				line-height: 80px;
				text-align: center;
				height: 80px;
			}
	
	.add-update-preview .el-textarea /deep/ .el-textarea__inner {
				border-radius: 10px;
				padding: 12px;
				box-shadow: none;
				outline: none;
				color: #666;
				background: #fafafa;
				width: 100%;
				font-size: 16px;
				border-color: #e8e9eb;
				border-width: 1px;
				border-style: solid;
				height: auto;
			}
	
	.add-update-preview .btn3 {
				border: 1px solid #fac115;
				cursor: pointer;
				border-radius: 4px;
				padding: 0 24px;
				margin: 4px;
				outline: none;
				color: #fac115;
				background: none;
				width: auto;
				font-size: 16px;
				height: 40px;
				.iconfont {
						margin: 0 2px;
						color: #fff;
						display: none;
						font-size: 16px;
						height: 40px;
					}
	}
	
	.add-update-preview .btn3:hover {
				opacity: 0.8;
			}
	.editor>.avatar-uploader {
		line-height: 0;
		height: 0;
	}
</style>
