<template>
	<div class="home-content" :style="{'backgroundImage': indexBgUrl?`url(${$base.url + indexBgUrl})`:''}">
		<div id="home-title" class="home-title animate__animated">
			<div class="titles">
				<div class="welcome-line1">欢迎 {{ homeWelcomeZhiwei }} {{ homeWelcomeXingming }} 使用</div>
				<div class="welcome-line2">{{ this.$project.projectName }}</div>
			</div>
		</div>
		<div class="statis-box" :class="{'statis-box-admin': isAdminHome}">
			<div id="statis1" class="statis1 animate__animated" v-if="isHomeVisible('yuangong','首页总数')">
				<div class="left"><span class="icon iconfont icon-liulan12"></span></div>
				<div class="right">
					<div class="num">{{yuangongCount}}</div>
					<div class="name">员工总数</div>
				</div>
			</div>
			<div id="statis2" class="statis2 animate__animated" v-if="isHomeVisible('renshiguanliyuan','首页总数')">
				<div class="left"><span class="icon iconfont icon-shuju17"></span></div>
				<div class="right">
					<div class="num">{{renshiguanliyuanCount}}</div>
					<div class="name">人事管理员总数</div>
				</div>
			</div>
			<div id="statis3" class="statis3 animate__animated" v-if="isHomeVisible('yuangongxinzi','首页总数')">
				<div class="left"><span class="icon iconfont icon-fenxiang"></span></div>
				<div class="right">
					<div class="num">{{yuangongxinziCount}}</div>
					<div class="name">员工薪资总数</div>
				</div>
			</div>
			<div id="statis4" class="statis4 animate__animated" v-if="isAdminHome" @click="goReminder('/yuangongxinzi')">
				<div class="left"><span class="icon iconfont icon-shuju17"></span></div>
				<div class="right">
					<div class="num">{{adminSalaryPendingCount}}</div>
					<div class="name">薪资待审核</div>
				</div>
			</div>
		</div>

		<div class="reminder-box" v-if="reminderCards.length">
			<div class="reminder-card" v-for="item in reminderCards" :key="item.key" @click="goReminder(item.path)">
				<div class="reminder-main">
					<div class="reminder-title">{{ item.title }}</div>
					<div class="reminder-desc">{{ item.desc }}</div>
				</div>
				<div class="reminder-count">{{ item.count }}</div>
			</div>
		</div>

		<div class="type4 type4-double">
			<div class="echarts1 animate__animated" v-if="isHomeVisible('yuangong','首页统计',2)">
				<div id="yuangongChart1" style="width: 100%;height: 100%"></div>
			</div>
			<div class="echarts2 animate__animated" v-if="isHomeVisible('renshiguanliyuan','首页统计',2)">
				<div id="renshiguanliyuanChart1" style="width: 100%;height: 100%"></div>
			</div>
		</div>

		<div id="dashboard-salary-row" class="dashboard-salary-row animate__animated" v-if="isHomeVisible('yuangongxinzi','首页统计',2)">
			<div class="salary-card">
				<div class="section-title">工资统计分析（已审核）</div>
				<div class="salary-summary-table-wrap">
					<el-table :data="xinziStatRows" stripe border size="small" class="salary-stat-table" empty-text="暂无数据">
						<el-table-column prop="zhibiao" label="指标" min-width="220" />
						<el-table-column prop="shuzhi" label="数值" min-width="140" align="right" />
					</el-table>
				</div>
				<div class="salary-charts-row">
					<div class="salary-histogram-wrap">
						<div id="yuangongxinziHistogram" class="salary-chart-inner"></div>
					</div>
					<div class="salary-trend-wrap">
						<div id="yuangongxinziTrend" class="salary-chart-inner"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script>
