  
<template>
	<div class="main-content" :style='{"padding":"50px 30px 30px"}'>
		<!-- 列表页 -->
		<template v-if="showFlag ">
			<el-form class="center-form-pv" :style='{"width":"100%","padding":"0","margin":"0 0 20px"}' :inline="true" :model="searchForm">
				<el-row :style='{"padding":"10px 20px 0","boxShadow":"0 3px 3px 0px #095dac","borderRadius":"0px","flexWrap":"wrap","background":"#fff","display":"flex","width":"100%","position":"relative"}' >
					<div v-if="isHrPositionMode" :style='{"margin":"0 1% 10px 0","display":"flex"}' class="select" label="姓名" prop="xingming">
						<label :style='{"margin":"0 10px 0 0","whiteSpace":"nowrap","color":"#666","display":"inline-block","lineHeight":"40px","fontSize":"16px","fontWeight":"500","height":"40px"}' class="item-label">姓名</label>
						<el-select clearable filterable allow-create default-first-option v-model="searchForm.xingming" placeholder="请选择或输入姓名">
							<el-option v-for="(item,index) in xingmingOptions" v-bind:key="index" :label="item" :value="item"></el-option>
						</el-select>
					</div>
					<div  :style='{"margin":"0 1% 10px 0","display":"flex"}' class="select" label="现职位" prop="xianzhiwei">
						<label :style='{"margin":"0 10px 0 0","whiteSpace":"nowrap","color":"#666","display":"inline-block","lineHeight":"40px","fontSize":"16px","fontWeight":"500","height":"40px"}' class="item-label">现职位</label>
						<el-select clearable v-model="searchForm.xianzhiwei" placeholder="请选择现职位">
							<el-option v-for="(item,index) in xianzhiweiOptions" v-bind:key="index" :label="item" :value="item"></el-option>
						</el-select>
					</div>
					<div v-if="!isHrPositionMode" :style='{"margin":"0 1% 10px 0","display":"flex"}'>
						<label :style='{"margin":"0 10px 0 0","whiteSpace":"nowrap","color":"#666","display":"inline-block","lineHeight":"40px","fontSize":"16px","fontWeight":"500","height":"40px"}' class="item-label">变动原因</label>
						<el-input v-model="searchForm.biandongyuanyin" placeholder="变动原因" @keydown.enter.native="search()" clearable></el-input>
					</div>
					<el-button class="search" type="success" @click="search()">
						<span class="icon iconfont icon-fangdajing01" :style='{"margin":"0 0px","fontSize":"16px","color":"#fff","height":"40px"}'></span>
						查询
					</el-button>
				</el-row>

				<el-row v-if="!isHrPositionMode" class="actions" :style='{"width":"calc(100% + 8px)","margin":"20px 0 20px -4px","flexWrap":"wrap","justifyContent":"flex-end","display":"flex"}'>
					<el-button class="add" v-if="isAuth('zhiweidiaodong','新增')" type="success" @click="addOrUpdateHandler()">
						<span class="icon iconfont icon-tianjia16" :style='{"margin":"0 0px","fontSize":"16px","color":"#0d5961","height":"auto"}'></span>
						添加
					</el-button>
					<el-button class="del" v-if="isAuth('zhiweidiaodong','删除')" :disabled="dataListSelections.length?false:true" type="danger" @click="deleteHandler()">
						<span class="icon iconfont icon-shanchu6" :style='{"margin":"0 0px","fontSize":"16px","color":"#f00","height":"auto"}'></span>
						删除
					</el-button>


				</el-row>
			</el-form>
			<div :style='{"border":"1px solid #e9eafc","width":"100%","padding":" 0px 15px 20px","boxShadow":"none","borderRadius":"10px","background":"#fff"}'>
				<el-table class="tables"
					:stripe='false'
					:style='{"padding":"0","borderColor":"#e7e8fc","borderRadius":"10px","borderWidth":"0px 0 0 0px","background":"#fff","width":"100%","borderStyle":"solid"}' 
					:border='false'
					v-if="true"
					:data="dataList"
					v-loading="dataListLoading"
					@selection-change="selectionChangeHandler">
					<el-table-column v-if="!isHrPositionMode" :resizable='true' type="selection" align="center" width="50"></el-table-column>
					<el-table-column :resizable='true' :sortable='true' label="序号" type="index" width="50"></el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="gonghao"
						label="工号">
						<template slot-scope="scope">
							{{scope.row.gonghao}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="xingming"
						label="姓名">
						<template slot-scope="scope">
							{{scope.row.xingming}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="zhiwei"
						:label="isHrPositionMode ? '现职位' : '原职位'">
						<template slot-scope="scope">
							{{scope.row.zhiwei}}
						</template>
					</el-table-column>
					<el-table-column v-if="!isHrPositionMode" :resizable='true' :sortable='true'
												prop="xianzhiwei"
						label="现职位">
						<template slot-scope="scope">
							{{scope.row.xianzhiwei}}
						</template>
					</el-table-column>
					<el-table-column v-if="!isHrPositionMode" :resizable='true' :sortable='true'
												prop="biandongriqi"
						label="变动日期">
						<template slot-scope="scope">
							{{scope.row.biandongriqi}}
						</template>
					</el-table-column>
					<el-table-column v-if="!isHrPositionMode" :resizable='true' :sortable='true'
												prop="guanlizhanghao"
						label="管理账号">
						<template slot-scope="scope">
							{{scope.row.guanlizhanghao}}
						</template>
					</el-table-column>
					<el-table-column v-if="!isHrPositionMode" :resizable='true' :sortable='true'
												prop="guanlixingming"
						label="管理姓名">
						<template slot-scope="scope">
							{{scope.row.guanlixingming}}
						</template>
					</el-table-column>
					<el-table-column width="300" label="操作">
						<template slot-scope="scope">
							<el-button class="view" v-if="!isHrPositionMode && isAuth('zhiweidiaodong','查看')" type="success" @click="addOrUpdateHandler(scope.row.id,'info')">
								<span class="icon iconfont icon-chakan2" :style='{"margin":"0 0px","fontSize":"14px","color":"#333","display":"none","height":"40px"}'></span>
								详情
							</el-button>
							<el-button class="btn8" v-if="!isHrPositionMode && isAuth('zhiweidiaodong','申诉')&&!isAgreeConfirmed(scope.row)" :disabled="isAppealSubmitted(scope.row)" @click="zhiweishensuCrossAddOrUpdateHandler(scope.row,'cross','','','[1]','你已申诉')" type="success">
								<span class="icon iconfont icon-xihuan" :style='{"margin":"0 0px","fontSize":"14px","color":"#333","display":"none","height":"40px"}'></span>
								申诉
							</el-button>
							<el-button class="btn8" v-if="!isHrPositionMode && isAuth('zhiweidiaodong','同意')&&!isAgreeHidden(scope.row)" @click="tongyixinxiCrossAddOrUpdateHandler(scope.row,'cross','','','[1]','请勿重复操作')" type="success">
								<span class="icon iconfont icon-xihuan" :style='{"margin":"0 0px","fontSize":"14px","color":"#333","display":"none","height":"40px"}'></span>
								同意
							</el-button>
							<el-button class="edit" v-if="isHrPositionMode && isAuth('zhiweidiaodong','修改')" type="success" @click="openAdjustFromEmployee(scope.row)">
								<span class="icon iconfont icon-xiugai13" :style='{"margin":"0 0px","fontSize":"14px","color":"rgba(255, 140, 0, 1)","display":"none","height":"40px"}'></span>
								修改
							</el-button>
							<el-button class="edit" v-if="!isHrPositionMode && isAuth('zhiweidiaodong','修改')" type="success" @click="addOrUpdateHandler(scope.row.id)">
								<span class="icon iconfont icon-xiugai13" :style='{"margin":"0 0px","fontSize":"14px","color":"rgba(255, 140, 0, 1)","display":"none","height":"40px"}'></span>
								修改
							</el-button>




							<el-button class="del" v-if="!isHrPositionMode && isAuth('zhiweidiaodong','删除')" type="primary" @click="deleteHandler(scope.row.id)">
								<span class="icon iconfont icon-shanchu6" :style='{"margin":"0 0px","fontSize":"14px","color":"rgba(220, 38, 38, 1)","display":"none","height":"40px"}'></span>
								删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
			<el-pagination
				@size-change="sizeChangeHandle"
				@current-change="currentChangeHandle"
				:current-page="pageIndex"
				background
				:page-sizes="[10, 50, 100, 200]"
				:page-size="pageSize"
				:layout="layouts.join()"
				:total="totalPage"
				prev-text="< "
				next-text="> "
				:hide-on-single-page="false"
				:style='{"padding":"0","margin":"20px 0 0","whiteSpace":"nowrap","color":"#333","display":"flex","width":"100%","fontWeight":"500","justifyContent":"center"}'
			></el-pagination>
		</template>
		<!-- 添加/修改页面 -->
		<add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>

		<zhiweishensu-cross-add-or-update v-if="zhiweishensuCrossAddOrUpdateFlag" :parent="this" ref="zhiweishensuCrossaddOrUpdate"></zhiweishensu-cross-add-or-update>
		<tongyixinxi-cross-add-or-update v-if="tongyixinxiCrossAddOrUpdateFlag" :parent="this" ref="tongyixinxiCrossaddOrUpdate"></tongyixinxi-cross-add-or-update>




	</div>
</template>

<script>
	import * as echarts from 'echarts'
	import chinaJson from "@/components/echarts/china.json";
	import axios from 'axios';
	import AddOrUpdate from "./add-or-update";
	import zhiweishensuCrossAddOrUpdate from "../zhiweishensu/add-or-update";
	import tongyixinxiCrossAddOrUpdate from "../tongyixinxi/add-or-update";
	import {
		Loading
	} from 'element-ui';
	export default {
		data() {
			return {
				indexQueryCondition: '',
				xingmingOptions: [],
				xianzhiweiOptions: [],
				searchForm: {
					key: ""
				},
				form:{},
				dataList: [],
				pageIndex: 1,
				pageSize: 10,
				totalPage: 0,
				dataListLoading: false,
				dataListSelections: [],
				actionButtonsHiddenMap: {},
				showFlag: true,
				addOrUpdateFlag:false,
				zhiweishensuCrossAddOrUpdateFlag: false,
				tongyixinxiCrossAddOrUpdateFlag: false,
				layouts: ["total","prev","pager","next","sizes","jumper"],
			};
		},
		created() {
			if(this.statType) {
				return false
			}
			this.restoreActionButtonsHiddenMap();
			this.init();
			this.getDataList();
		},
		mounted() {
		},
		watch: {
		},
		filters: {
			htmlfilter: function (val) {
				return val.replace(/<[^>]*>/g).replace(/undefined/g,'');
			}
		},
		computed: {
			tablename(){
				return this.$storage.get('sessionTable')
			},
			role(){
				return this.$storage.get('role')
			},
			isHrPositionMode() {
				return this.queryChange(['人事管理员', '管理员']);
			},
		},
		components: {
			AddOrUpdate,
			zhiweishensuCrossAddOrUpdate,
			tongyixinxiCrossAddOrUpdate,
		},
		methods: {
			queryChange(arr){
				for(let x in arr) {
					if(arr[x] == this.role) {
						return true
					}
				}
				return false
			},
			getActionButtonsHiddenStorageKey() {
				return 'zhiweidiaodong_action_buttons_hidden_v1';
			},
			restoreActionButtonsHiddenMap() {
				let storage = null;
				try {
					storage = window.sessionStorage.getItem(this.getActionButtonsHiddenStorageKey());
				} catch (e) {
					this.actionButtonsHiddenMap = {};
					return;
				}
				if(!storage) {
					this.actionButtonsHiddenMap = {};
					return;
				}
				try {
					let parsed = JSON.parse(storage);
					if(parsed && Object.prototype.toString.call(parsed) === '[object Object]') {
						this.actionButtonsHiddenMap = parsed;
						return;
					}
				} catch (e) {}
				this.actionButtonsHiddenMap = {};
			},
			saveActionButtonsHiddenMap() {
				try {
					window.sessionStorage.setItem(this.getActionButtonsHiddenStorageKey(), JSON.stringify(this.actionButtonsHiddenMap || {}));
				} catch (e) {}
			},
			getActionButtonsRowKey(row) {
				if(!row || row.id === undefined || row.id === null) {
					return '';
				}
				return String(row.id);
			},
			isActionButtonsHidden(row) {
				let rowKey = this.getActionButtonsRowKey(row);
				if(!rowKey) {
					return false;
				}
				let state = this.actionButtonsHiddenMap[rowKey];
				if(state === true) {
					return true;
				}
				return !!(state && (state.agreeConfirmed || state.appealSubmitted));
			},
			getActionButtonState(row) {
				let rowKey = this.getActionButtonsRowKey(row);
				if(!rowKey) {
					return {};
				}
				let state = this.actionButtonsHiddenMap[rowKey];
				if(state === true) {
					return { agreeConfirmed: true, appealSubmitted: true };
				}
				return state || {};
			},
			isAgreeConfirmed(row) {
				return !!this.getActionButtonState(row).agreeConfirmed;
			},
			isAppealSubmitted(row) {
				return !!this.getActionButtonState(row).appealSubmitted;
			},
			isAgreeHidden(row) {
				let state = this.getActionButtonState(row);
				return !!(state.agreeConfirmed || state.appealSubmitted);
			},
			hideActionButtons(row) {
				let rowKey = this.getActionButtonsRowKey(row);
				if(!rowKey) {
					return;
				}
				this.$set(this.actionButtonsHiddenMap, rowKey, { agreeConfirmed: true, appealSubmitted: true });
				this.saveActionButtonsHiddenMap();
			},
			markAppealSubmitted(row) {
				let rowKey = this.getActionButtonsRowKey(row);
				if(!rowKey) {
					return;
				}
				this.$set(this.actionButtonsHiddenMap, rowKey, { agreeConfirmed: false, appealSubmitted: true });
				this.saveActionButtonsHiddenMap();
			},
			zhiweishensuCrossAddOrUpdateHandler(row,type,crossOptAudit,crossOptPay,statusColumnName,tips,statusColumnValue){
				this._pendingAppealRow = row;
				this.showFlag = false;
				this.addOrUpdateFlag = false;
				this.zhiweishensuCrossAddOrUpdateFlag = true;
				this.$storage.set('crossObj',row);
				this.$storage.set('crossTable','zhiweidiaodong');
				this.$storage.set('statusColumnName',statusColumnName);
				this.$storage.set('statusColumnValue',statusColumnValue);
				this.$storage.set('tips',tips);
				if(statusColumnName!=''&&!statusColumnName.startsWith("[")) {
					var obj = this.$storage.getObj('crossObj');
					for (var o in obj){
						if(o==statusColumnName && obj[o]==statusColumnValue){
							this.$message({
								message: tips,
								type: "warning",
								duration: 1500,
								onClose: () => {
									this.getDataList();
								}
							});
							this.showFlag = true;
							this.zhiweishensuCrossAddOrUpdateFlag = false;
							return;
						}
					}
				}
				this.$nextTick(() => {
					this.$refs.zhiweishensuCrossaddOrUpdate.init(row.id,type);
				});
			},
			tongyixinxiCrossAddOrUpdateHandler(row,type,crossOptAudit,crossOptPay,statusColumnName,tips,statusColumnValue){
				this.$confirm('是否确认同意本次职位调动？','提示').then(async()=>{
					let data = {
						gonghao: row.gonghao,
						xingming: row.xingming,
						tongyishijian: this.getCurDateTime(),
						guanlizhanghao: row.guanlizhanghao,
						guanlixingming: row.guanlixingming,
						crossuserid: this.$storage.get('userid'),
						crossrefid: row.id
					}
					await this.$http.post('tongyixinxi/add', data).then(async res=>{
						this.hideActionButtons(row);
						this.$message.success('操作成功')
						this.getDataList()
					})
				}).catch(()=>{})
			},
			init () {
				this.$http({
					url: `option/yuangong/xingming`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.xingmingOptions = data.data;
					} else {
						this.$message.error(data.msg);
					}
				});
				this.$http({
					url: `option/zhiweixinxi/zhiwei`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.xianzhiweiOptions = data.data;
					} else {
						this.$message.error(data.msg);
					}
				});
			},
			search() {
				this.pageIndex = 1;
				this.getDataList();
			},
			openAdjustFromEmployee(row) {
				this.showFlag = false;
				this.addOrUpdateFlag = true;
				this.crossAddOrUpdateFlag = false;
				this.$nextTick(() => {
					this.$refs.addOrUpdate.init(row.id, 'fromEmployee', row);
				});
			},

			// 鑾峰彇鏁版嵁鍒楄〃
			getDataList() {
				this.dataListLoading = true;
				let params = {
					page: this.pageIndex,
					limit: this.pageSize,
					sort: 'id',
					order: 'desc',
				};
				if(this.searchForm.xingming!='' && this.searchForm.xingming!=undefined){
					params['xingming'] = this.isHrPositionMode ? ('%' + this.searchForm.xingming + '%') : this.searchForm.xingming;
				}
				if(this.searchForm.xianzhiwei!='' && this.searchForm.xianzhiwei!=undefined){
					params[this.isHrPositionMode ? 'zhiwei' : 'xianzhiwei'] = this.searchForm.xianzhiwei;
				}
				if(!this.isHrPositionMode && this.searchForm.biandongyuanyin!='' && this.searchForm.biandongyuanyin!=undefined){
					params['biandongyuanyin'] = '%' + this.searchForm.biandongyuanyin + '%';
				}
				let requestUrl = this.isHrPositionMode ? "yuangong/page" : "zhiweidiaodong/page";
				this.$http({
					url: requestUrl,
					method: "get",
					params: params
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.dataList = (data.data && data.data.list) || [];
						this.totalPage = (data.data && data.data.total) || 0;
					} else {
						this.dataList = [];
						this.totalPage = 0;
					}
					this.dataListLoading = false;
				});
			},
			// 姣忛〉鏁?
			sizeChangeHandle(val) {
				this.pageSize = val;
				this.pageIndex = 1;
				this.getDataList();
			},
			// 褰撳墠椤?
			currentChangeHandle(val) {
				this.pageIndex = val;
				this.getDataList();
			},
			// 澶氶€?
			selectionChangeHandler(val) {
				this.dataListSelections = val;
			},
			// 娣诲姞/淇敼
			addOrUpdateHandler(id,type) {
				this.showFlag = false;
				this.addOrUpdateFlag = true;
				this.crossAddOrUpdateFlag = false;
				if(type!='info'&&type!='msg'){
					type = 'else';
				}
				this.$nextTick(() => {
					this.$refs.addOrUpdate.init(id,type );
				});
			},
			// 鍒犻櫎
			async deleteHandler(id ) {
				var ids = id? [Number(id)]: this.dataListSelections.map(item => {
					return Number(item.id);
				});
				await this.$confirm(`纭畾杩涜[${id ? "鍒犻櫎" : "鎵归噺鍒犻櫎"}]鎿嶄綔?`, "鎻愮ず", {
					confirmButtonText: "纭畾",
					cancelButtonText: "鍙栨秷",
					type: "warning"
				}).then(async () => {
					await this.$http({
						url: "zhiweidiaodong/delete",
						method: "post",
						data: ids
					}).then(async ({ data }) => {
						if (data && data.code === 0) {
							this.$message({
								message: "鎿嶄綔鎴愬姛",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.search();
								}
							});
			
						} else {
							this.$message.error(data.msg);
						}
					});
				});
			},


		}

	};
