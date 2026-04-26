<template>
  <div class="addEdit-block">
    <el-form
      class="add-update-preview"
      ref="ruleForm"
      :model="ruleForm"
      :rules="rules"
      label-width="100%"
    >
      <template>
        <el-form-item class="input" v-if="type!='info'" label="工号" prop="gonghao">
          <el-input
            v-model="ruleForm.gonghao"
            placeholder="工号"
            clearable
            :readonly="ro.gonghao"
          ></el-input>
        </el-form-item>
        <el-form-item class="input" v-else label="工号" prop="gonghao">
          <el-input v-model="ruleForm.gonghao" placeholder="工号" readonly></el-input>
        </el-form-item>

        <el-form-item class="input" v-if="type!='info'" label="姓名" prop="xingming">
          <el-input
            v-model="ruleForm.xingming"
            placeholder="姓名"
            clearable
            :readonly="ro.xingming"
          ></el-input>
        </el-form-item>
        <el-form-item class="input" v-else label="姓名" prop="xingming">
          <el-input v-model="ruleForm.xingming" placeholder="姓名" readonly></el-input>
        </el-form-item>

        <el-form-item class="date" v-if="type!='info'" label="申诉日期" prop="shensuriqi">
          <el-date-picker
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
            v-model="ruleForm.shensuriqi"
            type="date"
            :readonly="ro.shensuriqi"
            placeholder="申诉日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item class="input" v-else-if="ruleForm.shensuriqi" label="申诉日期" prop="shensuriqi">
          <el-input v-model="ruleForm.shensuriqi" placeholder="申诉日期" readonly></el-input>
        </el-form-item>

        <el-form-item class="input" v-if="type!='info'" label="管理账号" prop="guanlizhanghao">
          <el-input
            v-model="ruleForm.guanlizhanghao"
            placeholder="管理账号"
            clearable
            :readonly="ro.guanlizhanghao"
          ></el-input>
        </el-form-item>
        <el-form-item class="input" v-else label="管理账号" prop="guanlizhanghao">
          <el-input v-model="ruleForm.guanlizhanghao" placeholder="管理账号" readonly></el-input>
        </el-form-item>

        <el-form-item class="input" v-if="type!='info'" label="管理姓名" prop="guanlixingming">
          <el-input
            v-model="ruleForm.guanlixingming"
            placeholder="管理姓名"
            clearable
            :readonly="ro.guanlixingming"
          ></el-input>
        </el-form-item>
        <el-form-item class="input" v-else label="管理姓名" prop="guanlixingming">
          <el-input v-model="ruleForm.guanlixingming" placeholder="管理姓名" readonly></el-input>
        </el-form-item>
      </template>

      <el-form-item class="textarea" v-if="type!='info'" label="申诉原因" prop="shensuyuanyin">
        <el-input
          style="min-width: 200px; max-width: 600px;"
          type="textarea"
          :rows="8"
          placeholder="申诉原因"
          v-model="ruleForm.shensuyuanyin"
          :readonly="ro.shensuyuanyin"
        ></el-input>
      </el-form-item>
      <el-form-item v-else-if="ruleForm.shensuyuanyin" label="申诉原因" prop="shensuyuanyin" class="textBox">
        <span class="text">{{ruleForm.shensuyuanyin}}</span>
      </el-form-item>

      <el-form-item class="btn">
        <el-button class="btn3" v-if="type!='info'" type="success" @click="onSubmit">
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
					gonghao : false,
					xingming : false,
					shensuyuanyin : false,
					shensuriqi : false,
					guanlizhanghao : false,
					guanlixingming : false,
					crossuserid : false,
					crossrefid : false,
					sfsh : false,
					shhf : false,
				},
			
				ruleForm: {
					gonghao: '',
					xingming: '',
					shensuyuanyin: '',
					shensuriqi: '',
					guanlizhanghao: '',
					guanlixingming: '',
					crossuserid: '',
					crossrefid: '',
					shhf: '',
				},

				rules: {
					gonghao: [
					{ required: true, message: 'required', trigger: 'blur' },
					],
					xingming: [
					],
					shensuyuanyin: [
					],
					shensuriqi: [
					],
					guanlizhanghao: [
					],
					guanlixingming: [
					],
					crossuserid: [
					],
					crossrefid: [
					],
					sfsh: [
					],
					shhf: [
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
			this.ruleForm.shensuriqi = this.getCurDate()
		},
		methods: {
			imgPreView(url){
				this.$parent.imgPreView(url)
			},
			
			download(file){
				window.open(`${file}`)
			},
			
			init(id,type ) {
				if (id) {
					this.id = id;
					this.type = type;
				}
				if(this.type=='info'||this.type=='else'||this.type=='msg'){
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
						if(o=='gonghao'){
							this.ruleForm.gonghao = obj[o];
							this.ro.gonghao = true;
							continue;
						}
						if(o=='xingming'){
							this.ruleForm.xingming = obj[o];
							this.ro.xingming = true;
							continue;
						}
						if(o=='shensuyuanyin'){
						this.ruleForm.shensuyuanyin = '\\u5DE5\\u8D44\\u6709\\u5F02\\u8BAE\\uFF0C\\u7533\\u8BF7\\u590D\\u6838';
							this.ro.shensuyuanyin = true;
							continue;
						}
						if(o=='shensuriqi'){
							this.ruleForm.shensuriqi = obj[o];
							this.ro.shensuriqi = true;
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
						if(o=='crossuserid'){
							this.ruleForm.crossuserid = obj[o];
							this.ro.crossuserid = true;
							continue;
						}
						if(o=='crossrefid'){
							this.ruleForm.crossrefid = obj[o];
							this.ro.crossrefid = true;
							continue;
						}
					}
					const crossTable = this.$storage.get('crossTable')
					if (crossTable === 'yuangongxinzi') {
						this.ruleForm.shensuyuanyin = '\u5DE5\u8D44\u6709\u5F02\u8BAE\uFF0C\u7533\u8BF7\u590D\u6838';
						this.ruleForm.shhf = 'salary_appeal';
					}
					
					this.$http({
					url: `${this.sessionTable}/session`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						var json = data.data;
						if(this.sessionTable!="users") {
							this.ro.shensuriqi = true;
						}
						if(this.sessionTable!="users") {
							this.ro.shensuriqi = true;
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
			
				}
			},
			

			async info(id) {
				await this.$http({
					url: `zhiweishensu/info/${id}`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.ruleForm = data.data;
						
						let reg=new RegExp('../../../upload','g')
					} else {
						this.$message.error(data.msg);
					}
				});
			},

			
			async onSubmit() {
					var objcross = this.$storage.getObj('crossObj');
					
					var crossuserid;
					var crossrefid;
					var crossoptnum;
					var finishNum = 0;
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
									else {
										crossuserid=this.$storage.get('userid');
										crossrefid=obj['id'];
										crossoptnum=this.$storage.get('statusColumnName');
										crossoptnum=crossoptnum.replace(/\[/,"").replace(/\]/,"");
									}
								}
								if(crossrefid && crossuserid) {
									this.ruleForm.crossuserid = crossuserid;
									this.ruleForm.crossrefid = crossrefid;
									let params = { 
										page: 1, 
										limit: 10, 
										crossuserid:this.ruleForm.crossuserid,
										crossrefid:this.ruleForm.crossrefid,
									} 
									await this.$http({ 
										url: "zhiweishensu/page", 
										method: "get", 
										params: params 
									}).then(({ 
										data 
									}) => { 
										if (data && data.code === 0) {
											finishNum = data.data.total
										}
									})
								}
								if(finishNum>=crossoptnum) {
									this.$message.error(this.$storage.get('tips'));
									return false;
								}
								const crossTable = this.$storage.get('crossTable')
								if (crossTable === 'yuangongxinzi') {
									this.ruleForm.shhf = 'salary_appeal';
									var obj = this.$storage.getObj('crossObj');
									this.ruleForm.crossuserid = this.$storage.get('userid');
									this.ruleForm.crossrefid = obj && obj.id ? obj.id : this.ruleForm.crossrefid;
									await this.$http.post('updateColumn/yuangongxinzi/1',{
										csuUpdateColumn: 'sfsh',
										csuUpdateColumnValue: '\u5F85\u5BA1\u6838',
										csuConditionColumn: 'id',
										csuConditionColumnValue: obj.id
									}).then(rs=>{})
									await this.$http.post('updateColumn/yuangongxinzi/1',{
										csuUpdateColumn: 'ispay',
										csuUpdateColumnValue: '\u7533\u8BC9\u9501\u5B9A',
										csuConditionColumn: 'id',
										csuConditionColumnValue: obj.id
									}).then(rs=>{})
								}
								if (crossTable === 'zhiweidiaodong') {
									var obj = this.$storage.getObj('crossObj');
									await this.$http.post('updateColumn/zhiweidiaodong/1',{
										csuUpdateColumn: 'zhuangtai',
										csuUpdateColumnValue: '\u5DF2\u7533\u8BC9',
										csuConditionColumn: 'id',
										csuConditionColumnValue: obj.id
									}).then(rs=>{
									})
								}
							}
							await this.$http({
								url: `zhiweishensu/${!this.ruleForm.id ? "save" : "update"}`,
								method: "post",
								data: this.ruleForm
							}).then(async ({ data }) => {
								if (data && data.code === 0) {
									this.$message({
										message: "submit success",
										duration: 1500,
										onClose: () => {
											this.parent.showFlag = true;
											this.parent.addOrUpdateFlag = false;
											this.parent.zhiweishensuCrossAddOrUpdateFlag = false;
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
			
			getUUID () {
				return new Date().getTime();
			},
			
			back() {
				this.parent.showFlag = true;
				this.parent.addOrUpdateFlag = false;
				this.parent.zhiweishensuCrossAddOrUpdateFlag = false;
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



