
















<template>
	<div class="addEdit-block">
		<el-form
			class="add-update-preview"
			ref="ruleForm"
			:model="ruleForm"
			:rules="rules"
			label-width="100%"
		>
			<template >
				<el-form-item class="select" v-if="type!='info'" label="姓名" prop="xingming" >
					<el-select :disabled="ro.xingming" @change="xingmingChange" v-model="ruleForm.xingming" placeholder="请选择姓名" filterable>
						<el-option
							v-for="(item,index) in xingmingOptions"
							v-bind:key="index"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="input" v-else-if="ruleForm.xingming" label="姓名" prop="xingming" >
					<el-input v-model="ruleForm.xingming" placeholder="姓名" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="工号" prop="gonghao" >
					<el-input v-model="ruleForm.gonghao" placeholder="工号" clearable  :readonly="ro.gonghao"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="工号" prop="gonghao" >
					<el-input v-model="ruleForm.gonghao" placeholder="工号" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="原职位" prop="zhiwei" >
					<el-input v-model="ruleForm.zhiwei" placeholder="原职位" clearable  :readonly="ro.zhiwei"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="原职位" prop="zhiwei" >
					<el-input v-model="ruleForm.zhiwei" placeholder="原职位" readonly></el-input>
				</el-form-item>
				<el-form-item class="select" v-if="type!='info'"  label="现职位" prop="xianzhiwei" >
					<el-select :disabled="ro.xianzhiwei" v-model="ruleForm.xianzhiwei" placeholder="请选择现职位"  filterable>
						<el-option
							v-for="(item,index) in xianzhiweiOptions"
							v-bind:key="index"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item v-else class="input" label="现职位" prop="xianzhiwei" >
					<el-input v-model="ruleForm.xianzhiwei"
						placeholder="现职位" readonly></el-input>
				</el-form-item>
				<el-form-item class="date" v-if="type!='info'" label="变动日期" prop="biandongriqi" >
					<el-date-picker
						format="yyyy 年 MM 月 dd 日"
						value-format="yyyy-MM-dd"
						v-model="ruleForm.biandongriqi" 
						type="date"
						:readonly="ro.biandongriqi"
						placeholder="变动日期"
					></el-date-picker> 
				</el-form-item>
				<el-form-item class="input" v-else-if="ruleForm.biandongriqi" label="变动日期" prop="biandongriqi" >
					<el-input v-model="ruleForm.biandongriqi" placeholder="变动日期" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="管理账号" prop="guanlizhanghao" >
					<el-input v-model="ruleForm.guanlizhanghao" placeholder="管理账号" clearable  :readonly="ro.guanlizhanghao"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="管理账号" prop="guanlizhanghao" >
					<el-input v-model="ruleForm.guanlizhanghao" placeholder="管理账号" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="管理姓名" prop="guanlixingming" >
					<el-input v-model="ruleForm.guanlixingming" placeholder="管理姓名" clearable  :readonly="ro.guanlixingming"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="管理姓名" prop="guanlixingming" >
					<el-input v-model="ruleForm.guanlixingming" placeholder="管理姓名" readonly></el-input>
				</el-form-item>
			</template>
			<el-form-item class="textarea" v-if="type!='info'" label="变动原因" prop="biandongyuanyin" >
				<el-input
					style="min-width: 200px; max-width: 600px;"
					type="textarea"
					:rows="8"
					placeholder="变动原因"
					v-model="ruleForm.biandongyuanyin" >
				</el-input>
			</el-form-item>
			<el-form-item v-else-if="ruleForm.biandongyuanyin" label="变动原因" prop="biandongyuanyin"  class="textBox">
				<span class="text">{{ruleForm.biandongyuanyin}}</span>
			</el-form-item>
			<el-form-item class="btn">
				<el-button class="btn3"  v-if="type!='info'" type="success" @click="onSubmit">
					<span class="icon iconfont icon-queren15"></span>
					提交
				</el-button>
				<el-button class="btn4" v-if="type!='info'" type="success" @click="back()">
					<span class="icon iconfont icon-guanbi2"></span>
					取消
				</el-button>
				<el-button class="btn5" v-if="type=='info'" type="success" @click="back()">
					<span class="icon iconfont icon-fanhui13"></span>
					返回
				</el-button>
			</el-form-item>
		</el-form>
    

	</div>