</script>
<style lang="scss" scoped>
	
	.center-form-pv {
		.el-date-editor.el-input {
			width: auto;
		}
	}
	
	.el-input {
		width: auto;
	}
	
	// form
	.center-form-pv .el-input {
		width: 100%;
	}
	.center-form-pv .el-input /deep/ .el-input__inner {
		border: 1px solid #b2d3ee;
		border-radius: 4px;
		padding: 0 12px;
		box-shadow: none;
		outline: none;
		color: #333;
		width: 100%;
		font-size: 16px;
		height: 36px;
	}
	.center-form-pv .el-select {
		width: 100%;
	}
	.center-form-pv .el-select /deep/ .el-input__inner {
		border: 1px solid #b2d3ee;
		border-radius: 4px;
		padding: 0 10px;
		box-shadow: none;
		outline: none;
		color: #333;
		width: 100%;
		font-size: 16px;
		height: 36px;
	}
	.center-form-pv .el-date-editor {
		width: 100%;
	}
	
	.center-form-pv .el-date-editor /deep/ .el-input__inner {
		border: 1px solid #b2d3ee;
		border-radius: 4px;
		padding: 0 10px 0 30px;
		box-shadow: none;
		outline: none;
		color: #333;
		width: 100%;
		font-size: 16px;
		height: 36px;
	}
	
	.center-form-pv .search {
		border: 0;
		cursor: pointer;
		border-radius: 4px;
		padding: 0 12px 0 10px;
		outline: none;
		color: #fff;
		background: #095dac;
		width: auto;
		font-size: 16px;
		height: 36px;
	}
	
	.center-form-pv .search:hover {
		opacity: 0.8;
	}
	
	.center-form-pv .actions .add {
		border: 1px solid #0d5961;
		cursor: pointer;
		border-radius: 6px;
		padding: 0 10px;
		margin: 4px;
		outline: none;
		color: #0d5961;
		background: #fff;
		width: auto;
		font-size: 14px;
		height: 36px;
	}
	
	.center-form-pv .actions .add:hover {
		opacity: 0.8;
	}
	
	.center-form-pv .actions .del {
		border: 1px solid #f00;
		cursor: pointer;
		border-radius: 6px;
		padding: 0 10px;
		margin: 4px;
		outline: none;
		color: #f00;
		background: #fff;
		width: auto;
		font-size: 14px;
		height: 36px;
	}
	
	.center-form-pv .actions .del:hover {
		opacity: 0.8;
	}
	
	.center-form-pv .actions .statis {
		border: 1px solid #f9b000;
		cursor: pointer;
		border-radius: 6px;
		padding: 0 10px;
		margin: 4px;
		outline: none;
		color: #f9b000;
		background: #fff;
		width: auto;
		font-size: 14px;
		height: 36px;
	}
	
	.center-form-pv .actions .statis:hover {
		opacity: 0.8;
	}
	
	.center-form-pv .actions .btn18 {
		border: 1px solid #b2d3ef;
		cursor: pointer;
		border-radius: 6px;
		padding: 0 10px;
		margin: 4px;
		outline: none;
		color: #206db5;
		background: #fff;
		width: auto;
		font-size: 14px;
		height: 36px;
	}
	
	.center-form-pv .actions .btn18:hover {
		opacity: 0.8;
	}
	
	// table
	.el-table /deep/ .el-table__header-wrapper thead {
		color: #999;
		font-weight: 500;
		width: 100%;
	}
	
	.el-table /deep/ .el-table__header-wrapper thead tr {
		background: none;
	}
	
	.el-table /deep/ .el-table__header-wrapper thead tr th {
		padding: 12px 0;
		background: none;
		border-color: #545454;
		border-width: 0 0px 2px 0;
		border-style: dotted;
		text-align: center;
	}

	.el-table /deep/ .el-table__header-wrapper thead tr th .cell {
		padding: 0 0 0 5px;
		word-wrap: normal;
		color: #333;
		white-space: normal;
		font-weight: bold;
		display: flex;
		vertical-align: middle;
		font-size: 15px;
		line-height: 24px;
		text-overflow: ellipsis;
		word-break: break-all;
		width: 100%;
		justify-content: flex-start;
		align-items: center;
		position: relative;
		min-width: 110px;
	}

	.el-table /deep/ .el-table__body-wrapper {
		position: relative;
	}
	.el-table /deep/ .el-table__body-wrapper tbody {
		width: 100%;
	}

	.el-table /deep/ .el-table__body-wrapper tbody tr {
		background: #fff;
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td {
		padding: 4px 0;
		color: #555;
		background: none;
		border-color: #545454;
		border-width: 0 0px 2px 0;
		border-style: dotted;
		text-align: left;
	}
	
		
	.el-table /deep/ .el-table__body-wrapper tbody tr:hover td {
		padding: 4px 0;
		color: #000;
		background: #f4f8fe;
		border-color: #545454;
		border-width: 0 0px 2px 0;
		border-style: dotted;
		text-align: left;
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td {
		padding: 4px 0;
		color: #555;
		background: none;
		border-color: #545454;
		border-width: 0 0px 2px 0;
		border-style: dotted;
		text-align: left;
	}

	.el-table /deep/ .el-table__body-wrapper tbody tr td .cell {
		padding: 0 0 0 5px;
		overflow: hidden;
		word-break: break-all;
		white-space: normal;
		font-size: inherit;
		line-height: 24px;
		text-overflow: ellipsis;
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td .view {
		border: 1px solid #216db4;
		cursor: pointer;
		border-radius: 6px;
		padding: 0 10px;
		margin: 0 5px 5px 0;
		outline: none;
		color: #216db4;
		background: none;
		width: auto;
		font-size: 14px;
		height: 32px;
		order: 3;
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td .view:hover {
		opacity: 0.8;
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td .add {
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td .add:hover {
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td .edit {
		border: 1px solid #22c08e;
		cursor: pointer;
		border-radius: 6px;
		padding: 0 10px;
		margin: 0 5px 5px 0;
		outline: none;
		color: #22c08e;
		background: none;
		width: auto;
		font-size: 14px;
		height: 32px;
		order: -1;
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td .edit:hover {
		opacity: 0.8;
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td .del {
		border: 1px solid #e7606a;
		cursor: pointer;
		border-radius: 6px;
		padding: 0 10px;
		margin: 0 5px 5px 0;
		outline: none;
		color: #e7606a;
		background: none;
		width: auto;
		font-size: 14px;
		height: 32px;
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td .del:hover {
		opacity: 0.8;
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td .btn8 {
		border: 1px solid #70a0fa;
		cursor: pointer;
		border-radius: 6px;
		padding: 0 10px;
		margin: 0 5px 5px 0;
		outline: none;
		color: #4484f9;
		background: none;
		width: auto;
		font-size: 14px;
		height: 32px;
		order: 5;
	}
	
	.el-table /deep/ .el-table__body-wrapper tbody tr td .btn8:hover {
		opacity: 0.8;
	}
	
	// pagination
	.main-content .el-pagination /deep/ .el-pagination__total {
		margin: 0 20px 0 0;
		color: #666;
		font-weight: 400;
		display: inline-block;
		vertical-align: top;
		font-size: 15px;
		line-height: 28px;
		height: 28px;
	}
	
	.main-content .el-pagination /deep/ .btn-prev {
		border: none;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px;
		color: #666;
		background: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: 15px;
		line-height: 28px;
		min-width: 35px;
		height: 28px;
	}
	
	.main-content .el-pagination /deep/ .btn-next {
		border: none;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px;
		color: #666;
		background: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: 15px;
		line-height: 28px;
		min-width: 35px;
		height: 28px;
	}
	
	.main-content .el-pagination /deep/ .btn-prev:disabled {
		border: none;
		cursor: not-allowed;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px;
		color: #666;
		background: #eee;
		display: inline-block;
		vertical-align: top;
		font-size: 15px;
		line-height: 28px;
		height: 28px;
	}
	
	.main-content .el-pagination /deep/ .btn-next:disabled {
		border: none;
		cursor: not-allowed;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px;
		color: #666;
		background: #eee;
		display: inline-block;
		vertical-align: top;
		font-size: 15px;
		line-height: 28px;
		height: 28px;
	}

	.main-content .el-pagination /deep/ .el-pager {
		padding: 0;
		margin: 0;
		display: inline-block;
		vertical-align: top;
	}

	.main-content .el-pagination /deep/ .el-pager .number {
		cursor: pointer;
		padding: 0 4px;
		margin: 0 5px;
		color: #666;
		display: inline-block;
		vertical-align: top;
		font-size: 15px;
		line-height: 28px;
		border-radius: 2px;
		background: #fff;
		text-align: center;
		min-width: 30px;
		height: 28px;
	}
	
	.main-content .el-pagination /deep/ .el-pager .number:hover {
		cursor: pointer;
		padding: 0 4px;
		margin: 0 5px;
		color: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: 15px;
		line-height: 28px;
		border-radius: 2px;
		background: #4f7df5;
		text-align: center;
		min-width: 30px;
		height: 28px;
	}
	
	.main-content .el-pagination /deep/ .el-pager .number.active {
		cursor: default;
		padding: 0 4px;
		margin: 0 5px;
		color: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: 15px;
		line-height: 28px;
		border-radius: 2px;
		background: #4f7df5;
		text-align: center;
		min-width: 30px;
		height: 28px;
	}
	
	.main-content .el-pagination /deep/ .el-pagination__sizes {
		display: inline-block;
		vertical-align: top;
		font-size: 15px;
		line-height: 28px;
		height: 28px;
	}
	
	.main-content .el-pagination /deep/ .el-pagination__sizes .el-input {
		margin: 0 5px;
		width: 100px;
		position: relative;
	}
	
	.main-content .el-pagination /deep/ .el-pagination__sizes .el-input .el-input__inner {
		border: 0px solid #DCDFE6;
		cursor: pointer;
		padding: 0 25px 0 8px;
		color: #606266;
		display: inline-block;
		font-size: 15px;
		line-height: 28px;
		border-radius: 3px;
		outline: 0;
		background: #FFF;
		width: 100%;
		text-align: center;
		height: 28px;
	}
	
	.main-content .el-pagination /deep/ .el-pagination__sizes .el-input span.el-input__suffix {
		top: 0;
		position: absolute;
		right: 0;
		height: 100%;
	}
	
	.main-content .el-pagination /deep/ .el-pagination__sizes .el-input .el-input__suffix .el-select__caret {
		cursor: pointer;
		color: #C0C4CC;
		width: 25px;
		font-size: 15px;
		line-height: 28px;
		text-align: center;
	}
	
	.main-content .el-pagination /deep/ .el-pagination__jump {
		margin: 0 0 0 24px;
		color: #606266;
		display: inline-block;
		vertical-align: top;
		font-size: 15px;
		line-height: 28px;
		height: 28px;
	}
	
	.main-content .el-pagination /deep/ .el-pagination__jump .el-input {
		border-radius: 3px;
		padding: 0 2px;
		margin: 0 2px;
		display: inline-block;
		width: 50px;
		font-size: 15px;
		line-height: 18px;
		position: relative;
		text-align: center;
		height: 28px;
	}
	
	.main-content .el-pagination /deep/ .el-pagination__jump .el-input .el-input__inner {
		border: 0px solid #DCDFE6;
		cursor: pointer;
		padding: 0 3px;
		color: #606266;
		display: inline-block;
		font-size: 15px;
		line-height: 28px;
		border-radius: 3px;
		outline: 0;
		background: #FFF;
		width: 100%;
		text-align: center;
		height: 28px;
	}
	
	// list one
	.one .list1-view {
		border: 0;
		cursor: pointer;
		padding: 0 10px;
		margin: 0 5px 5px 0;
		color: #095dac;
		font-size: 14px;
		border-radius: 30px;
		outline: none;
		background: #00c7f230;
		width: auto;
		min-width: 60px;
		height: 32px;
		order: 3;
	}
	
	.one .list1-view:hover {
		opacity: 0.8;
	}
	
	.one .list1-edit {
		border: 0;
		cursor: pointer;
		border-radius: 30px;
		padding: 0 10px;
		margin: 0 5px 5px 0;
		outline: none;
		color: #4f7df5;
		background: #edf2ff;
		width: auto;
		font-size: 14px;
		min-width: 60px;
		height: 32px;
	}
	
	.one .list1-edit:hover {
		opacity: 0.8;
	}
	
	.one .list1-del {
		border: 0;
		cursor: pointer;
		border-radius: 30px;
		padding: 0 10px;
		margin: 0 5px 5px 0;
		outline: none;
		color: #f00;
		background: #ffefed;
		width: auto;
		font-size: 14px;
		min-width: 60px;
		height: 32px;
	}
	
	.one .list1-del:hover {
		opacity: 0.8;
	}
	
	.one .list1-btn8 {
		border: 0;
		cursor: pointer;
		padding: 0 10px;
		margin: 0 5px 5px 0;
		color: #095dac;
		font-size: 14px;
		border-radius: 30px;
		outline: none;
		background: #aaceec50;
		width: auto;
		min-width: 60px;
		height: 32px;
		order: 11;
	}
	
	.one .list1-btn8:hover {
		opacity: 0.8;
	}
	
	.main-content .el-table .el-switch {
		display: inline-flex;
		vertical-align: middle;
		line-height: 30px;
		position: relative;
		align-items: center;
		height: 30px;
	}
	.main-content .el-table .el-switch /deep/ .el-switch__label--left {
		cursor: pointer;
		margin: 0 10px 0 0;
		color: #333;
		font-weight: 500;
		display: none;
		vertical-align: middle;
		font-size: 16px;
		transition: .2s;
		height: 30px;
	}
	.main-content .el-table .el-switch /deep/ .el-switch__label--right {
		cursor: pointer;
		margin: 0 0 0 10px;
		color: #333;
		font-weight: 500;
		display: none;
		vertical-align: middle;
		font-size: 16px;
		transition: .2s;
		height: 30px;
	}
	.main-content .el-table .el-switch /deep/ .el-switch__core {
		border: 1px solid #ff8b00;
		cursor: pointer;
		border-radius: 15px;
		margin: 0;
		outline: 0;
		background: #ff8b00;
		display: inline-block;
		width: 36px;
		box-sizing: border-box;
		transition: border-color .3s,background-color .3s;
		height: 18px;
	}
	.main-content .el-table .el-switch /deep/ .el-switch__core::after {
		border-radius: 100%;
		top: 1px;
		left: 2px;
		background: #FFF;
		width: 14px;
		position: absolute;
		transition: all .3s;
		height: 14px;
	}
	.main-content .el-table .el-switch.is-checked /deep/ .el-switch__core::after {
		margin: 0 0 0 -16px;
		left: 100%;
	}
	
	.main-content .el-table .el-rate /deep/ .el-rate__item {
		cursor: pointer;
		display: inline-block;
		vertical-align: middle;
		font-size: 0;
		position: relative;
	}
	.main-content .el-table .el-rate /deep/ .el-rate__item .el-rate__icon {
		margin: 0 3px;
		display: inline-block;
		font-size: 18px;
		position: relative;
		transition: .3s;
	}

	.chartDialog /deep/ .el-dialog {
		background: #fff;
	}
</style>

