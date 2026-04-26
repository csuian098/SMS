  
<template>
	<div class="main-content" :style='{"padding":"50px 30px 30px"}'>
		<!-- 列表页 -->
		<template v-if="showFlag ">
			<el-form class="center-form-pv" :style='{"width":"100%","padding":"0","margin":"0 0 20px"}' :inline="true" :model="searchForm">
				<el-row v-if="queryChange(['人事管理员', '管理员'])" :style='{"padding":"10px 20px 0","boxShadow":"0 3px 3px 0px #095dac","borderRadius":"0px","flexWrap":"wrap","background":"#fff","display":"flex","width":"100%","position":"relative","alignItems":"center"}' >
					<div :style='{"margin":"0 1% 10px 0","display":"flex"}'>
						<label :style='{"margin":"0 10px 0 0","whiteSpace":"nowrap","color":"#666","display":"inline-block","lineHeight":"40px","fontSize":"16px","fontWeight":"500","height":"40px"}' class="item-label">工号</label>
						<el-input v-model="searchForm.gonghao" placeholder="工号" clearable></el-input>
					</div>
					<div :style='{"margin":"0 1% 10px 0","display":"flex"}'>
						<label :style='{"margin":"0 10px 0 0","whiteSpace":"nowrap","color":"#666","display":"inline-block","lineHeight":"40px","fontSize":"16px","fontWeight":"500","height":"40px"}' class="item-label">姓名</label>
						<el-input v-model="searchForm.xingming" placeholder="姓名" clearable></el-input>
					</div>
					<div :style='{"margin":"0 1% 10px 0","display":"flex","alignItems":"center"}'>
						<label :style='{"margin":"0 10px 0 0","whiteSpace":"nowrap","color":"#666","display":"inline-block","lineHeight":"40px","fontSize":"16px","fontWeight":"500","height":"40px"}' class="item-label">月份</label>
						<el-date-picker v-model="calendarMonth" type="month" value-format="yyyy-MM" placeholder="选择月份" size="small" @change="onCalendarMonthChange"></el-date-picker>
					</div>
					<el-button type="primary" size="small" :style='{"margin":"0 8px 10px 0"}' @click="search">查询</el-button>
				</el-row>
				<el-row v-else :style='{"padding":"10px 20px","boxShadow":"0 3px 3px 0px #095dac","borderRadius":"0px","flexWrap":"wrap","background":"#fff","display":"flex","width":"100%","alignItems":"center","gap":"12px"}'>
					<label :style='{"whiteSpace":"nowrap","color":"#666","fontSize":"15px","fontWeight":"500"}'>我的考勤月历</label>
					<el-date-picker v-model="calendarMonth" type="month" value-format="yyyy-MM" placeholder="选择月份" size="small" @change="onCalendarMonthChange"></el-date-picker>
					<el-button type="primary" size="small" @click="onCalendarMonthChange">刷新日历</el-button>
				</el-row>

				<div class="sign-calendar-panel" :style='{"margin":"16px 0","padding":"16px 18px","border":"1px solid #e9eafc","borderRadius":"10px","background":"#fff"}'>
					<div class="sign-calendar-toolbar">
						<span class="sign-calendar-title">{{ calendarMonth }} 考勤</span>
						<span class="sign-legend"><span class="sign-dot sign-dot-ot"></span>签到+加班</span>
						<span class="sign-legend"><span class="sign-dot sign-dot-sign"></span>已签到</span>
						<span class="sign-legend"><span class="sign-dot sign-dot-none"></span>未签到/请假</span>
						<span class="sign-legend"><span class="sign-dot sign-dot-idle"></span>待查询（未加载后台数据）</span>
					</div>
					<div v-loading="calendarLoading" class="sign-calendar-inner">
						<div class="sign-week-row">
							<span v-for="w in weekNames" :key="w" class="sign-week-cell">{{ w }}</span>
						</div>
						<div class="sign-days-grid">
							<div v-for="(c, idx) in calendarCells" :key="'c'+idx" :class="['sign-day-cell', c.pad ? 'is-pad' : '', c.today ? 'is-today' : '', !c.pad && selectedDate === c.dateStr ? 'is-selected' : '']" @click="onCalendarDayClick(c)">
								<template v-if="!c.pad">
									<span class="sign-day-num">{{ c.day }}</span>
									<span v-if="c.markType === 'sign_ot'" class="sign-day-mark sign-day-mark-ot" title="当日已签到并有加班记录"></span>
									<span v-else-if="c.markType === 'sign'" class="sign-day-mark sign-day-mark-sign" title="当日已签到"></span>
									<span v-else-if="c.markType === 'none'" class="sign-day-mark sign-day-mark-none" title="当日无签到记录（含请假等未出勤）"></span>
									<span v-else class="sign-day-mark sign-day-mark-idle" title="尚未加载当月签到数据（人事请先输入工号或姓名并点击查询；加载完成前不表示考勤结果）"></span>
								</template>
							</div>
						</div>
						<p v-if="calendarHint" class="sign-calendar-hint">{{ calendarHint }}</p>
					</div>
				</div>

				<el-row v-if="tablename=='yuangong'" class="sign-actions-row" :style='{"width":"100%","margin":"16px 0 0","padding":"0","display":"flex","gap":"12px"}'>
					<el-button type="primary" class="sign-btn" :disabled="!canQuickSign('签到')" @click="quickSign('签到')">1 签到</el-button>
					<el-button type="primary" class="sign-btn" :disabled="!canQuickSign('签退')" @click="quickSign('签退')">2 签退</el-button>
					<el-button type="primary" class="sign-btn" :disabled="!canQuickSign('加班开始')" @click="quickSign('加班开始')">3 加班开始</el-button>
					<el-button type="primary" class="sign-btn" :disabled="!canQuickSign('加班结束')" @click="quickSign('加班结束')">4 加班结束</el-button>
				</el-row>
			</el-form>
			<div :style='{"border":"1px solid #e9eafc","width":"100%","padding":" 0px 15px 20px","boxShadow":"none","borderRadius":"10px","background":"#fff"}'>
