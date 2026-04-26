  
<template>
	<div class="main-content" :style='{"padding":"50px 30px 30px"}'>
		<!-- 列表页 -->
		<template v-if="showFlag">
			<el-form class="center-form-pv" :style='{"width":"100%","padding":"0","margin":"0 0 20px"}' :inline="true" :model="searchFormSafe">
				<el-row :style='{"padding":"10px 20px 0","boxShadow":"0 3px 3px 0px #095dac","borderRadius":"0px","flexWrap":"wrap","background":"#fff","display":"flex","width":"100%","position":"relative"}' >
					<div v-if="['人事管理员', '管理员'].includes(role)" :style='{"margin":"0 1% 10px 0","display":"flex"}' class="select" label="姓名" prop="xingming">
						<label :style='{"margin":"0 10px 0 0","whiteSpace":"nowrap","color":"#666","display":"inline-block","lineHeight":"40px","fontSize":"16px","fontWeight":"500","height":"40px"}' class="item-label">姓名</label>
						<el-select
							clearable
							filterable
							allow-create
							default-first-option
							v-model="searchFormSafe.xingming"
							placeholder="请选择/输入姓名">
							<el-option v-for="(item,index) in xingmingOptions" v-bind:key="index" :label="item" :value="item"></el-option>
						</el-select>
					</div>
					<div v-if="['人事管理员', '管理员'].includes(role)" :style='{"margin":"0 1% 10px 0","display":"flex"}' class="select" label="支付状态" prop="ispay">
						<label :style='{"margin":"0 10px 0 0","whiteSpace":"nowrap","color":"#666","display":"inline-block","lineHeight":"40px","fontSize":"16px","fontWeight":"500","height":"40px"}' class="item-label">支付状态</label>
						<el-select clearable v-model="searchFormSafe.ispay" placeholder="请选择支付状态">
							<el-option v-for="(item,index) in ispayOptions" v-bind:key="'ispay'+index" :label="item" :value="item"></el-option>
						</el-select>
					</div>
					<div  :style='{"margin":"0 1% 10px 0","display":"flex"}'>
						<label :style='{"margin":"0 10px 0 0","whiteSpace":"nowrap","color":"#666","display":"inline-block","lineHeight":"40px","fontSize":"16px","fontWeight":"500","height":"40px"}' class="item-label">登记日期起始</label>
						<el-date-picker v-model="searchFormSafe.dengjiriqistart" type="date" value-format="yyyy-MM-dd" format="yyyy-MM-dd" placeholder="登记日期起始"></el-date-picker>
					</div>
					<div  :style='{"margin":"0 1% 10px 0","display":"flex"}'>
						<label :style='{"margin":"0 10px 0 0","whiteSpace":"nowrap","color":"#666","display":"inline-block","lineHeight":"40px","fontSize":"16px","fontWeight":"500","height":"40px"}' class="item-label">登记日期结束</label>
						<el-date-picker v-model="searchFormSafe.dengjiriqiend" type="date" value-format="yyyy-MM-dd" format="yyyy-MM-dd" placeholder="登记日期结束"></el-date-picker>
					</div>
					<el-button class="search" type="success" @click="search()">
						<span class="icon iconfont icon-fangdajing01" :style='{"margin":"0 0px","fontSize":"16px","color":"#fff","height":"40px"}'></span>
						查询
					</el-button>
				</el-row>

				<el-row class="actions" :style='{"width":"calc(100% + 8px)","margin":"20px 0 20px -4px","flexWrap":"wrap","justifyContent":"flex-end","display":"flex"}'>
					<!-- <el-button class="btn18" v-if="queryChange(['人事管理员', '管理员'])" type="success" @click="recalculateAllSalary">
						<span class="icon iconfont icon-shuaxin13" :style='{"margin":"0 0px","fontSize":"16px","color":"#206db5","height":"auto"}'></span>
						重新计算
					</el-button> -->
					<el-button class="add" v-if="isAuth('yuangongxinzi','新增')" type="success" @click="addOrUpdateHandler()">
						<span class="icon iconfont icon-tianjia16" :style='{"margin":"0 0px","fontSize":"16px","color":"#0d5961","height":"auto"}'></span>
						添加
					</el-button>
					<el-button class="btn18" v-if="isAuth('yuangongxinzi','审核')" :disabled="dataListSelections.length?false:true" type="success" @click="shBatchDialog()">
						<span class="icon iconfont icon-shenhe9" :style='{"margin":"0 0px","fontSize":"16px","color":"#206db5","height":"auto"}'></span>
						审核
					</el-button>
					<el-button class="btn18" v-if="['人事管理员'].includes(role) && isAuth('yuangongxinzi','支付')" :disabled="dataListSelections.length?false:true" type="success" @click="payBatchDialog()">
						<span class="icon iconfont icon-shenhe9" :style='{"margin":"0 0px","fontSize":"16px","color":"#206db5","height":"auto"}'></span>
						支付
					</el-button>
					<el-button class="del" v-if="isAuth('yuangongxinzi','删除')" :disabled="dataListSelections.length?false:true" type="danger" @click="deleteHandler()">
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
					<el-table-column :resizable='true' type="selection" align="center" width="50"></el-table-column>
					<el-table-column :resizable='true' :sortable='true' label="序号" type="index" width="50" />
					<el-table-column :resizable='true' :sortable='true'
												prop="xingming"
						label="姓名">
						<template slot-scope="scope">
							{{scope.row.xingming}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="gonghao"
						label="工号">
						<template slot-scope="scope">
							{{scope.row.gonghao}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="qingjiatianshu"
						label="请假天数">
						<template slot-scope="scope">
							{{scope.row.qingjiatianshu}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="weiqiandaotianshu"
						label="未签到天数">
						<template slot-scope="scope">
							{{scope.row.weiqiandaotianshu}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="weiqiandaokouxin"
						label="未签到扣薪">
						<template slot-scope="scope">
							{{scope.row.weiqiandaokouxin}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="jiaqikouxin"
						label="假期扣薪">
						<template slot-scope="scope">
							{{scope.row.jiaqikouxin}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="jibengongzi"
						label="基本工资">
						<template slot-scope="scope">
							{{scope.row.jibengongzi}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="jiabangongzi"
						label="加班工资">
						<template slot-scope="scope">
							{{scope.row.jiabangongzi}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="koukuanjine"
						label="扣款金额">
						<template slot-scope="scope">
							{{scope.row.koukuanjine}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="shifagongzi"
						label="实发工资">
						<template slot-scope="scope">
							{{scope.row.shifagongzi}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="koukuanyuanyin"
						label="扣款原因">
						<template slot-scope="scope">
							{{scope.row.koukuanyuanyin}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="dengjiriqi"
						label="登记日期">
						<template slot-scope="scope">
							{{scope.row.dengjiriqi}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="guanlizhanghao"
						label="管理账号">
						<template slot-scope="scope">
							{{scope.row.guanlizhanghao}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="guanlixingming"
						label="管理姓名">
						<template slot-scope="scope">
							{{scope.row.guanlixingming}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true' prop="sfsh" label="审核状态">
						<template slot-scope="scope">
							<el-tag v-if="scope.row.sfsh=='否'" type="danger">未通过</el-tag>
							<el-tag v-else-if="scope.row.sfsh=='待审核'" type="warning">待审核</el-tag>
							<el-tag v-else-if="scope.row.sfsh=='是'" type="success">通过</el-tag>
							<span v-else>{{ scope.row.sfsh }}</span>
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true' prop="ispay" label="支付状态">
						<template slot-scope="scope">
							<el-tag v-if="scope.row.ispay=='已支付'" type="success">已支付</el-tag>
							<el-tag v-else-if="scope.row.ispay=='未支付'" type="warning">未支付</el-tag>
							<el-tag v-else-if="scope.row.ispay=='申诉锁定'" type="danger">申诉锁定</el-tag>
							<span v-else>{{ scope.row.ispay || '未支付' }}</span>
						</template>
					</el-table-column>
					<el-table-column width="360" label="操作">
						<template slot-scope="scope">
							<el-button class="view" v-if="true" type="success" @click="addOrUpdateHandler(scope.row.id,'info')">
								<span class="icon iconfont icon-chakan2" :style='{"margin":"0 0px","fontSize":"14px","color":"#333","display":"none","height":"40px"}'></span>
								详情
							</el-button>
							<el-button class="edit" v-if="canModifySalary(scope.row)" type="success" @click="addOrUpdateHandler(scope.row.id)">
								<span class="icon iconfont icon-xiugai13" :style='{"margin":"0 0px","fontSize":"14px","color":"rgba(255, 140, 0, 1)","display":"none","height":"40px"}'></span>
								修改
							</el-button>
							<el-button class="btn8" v-if="['人事管理员'].includes(role) && isAuth('yuangongxinzi','支付')" :disabled="isPayLocked(scope.row)" type="success" @click="payBatchDialog(scope.row)">
								<span class="icon iconfont icon-zhuanfa13" :style='{"margin":"0 0px","fontSize":"14px","color":"#db9663","display":"none","height":"40px"}'></span>
								支付
							</el-button>
							<el-button class="btn8" v-if="tablename=='yuangong'" @click="zhiweishensuCrossAddOrUpdateHandler(scope.row,'cross','','')" type="success">
								<span class="icon iconfont icon-zhuanfa13" :style='{"margin":"0 0px","fontSize":"14px","color":"#db9663","display":"none","height":"40px"}'></span>
								申诉
							</el-button>
							<el-button class="btn8" v-if="tablename=='yuangong'" @click="salaryTongyiHandler(scope.row)" type="success">
								<span class="icon iconfont icon-zhuanfa13" :style='{"margin":"0 0px","fontSize":"14px","color":"#db9663","display":"none","height":"40px"}'></span>
								同意
							</el-button>
							<el-button class="del" v-if="isAuth('yuangongxinzi','删除')" type="primary" @click="deleteHandler(scope.row.id)">
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
				:page-sizes="[5, 10, 50, 100, 200]"
				:page-size="pageSize"
				:layout="layouts.join()"
				:total="totalPage"
				prev-text="< "
				next-text="> "
				:hide-on-single-page="false"
				:style='{"padding":"0","margin":"20px 0 0","whiteSpace":"nowrap","color":"#333","display":"flex","width":"100%","fontWeight":"500","justifyContent":"center"}'
			></el-pagination>
		</template>
		<!-- 添加/修改页面，将父组件的 search 方法传递给子组件 -->
		<add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>
		<zhiweishensu-cross-add-or-update v-if="zhiweishensuCrossAddOrUpdateFlag" :parent="this" ref="zhiweishensuCrossaddOrUpdate"></zhiweishensu-cross-add-or-update>

		<el-dialog :title="this.batchIds.length>1?'批量审核':'审核'" :visible.sync="sfshBatchVisiable" width="50%">
			<el-form ref="shBatchForm" :model="shBatchForm" :rules="shRules" label-width="80px">
				<el-form-item label="审核状态" prop="sfsh">
					<el-select v-model="shBatchForm.sfsh" placeholder="审核状态">
						<el-option label="通过" value="是"></el-option>
						<el-option label="不通过" value="否"></el-option>
						<el-option label="待审核" value="待审核"></el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="sfshBatchVisiable=false">取消</el-button>
				<el-button type="primary" @click="shBatchHandler">确定</el-button>
			</span>
		</el-dialog>

		<el-dialog :title="this.payBatchIds.length>1?'批量支付':'支付'" :visible.sync="payBatchVisiable" width="40%">
			<el-form ref="payBatchForm" :model="payBatchForm" :rules="payRules" label-width="80px">
				<el-form-item label="支付状态" prop="ispay">
					<el-select v-model="payBatchForm.ispay" placeholder="支付状态">
						<el-option v-for="(item,index) in ispayOptions" :key="'paystatus'+index" :label="item" :value="item"></el-option>
					</el-select>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="payBatchVisiable=false">取消</el-button>
				<el-button type="primary" @click="payBatchHandler">确定</el-button>
			</span>
		</el-dialog>

		
		<template v-if="statType">
			<div class="statShow1">
				<div class="echarts1" v-if="isAuth('yuangongxinzi','员工薪资')">
					<div id="shifagongziChart1" style=" width: 100%;height: 100%"></div>
				</div>
			</div>
		</template>

	</div>
</template>

<script>
	import * as echarts from 'echarts'
	import chinaJson from "@/components/echarts/china.json";
	import axios from 'axios';
	import AddOrUpdate from "./add-or-update";
	import zhiweishensuCrossAddOrUpdate from "../zhiweishensu/add-or-update";
	import {
		Loading
	} from 'element-ui';
	export default {
		data() {
			return {
				indexQueryCondition: '',
				xingmingOptions: [],
				ispayOptions: ['未支付', '已支付', '申诉锁定'],
				searchForm: {
					key: "",
					xingming: '',
					ispay: '',
					dengjiriqistart: '',
					dengjiriqiend: ''
				},
				form:{},
				dataList: [],
				pageIndex: 1,
				pageSize: 5,
				totalPage: 0,
				dataListLoading: false,
				dataListSelections: [],
				crossAddOrUpdateFlag: false,
				sfshBatchVisiable: false,
				payBatchVisiable: false,
				shBatchForm: {
					sfsh: ''
				},
				payBatchForm: {
					ispay: ''
				},
				shRules: {
					sfsh: [{ required: true, message: '审核状态不能为空', trigger: 'blur' }],
				},
				payRules: {
					ispay: [{ required: true, message: '支付状态不能为空', trigger: 'blur' }],
				},
				batchIds: [],
				payBatchIds: [],
				showFlag: true,
				zhiweishensuCrossAddOrUpdateFlag: false,
				line: {"backgroundColor":"transparent","yAxis":{"axisLabel":{"borderType":"solid","rotate":0,"padding":0,"shadowOffsetX":0,"margin":15,"backgroundColor":"transparent","borderColor":"#000","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"width":"","fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":true,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#666","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"minInterval":1,"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,0.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"xAxis":{"axisLabel":{"borderType":"solid","rotate":30,"padding":0,"shadowOffsetX":0,"margin":10,"backgroundColor":"transparent","borderColor":"#000","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"truncate","borderRadius":0,"borderWidth":0,"width":120,"interval":0,"fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":true,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":false},"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"color":["#378bb9","#095dac","#fbc62f","#16606a","#f75d5f","#5967cc","#faa07b","#9ab7a8","#ea7ccc"],"legend":{"padding":0,"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"orient":"horizontal","shadowBlur":0,"bottom":"auto","itemHeight":14,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"right":"auto","top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"right","borderWidth":0,"width":"80%","itemWidth":20,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":24,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{"showSymbol":true,"symbol":"pin","symbolSize":18},"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"title":{"borderType":"solid","padding":0,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"shadowBlur":0,"bottom":"auto","show":true,"right":"auto","top":"auto","borderRadius":0,"left":"left","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","fontSize":14,"lineHeight":24,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":600,"textBorderColor":"#666","textShadowBlur":0},"shadowColor":"transparent"}},
				bar: {"backgroundColor":"transparent","yAxis":{"axisLabel":{"borderType":"solid","rotate":0,"padding":0,"shadowOffsetX":0,"margin":12,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"width":"","fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":true,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#666","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"minInterval":1,"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,0.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"xAxis":{"axisLabel":{"borderType":"solid","rotate":30,"padding":0,"shadowOffsetX":0,"margin":10,"backgroundColor":"transparent","borderColor":"#000","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"truncate","borderRadius":0,"borderWidth":0,"width":120,"interval":0,"fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":true,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":false},"minInterval":1,"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"color":["#378bb9","#095dac","#fbc62f","#16606a","#f75d5f","#5967cc","#faa07b","#9ab7a8","#ea7ccc"],"legend":{"padding":0,"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"orient":"horizontal","shadowBlur":0,"bottom":"auto","itemHeight":14,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"right":"auto","top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"right","borderWidth":0,"width":"80%","itemWidth":20,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":12,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"grid":{"x":"25%","y":"15%","y2":"15%","x2":"5%"},"series":{"barWidth":"auto","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"#666","shadowOffsetY":0,"color":"","shadowBlur":0,"barBorderRadius":[30,30,30,30],"borderWidth":0,"opacity":1,"shadowColor":"#000"},"colorBy":"data","barCategoryGap":"20%"},"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"title":{"borderType":"solid","padding":0,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"subtext":"","shadowBlur":0,"bottom":"auto","show":true,"right":"auto","subtextStyle":{"padding":[5,0,0,0],"borderColor":"red","color":"red","borderWidth":10},"top":"auto","borderRadius":0,"left":"left","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","fontSize":14,"lineHeight":24,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":600,"textBorderColor":"#666","textShadowBlur":0},"shadowColor":"transparent"},"base":{"animate":false,"interval":2000}},
				pie: {"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"backgroundColor":"transparent","color":["#378bb9","#095dac","#fbc62f","#16606a","#f75d5f","#5967cc","#faa07b","#9ab7a8","#ea7ccc"],"title":{"borderType":"solid","padding":[5,0,0,0],"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"shadowBlur":0,"bottom":"auto","show":true,"right":"auto","top":"auto","borderRadius":0,"left":"left","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","fontSize":14,"lineHeight":14,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":600,"textBorderColor":"#666","textShadowBlur":0},"shadowColor":"transparent"},"legend":{"padding":[5,0,0,0],"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"orient":"horizontal","shadowBlur":0,"bottom":"auto","itemHeight":2,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"right":0,"top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"right","borderWidth":0,"width":"80%","itemWidth":2,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":12,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{"itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"#666","shadowOffsetY":0,"color":"","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"#000"},"label":{"borderType":"solid","rotate":0,"padding":0,"textBorderWidth":0,"backgroundColor":"transparent","borderColor":"#666","color":"inherit","show":true,"textShadowColor":"transparent","distanceToLabelLine":5,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"fontSize":12,"lineHeight":18,"textShadowOffsetX":0,"position":"outside","textShadowOffsetY":0,"textBorderType":"solid","textBorderColor":"#666","textShadowBlur":0},"labelLine":{"show":true,"length":10,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"#666","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"#000"},"length2":14,"smooth":false}}},
				funnel: {"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"backgroundColor":"transparent","color":["#378bb9","#095dac","#fbc62f","#16606a","#f75d5f","#5967cc","#faa07b","#9ab7a8","#ea7ccc"],"title":{"borderType":"solid","padding":2,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#ccc","shadowOffsetY":0,"shadowBlur":0,"bottom":"auto","show":true,"right":"auto","top":"auto","borderRadius":0,"left":"center","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#666","textShadowColor":"transparent","fontSize":14,"lineHeight":12,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"#ccc","textShadowBlur":0},"shadowColor":"transparent"},"legend":{"padding":5,"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#ccc","shadowOffsetY":0,"orient":"vertical","shadowBlur":0,"bottom":"auto","itemHeight":2,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"left","borderWidth":0,"width":"auto","itemWidth":2,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":20,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{"itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"#000","shadowOffsetY":0,"color":"","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"#000"},"label":{"borderType":"solid","rotate":0,"padding":0,"textBorderWidth":0,"backgroundColor":"transparent","borderColor":"#fff","color":"","show":true,"textShadowColor":"transparent","distanceToLabelLine":5,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"fontSize":12,"lineHeight":18,"textShadowOffsetX":0,"position":"outside","textShadowOffsetY":0,"textBorderType":"solid","textBorderColor":"#fff","textShadowBlur":0},"labelLine":{"show":true,"length":10,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"#000"},"length2":14,"smooth":false}}},
				boardBase: {"funnelNum":8,"lineNum":8,"radarNum":8,"gaugeNum":8,"barNum":8,"pieNum":8},
				gauge: {"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"backgroundColor":"transparent","color":["#378bb9","#095dac","#fbc62f","#16606a","#f75d5f","#5967cc","#faa07b","#9ab7a8","#ea7ccc"],"title":{"top":"top","left":"left","textStyle":{"fontSize":14,"lineHeight":24,"color":"#333","fontWeight":600}},"series":{"pointer":{"offsetCenter":[0,"10%"],"icon":"path://M2.9,0.7L2.9,0.7c1.4,0,2.6,1.2,2.6,2.6v115c0,1.4-1.2,2.6-2.6,2.6l0,0c-1.4,0-2.6-1.2-2.6-2.6V3.3C0.3,1.9,1.4,0.7,2.9,0.7z","width":8,"length":"80%"},"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"opacity":0.5,"shadowBlur":1,"shadowColor":"#000"},"roundCap":true},"anchor":{"show":true,"itemStyle":{"color":"inherit"},"size":18,"showAbove":true},"emphasis":{"disabled":false},"progress":{"show":true,"roundCap":true,"overlap":true},"splitNumber":25,"detail":{"formatter":"{value}","backgroundColor":"inherit","color":"#fff","borderRadius":3,"width":20,"fontSize":12,"height":10},"title":{"fontSize":14},"animation":true}},
				radar: {"backgroundColor":"transparent","radar":{"shape":"circle"},"color":["#365E77","#DF308C","#0CB906","#7690cb","#49ada0","#5BBAEC","#a68a28","#EE142F","#FFE9E9"],"legend":{"padding":5,"itemGap":5,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#ccc","shadowOffsetY":0,"orient":"vertical","shadowBlur":0,"bottom":"auto","itemHeight":4,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"right":"auto","top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"right","borderWidth":0,"width":"auto","itemWidth":4,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":24,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{},"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#7987FD"}},"title":{"top":"top","left":"left","textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","fontSize":14,"lineHeight":14,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"#666","textShadowBlur":0}}},
				chartVisiable1: false,
				addOrUpdateFlag:false,
				layouts: ["total","prev","pager","next","sizes","jumper"],
			};
		},
		created() {
			this.resetListViewState()
		},
		activated() {
			this.resetListViewState()
		},
		mounted() {
			if(this.statType) {
				this.statChartClick()
			}
		},
		watch: {
			statType(newsValue) {
				if(newsValue) {
					this.statChartClick()
				}
			},
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
			statType() {
				const p = (this.$route && typeof this.$route.path === 'string') ? this.$route.path : ''
				return p === '/yuangongxinzistat'
			},
			searchFormSafe: {
				get() {
					if (!this.searchForm || typeof this.searchForm !== 'object') {
						this.searchForm = {
							key: "",
							xingming: '',
							ispay: '',
							dengjiriqistart: '',
							dengjiriqiend: ''
						}
					}
					if (this.searchForm.xingming === undefined) this.$set(this.searchForm, 'xingming', '')
					if (this.searchForm.ispay === undefined) this.$set(this.searchForm, 'ispay', '')
					if (this.searchForm.dengjiriqistart === undefined) this.$set(this.searchForm, 'dengjiriqistart', '')
					if (this.searchForm.dengjiriqiend === undefined) this.$set(this.searchForm, 'dengjiriqiend', '')
					return this.searchForm
				},
				set(v) {
					this.searchForm = v
				}
			}
		},
		components: {
			AddOrUpdate,
			zhiweishensuCrossAddOrUpdate,
		},
		methods: {
			resetListViewState() {
				this.showFlag = true
				this.addOrUpdateFlag = false
				this.crossAddOrUpdateFlag = false
				this.zhiweishensuCrossAddOrUpdateFlag = false
				this.init()
				this.getDataList()
			},
			canModifySalary(row) {
				if (!row || (row.sfsh && row.sfsh !== '待审核')) {
					return false;
				}
				return this.isAuth('yuangongxinzi','修改') || this.tablename === 'renshiguanliyuan';
			},
			isPayLocked(row) {
				return !!(row && row.ispay === '申诉锁定');
			},
			recalculateAllSalary() {
				this.$confirm('确认按当前规则重新计算全部薪资吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.dataListLoading = true
					this.$http({
						url: 'yuangongxinzi/recalculateAll',
						method: 'post'
					}).then(({ data }) => {
						this.dataListLoading = false
						if (data && data.code === 0) {
							const n = data.recalculated != null ? data.recalculated : (data.data && data.data.recalculated)
							const doneMsg = n != null ? ('重新计算完成，共 ' + n + ' 条') : '重新计算完成'
							this.$message.success(doneMsg)
							this.getDataList()
						} else {
							this.$message.error((data && data.msg) || '重新计算失败')
						}
					}).catch(() => {
						this.dataListLoading = false
						this.$message.error('重新计算失败，请稍后重试')
					})
				})
			},
			statChartClick() {
				if(this.isAuth('yuangongxinzi','员工工资')) {
					this.chartDialogShow1()
				}
			},
			queryChange(arr){
				for(let x in arr) {
					if(arr[x] == this.role) {
						return true
					}
				}
				return false
			},
			changeStatQuery(arr) {
				if(arr.length==1) {
					if(arr[0] == 'users'&&this.$storage.get("sessionTable")=='users') {
						return true
					}
				}
				for(let x in arr) {
					if(arr[x] == this.role) {
						return true
					}
				}
				return false
			},
			chartDialogShow1() {
				this.chartVisiable1 = true
				this.chartDialog1()
			},
			// 统计接口1
			chartDialog1() {
				this.$nextTick(()=>{
					var shifagongziChart1 = echarts.init(document.getElementById("shifagongziChart1"),'macarons');
					let params = {}
					if(params.conditionColumn) {
						params.conditionColumn += ';' + 'sfsh'
						params.conditionValue += ';' + '是'
					}else {
						params.conditionColumn = 'sfsh'
						params.conditionValue = '是'
					}
					this.$http({
						url: "yuangongxinzi/value/xingming/shifagongzi",
						method: "get",
						params
					}).then(({data})=>{
						if (data && data.code === 0) {
							let res = data.data;
							let xAxis = [];
							let yAxis = [];
							let pArray = []
							for(let i=0;i<res.length;i++){
								if(this.boardBase&&i==this.boardBase.barNum){
									break;
								}
								// 缁熻鍥捐缃浜嗗悧
								xAxis.push(res[i].xingming);
								yAxis.push(parseFloat((res[i].total)));
								pArray.push({
									value: parseFloat((res[i].total)),
									name: res[i].xingming
								})
							}
							var option = {};
							let titleObj = this.bar.title
							titleObj.text = '员工薪资'
							
							const legendObj = this.bar.legend
							let tooltipObj = {trigger: 'item',formatter: '{b} : {c}'}
							tooltipObj = Object.assign(tooltipObj , this.bar.tooltip?this.bar.tooltip:{})
							let xAxisObj = this.bar.xAxis
							xAxisObj.type = 'category'
							xAxisObj.data = xAxis
							
							let yAxisObj = this.bar.yAxis
							yAxisObj.type = 'value'
							let seriesObj = {
								data: yAxis,
								type: 'bar',
							}
							seriesObj = Object.assign(seriesObj , this.bar.series)
							const gridObj = this.bar.grid
							option = {
								backgroundColor: this.bar.backgroundColor,
								color: this.bar.color,
								title: titleObj,
								legend: legendObj,
								grid: gridObj,
								tooltip: tooltipObj,
								xAxis: xAxisObj,
								yAxis: yAxisObj,
								series: [seriesObj]
							};
							// 使用配置项和数据渲染图表
							shifagongziChart1.setOption(option);
							  //鏍规嵁绐楀彛鐨勫ぇ灏忓彉鍔ㄥ浘琛?
							window.onresize = function() {
								shifagongziChart1.resize();
							};
						}
					})
				})
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
			},
			search() {
				this.searchFormSafe
				this.pageIndex = 1;
				this.getDataList();
			},

			// 获取数据列表
			getDataList() {
				const sf = this.searchFormSafe
				this.dataListLoading = true;
				let params = {
					page: this.pageIndex,
					limit: this.pageSize,
					sort: 'id',
					order: 'desc',
				}
				if(sf.xingming!='' && sf.xingming!=undefined){
					params['xingming'] = sf.xingming
				}
				if(sf.ispay!='' && sf.ispay!=undefined){
					params['ispay'] = sf.ispay
				}
				if(sf.dengjiriqistart!='' && sf.dengjiriqistart!=undefined ){
					params['dengjiriqistart'] = sf.dengjiriqistart
				}
				if(sf.dengjiriqiend!='' && sf.dengjiriqiend!=undefined){
					params['dengjiriqiend'] = sf.dengjiriqiend
				}
				this.$http({
					url: "yuangongxinzi/page",
					method: "get",
					params: params
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.dataList = data.data.list;
						this.totalPage = data.data.total;
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
			selectionChangeHandler(val) {
				this.dataListSelections = val;
			},
			payBatchDialog(row) {
				this.payBatchIds = [];
				const rows = row ? [row] : this.dataListSelections;
				for (let x in rows) {
					const current = rows[x];
					if (!current || !current.id) {
						continue;
					}
					if (this.isPayLocked(current)) {
						this.$message.error('存在申诉中的数据，暂不能支付');
						this.payBatchIds = [];
						return false;
					}
					if (current.sfsh !== '是') {
						this.$message.error('存在未审核通过的数据，不能继续支付');
						this.payBatchIds = [];
						return false;
					}
					this.payBatchIds.push(current.id);
				}
				if (!this.payBatchIds.length) {
					this.$message.error('请选择要支付的数据');
					return false;
				}
				this.payBatchForm.ispay = '';
				this.payBatchVisiable = true;
			},
			payBatchHandler() {
				this.$refs['payBatchForm'].validate(valid => {
					if (valid) {
						this.$confirm(`是否${this.payBatchIds.length > 1 ? '批量设置' : '设置'}支付状态？`, '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						}).then(() => {
							const requests = this.payBatchIds.map(id => {
								return this.$http({
									url: 'yuangongxinzi/update',
									method: 'post',
									data: {
										id,
										ispay: this.payBatchForm.ispay
									}
								});
							});
							Promise.all(requests).then(() => {
								this.$message({
									message: '操作成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.getDataList();
										this.payBatchVisiable = false;
										this.payBatchIds = [];
										this.payBatchForm.ispay = '';
									}
								});
							}).catch(() => {
								this.$message.error('支付状态更新失败，请稍后重试');
							});
						});
					}
				});
			},
			async deleteHandler(id) {
				var ids = id ? [Number(id)] : this.dataListSelections.map(item => {
					return Number(item.id);
				});
				await this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then(async () => {
					await this.$http({
						url: "yuangongxinzi/delete",
						method: "post",
						data: ids
					}).then(async ({ data }) => {
						if (data && data.code === 0) {
							this.$message({
								message: "操作成功",
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
			shBatchDialog() {
				this.batchIds = [];
				for (let x in this.dataListSelections) {
					const row = this.dataListSelections[x];
					if (row.sfsh && row.sfsh !== '待审核') {
						this.$message.error('存在已审核数据，不能继续操作');
						this.batchIds = [];
						return false;
					}
					this.batchIds.push(row.id);
				}
				this.sfshBatchVisiable = true;
			},
			shBatchHandler() {
				this.$refs['shBatchForm'].validate(valid => {
					if (valid) {
						this.$confirm(`是否${this.batchIds.length > 1 ? '批量审核' : '审核'}选中数据？`, '提示', {
							confirmButtonText: '确定',
							cancelButtonText: '取消',
							type: 'warning'
						}).then(() => {
							this.$http({
								url: 'yuangongxinzi/shBatch?sfsh=' + this.shBatchForm.sfsh,
								method: 'post',
								data: this.batchIds
							}).then(({ data }) => {
								if (data && data.code === 0) {
									this.$message({
										message: '操作成功',
										type: 'success',
										duration: 1500,
										onClose: () => {
											this.getDataList();
											this.sfshBatchVisiable = false;
											this.batchIds = [];
											this.shBatchForm.sfsh = '';
										}
									});
								} else {
									this.$message.error(data.msg);
								}
							});
						});
					}
				});
			},
			zhiweishensuCrossAddOrUpdateHandler(row, type, statusColumnName = '', tips = '') {
				this.showFlag = false;
				this.addOrUpdateFlag = false;
				this.zhiweishensuCrossAddOrUpdateFlag = true;
				this.$storage.set('crossObj', row);
				this.$storage.set('crossTable', 'yuangongxinzi');
				this.$storage.set('statusColumnName', statusColumnName);
				this.$storage.set('statusColumnValue', '');
				this.$storage.set('tips', tips);
				this.$nextTick(() => {
					this.$refs.zhiweishensuCrossaddOrUpdate.init(row.id, type);
				});
			},
			async salaryTongyiHandler(row) {
				if (!row || !row.id) {
					return;
				}
				await this.$confirm('确认同意本次薪资发放结果吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(async () => {
					const params = {
						page: 1,
						limit: 1,
						crossuserid: this.$storage.get('userid'),
						crossrefid: row.id
					};
					let existed = false;
					await this.$http({
						url: 'tongyixinxi/page',
						method: 'get',
						params
					}).then(({ data }) => {
						if (data && data.code === 0) {
							existed = data.data.total > 0;
						}
					});
					if (existed) {
						this.$message.warning('该薪资记录已同意，请勿重复操作');
						return;
					}
					await this.$http({
						url: 'tongyixinxi/add',
						method: 'post',
						data: {
							gonghao: row.gonghao,
							xingming: row.xingming,
							tongyishijian: this.getCurDateTime ? this.getCurDateTime() : new Date().toISOString().slice(0, 19).replace('T', ' '),
							guanlizhanghao: row.guanlizhanghao,
							guanlixingming: row.guanlixingming,
							crossuserid: this.$storage.get('userid'),
							crossrefid: row.id
						}
					}).then(({ data }) => {
						if (data && data.code === 0) {
							this.$message.success('已同意');
						} else {
							this.$message.error(data.msg || '操作失败');
						}
					});
				}).catch(() => {});
			},
			// 添加/修改
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
	// echarts1
	.statShow1 {
		padding: 0;
		align-content: flex-start;
		background: none;
		display: flex;
		width: 100%;
		justify-content: space-between;
		flex-wrap: wrap;
		height: auto;
		.echarts1 {
			border-radius: 0px;
			padding: 20px;
			box-shadow: none;
			margin: 10px;
			background: rgba(255,255,255,1);
			width: 100%;
			transition: 0.3s;
			height: 400px;
		}
		.echarts1:hover {
			box-shadow: 0 3px 0px rgba(0,0,0,.1);
			transform: translate3d(0, -6px, 0);
			z-index: 1;
			background: rgba(255,255,255,1);
		}
	}
</style>