</template>
<script>
	export default {
		data() {
			return {
				id: '',
				type: '',
			
			
				ro:{
					xingming : false,
					gonghao : false,
					zhiwei : false,
					xianzhiwei : false,
					biandongyuanyin : false,
					biandongriqi : false,
					guanlizhanghao : false,
					guanlixingming : false,
				},
			
				ruleForm: {
					xingming: '',
					gonghao: '',
					zhiwei: '实习生',
					xianzhiwei: '',
					biandongyuanyin: '',
					biandongriqi: '',
					guanlizhanghao: '',
					guanlixingming: '',
				},
				xingmingOptions: [],
				xianzhiweiOptions: [],

				rules: {
					xingming: [
					],
					gonghao: [
						{ required: true, message: '工号不能为空', trigger: 'blur' },
					],
					zhiwei: [
					],
					xianzhiwei: [
					],
					biandongyuanyin: [
					],
					biandongriqi: [
					],
					guanlizhanghao: [
					],
					guanlixingming: [
					],
				},
			};
		},
		props: ["parent"],
		computed: {
			sessionForm() {
				return JSON.parse(this.$storage.getObj('userForm'))
			},
			sessionTable() {
				return this.$storage.get('sessionTable')
			},



		},
		components: {
		},
		created() {
			this.ruleForm.biandongriqi = this.getCurDate()
		},
		methods: {
			imgPreView(url){
				this.$parent.imgPreView(url)
			},
			// 下载
			download(file){
				window.open(`${file}`)
			},
			// 初始化
			init(id,type,sourceEmployeeRow) {
				this.id = '';
				this.type = type || '';
				this.ro = {
					xingming : false,
					gonghao : false,
					zhiwei : false,
					xianzhiwei : false,
					biandongyuanyin : false,
					biandongriqi : false,
					guanlizhanghao : false,
					guanlixingming : false,
				};
				this.ruleForm = {
					xingming: '',
					gonghao: '',
					zhiwei: '实习生',
					xianzhiwei: '',
					biandongyuanyin: '',
					biandongriqi: this.getCurDate(),
					guanlizhanghao: '',
					guanlixingming: '',
				};
				if (id) {
					this.id = id;
				}
				if(this.type=='fromEmployee') {
					if(sourceEmployeeRow) {
						this.ruleForm.xingming = sourceEmployeeRow.xingming || '';
						this.ruleForm.gonghao = sourceEmployeeRow.gonghao || '';
						this.ruleForm.zhiwei = sourceEmployeeRow.zhiwei || '';
						this.ruleForm.xianzhiwei = sourceEmployeeRow.zhiwei || '';
					}
					this.id = '';
					this.ro.xingming = true;
					this.ro.gonghao = true;
					this.ro.zhiwei = true;
					if((!this.ruleForm.gonghao || !this.ruleForm.xingming) && id){
						this.$http({
							url: `yuangong/info/${id}`,
							method: "get"
						}).then(({ data }) => {
							if (data && data.code === 0) {
								this.ruleForm.xingming = data.data.xingming || '';
								this.ruleForm.gonghao = data.data.gonghao || '';
								this.ruleForm.zhiwei = data.data.zhiwei || '';
								this.ruleForm.xianzhiwei = data.data.zhiwei || '';
							}
						});
					}
				} else if((this.type=='info'||this.type=='else'||this.type=='msg') && id){
					this.info(id);
				}else if(this.type=='logistics'){
					for(let x in this.ro) {
						this.ro[x] = true
					}
					this.logistics=false;
					this.info(id);
				}else if(this.type=='cross'){
					var obj = this.$storage.getObj('crossObj');
					for (var o in obj){
						if(o=='xingming'){
							this.ruleForm.xingming = obj[o];
							this.ro.xingming = true;
							continue;
						}
						if(o=='gonghao'){
							this.ruleForm.gonghao = obj[o];
							this.ro.gonghao = true;
							continue;
						}
						if(o=='zhiwei'){
							this.ruleForm.zhiwei = obj[o];
							this.ro.zhiwei = true;
							continue;
						}
						if(o=='xianzhiwei'){
							this.ruleForm.xianzhiwei = obj[o];
							this.ro.xianzhiwei = true;
							continue;
						}
						if(o=='biandongyuanyin'){
							this.ruleForm.biandongyuanyin = obj[o];
							this.ro.biandongyuanyin = true;
							continue;
						}
						if(o=='biandongriqi'){
							this.ruleForm.biandongriqi = obj[o];
							this.ro.biandongriqi = true;
							continue;
						}
						if(o=='guanlizhanghao'){
							this.ruleForm.guanlizhanghao = obj[o];
							this.ro.guanlizhanghao = true;
							continue;
						}
						if(o=='guanlixingming'){
							this.ruleForm.guanlixingming = obj[o];
							this.ro.guanlixingming = true;
							continue;
						}
					}
					this.ruleForm.zhiwei = '实习生'; 				}
				// 获取用户信息
				this.$http({
					url: `${this.sessionTable}/session`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						var json = data.data;
						if(this.sessionTable!="users") {
							this.ro.zhiwei = true;
						}
						if(this.sessionTable!="users") {
							this.ro.zhiwei = true;
						}
						if(((json.guanlizhanghao!=''&&json.guanlizhanghao) || json.guanlizhanghao==0) && this.sessionTable!="users"){
							this.ruleForm.guanlizhanghao = json.guanlizhanghao
							this.ro.guanlizhanghao = true;
						}
						if(((json.guanlixingming!=''&&json.guanlixingming) || json.guanlixingming==0) && this.sessionTable!="users"){
							this.ruleForm.guanlixingming = json.guanlixingming
							this.ro.guanlixingming = true;
						}
					} else {
						this.$message.error(data.msg);
					}
				});
				this.$http({
					url: `option/yuangong/xingming`,
					method: "get",
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.xingmingOptions = data.data;
					} else {
						this.$message.error(data.msg);
					}
				});
				this.$http({
					url: `option/zhiweixinxi/zhiwei`,
					method: "get",
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.xianzhiweiOptions = data.data;
					} else {
						this.$message.error(data.msg);
					}
				});
			
			},
			// 下二随
			xingmingChange () {
				this.$http({
					url: `follow/yuangong/xingming?columnValue=`+ this.ruleForm.xingming,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						if(data.data.gonghao){
							this.ruleForm.gonghao = data.data.gonghao
						}
						if(data.data.zhiwei){
							this.ruleForm.zhiwei = data.data.zhiwei
						}
					} else {
						this.$message.error(data.msg);
					}
				});
			},
			// 多级联动参数

			async info(id) {
				await this.$http({
					url: `zhiweidiaodong/info/${id}`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.ruleForm = data.data;
						//解决前台上传图片后台不显示的问题
						let reg=new RegExp('../../../upload','g')//g代表全部
					} else {
						this.$message.error(data.msg);
					}
				});
			},

			// 提交
			async onSubmit() {
					var objcross = this.$storage.getObj('crossObj');
					if(!this.ruleForm.id) {
						delete this.ruleForm.userid
					}
					await this.$refs["ruleForm"].validate(async valid => {
						if (valid) {
							if(this.type=='cross'){
								var statusColumnName = this.$storage.get('statusColumnName');
								var statusColumnValue = this.$storage.get('statusColumnValue');
								if(statusColumnName!='') {
									var obj = this.$storage.getObj('crossObj');
									if(statusColumnName && !statusColumnName.startsWith("[")) {
										for (var o in obj){
											if(o==statusColumnName){
												obj[o] = statusColumnValue;
											}
										}
										var table = this.$storage.get('crossTable');
										await this.$http({
											url: `${table}/update`,
											method: "post",
											data: obj
										}).then(({ data }) => {});
									}
								}
							}
							await this.$http({
								url: `zhiweidiaodong/${!this.ruleForm.id ? "save" : "update"}`,
								method: "post",
								data: this.ruleForm
							}).then(async ({ data }) => {
								if (data && data.code === 0) {
									this.$message({
										message: "操作成功",
										type: "success",
										duration: 1500,
										onClose: () => {
											this.parent.showFlag = true;
											this.parent.addOrUpdateFlag = false;
											this.parent.zhiweidiaodongCrossAddOrUpdateFlag = false;
											this.parent.search();
										}
									});
								} else {
									this.$message.error(data.msg);
								}
							});
						}
					});
			},
			// 获取uuid
			getUUID () {
				return new Date().getTime();
			},
			// 返回
			back() {
				this.parent.showFlag = true;
				this.parent.addOrUpdateFlag = false;
				this.parent.zhiweidiaodongCrossAddOrUpdateFlag = false;
			},
		}
	};