<!--				<p v-if="listUsesMonthFilter && calendarMonthValid" class="sign-list-range-hint">下方列表与上方日历一致，仅展示签到时间落在「{{ calendarMonth }}」内的记录。若日历全红但您认为应有签到，请把「月份」选到与库里签到时间一致的年月（例如数据在 2025 年则选 2025-04）。</p>-->
				<el-table class="tables"
					:stripe='false'
					:style='{"padding":"0","borderColor":"#e7e8fc","borderRadius":"10px","borderWidth":"0px 0 0 0px","background":"#fff","width":"100%","borderStyle":"solid"}' 
					:border='false'
					v-if="isAuth('yuangongqiandao','查看')"
					:data="dataList"
					v-loading="dataListLoading || quickSignLoading">
					<el-table-column :resizable='true' :sortable='true' label="序号" type="index" width="50" />
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
					<el-table-column  :resizable='true' prop="touxiang" width="200" label="头像">
						<template slot-scope="scope">
							<div v-if="scope.row.touxiang">
								<img v-if="scope.row.touxiang.substring(0,4)=='http'&&scope.row.touxiang.split(',w').length>1" :src="scope.row.touxiang" width="100" height="100" style="object-fit: cover" @click="imgPreView(scope.row.touxiang)">
								<img v-else-if="scope.row.touxiang.substring(0,4)=='http'" :src="scope.row.touxiang.split(',')[0]" width="100" height="100" style="object-fit: cover" @click="imgPreView(scope.row.touxiang.split(',')[0])">
								<img v-else :src="$base.url+scope.row.touxiang.split(',')[0]" width="100" height="100" style="object-fit: cover" @click="imgPreView($base.url+scope.row.touxiang.split(',')[0])">
							</div>
							<div v-else>无图片</div>
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="qiandaoshijian"
						label="签到时间">
						<template slot-scope="scope">
							{{scope.row.qiandaoshijian}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="qiandaodidian"
						label="签到类型">
						<template slot-scope="scope">
							{{scope.row.qiandaodidian}}
						</template>
					</el-table-column>
					<el-table-column width="300" label="操作">
						<template slot-scope="scope">
							<el-button class="view" v-if=" isAuth('yuangongqiandao','查看')" type="success" @click="addOrUpdateHandler(scope.row.id,'info')">
								<span class="icon iconfont icon-chakan2" :style='{"margin":"0 0px","fontSize":"14px","color":"#333","display":"none","height":"40px"}'></span>
								详情
							</el-button>
							<el-button class="edit" v-if=" isAuth('yuangongqiandao','修改') " type="success" @click="addOrUpdateHandler(scope.row.id)">
								<span class="icon iconfont icon-xiugai13" :style='{"margin":"0 0px","fontSize":"14px","color":"rgba(255, 140, 0, 1)","display":"none","height":"40px"}'></span>
								修改
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
		<!-- 添加/修改页面  将父组件的search方法传递给子组件-->
		<add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>





		<el-dialog title="预览图" :visible.sync="previewVisible" width="50%">
			<img :src="previewImg" alt="" style="width: 100%;">
		</el-dialog>
	</div>
</template>

<script>
	import * as echarts from 'echarts'
	import chinaJson from "@/components/echarts/china.json";
	import axios from 'axios';
	import AddOrUpdate from "./add-or-update";
	import {
		Loading
	} from 'element-ui';
	export default {
		data() {
			const d = new Date()
			const ym = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`
			return {
				indexQueryCondition: '',
				searchForm: {
					key: ""
				},
				form:{},
				dataList: [],
				pageIndex: 1,
				pageSize: 5,
				totalPage: 0,
				dataListLoading: false,
				quickSignLoading: false,
				showFlag: true,
				addOrUpdateFlag:false,
				layouts: ["total","prev","pager","next","sizes","jumper"],
				previewImg: '',
				previewVisible: false,
				calendarMonth: ym,
				calendarSignMap: {},
				/** 人事/管理员未填工号、姓名时 true：不请求后台，日历用灰色占位 */
				signCalendarAwaitingFilter: false,
				/** 当月日历是否已拿到一次后台列表（避免请求前整月误判为未签到） */
				calendarDataReady: false,
				calendarLoading: false,
				calendarHint: '',
				selectedDate: null,
				weekNames: ['日', '一', '二', '三', '四', '五', '六'],
				todaySignState: {
					hasSignIn: false,
					hasSignOut: false,
					hasOvertimeStart: false,
					hasOvertimeEnd: false,
				},
			};
		},
		created() {
			if(this.statType) {
				return false
			}
			this.init();
			this.getDataList();
		},
		mounted() {
			this.$nextTick(() => {
				this.loadSignCalendar();
			});
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
			calendarMonthValid() {
				const ym = this.calendarMonth;
				return !!(ym && /^\d{4}-\d{2}$/.test(ym));
			},
			/** 员工或人事已填工号/姓名时：列表按所选月份筛；人事未填则列表不按月筛，避免默认当前月无数据时整表空白 */
			listUsesMonthFilter() {
				if (!this.calendarMonthValid) return false;
				if (!this.queryChange(['人事管理员', '管理员'])) {
					return true;
				}
				const gh = this.plain(this.searchForm.gonghao);
				const xm = this.plain(this.searchForm.xingming);
				return !!(gh || xm);
			},
			calendarCells() {
				const ym = this.calendarMonth;
				if (!ym || !/^\d{4}-\d{2}$/.test(ym)) {
					return [];
				}
				const parts = ym.split('-').map(Number);
				const y = parts[0];
				const m = parts[1];
				const pad = (n) => (n < 10 ? '0' : '') + n;
				const firstDow = new Date(y, m - 1, 1).getDay();
				const daysInMonth = new Date(y, m, 0).getDate();
				const now = new Date();
				const todayStr = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}`;
				const cells = [];
				for (let i = 0; i < firstDow; i++) {
					cells.push({ pad: true });
				}
				for (let day = 1; day <= daysInMonth; day++) {
					const ds = `${y}-${pad(m)}-${pad(day)}`;
					const placeholder = this.signCalendarAwaitingFilter || !this.calendarDataReady
					let markType = 'idle'
					if (!placeholder) {
						if (this.calendarSignMap[ds] === 'sign_ot' || this.calendarSignMap[ds] === 'sign') {
							markType = this.calendarSignMap[ds]
						} else {
							markType = 'none'
						}
					}
					cells.push({
						pad: false,
						day,
						dateStr: ds,
						markType,
						today: ds === todayStr,
					});
				}
				return cells;
			},
		},
		components: {
			AddOrUpdate,
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
			nowDateTimeStr() {
				const d = new Date()
				const p = n => (n < 10 ? '0' : '') + n
				return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())} ${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}`
			},
			quickSign(qiandaodidian) {
				if (this.tablename !== 'yuangong') {
					this.$message.warning('请使用员工账号进行打卡')
					return
				}
				let u = {}
				try {
					const raw = this.$storage.getObj('userForm')
					u = typeof raw === 'string' ? JSON.parse(raw) : (raw || {})
				} catch (e) {
					this.$message.error('无法读取登录信息')
					return
				}
				const payload = {
					gonghao: u.gonghao || '',
					xingming: u.xingming || '',
					touxiang: u.touxiang || '',
					qiandaoshijian: this.nowDateTimeStr(),
					qiandaodidian: qiandaodidian
				}
				if (!payload.gonghao) {
					this.$message.error('工号为空，无法打卡')
					return
				}
				this.quickSignLoading = true
				this.$http({
					url: 'yuangongqiandao/save',
					method: 'post',
					data: payload
				}).then(({ data }) => {
					this.quickSignLoading = false
					if (data && data.code === 0) {
						this.$message.success(qiandaodidian + ' 已记录')
						this.search()
					} else {
						this.$message.error((data && data.msg) || '保存失败')
					}
				}).catch(() => {
					this.quickSignLoading = false
				})
			},
			imgPreView(url){
				this.previewImg = url
				this.previewVisible = true
				
			},
			init () {
			},
			search() {
				this.pageIndex = 1;
				this.selectedDate = null;
				this.getDataList();
				this.loadSignCalendar();
			},
			plain(s) {
				return (s == null ? '' : String(s)).replace(/%/g, '').trim();
			},
			/** 与日历同一自然月，避免列表混入其他年份/月份造成「有签到却全红」的错觉 */
			applyCalendarMonthRangeToParams(params) {
				const ym = this.calendarMonth;
				if (!ym || !/^\d{4}-\d{2}$/.test(ym)) return;
				const parts = ym.split('-').map(Number);
				const y = parts[0];
				const m = parts[1];
				const pad = (n) => (n < 10 ? '0' : '') + n;
				const lastDay = new Date(y, m, 0).getDate();
				params.qiandaoshijian_start = `${ym}-01 00:00:00`;
				params.qiandaoshijian_end = `${ym}-${pad(lastDay)} 23:59:59`;
			},
			onCalendarDayClick(c) {
				if (c.pad) return;
				this.selectedDate = this.selectedDate === c.dateStr ? null : c.dateStr;
				this.pageIndex = 1;
				this.getDataList();
			},
			onCalendarMonthChange() {
				this.selectedDate = null;
				this.loadSignCalendar();
				this.getDataList();
			},
			canQuickSign(type) {
				const s = this.todaySignState || {}
				if (type === '签到') return !s.hasSignIn && !s.hasSignOut && !s.hasOvertimeStart && !s.hasOvertimeEnd
				if (type === '签退') return s.hasSignIn && !s.hasSignOut && !s.hasOvertimeStart && !s.hasOvertimeEnd
				if (type === '加班开始') return s.hasSignIn && s.hasSignOut && !s.hasOvertimeStart && !s.hasOvertimeEnd
				if (type === '加班结束') return s.hasOvertimeStart && !s.hasOvertimeEnd
				return false
			},
			loadTodaySignState() {
				if (this.tablename !== 'yuangong') return
				let u = {}
				try {
					const raw = this.$storage.getObj('userForm')
					u = typeof raw === 'string' ? JSON.parse(raw) : (raw || {})
				} catch (e) {
					return
				}
				const gonghao = u.gonghao || ''
				if (!gonghao) return
				const d = new Date()
				const p = n => (n < 10 ? '0' : '') + n
				const day = `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}`
				const params = {
					page: 1,
					limit: 200,
					sort: 'qiandaoshijian',
					order: 'asc',
					gonghao: gonghao,
					qiandaoshijian_start: `${day} 00:00:00`,
					qiandaoshijian_end: `${day} 23:59:59`,
				}
				this.$http({
					url: 'yuangongqiandao/page',
					method: 'get',
					params,
				}).then(({ data }) => {
					const state = {
						hasSignIn: false,
						hasSignOut: false,
						hasOvertimeStart: false,
						hasOvertimeEnd: false,
					}
					if (data && data.code === 0 && data.data && data.data.list) {
						data.data.list.forEach(row => {
							const t = row.qiandaodidian == null ? '' : String(row.qiandaodidian)
							if (t === '签到') state.hasSignIn = true
							if (t === '签退') state.hasSignOut = true
							if (t === '加班开始') state.hasOvertimeStart = true
							if (t === '加班结束') state.hasOvertimeEnd = true
						})
					}
					this.todaySignState = state
				})
			},
			loadSignCalendar() {
				const ym = this.calendarMonth;
				if (!ym || !/^\d{4}-\d{2}$/.test(ym)) {
					return;
				}
				if (this.queryChange(['人事管理员', '管理员'])) {
					const gh = this.plain(this.searchForm.gonghao);
					const xm = this.plain(this.searchForm.xingming);
					if (!gh && !xm) {
						this.calendarSignMap = {};
						this.signCalendarAwaitingFilter = true;
						this.calendarDataReady = false;
						this.calendarHint = '请输入工号或姓名后点击「查询」，即可从后台加载当月签到日历（未查询前灰色圆点不代表考勤结果）。';
						return;
					}
				}
				this.signCalendarAwaitingFilter = false;
				this.calendarDataReady = false;
				this.calendarHint = '';
				const parts = ym.split('-').map(Number);
				const y = parts[0];
				const m = parts[1];
				const pad = (n) => (n < 10 ? '0' : '') + n;
				const lastDay = new Date(y, m, 0).getDate();
				const start = `${ym}-01 00:00:00`;
				const end = `${ym}-${pad(lastDay)} 23:59:59`;
				let params = {
					page: 1,
					limit: 4000,
					sort: 'qiandaoshijian',
					order: 'asc',
					qiandaoshijian_start: start,
					qiandaoshijian_end: end,
				};
				const gh = this.plain(this.searchForm.gonghao);
				const xm = this.plain(this.searchForm.xingming);
				if (gh) {
					params.gonghao = '%' + gh + '%';
				}
				if (xm) {
					params.xingming = '%' + xm + '%';
				}
				this.calendarLoading = true;
				this.$http({
					url: 'yuangongqiandao/page',
					method: 'get',
					params,
				}).then(({ data }) => {
					this.calendarLoading = false;
					if (!data || data.code !== 0) {
						this.$message.error((data && data.msg) || '加载签到日历失败')
						this.calendarSignMap = {}
						this.calendarDataReady = false
						return
					}
					this.calendarDataReady = true
					const byDay = {}
					if (data.data && data.data.list) {
						data.data.list.forEach((row) => {
							const t = row.qiandaoshijian
							if (!t) return
							const dayKey = String(t).substring(0, 10)
							if (!byDay[dayKey]) byDay[dayKey] = { sign: false, ot: false }
							const typ = row.qiandaodidian == null ? '' : String(row.qiandaodidian).trim()
							if (typ === '签到' || typ.indexOf('签到') >= 0) {
								byDay[dayKey].sign = true
							}
							if (typ === '加班开始' || typ === '加班结束') {
								byDay[dayKey].ot = true
							}
						})
					}
					const map = {}
					Object.keys(byDay).forEach((d) => {
						const { sign, ot } = byDay[d]
						if (sign && ot) map[d] = 'sign_ot'
						else if (sign) map[d] = 'sign'
					})
					this.calendarSignMap = map
				}).catch(() => {
					this.calendarLoading = false;
					this.calendarDataReady = false;
					this.$message.error('加载签到日历失败，请检查网络后重试')
				});
			},

			// 获取数据列表
			getDataList() {
				this.dataListLoading = true;
				let params = {
					page: this.pageIndex,
					limit: this.pageSize,
					sort: 'qiandaoshijian',
					order: 'desc',
				}
				if(this.searchForm.gonghao!='' && this.searchForm.gonghao!=undefined){
					params['gonghao'] = '%' + this.searchForm.gonghao + '%'
				}
				if(this.searchForm.xingming!='' && this.searchForm.xingming!=undefined){
					params['xingming'] = '%' + this.searchForm.xingming + '%'
				}
				if (this.selectedDate) {
					params.qiandaoshijian_start = `${this.selectedDate} 00:00:00`;
					params.qiandaoshijian_end = `${this.selectedDate} 23:59:59`;
				} else if (this.listUsesMonthFilter) {
					this.applyCalendarMonthRangeToParams(params);
				}
				this.$http({
					url: "yuangongqiandao/page",
					method: "get",
					params: params
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.dataList = data.data.list;
						this.totalPage = data.data.total;
					} else {
						this.dataList = [];
						this.totalPage = 0;
						if (data && data.msg) {
							this.$message.error(data.msg)
						}
					}
					this.dataListLoading = false;
					this.loadTodaySignState()
				}).catch(() => {
					this.dataListLoading = false;
					this.$message.error('加载签到列表失败，请稍后重试')
				});
			},
			// 每页数
			sizeChangeHandle(val) {
				this.pageSize = val;
				this.pageIndex = 1;
				this.getDataList();
			},
			// 当前页
			currentChangeHandle(val) {
				this.pageIndex = val;
				this.getDataList();
			},
			// 添加/修改
			addOrUpdateHandler(id,type) {
				this.showFlag = false;
				this.addOrUpdateFlag = true;
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
	.sign-actions-row {
		.sign-btn {
			flex: 1;
			margin: 0 !important;
			min-height: 42px;
			font-size: 15px;
		}
	}
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

	.sign-calendar-toolbar {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		gap: 16px;
		margin-bottom: 12px;
	}
	.sign-calendar-title {
		font-size: 16px;
		font-weight: 600;
		color: #095dac;
	}
	.sign-legend {
		font-size: 13px;
		color: #666;
		display: inline-flex;
		align-items: center;
		gap: 6px;
	}
	.sign-dot {
		display: inline-block;
		width: 12px;
		height: 12px;
		border-radius: 50%;
	}
	.sign-dot-ot {
		background: #e6a23c;
		box-shadow: 0 0 0 1px rgba(230, 162, 60, 0.35);
	}
	.sign-dot-sign {
		background: #19be6b;
	}
	.sign-dot-none {
		background: #e02424;
	}
	.sign-dot-idle {
		background: #dcdfe6;
		box-shadow: 0 0 0 1px rgba(144, 147, 153, 0.35);
	}
	.sign-week-row {
		display: grid;
		grid-template-columns: repeat(7, 1fr);
		gap: 6px;
		margin-bottom: 6px;
	}
	.sign-week-cell {
		text-align: center;
		font-size: 13px;
		color: #666;
		font-weight: 500;
		padding: 6px 0;
	}
	.sign-days-grid {
		display: grid;
		grid-template-columns: repeat(7, 1fr);
		gap: 6px;
	}
	.sign-day-cell {
		min-height: 76px;
		border: 1px solid #e7e8fc;
		border-radius: 8px;
		padding: 8px 4px;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: flex-start;
		background: #fafcfe;
	}
	.sign-day-cell.is-pad {
		min-height: 0;
		height: 8px;
		border: none;
		background: transparent;
		padding: 0;
	}
	.sign-day-cell:not(.is-pad) {
		cursor: pointer;
		transition: background 0.15s;
	}
	.sign-day-cell:not(.is-pad):hover {
		background: #eef4fd;
	}
	.sign-day-cell.is-today {
		box-shadow: inset 0 0 0 2px #095dac;
	}
	.sign-day-cell.is-selected {
		background: #ddeaff;
		box-shadow: inset 0 0 0 2px #4f7df5;
	}
	.sign-day-num {
		font-size: 15px;
		font-weight: 600;
		color: #333;
	}
	.sign-day-mark {
		width: 18px;
		height: 18px;
		border-radius: 50%;
		margin-top: 8px;
		flex-shrink: 0;
	}
	.sign-day-mark-ot {
		background: linear-gradient(145deg, #f5d000, #e6a23c);
		box-shadow: 0 0 0 1px rgba(230, 162, 60, 0.45);
	}
	.sign-day-mark-sign {
		background: #19be6b;
	}
	.sign-day-mark-none {
		background: #e02424;
	}
	.sign-day-mark-idle {
		background: #eef0f4;
		box-shadow: inset 0 0 0 2px #c0c4cc;
	}
	.sign-calendar-hint {
		margin: 12px 0 0;
		font-size: 13px;
		color: #909399;
	}
	.sign-list-range-hint {
		margin: 0 0 12px;
		padding: 8px 0 0;
		font-size: 13px;
		color: #606266;
		line-height: 1.5;
	}
</style>