import 'animate.css'
//4
import router from '@/router/router-static'
import * as echarts from 'echarts'
export default {
	data() {
		return {
			yuangongCount: 0,
			renshiguanliyuanCount: 0,
			yuangongxinziCount: 0,
			adminSalaryPendingCount: 0,
			/** 棣栭〉钖祫鍖猴細姹囨€绘寚鏍囪〃 */
			xinziStatRows: [],
			salaryHistogramChartInst: null,
			salaryTrendChartInst: null,
			reminderCards: [],
			line: {"backgroundColor":"transparent","yAxis":{"axisLabel":{"borderType":"solid","rotate":0,"padding":0,"shadowOffsetX":0,"margin":15,"backgroundColor":"transparent","borderColor":"#000","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"width":"","fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":true,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#666","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"minInterval":1,"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,0.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"xAxis":{"axisLabel":{"borderType":"solid","rotate":30,"padding":0,"shadowOffsetX":0,"margin":10,"backgroundColor":"transparent","borderColor":"#000","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"truncate","borderRadius":0,"borderWidth":0,"width":120,"interval":0,"fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":true,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":false},"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"color":["#378bb9","#095dac","#fbc62f","#16606a","#f75d5f","#5967cc","#faa07b","#9ab7a8","#ea7ccc"],"legend":{"padding":0,"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"orient":"horizontal","shadowBlur":0,"bottom":"auto","itemHeight":14,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"right":"auto","top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"right","borderWidth":0,"width":"80%","itemWidth":20,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":24,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{"showSymbol":true,"symbol":"pin","symbolSize":18},"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"title":{"borderType":"solid","padding":0,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"shadowBlur":0,"bottom":"auto","show":true,"right":"auto","top":"auto","borderRadius":0,"left":"left","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","fontSize":14,"lineHeight":24,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":600,"textBorderColor":"#666","textShadowBlur":0},"shadowColor":"transparent"}},
			bar: {"backgroundColor":"transparent","yAxis":{"axisLabel":{"borderType":"solid","rotate":0,"padding":0,"shadowOffsetX":0,"margin":12,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"width":"","fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":true,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#666","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"minInterval":1,"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,0.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"xAxis":{"axisLabel":{"borderType":"solid","rotate":30,"padding":0,"shadowOffsetX":0,"margin":10,"backgroundColor":"transparent","borderColor":"#000","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"truncate","borderRadius":0,"borderWidth":0,"width":120,"interval":0,"fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":true,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":false},"minInterval":1,"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"color":["#378bb9","#095dac","#fbc62f","#16606a","#f75d5f","#5967cc","#faa07b","#9ab7a8","#ea7ccc"],"legend":{"padding":0,"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"orient":"horizontal","shadowBlur":0,"bottom":"auto","itemHeight":14,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"right":"auto","top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"right","borderWidth":0,"width":"80%","itemWidth":20,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":12,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"grid":{"x":"25%","y":"15%","y2":"15%","x2":"5%"},"series":{"barWidth":"auto","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"#666","shadowOffsetY":0,"color":"","shadowBlur":0,"barBorderRadius":[30,30,30,30],"borderWidth":0,"opacity":1,"shadowColor":"#000"},"colorBy":"data","barCategoryGap":"20%"},"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"title":{"borderType":"solid","padding":0,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"subtext":"","shadowBlur":0,"bottom":"auto","show":true,"right":"auto","subtextStyle":{"padding":[5,0,0,0],"borderColor":"red","color":"red","borderWidth":10},"top":"auto","borderRadius":0,"left":"left","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","fontSize":14,"lineHeight":24,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":600,"textBorderColor":"#666","textShadowBlur":0},"shadowColor":"transparent"},"base":{"animate":false,"interval":2000}},
			pie: {"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"backgroundColor":"transparent","color":["#378bb9","#095dac","#fbc62f","#16606a","#f75d5f","#5967cc","#faa07b","#9ab7a8","#ea7ccc"],"title":{"borderType":"solid","padding":[5,0,0,0],"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"shadowBlur":0,"bottom":"auto","show":true,"right":"auto","top":"auto","borderRadius":0,"left":"left","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","fontSize":14,"lineHeight":14,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":600,"textBorderColor":"#666","textShadowBlur":0},"shadowColor":"transparent"},"legend":{"padding":[5,0,0,0],"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"orient":"horizontal","shadowBlur":0,"bottom":"auto","itemHeight":2,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"right":0,"top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"right","borderWidth":0,"width":"80%","itemWidth":2,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":12,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{"itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"#666","shadowOffsetY":0,"color":"","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"#000"},"label":{"borderType":"solid","rotate":0,"padding":0,"textBorderWidth":0,"backgroundColor":"transparent","borderColor":"#666","color":"inherit","show":true,"textShadowColor":"transparent","distanceToLabelLine":5,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"fontSize":12,"lineHeight":18,"textShadowOffsetX":0,"position":"outside","textShadowOffsetY":0,"textBorderType":"solid","textBorderColor":"#666","textShadowBlur":0},"labelLine":{"show":true,"length":10,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"#666","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"#000"},"length2":14,"smooth":false}}},
			funnel: {"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"backgroundColor":"transparent","color":["#378bb9","#095dac","#fbc62f","#16606a","#f75d5f","#5967cc","#faa07b","#9ab7a8","#ea7ccc"],"title":{"borderType":"solid","padding":2,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#ccc","shadowOffsetY":0,"shadowBlur":0,"bottom":"auto","show":true,"right":"auto","top":"auto","borderRadius":0,"left":"center","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#666","textShadowColor":"transparent","fontSize":14,"lineHeight":12,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"#ccc","textShadowBlur":0},"shadowColor":"transparent"},"legend":{"padding":5,"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#ccc","shadowOffsetY":0,"orient":"vertical","shadowBlur":0,"bottom":"auto","itemHeight":2,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"left","borderWidth":0,"width":"auto","itemWidth":2,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":20,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{"itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"#000","shadowOffsetY":0,"color":"","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"#000"},"label":{"borderType":"solid","rotate":0,"padding":0,"textBorderWidth":0,"backgroundColor":"transparent","borderColor":"#fff","color":"","show":true,"textShadowColor":"transparent","distanceToLabelLine":5,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"fontSize":12,"lineHeight":18,"textShadowOffsetX":0,"position":"outside","textShadowOffsetY":0,"textBorderType":"solid","textBorderColor":"#fff","textShadowBlur":0},"labelLine":{"show":true,"length":10,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"#000"},"length2":14,"smooth":false}}},
			boardBase: {"funnelNum":8,"lineNum":8,"radarNum":8,"gaugeNum":8,"barNum":8,"pieNum":8},
			gauge: {"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"backgroundColor":"transparent","color":["#378bb9","#095dac","#fbc62f","#16606a","#f75d5f","#5967cc","#faa07b","#9ab7a8","#ea7ccc"],"title":{"top":"top","left":"left","textStyle":{"fontSize":14,"lineHeight":24,"color":"#333","fontWeight":600}},"series":{"pointer":{"offsetCenter":[0,"10%"],"icon":"path://M2.9,0.7L2.9,0.7c1.4,0,2.6,1.2,2.6,2.6v115c0,1.4-1.2,2.6-2.6,2.6l0,0c-1.4,0-2.6-1.2-2.6-2.6V3.3C0.3,1.9,1.4,0.7,2.9,0.7z","width":8,"length":"80%"},"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"opacity":0.5,"shadowBlur":1,"shadowColor":"#000"},"roundCap":true},"anchor":{"show":true,"itemStyle":{"color":"inherit"},"size":18,"showAbove":true},"emphasis":{"disabled":false},"progress":{"show":true,"roundCap":true,"overlap":true},"splitNumber":25,"detail":{"formatter":"{value}","backgroundColor":"inherit","color":"#fff","borderRadius":3,"width":20,"fontSize":12,"height":10},"title":{"fontSize":14},"animation":true}},
			radar: {"backgroundColor":"transparent","radar":{"shape":"circle"},"color":["#365E77","#DF308C","#0CB906","#7690cb","#49ada0","#5BBAEC","#a68a28","#EE142F","#FFE9E9"],"legend":{"padding":5,"itemGap":5,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#ccc","shadowOffsetY":0,"orient":"vertical","shadowBlur":0,"bottom":"auto","itemHeight":4,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"right":"auto","top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"right","borderWidth":0,"width":"auto","itemWidth":4,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":24,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{},"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#7987FD"}},"title":{"top":"top","left":"left","textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","fontSize":14,"lineHeight":14,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"#666","textShadowBlur":0}}},
			indexBgUrl: '',
			indexLogoUrl: '',
		};
	},
	mounted(){
		this.init();
		this.getyuangongCount();
		if(this.isHomeVisible('yuangong','首页统计',2)){
			this.yuangongChat1();
		}
		this.getrenshiguanliyuanCount();
		if(this.isHomeVisible('renshiguanliyuan','首页统计',2)){
			this.renshiguanliyuanChat1();
		}
		this.getyuangongxinziCount();
		if(this.isHomeVisible('yuangongxinzi','首页统计',2)){
			this.yuangongxinziChat1();
		}
		this.loadHomeReminders();
		window.addEventListener('scroll', this.handleScroll)
		window.addEventListener('resize', this.resizeAllHomeCharts)
		setTimeout(()=>{
			this.handleScroll()
		},100)
	},
	beforeDestroy() {
		window.removeEventListener('scroll', this.handleScroll)
		window.removeEventListener('resize', this.resizeAllHomeCharts)
		if (this.salaryHistogramChartInst) {
			this.salaryHistogramChartInst.dispose()
			this.salaryHistogramChartInst = null
		}
		if (this.salaryTrendChartInst) {
			this.salaryTrendChartInst.dispose()
			this.salaryTrendChartInst = null
		}
	},
	created() {
		this.$http.get('config/info?name=bIndexBackgroundImg',).then(rs=>{this.indexBgUrl = rs.data.data?rs.data.data.value:''})
		this.$http.get('config/info?name=bHomeLogo',).then(rs=>{this.indexLogoUrl = rs.data.data?rs.data.data.value:''})
	},
	computed: {
		sessionForm() {
			return JSON.parse(this.$storage.getObj('userForm'))
		},
		isAdminHome() {
			const role = this.$storage.get('role')
			const table = this.$storage.get('sessionTable')
			return table === 'users' || role === '管理员' || role === 'admin'
		},
		avatar(){
			return this.$storage.get('headportrait')?this.$storage.get('headportrait'):''
		},
		sessionUserProfile() {
			try {
				const raw = this.$storage.getObj('userForm')
				if (raw == null) return {}
				return typeof raw === 'string' ? JSON.parse(raw) : raw
			} catch (e) {
				return {}
			}
		},
		homeWelcomeZhiwei() {
			const table = this.$storage.get('sessionTable')
			const u = this.sessionUserProfile
			if (table === 'yuangong') {
				const zw = (u.zhiwei || '').trim()
				if (zw) return zw
			}
			return (this.$storage.get('role') || '鐢ㄦ埛').trim() || '鐢ㄦ埛'
		},
		homeWelcomeXingming() {
			const table = this.$storage.get('sessionTable')
			const u = this.sessionUserProfile
			if (table === 'yuangong') {
				const n = (u.xingming || '').trim()
				if (n) return n
			}
			if (table === 'renshiguanliyuan') {
				const n = (u.guanlixingming || '').trim()
				if (n) return n
			}
			if (table === 'users') {
				const n = (u.username || '').trim()
				if (n) return n
			}
			return (this.$storage.get('adminName') || '用户').trim() || '用户'
		},
	},
	methods:{
		isHomeVisible(table, action, type) {
			const role = this.$storage.get('role')
			if (role === '管理员' || role === 'admin') return true
			if (this.isAuth(table, action, type)) return true
			const oldActionMap = {
				'首页总数': '棣栭〉鎬绘暟',
				'首页统计': '棣栭〉缁熻'
			}
			return this.isAuth(table, oldActionMap[action] || action, type)
		},
		formatMoney(val) {
			if (val === null || val === undefined || val === '') return '-'
			const n = Number(val)
			if (Number.isNaN(n)) return '-'
			return n.toLocaleString('zh-CN', { minimumFractionDigits: 0, maximumFractionDigits: 2 })
		},
		getSalaryAuditParams() {
			return {
				conditionColumn: 'sfsh',
				conditionValue: '是'
			}
		},
		goReminder(path) {
			if (!path) return
			this.$router.push({ path })
		},
		async getPageTotal(url, params = {}) {
			const query = Object.assign({ page: 1, limit: 1, sort: 'id', order: 'desc' }, params)
			const { data } = await this.$http({ url, method: 'get', params: query })
			if (data && data.code === 0 && data.data) {
				return Number(data.data.total || 0)
			}
			return 0
		},
		async getEmployeeSalaryConfirmCount() {
			const userId = this.$storage.get('userid')
			const salaryRes = await this.$http({
				url: 'yuangongxinzi/page',
				method: 'get',
				params: { page: 1, limit: 1000, sort: 'id', order: 'desc', sfsh: '是', ispay: '未支付' }
			})
			const salaryList = salaryRes.data && salaryRes.data.code === 0 && salaryRes.data.data ? (salaryRes.data.data.list || []) : []
			if (!salaryList.length) return 0
			const agreeRes = await this.$http({
				url: 'tongyixinxi/page',
				method: 'get',
				params: { page: 1, limit: 1000, sort: 'id', order: 'desc', crossuserid: userId }
			})
			const agreeList = agreeRes.data && agreeRes.data.code === 0 && agreeRes.data.data ? (agreeRes.data.data.list || []) : []
			const agreedIds = new Set(agreeList.map(item => String(item.crossrefid)))
			return salaryList.filter(item => !agreedIds.has(String(item.id))).length
		},
		async loadHomeReminders() {
			const table = this.$storage.get('sessionTable')
			const role = this.$storage.get('role')
			const cards = []
			this.adminSalaryPendingCount = 0
			try {
				if (table === 'yuangong') {
					const salaryConfirmCount = await this.getEmployeeSalaryConfirmCount()
					const appealPendingCount = await this.getPageTotal('zhiweishensu/page', { sfsh: '待审核' })
					const gonghao = (this.sessionUserProfile && this.sessionUserProfile.gonghao) || this.$storage.get('adminName') || ''
					const leavePendingCount = await this.getPageTotal('qingjiashenqing/page', { gonghao, sfsh: '待审核' })
					cards.push({
						key: 'employee-salary-confirm',
						title: '薪资待确认',
						desc: salaryConfirmCount > 0 ? `有 ${salaryConfirmCount} 条薪资信息待确认` : '暂无薪资信息需要确认',
						count: salaryConfirmCount,
						path: '/yuangongxinzi'
					})
					cards.push({
						key: 'employee-appeal-pending',
						title: '申诉处理中',
						desc: appealPendingCount > 0 ? `有 ${appealPendingCount} 条申诉等待处理` : '暂无待处理申诉',
						count: appealPendingCount,
						path: '/zhiweishensu'
					})
					cards.push({
						key: 'employee-leave-pending',
						title: '请假待处理',
						desc: leavePendingCount > 0 ? `有 ${leavePendingCount} 条请假申请待处理` : '暂无请假申请需要处理',
						count: leavePendingCount,
						path: '/qingjiashenqing'
					})
				} else if (table === 'renshiguanliyuan') {
					const leavePendingCount = await this.getPageTotal('qingjiashenqing/page', { sfsh: '待审核' })
					const appealPendingCount = await this.getPageTotal('zhiweishensu/page', { sfsh: '待审核' })
					const salaryPendingCount = await this.getPageTotal('yuangongxinzi/page', { sfsh: '待审核' })
					const salaryPayCount = await this.getPageTotal('yuangongxinzi/page', { sfsh: '是', ispay: '未支付' })
					cards.push({ key: 'hr-leave-pending', title: '请假待审核', desc: `有 ${leavePendingCount} 条请假申请待审核`, count: leavePendingCount, path: '/qingjiashenqing' })
					cards.push({ key: 'hr-appeal-pending', title: '申诉待处理', desc: `有 ${appealPendingCount} 条申诉信息待处理`, count: appealPendingCount, path: '/zhiweishensu' })
					cards.push({ key: 'hr-salary-pending', title: '薪资待审核', desc: `有 ${salaryPendingCount} 条薪资信息待审核`, count: salaryPendingCount, path: '/yuangongxinzi' })
					cards.push({ key: 'hr-salary-pay', title: '工资待支付', desc: `有 ${salaryPayCount} 条薪资信息待支付`, count: salaryPayCount, path: '/yuangongxinzi' })
				} else if (table === 'users' || role === '管理员' || role === 'admin') {
					const salaryPendingCount = await this.getPageTotal('yuangongxinzi/page', { sfsh: '待审核' })
					this.adminSalaryPendingCount = salaryPendingCount
				}
				this.reminderCards = cards
			} catch (e) {
				this.reminderCards = []
			}
		},
		// 格式化工资区间标签
		formatSalaryBinLabel(lo, hi) {
			const toQian = (v) => {
				const k = Math.round(v / 100) / 10
				return Number.isInteger(k) ? String(k) : k.toFixed(1)
			}
            return `${toQian(lo)}-${toQian(hi)}千`
		},
		// 按实发工资区间统计人数
		buildSalaryHistogram(res) {
			const rows = res || []
			const totals = rows.map((r) => parseFloat(r.total)).filter((n) => !Number.isNaN(n))
			if (!totals.length) {
				return { labels: [], counts: [] }
			}
			const minV = Math.min(...totals)
			const maxV = Math.max(...totals)
			if (minV === maxV) {
				const k = Math.round(minV / 100) / 10
				const ks = Number.isInteger(k) ? String(k) : k.toFixed(1)
                return { labels: [`实发 ${ks} 千（同值）`], counts: [totals.length] }
			}
			const binN = Math.min(7, Math.max(4, Math.ceil(Math.sqrt(totals.length))))
			const step = (maxV - minV) / binN || 1
			const labels = []
			const counts = new Array(binN).fill(0)
			for (let i = 0; i < binN; i++) {
				const lo = minV + i * step
				const hi = i === binN - 1 ? maxV : minV + (i + 1) * step
				labels.push(this.formatSalaryBinLabel(lo, hi))
			}
			totals.forEach((v) => {
				let idx = Math.floor((v - minV) / step)
				if (idx >= binN) idx = binN - 1
				if (idx < 0) idx = 0
				counts[idx]++
			})
			return { labels, counts }
		},
		resizeAllHomeCharts() {
			['yuangongChart1', 'renshiguanliyuanChart1', 'yuangongxinziHistogram', 'yuangongxinziTrend'].forEach((id) => {
				const el = document.getElementById(id)
				if (!el) return
				const inst = echarts.getInstanceByDom(el)
				if (inst) inst.resize()
			})
		},
		handleScroll() {
			let arr = [
				{id:'home-title',css:'animate__'},
				{id:'statis1',css:'animate__'},
				{id:'statis2',css:'animate__'},
				{id:'statis3',css:'animate__'},
				{id:'statis4',css:'animate__'},
				{id:'yuangongChart1',css:'animate__'},
				{id:'renshiguanliyuanChart1',css:'animate__'},
				{id:'yuangongxinziHistogram',css:'animate__'},
				{id:'yuangongxinziTrend',css:'animate__'},
				{id:'dashboard-salary-row',css:'animate__'},
			]
			
			for (let i in arr) {
				let doc = document.getElementById(arr[i].id)
				if (doc) {
					let top = doc.offsetTop
					let win_top = window.innerHeight + window.pageYOffset
					// console.log(top,win_top)
					if (win_top > top && doc.classList.value.indexOf(arr[i].css) < 0) {
						// console.log(doc)
						doc.classList.add(arr[i].css)
					}
				}
			}
		},
		changeStatQuery(arr) {
			if(arr.length==1) {
				if(arr[0] == 'users'&&this.$storage.get("sessionTable")=='users') {
					return true
				}
			}
			let role = this.$storage.get('role')
			for(let x in arr) {
				if(arr[x] == role) {
					return true
				}
			}
			return false
		},
		init(){
			if(this.$storage.get('Token')){
				this.$http({
					url: `${this.$storage.get('sessionTable')}/session`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code != 0) {
						router.push({ name: 'login' })
					}
				});
			}else{
				router.push({ name: 'login' })
			}
		},
		getyuangongCount() {
			this.$http({
				url: `yuangong/count`,
				method: "get"
			}).then(({
				data
			}) => {
				if (data && data.code == 0) {
					this.yuangongCount = data.data
				}
			})
		},
		yuangongChat1(e=null) {
			this.$nextTick(()=>{
				var yuangongChart1 = echarts.init(document.getElementById("yuangongChart1"),'macarons');
				let params = {
				}
				this.$http({
					url: "yuangong/group/xingbie",
					method: "get",
					params
				}).then(({ data }) => {
					if (data && data.code === 0) {
						let res = data.data||[];
						// 统计图配置
						let xAxis = [];
						let yAxis = [];
						let pArray = []
						for(let i=0;i<res.length;i++){
							if(this.boardBase&&i==this.boardBase.pieNum){
								break;
							}
							xAxis.push(res[i].xingbie);
							yAxis.push(parseFloat((res[i].total)));
							pArray.push({
								value: parseFloat((res[i].total)),
								name: res[i].xingbie
							})
						}
						var option = {};
						let titleObj = this.pie.title
						titleObj.text = '员工人数'
						
						const legendObj = this.pie.legend
						let tooltipObj = {trigger: 'item',formatter: '{b} : {c} ({d}%)'}
						tooltipObj = Object.assign(tooltipObj , this.pie.tooltip?this.pie.tooltip:{})
						
						let seriesObj = {
							type: 'pie',
							radius: '55%',
							center: ['50%', '60%'],
							data: pArray,
							emphasis: {
								itemStyle: {
									shadowBlur: 10,
									shadowOffsetX: 0,
									shadowColor: 'rgba(0, 0, 0, 0.5)'
								}
							}
						}
						seriesObj = Object.assign(seriesObj , this.pie.series)
						const gridObj = this.pie.grid
						option = {
							backgroundColor: this.pie.backgroundColor,
							color: this.pie.color,
							title: titleObj,
							legend: legendObj,
							grid: gridObj,
							tooltip: tooltipObj,
							series: [seriesObj]
						};
						// 使用配置项和数据展示图表
						yuangongChart1.setOption(option, true)
						yuangongChart1.resize()
					}else{
						this.$message({
							message: data.msg,
							type: "warning",
							duration: 1500,
						})
					}
				});
			})
		},


		getrenshiguanliyuanCount() {
			this.$http({
				url: `renshiguanliyuan/count`,
				method: "get"
			}).then(({
				data
			}) => {
				if (data && data.code == 0) {
					this.renshiguanliyuanCount = data.data
				}
			})
		},
		renshiguanliyuanChat1(e=null) {
			this.$nextTick(()=>{
				var renshiguanliyuanChart1 = echarts.init(document.getElementById("renshiguanliyuanChart1"),'macarons');
				let params = {
				}
				this.$http({
					url: "renshiguanliyuan/group/xingbie",
					method: "get",
					params
				}).then(({ data }) => {
					if (data && data.code === 0) {
						let res = data.data||[];
						// 统计图配置
						let xAxis = [];
						let yAxis = [];
						let pArray = []
						for(let i=0;i<res.length;i++){
							if(this.boardBase&&i==this.boardBase.lineNum){
								break;
							}
							xAxis.push(res[i].xingbie);
							yAxis.push(parseFloat((res[i].total)));
							pArray.push({
								value: parseFloat((res[i].total)),
								name: res[i].xingbie
							})
						}
						var option = {};
						let titleObj = this.line.title
						titleObj.text = '人事人数'
						
						const legendObj = this.line.legend
						let tooltipObj = { trigger: 'item',formatter: '{b} : {c}'}
						tooltipObj = Object.assign(tooltipObj , this.line.tooltip?this.line.tooltip:{})
						let xAxisObj = this.line.xAxis
						xAxisObj.type = 'category'
						xAxisObj.data = xAxis
						
						let yAxisObj = this.line.yAxis
						yAxisObj.type = 'value'
						const gridObj = this.line.grid
						
						let seriesObj = {
							data: yAxis,
							type: 'line',
							areaStyle: {}
						}
						seriesObj = Object.assign(seriesObj , this.line.series)
						option = {
							backgroundColor: this.line.backgroundColor,
							color: this.line.color,
							title: titleObj,
							legend: legendObj,
							grid: gridObj,
							tooltip: tooltipObj,
							xAxis: xAxisObj,
							yAxis: yAxisObj,
							series: [seriesObj]
						};
						// 使用配置项和数据展示图表
						renshiguanliyuanChart1.setOption(option, true)
						renshiguanliyuanChart1.resize()
					}else{
						this.$message({
							message: data.msg,
							type: "warning",
							duration: 1500,
						})
					}
				});
			})
		},


		getyuangongxinziCount() {
			this.$http({
				url: `yuangongxinzi/count`,
				method: "get"
			}).then(({
				data
			}) => {
				if (data && data.code == 0) {
					this.yuangongxinziCount = data.data
				}
			})
		},
		yuangongxinziChat1() {
			this.$nextTick(() => {
				const histEl = document.getElementById('yuangongxinziHistogram')
				const trendEl = document.getElementById('yuangongxinziTrend')
				if (!histEl || !trendEl) return
				if (this.salaryHistogramChartInst) {
					this.salaryHistogramChartInst.dispose()
					this.salaryHistogramChartInst = null
				}
				if (this.salaryTrendChartInst) {
					this.salaryTrendChartInst.dispose()
					this.salaryTrendChartInst = null
				}
				this.salaryHistogramChartInst = echarts.init(histEl, 'macarons')
				this.salaryTrendChartInst = echarts.init(trendEl, 'macarons')
				const params = this.getSalaryAuditParams()
				this.$http({
					url: `yuangongxinzi/value/xingming/shifagongzi`,
					method: 'get',
					params
				}).then(({ data }) => {
					if (data && data.code === 0) {
						const res = data.data || []
						const totals = res.map((r) => parseFloat(r.total)).filter((n) => !Number.isNaN(n))
						const n = totals.length
						const sum = n ? totals.reduce((a, b) => a + b, 0) : 0
						const avg = n ? sum / n : 0
						const maxV = n ? Math.max(...totals) : 0
						const minV = n ? Math.min(...totals) : 0
						this.xinziStatRows = n
							? [
									{ zhibiao: '已审核发薪人数', shuzhi: String(n) },
									{ zhibiao: '实发合计', shuzhi: this.formatMoney(sum) },
									{ zhibiao: '平均实发', shuzhi: this.formatMoney(avg) },
									{ zhibiao: '最高实发', shuzhi: this.formatMoney(maxV) },
									{ zhibiao: '最低实发', shuzhi: this.formatMoney(minV) }
							  ]
							: []
						const { labels, counts } = this.buildSalaryHistogram(res)
						const histTitle = JSON.parse(JSON.stringify(this.bar.title))
						histTitle.text = '实发区间人数'
						histTitle.left = 'center'
						histTitle.top = 2
						const xAxisH = JSON.parse(JSON.stringify(this.bar.xAxis))
						xAxisH.type = 'category'
						xAxisH.data = labels
						xAxisH.axisLabel = {
							interval: 0,
							fontSize: 11,
							color: '#334155',
							lineHeight: 16,
							rotate: 0,
							margin: 12,
							hideOverlap: true
						}
						const yAxisH = JSON.parse(JSON.stringify(this.bar.yAxis))
						yAxisH.type = 'value'
						yAxisH.minInterval = 1
						yAxisH.name = '人数'
						yAxisH.nameTextStyle = { fontSize: 11, color: '#666' }
						yAxisH.splitLine = Object.assign({}, yAxisH.splitLine || {}, {
							lineStyle: { color: '#e8edf2', type: 'dashed' }
						})
						yAxisH.axisLabel = Object.assign({}, yAxisH.axisLabel || {}, { color: '#555', fontSize: 11 })
						// 使用简洁柱形，避免旧模板样式过重
						const seriesH = {
							type: 'bar',
							data: counts,
							barMaxWidth: 26,
							barGap: '32%',
							itemStyle: {
								color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
									{ offset: 0, color: '#5a9fd4' },
									{ offset: 1, color: '#2a6fa8' }
								]),
								borderRadius: [3, 3, 0, 0]
							},
							emphasis: {
								itemStyle: {
									color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
										{ offset: 0, color: '#6eb0e0' },
										{ offset: 1, color: '#095dac' }
									])
								}
							}
						}
						this.salaryHistogramChartInst.setOption({
							backgroundColor: '#fafbfc',
							color: ['#3d7ea6'],
							title: histTitle,
							legend: { show: false },
							grid: { left: 14, right: 12, top: 42, bottom: 10, containLabel: true },
							tooltip: Object.assign({}, this.bar.tooltip || {}, {
								trigger: 'axis',
								axisPointer: { type: 'shadow' },
								formatter: (p) => {
									const x = p && p[0]
									if (!x) return ''
                                    return `实发区间：${x.axisValue}<br/>人数：${x.data} 人`
								}
							}),
							xAxis: xAxisH,
							yAxis: yAxisH,
							series: [seriesH]
						})
                        const monthPath = `yuangongxinzi/value/dengjiriqi/shifagongzi/${encodeURIComponent('月')}`
						this.$http({ url: monthPath, method: 'get', params }).then(({ data: d2 }) => {
							if (d2 && d2.code === 0) {
								let list = d2.data || []
								list = list.slice().sort((a, b) => String(a.dengjiriqi).localeCompare(String(b.dengjiriqi)))
								const xM = list.map((r) => r.dengjiriqi)
								const yM = list.map((r) => parseFloat(r.total) || 0)
								const tTitle = JSON.parse(JSON.stringify(this.line.title))
								tTitle.text = '按月实发合计'
								tTitle.left = 'center'
								tTitle.top = 2
								const xAxisT = JSON.parse(JSON.stringify(this.line.xAxis))
								xAxisT.type = 'category'
								xAxisT.data = xM
								xAxisT.axisLabel = Object.assign({}, xAxisT.axisLabel || {}, {
									rotate: 22,
									fontSize: 10,
									color: '#555',
									interval: 0,
									hideOverlap: true
								})
								const yAxisT = JSON.parse(JSON.stringify(this.line.yAxis))
								yAxisT.type = 'value'
								yAxisT.axisLabel = Object.assign({}, yAxisT.axisLabel || {}, { fontSize: 11, color: '#555' })
								const seriesT = Object.assign({}, this.line.series, {
									type: 'line',
									data: yM,
									smooth: true,
									symbol: 'circle',
									symbolSize: 6,
									areaStyle: { opacity: 0.12 }
								})
								this.salaryTrendChartInst.setOption({
									backgroundColor: this.line.backgroundColor,
									color: this.line.color,
									title: tTitle,
									legend: { show: false },
									grid: { left: 10, right: 12, top: 40, bottom: 6, containLabel: true },
									tooltip: Object.assign({}, this.line.tooltip || {}, {
										trigger: 'axis',
										formatter: (p) => {
											const x = p && p[0]
											if (!x) return ''
                                            return `${x.axisValue}<br/>实发合计：${this.formatMoney(x.data)}`
										}
									}),
									xAxis: xAxisT,
									yAxis: yAxisT,
									series: [seriesT]
								})
							} else {
								this.salaryTrendChartInst.clear()
								if (d2 && d2.msg) {
									this.$message.warning(d2.msg)
								}
							}
							this.$nextTick(() => this.resizeAllHomeCharts())
						}).catch(() => {
							this.salaryTrendChartInst.clear()
							this.$message.error('加载月度薪资统计失败')
						})
					} else {
						this.xinziStatRows = []
						this.salaryHistogramChartInst.clear()
						this.salaryTrendChartInst.clear()
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500
						})
					}
				}).catch(() => {
					this.xinziStatRows = []
					if (this.salaryHistogramChartInst) this.salaryHistogramChartInst.clear()
					if (this.salaryTrendChartInst) this.salaryTrendChartInst.clear()
					this.$message.error('加载薪资统计失败')
				})
			})
		},




	}
};
</script>
<style lang="scss" scoped>
	.home-content {
		padding: 0 30px 30px;
		background: url(http://codegen.caihongy.cn/20250919/9d340ed8b55443539e5b7ce1d5713b91.png) no-repeat center top / cover;
		display: flex;
		width: 100%;
		min-height: 100vh;
		flex-wrap: wrap;
		.home-title {
			padding: 10px 0 0;
			box-shadow: none;
			margin: 10px 0 0;
			display: flex;
			width: 100%;
			justify-content: center;
			align-items: center;
			transition: 0.3s;
			.titles {
				padding: 0;
				color: #333;
				font-size: 30px;
				line-height: 1.4;
				text-align: center;
				.welcome-line1 {
					font-size: 30px;
					font-weight: 600;
					line-height: 1.5;
				}
				.welcome-line2 {
					margin-top: 8px;
					font-size: 22px;
					font-weight: 500;
					color: #444;
					line-height: 1.4;
				}
			}
		}
		.home-title:hover {
			transform: translate3d(0, 0px, 0);
		}
		.statis-box {
			margin: 20px 0;
			background: none;
			display: flex;
			width: 100%;
			justify-content: center;
			align-items: center;
			flex-wrap: wrap;
			&.statis-box-admin {
				justify-content: space-between;
				.statis1,
				.statis2,
				.statis3,
				.statis4 {
					width: calc(25% - 20px);
				}
				.statis4 {
					cursor: pointer;
				}
			}
			.statis1 {
				border: 1px solid #e5f0f9;
				border-radius: 0px;
				box-shadow: none;
				padding: 20px;
				margin: 10px;
				background: #fff;
				display: flex;
				width: calc(20% - 20px);
				transition: 0.3s;
				height: 130px;
				.left {
					background: none;
					display: flex;
					width: 40px;
					justify-content: center;
					align-items: center;
					height: 40px;
					order: 2;
					.iconfont {
						border-radius: 0;
						padding: 0;
						color: #00c7f2;
						background: #eaf3fa;
						font-weight: 500;
						width: 48px;
						font-size: 32px;
						line-height: 40px;
						text-align: center;
						height: 40px;
					}
				}
				.right {
					flex-direction: column;
					display: flex;
					width: 100%;
					justify-content: space-between;
					.num {
						margin: 5px 0;
						color: #333;
						font-weight: 600;
						font-size: 30px;
						line-height: 24px;
						height: 24px;
						order: 2;
					}
					.name {
						margin: 5px 0;
						color: #666;
						font-size: 14px;
						line-height: 24px;
						height: 24px;
					}
				}
			}
			.statis1:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.statis2 {
				border: 1px solid #e5f0f9;
				border-radius: 0px;
				box-shadow: none;
				padding: 20px;
				margin: 10px;
				background: #fff;
				display: flex;
				width: calc(20% - 20px);
				transition: 0.3s;
				height: 130px;
				.left {
					background: none;
					display: flex;
					width: 48px;
					justify-content: center;
					align-items: center;
					height: 48px;
					order: 2;
					.iconfont {
						border-radius: 0;
						padding: 0;
						color: #00c7f2;
						background: #eaf3fa;
						font-weight: 500;
						width: 48px;
						font-size: 32px;
						line-height: 40px;
						text-align: center;
						height: 40px;
					}
				}
				.right {
					flex-direction: column;
					display: flex;
					width: 100%;
					justify-content: space-between;
					.num {
						margin: 5px 0;
						color: #333;
						font-weight: bold;
						font-size: 30px;
						line-height: 24px;
						height: 24px;
						order: 2;
					}
					.name {
						margin: 5px 0;
						color: #666;
						font-size: 16px;
						line-height: 24px;
						height: 24px;
					}
				}
			}
			.statis2:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.statis3 {
				border: 1px solid #e5f0f9;
				border-radius: 0px;
				box-shadow: none;
				padding: 20px;
				margin: 10px;
				background: #fff;
				display: flex;
				width: calc(20% - 20px);
				transition: 0.3s;
				height: 130px;
				.left {
					background: none;
					display: flex;
					width: 48px;
					justify-content: center;
					align-items: center;
					height: 48px;
					order: 2;
					.iconfont {
						border-radius: 0;
						padding: 0;
						color: #00c7f2;
						background: #eaf3fa;
						font-weight: 500;
						width: 48px;
						font-size: 32px;
						line-height: 40px;
						text-align: center;
						height: 40px;
					}
				}
				.right {
					flex-direction: column;
					display: flex;
					width: 100%;
					justify-content: space-between;
					.num {
						margin: 5px 0;
						color: #333;
						font-weight: bold;
						font-size: 30px;
						line-height: 24px;
						height: 24px;
						order: 2;
					}
					.name {
						margin: 5px 0;
						color: #666;
						font-size: 16px;
						line-height: 24px;
						height: 24px;
					}
				}
			}
			.statis3:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.statis4 {
				border: 1px solid #e5f0f9;
				border-radius: 0px;
				box-shadow: none;
				padding: 20px;
				margin: 10px;
				background: #fff;
				display: flex;
				width: calc(20% - 20px);
				transition: 0.3s;
				height: 130px;
				.left {
					background: none;
					display: flex;
					width: 48px;
					justify-content: center;
					align-items: center;
					height: 48px;
					order: 2;
					.iconfont {
						border-radius: 0;
						padding: 0;
						color: #00c7f2;
						background: #eaf3fa;
						font-weight: 500;
						width: 48px;
						font-size: 32px;
						line-height: 40px;
						text-align: center;
						height: 40px;
					}
				}
				.right {
					flex-direction: column;
					display: flex;
					width: 100%;
					justify-content: space-between;
					.num {
						margin: 5px 0;
						color: #333;
						font-weight: bold;
						font-size: 30px;
						line-height: 24px;
						height: 24px;
						order: 2;
					}
					.name {
						margin: 5px 0;
						color: #666;
						font-size: 16px;
						line-height: 24px;
						height: 24px;
					}
				}
			}
			.statis4:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.statis5 {
				border: 1px solid #e5f0f9;
				border-radius: 0px;
				box-shadow: none;
				padding: 20px;
				margin: 10px;
				background: #fff;
				display: flex;
				width: calc(20% - 20px);
				transition: 0.3s;
				height: 130px;
				.left {
					background: none;
					display: flex;
					width: 48px;
					justify-content: center;
					align-items: center;
					height: 48px;
					order: 2;
					.iconfont {
						border-radius: 0;
						padding: 0;
						color: #00c7f2;
						background: #eaf3fa;
						font-weight: 500;
						width: 48px;
						font-size: 32px;
						line-height: 40px;
						text-align: center;
						height: 40px;
					}
				}
				.right {
					flex-direction: column;
					display: flex;
					width: 100%;
					justify-content: space-between;
					.num {
						margin: 5px 0;
						color: #333;
						font-weight: bold;
						font-size: 30px;
						line-height: 24px;
						height: 24px;
						order: 2;
					}
					.name {
						margin: 5px 0;
						color: #666;
						font-size: 16px;
						line-height: 24px;
						height: 24px;
					}
				}
			}
			.statis5:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.statis6 {
				border: 1px solid #e5f0f9;
				border-radius: 0px;
				box-shadow: none;
				padding: 20px;
				margin: 10px;
				background: #fff;
				display: flex;
				width: calc(20% - 20px);
				transition: 0.3s;
				height: 130px;
				.left {
					background: none;
					display: flex;
					width: 48px;
					justify-content: center;
					align-items: center;
					height: 48px;
					order: 2;
					.iconfont {
						border-radius: 0;
						padding: 0;
						color: #00c7f2;
						background: #eaf3fa;
						font-weight: 500;
						width: 48px;
						font-size: 32px;
						line-height: 40px;
						text-align: center;
						height: 40px;
					}
				}
				.right {
					flex-direction: column;
					display: flex;
					width: 100%;
					justify-content: space-between;
					.num {
						margin: 5px 0;
						color: #333;
						font-weight: bold;
						font-size: 30px;
						line-height: 24px;
						height: 24px;
						order: 2;
					}
					.name {
						margin: 5px 0;
						color: #666;
						font-size: 16px;
						line-height: 24px;
						height: 24px;
					}
				}
			}
			.statis6:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.statis7 {
				border: 1px solid #e5f0f9;
				border-radius: 0px;
				box-shadow: none;
				padding: 20px;
				margin: 10px;
				background: #fff;
				display: flex;
				width: calc(20% - 20px);
				transition: 0.3s;
				height: 130px;
				.left {
					background: none;
					display: flex;
					width: 48px;
					justify-content: center;
					align-items: center;
					height: 48px;
					order: 2;
					.iconfont {
						border-radius: 0;
						padding: 0;
						color: #00c7f2;
						background: #eaf3fa;
						font-weight: 500;
						width: 48px;
						font-size: 32px;
						line-height: 40px;
						text-align: center;
						height: 40px;
					}
				}
				.right {
					flex-direction: column;
					display: flex;
					width: 100%;
					justify-content: space-between;
					.num {
						margin: 5px 0;
						color: #333;
						font-weight: bold;
						font-size: 30px;
						line-height: 24px;
						height: 24px;
						order: 2;
					}
					.name {
						margin: 5px 0;
						color: #666;
						font-size: 16px;
						line-height: 24px;
						height: 24px;
					}
				}
			}
			.statis7:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.statis8 {
				border: 1px solid #e5f0f9;
				border-radius: 0px;
				box-shadow: none;
				padding: 20px;
				margin: 10px;
				background: #fff;
				display: flex;
				width: calc(20% - 20px);
				transition: 0.3s;
				height: 130px;
				.left {
					background: none;
					display: flex;
					width: 48px;
					justify-content: center;
					align-items: center;
					height: 48px;
					order: 2;
					.iconfont {
						border-radius: 0;
						padding: 0;
						color: #00c7f2;
						background: #eaf3fa;
						font-weight: 500;
						width: 48px;
						font-size: 32px;
						line-height: 40px;
						text-align: center;
						height: 40px;
					}
				}
				.right {
					flex-direction: column;
					display: flex;
					width: 100%;
					justify-content: space-between;
					.num {
						margin: 5px 0;
						color: #333;
						font-weight: bold;
						font-size: 30px;
						line-height: 24px;
						height: 24px;
						order: 2;
					}
					.name {
						margin: 5px 0;
						color: #666;
						font-size: 16px;
						line-height: 24px;
						height: 24px;
					}
				}
			}
			.statis8:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.statis9 {
				border: 1px solid #e5f0f9;
				border-radius: 0px;
				box-shadow: none;
				padding: 20px;
				margin: 10px;
				background: #fff;
				display: flex;
				width: calc(20% - 20px);
				transition: 0.3s;
				height: 130px;
				.left {
					background: none;
					display: flex;
					width: 48px;
					justify-content: center;
					align-items: center;
					height: 48px;
					order: 2;
					.iconfont {
						border-radius: 0;
						padding: 0;
						color: #00c7f2;
						background: #eaf3fa;
						font-weight: 500;
						width: 48px;
						font-size: 32px;
						line-height: 40px;
						text-align: center;
						height: 40px;
					}
				}
				.right {
					flex-direction: column;
					display: flex;
					width: 100%;
					justify-content: space-between;
					.num {
						margin: 5px 0;
						color: #333;
						font-weight: bold;
						font-size: 30px;
						line-height: 24px;
						height: 24px;
						order: 2;
					}
					.name {
						margin: 5px 0;
						color: #666;
						font-size: 16px;
						line-height: 24px;
						height: 24px;
					}
				}
			}
			.statis9:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.statis10 {
				border: 1px solid #e5f0f9;
				border-radius: 0px;
				box-shadow: none;
				padding: 20px;
				margin: 10px;
				background: #fff;
				display: flex;
				width: calc(20% - 20px);
				transition: 0.3s;
				height: 130px;
				.left {
					background: none;
					display: flex;
					width: 48px;
					justify-content: center;
					align-items: center;
					height: 48px;
					order: 2;
					.iconfont {
						border-radius: 0;
						padding: 0;
						color: #00c7f2;
						background: #eaf3fa;
						font-weight: 500;
						width: 48px;
						font-size: 32px;
						line-height: 40px;
						text-align: center;
						height: 40px;
					}
				}
				.right {
					flex-direction: column;
					display: flex;
					width: 100%;
					justify-content: space-between;
					.num {
						margin: 5px 0;
						color: #333;
						font-weight: bold;
						font-size: 30px;
						line-height: 24px;
						height: 24px;
						order: 2;
					}
					.name {
						margin: 5px 0;
						color: #666;
						font-size: 16px;
						line-height: 24px;
						height: 24px;
					}
				}
			}
			.statis10:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
		}
		.reminder-box {
			display: flex;
			flex-wrap: wrap;
			width: 100%;
			margin: 0 0 10px;
			padding: 0;
			.reminder-card {
				box-sizing: border-box;
				display: flex;
				align-items: center;
				justify-content: space-between;
				width: calc(25% - 20px);
				min-width: 230px;
				min-height: 96px;
				margin: 10px;
				padding: 18px 20px;
				background: #fff;
				border: 1px solid #e5f0f9;
				border-radius: 8px;
				box-shadow: 0 2px 8px rgba(9, 93, 172, 0.06);
				cursor: pointer;
				transition: 0.25s;
				.reminder-main {
					min-width: 0;
					padding-right: 12px;
					.reminder-title {
						margin-bottom: 8px;
						color: #1a3a52;
						font-size: 16px;
						font-weight: 600;
						line-height: 22px;
					}
					.reminder-desc {
						color: #666;
						font-size: 13px;
						line-height: 20px;
						word-break: break-word;
					}
				}
				.reminder-count {
					flex-shrink: 0;
					min-width: 48px;
					color: #095dac;
					font-size: 30px;
					font-weight: 700;
					line-height: 1;
					text-align: right;
				}
			}
			.reminder-card:hover {
				box-shadow: 0 8px 24px rgba(9, 93, 172, 0.1);
				transform: translate3d(0, -4px, 0);
			}
		}
		/* 工资统计：在员工人数/人事人数下方，全宽显示 */
		.dashboard-salary-row {
			width: 100%;
			padding: 0;
			margin-top: 6px;
		}
		.dashboard-salary-row .salary-card {
			display: flex;
			flex-direction: column;
			align-items: stretch;
			box-sizing: border-box;
			width: calc(100% - 20px);
			margin: 10px;
			padding: 20px 22px 18px;
			background: rgba(255, 255, 255, 1);
			border: 1px solid #e5f0f9;
			border-radius: 10px;
			transition: 0.25s;
			box-shadow: 0 2px 8px rgba(9, 93, 172, 0.06);
		}
		.dashboard-salary-row .salary-card:hover {
			box-shadow: 0 8px 24px rgba(9, 93, 172, 0.1);
		}
		.dashboard-salary-row .section-title {
			margin: 0 0 14px;
			padding-bottom: 10px;
			border-bottom: 1px solid #eef2f6;
			color: #1a3a52;
			font-size: 16px;
			font-weight: 600;
			line-height: 1.35;
		}
		.dashboard-salary-row .salary-summary-table-wrap {
			width: 100%;
			margin-bottom: 16px;
		}
		.dashboard-salary-row .salary-stat-table /deep/ .cell {
			white-space: normal;
			word-break: break-word;
			line-height: 1.45;
		}
		.dashboard-salary-row .salary-charts-row {
			display: flex;
			flex-wrap: wrap;
			gap: 14px;
			width: 100%;
			align-items: stretch;
		}
		.dashboard-salary-row .salary-histogram-wrap,
		.dashboard-salary-row .salary-trend-wrap {
			flex: 1;
			min-width: 300px;
			height: 300px;
			position: relative;
			background: #f7fafc;
			border-radius: 8px;
			border: 1px solid #eef2f6;
			box-sizing: border-box;
			overflow: hidden;
		}
		.dashboard-salary-row .salary-chart-inner {
			position: absolute;
			left: 0;
			top: 0;
			right: 0;
			bottom: 0;
			width: 100%;
			height: 100%;
		}
		@media (max-width: 768px) {
			.dashboard-salary-row .salary-histogram-wrap,
			.dashboard-salary-row .salary-trend-wrap {
				flex: 1 1 100%;
				min-width: 100%;
				height: 280px;
			}
		}
		// echarts4
		.type4 {
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
				width: calc(50% - 20px);
				transition: 0.3s;
				height: 440px;
			}
			.echarts1:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.echarts2 {
				border-radius: 0px;
				padding: 20px;
				box-shadow: none;
				margin: 10px;
				background: rgba(255,255,255,1);
				width: calc(50% - 20px);
				transition: 0.3s;
				height: 440px;
			}
			.echarts2:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.echarts3 {
				border-radius: 0px;
				padding: 20px;
				box-shadow: none;
				margin: 10px;
				background: rgba(255,255,255,1);
				width: calc(50% - 20px);
				transition: 0.3s;
				height: 400px;
			}
			.echarts3:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
			.echarts4 {
				border-radius: 0px;
				padding: 20px;
				box-shadow: none;
				margin: 10px;
				background: rgba(255,255,255,1);
				width: calc(50% - 20px);
				transition: 0.3s;
				height: 400px;
			}
			.echarts4:hover {
				box-shadow: 0 3px 0px rgba(0,0,0,.1);
				transform: translate3d(0, -6px, 0);
				z-index: 1;
				background: rgba(255,255,255,1);
			}
		}
	}
	
	.echarts-flag-2 {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		padding: 10px 20px;
		background: rebeccapurple;
	
		&>div {
			width: 32%;
			height: 300px;
			margin: 10px 0;
			background: rgba(255,255,255,.1);
			border-radius: 8px;
			padding: 10px 20px;
		}
	}
	.animate__animated {
		animation-fill-mode: none;
	}
</style>