</script>
<style lang="scss" scoped>
	.addEdit-block {
		padding: 30px;
	}
	.add-update-preview {
		border: 0px solid #ddd;
		border-radius: 10px;
		padding: 30px;
		box-shadow: none;
		background: #ffffff;
		display: flex;
		flex-wrap: wrap;
	}
	.amap-wrapper {
		width: 100%;
		height: 500px;
	}
	
	.search-box {
		position: absolute;
	}
	
	.el-date-editor.el-input {
		width: auto;
	}
	.add-update-preview /deep/ .el-form-item {
		margin: 0 20px 20px;
		width: calc(33.33% - 40px);
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
	.add-update-preview /deep/ .el-form-item.editorBox {
		margin: 0 20px 20px;
		width: 100%;
	}
	.add-update-preview .el-form-item.editorBox /deep/ .el-form-item__label {
		padding: 0 10px;
		color: #666;
		white-space: nowrap;
		font-weight: 500;
		display: block;
		width: 100%;
		font-size: 16px;
		line-height: 40px;
		text-align: left;
	}
	
	.add-update-preview .el-form-item.editorBox /deep/ .el-form-item__content {
		margin-left: auto !important;
		padding: 0 10px;
		margin: auto !important;
		display: flex;
		width: 100%;
		justify-content: flex-start;
		align-items: flex-start;
		flex-wrap: wrap;
	}
	.add-update-preview /deep/.el-form-item.editorBox .editor {
		box-shadow: none;
		max-width: 100% !important;
		width: 100%;
		height: auto;
	}
	.add-update-preview /deep/.el-form-item.editorBox .editor .ql-toolbar {
		border: 1px solid #eee;
		background: none;
		border-width: 1px 1px 0;
	}
	.add-update-preview /deep/.el-form-item.editorBox .editor .ql-container {
		border: 1px solid #eee;
		background: none;
		min-height: 200px;
	}
	.add-update-preview /deep/.el-form-item.editorBox .editor .ql-container .ql-blank::before {
		color: #000;
	}
	
	.add-update-preview /deep/ .el-form-item.textBox {
		margin: 0 20px 20px;
		width: 100%;
	}
	.add-update-preview .el-form-item.textBox /deep/ .el-form-item__label {
		padding: 0 10px;
		color: #666;
		white-space: nowrap;
		font-weight: 500;
		display: block;
		width: 100%;
		font-size: 16px;
		line-height: 40px;
		text-align: left;
	}
	
	.add-update-preview .el-form-item.textBox /deep/ .el-form-item__content {
		margin-left: auto !important;
		padding: 0 10px;
		margin: auto !important;
		display: flex;
		width: 100%;
		justify-content: flex-start;
		align-items: flex-start;
		flex-wrap: wrap;
	}
	.add-update-preview /deep/.el-form-item.textBox span.text {
		padding: 0;
		color: #666;
		font-weight: 500;
		display: inline-block;
		font-size: 16px;
		line-height: 40px;
	}
	
	.add-update-preview .el-input {
		width: 100%;
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
	.add-update-preview .el-input /deep/ .el-input__inner[readonly="readonly"] {
		border: 0px solid #ddd;
		cursor: not-allowed;
		border-radius: 10px;
		padding: 0 12px;
		box-shadow: none;
		outline: none;
		color: #999;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .el-input-number {
		text-align: left;
		width: 100%;
	}
	.add-update-preview .el-input-number /deep/ .el-input__inner {
		text-align: left;
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
	.add-update-preview .el-input-number /deep/ .is-disabled .el-input__inner {
		text-align: left;
		border: 0px solid #ddd;
		cursor: not-allowed;
		border-radius: 10px;
		padding: 0 12px;
		box-shadow: none;
		outline: none;
		color: #999;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .el-input-number /deep/ .el-input-number__decrease {
		display: none;
	}
	.add-update-preview .el-input-number /deep/ .el-input-number__increase {
		display: none;
	}
	.add-update-preview .el-select {
		width: 100%;
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
	.add-update-preview .el-select /deep/ .is-disabled .el-input__inner {
		border: 0;
		cursor: not-allowed;
		border-radius: 10px;
		padding: 0 10px;
		box-shadow: none;
		outline: none;
		color: #999;
		background: #f8f8f8;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .el-date-editor {
		width: 100%;
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
	.add-update-preview .el-date-editor /deep/ .el-input__inner[readonly="readonly"] {
		border: 0;
		cursor: not-allowed;
		border-radius: 10px;
		padding: 0 10px 0 30px;
		box-shadow: none;
		outline: none;
		color: #999;
		background: #f8f8f8;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .viewBtn {
		border: 1px solid #206cb4;
		cursor: pointer;
		border-radius: 4px;
		padding: 0 15px;
		margin: 0 20px 0 0;
		outline: none;
		color: #206cb4;
		background: #eaf3ff;
		width: auto;
		font-size: 14px;
		line-height: 40px;
		height: 40px;
		.iconfont {
			margin: 0 2px;
			color: #fff;
			display: none;
			font-size: 14px;
			height: 40px;
		}
	}
	.add-update-preview .viewBtn:hover {
		opacity: 0.8;
	}
	.add-update-preview .downBtn {
		border: 1px solid #206cb4;
		cursor: pointer;
		border-radius: 4px;
		padding: 0 15px;
		margin: 0 20px 0 0;
		outline: none;
		color: #206cb4;
		background: #eaf3ff;
		width: auto;
		font-size: 14px;
		line-height: 40px;
		height: 40px;
		.iconfont {
			margin: 0 2px;
			color: #fff;
			display: none;
			font-size: 14px;
			height: 40px;
		}
	}
	.add-update-preview .downBtn:hover {
		opacity: 0.8;
	}
	.add-update-preview .unBtn {
		border: 1px solid #eaebed;
		cursor: pointer;
		border-radius: 4px;
		padding: 0 15px;
		margin: 0 20px 0 0;
		outline: none;
		color: #333;
		background: #fafafa;
		width: auto;
		font-size: 14px;
		line-height: 40px;
		height: 40px;
		.iconfont {
			margin: 0 2px;
			color: #fff;
			display: none;
			font-size: 14px;
			height: 40px;
		}
	}
	.add-update-preview .unBtn:hover {
		opacity: 0.8;
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
	
	.add-update-preview /deep/ .upload .upload-img {
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
	.add-update-preview /deep/ .el-upload__tip {
		padding: 0 10px;
		color: #666;
		font-size: 15px;
	}
	.add-update-preview /deep/ .el-form-item.fileupload {
		margin: 0 20px 20px;
		width: calc(33.33% - 40px);
	}
	.add-update-preview .el-form-item.fileupload /deep/ .el-form-item__label {
		padding: 0 10px;
		color: #666;
		white-space: nowrap;
		font-weight: 500;
		width: 100%;
		font-size: 16px;
		line-height: 40px;
		text-align: left;
	}
	
	.add-update-preview .el-form-item.fileupload /deep/ .el-form-item__content {
		margin-left: auto !important;
		margin: auto !important;
		display: flex;
		width: 100%;
		justify-content: flex-start;
		align-items: flex-start;
		flex-wrap: wrap;
	}
	.add-update-preview .el-form-item.fileupload /deep/ .el-upload-dragger {
		border: 1px dashed #d9d9d9;
		cursor: pointer;
		background-color: #fff;
		border-radius: 10px;
		padding: 5px 40px 20px;
		overflow: hidden;
		width: auto;
		box-sizing: border-box;
		text-align: center;
		height: auto;
	}
	.add-update-preview .el-form-item.fileupload .el-upload-dragger /deep/ .el-icon-upload {
		margin: 0;
		color: #206cb480;
		font-size: 66px;
		line-height: 1;
	}
	.add-update-preview .el-form-item.fileupload .el-upload-dragger /deep/ .el-upload__text {
		color: #606266;
		textalign: center;
		fontsize: 14px;
		line-height: 1;
	}
	.add-update-preview .el-form-item.fileupload .el-upload-dragger /deep/ .el-upload__text em {
		fontstyle: normal;
		color: #409EFF;
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
	.add-update-preview .el-textarea /deep/ .el-textarea__inner[readonly="readonly"] {
		border: 0;
		cursor: not-allowed;
		border-radius: 4px;
		padding: 12px;
		box-shadow: none;
		outline: none;
		color: #666;
		width: 100%;
		font-size: 16px;
		height: auto;
	}
	.add-update-preview /deep/ .el-form-item.btn {
		padding: 0 30px;
		margin: 20px auto 0;
		display: flex;
		width: 100%;
		justify-content: flex-end;
		.btn1 {
			border: 1px solid #206cb4;
			cursor: pointer;
			border-radius: 4px;
			padding: 0 24px;
			margin: 4px;
			outline: none;
			color: #206cb4;
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
		.btn1:hover {
			opacity: 0.8;
		}
		.btn2 {
			border: 1px solid #397cf9;
			cursor: pointer;
			border-radius: 4px;
			padding: 0 24px;
			margin: 4px;
			outline: none;
			color: #397cf9;
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
		.btn2:hover {
			opacity: 0.8;
		}
		.btn3 {
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
		.btn3:hover {
			opacity: 0.8;
		}
		.btn4 {
			border: 1px solid #8a999f;
			cursor: pointer;
			border-radius: 4px;
			padding: 0 24px;
			margin: 4px;
			outline: none;
			color: #8a999f;
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
		.btn4:hover {
			opacity: 0.8;
		}
		.btn5 {
			border: 1px solid #03c4f1;
			cursor: pointer;
			border-radius: 4px;
			padding: 0 24px;
			margin: 4px;
			outline: none;
			color: #03c4f1;
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
		.btn5:hover {
			opacity: 0.8;
		}
	}
	.add-update-preview .el-form-item.btn /deep/ .el-form-item__label {
		padding: 0 10px;
		color: #666;
		white-space: nowrap;
		font-weight: 500;
		width: 100%;
		font-size: 16px;
		line-height: 40px;
		text-align: left;
	}
	
	.add-update-preview .el-form-item.btn /deep/ .el-form-item__content {
		margin-left: auto !important;
		margin: auto !important;
		display: flex;
		width: 100%;
		justify-content: flex-start;
		align-items: flex-start;
		flex-wrap: wrap;
	}
</style>
