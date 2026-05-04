


































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
					<el-input v-model="ruleForm.gonghao" placeholder="工号" clearable  :readonly="ro.gonghao" @blur="gonghaoChange" @clear="clearYuangongLinkage"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="工号" prop="gonghao" >
					<el-input v-model="ruleForm.gonghao" placeholder="工号" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="请假天数" prop="qingjiatianshu" >
					<el-input v-model.number="ruleForm.qingjiatianshu" placeholder="请假天数" clearable  :readonly="ro.qingjiatianshu"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="请假天数" prop="qingjiatianshu" >
					<el-input v-model="ruleForm.qingjiatianshu" placeholder="请假天数" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" label="未签到天数" prop="weiqiandaotianshu">
					<el-input v-model="ruleForm.weiqiandaotianshu" placeholder="未签到天数" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" label="未签到扣薪" prop="weiqiandaokouxin">
					<el-input v-model="ruleForm.weiqiandaokouxin" placeholder="未签到扣薪" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" label="请假扣薪" prop="jiaqikouxin">
					<el-input v-model="ruleForm.jiaqikouxin" placeholder="假期扣薪" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="基本工资" prop="jibengongzi" >
					<el-input-number v-model="ruleForm.jibengongzi" placeholder="基本工资" :disabled="ro.jibengongzi" @change="handleJibengongziChange"></el-input-number>
				</el-form-item>
				<el-form-item v-else class="input" label="基本工资" prop="jibengongzi" >
					<el-input v-model="ruleForm.jibengongzi" placeholder="基本工资" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="加班工资" prop="jiabangongzi" >
					<el-input-number v-model="ruleForm.jiabangongzi" placeholder="加班工资" :disabled="ro.jiabangongzi" @change="calcShifagongzi"></el-input-number>
				</el-form-item>
				<el-form-item v-else class="input" label="加班工资" prop="jiabangongzi" >
					<el-input v-model="ruleForm.jiabangongzi" placeholder="加班工资" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" label="加班时长(小时)">
					<el-input :value="jiabanshichangDisplay" placeholder="加班时长(小时)" readonly></el-input>
				</el-form-item>
				<el-form-item class="input linkage-field" v-if="type!='info'"  label="职位补贴" prop="gangweibutie" >
					<el-select v-if="gangweibutieType!=='自定义'" v-model="gangweibutieType" placeholder="请选择职位补贴" :disabled="ro.gangweibutie" @change="handleGangweibutieTypeChange">
						<el-option label="一等补贴（500）" value="一等补贴"></el-option>
						<el-option label="二等补贴（300）" value="二等补贴"></el-option>
						<el-option label="三等补贴（100）" value="三等补贴"></el-option>
						<el-option label="无补贴" value="无补贴"></el-option>
						<el-option label="自定义" value="自定义"></el-option>
					</el-select>
					<el-input v-if="gangweibutieType==='自定义'" v-model="gangweibutieCustomName" placeholder="自定义" clearable :readonly="ro.gangweibutie"></el-input>
					<el-input-number v-if="gangweibutieType==='自定义'" v-model="ruleForm.gangweibutie" placeholder="请输入补贴金额" :disabled="ro.gangweibutie" @change="calcShifagongzi"></el-input-number>
					<el-input v-else v-model="ruleForm.gangweibutie" placeholder="职位补贴" disabled></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="职位补贴" prop="gangweibutie" >
					<el-input v-model="ruleForm.gangweibutie" placeholder="职位补贴" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="扣款金额" prop="koukuanjine" >
					<el-input-number v-model="ruleForm.koukuanjine" placeholder="扣款金额" :disabled="ro.koukuanjine || koukuanType !== '其他扣款'" @change="calcShifagongzi"></el-input-number>
				</el-form-item>
				<el-form-item v-else class="input" label="扣款金额" prop="koukuanjine" >
					<el-input v-model="ruleForm.koukuanjine" placeholder="扣款金额" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'" label="实发工资" prop="shifagongzi" >
					<el-input v-model="ruleForm.shifagongzi" placeholder="实发工资" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-else-if="ruleForm.shifagongzi" label="实发工资" prop="shifagongzi" >
					<el-input v-model="ruleForm.shifagongzi" placeholder="实发工资" readonly></el-input>
				</el-form-item>
				<el-form-item class="input linkage-field" v-if="type!='info'"  label="扣款原因" prop="koukuanyuanyin" >
					<el-select v-model="koukuanType" placeholder="请选择扣款原因" :disabled="ro.koukuanyuanyin" @change="handleKoukuanTypeChange">
						<el-option label="社保公积金" value="社保公积金"></el-option>
						<el-option label="其他扣款" value="其他扣款"></el-option>
						<el-option label="无扣款" value="无扣款"></el-option>
					</el-select>
					<el-input v-if="koukuanType==='其他扣款'" v-model="ruleForm.koukuanyuanyin" placeholder="请输入扣款原因" clearable :readonly="ro.koukuanyuanyin"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="扣款原因" prop="koukuanyuanyin" >
					<el-input v-model="ruleForm.koukuanyuanyin" placeholder="扣款原因" readonly></el-input>
				</el-form-item>
				<el-form-item class="date" v-if="type!='info'" label="登记日期" prop="dengjiriqi" >
					<el-date-picker
						format="yyyy 年 MM 月 dd 日"
						value-format="yyyy-MM-dd"
						v-model="ruleForm.dengjiriqi" 
						@change="syncKaoqinAndQingjiaData"
						type="date"
						:readonly="ro.dengjiriqi"
						placeholder="登记日期"
					></el-date-picker> 
				</el-form-item>
				<el-form-item class="input" v-else-if="ruleForm.dengjiriqi" label="登记日期" prop="dengjiriqi" >
					<el-input v-model="ruleForm.dengjiriqi" placeholder="登记日期" readonly></el-input>
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
	import { 
		isNumber,
		isIntNumer,
	} from "@/utils/validate";
	export default {
		data() {
			var validateNumber = (rule, value, callback) => {
				if(!value){
					callback();
				} else if (!isNumber(value)) {
					callback(new Error("请输入数字"));
				} else {
					callback();
				}
			};
			var validateIntNumber = (rule, value, callback) => {
				if(!value){
					callback();
				} else if (!isIntNumer(value)) {
					callback(new Error("请输入整数"));
				} else {
					callback();
				}
			};
			return {
				id: '',
				type: '',
				jiabancishuTotal: 0,
				jiabanshichangTotal: 0,
				yuangongBaseJiabancishu: 0,
				yuangongBaseQingjiatianshu: 0,
				gangweibutieType: '无补贴',
				gangweibutieCustomName: '',
				koukuanType: '无扣款',
			
			
				ro:{
					xingming : false,
					gonghao : false,
					qingjiatianshu : true,
					weiqiandaotianshu : true,
					weiqiandaokouxin : true,
					jibengongzi : false,
					jiabangongzi : true,
					jixiaojiangjin : false,
					gangweibutie : false,
					koukuanjine : false,
					jiaqikouxin : true,
					shifagongzi : false,
					koukuanyuanyin : false,
					dengjiriqi : false,
					guanlizhanghao : false,
					guanlixingming : false,
				},
			
				ruleForm: {
					xingming: '',
					gonghao: '',
					qingjiatianshu: '',
					weiqiandaotianshu: '',
					weiqiandaokouxin: '',
					jibengongzi: '',
					jiabangongzi: '',
					jixiaojiangjin: 0,
					gangweibutie: '',
					koukuanjine: '',
					jiaqikouxin: '',
					shifagongzi: '',
					koukuanyuanyin: '',
					dengjiriqi: '',
					guanlizhanghao: '',
					guanlixingming: '',
				},
				xingmingOptions: [],

				rules: {
					xingming: [
					],
					gonghao: [
						{ required: true, message: '工号不能为空', trigger: 'blur' },
					],
					qingjiatianshu: [
						{ validator: validateIntNumber, trigger: 'blur' },
					],
					jibengongzi: [
						{ validator: validateNumber, trigger: 'blur' },
					],
					jiabangongzi: [
						{ validator: validateNumber, trigger: 'blur' },
					],
					gangweibutie: [
						{ validator: validateNumber, trigger: 'blur' },
					],
					koukuanjine: [
						{ validator: validateNumber, trigger: 'blur' },
					],
					shifagongzi: [
						{ validator: validateNumber, trigger: 'blur' },
					],
					koukuanyuanyin: [
					],
					dengjiriqi: [
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
			jiabanshichangDisplay() {
				const allJiabanshichang = Number(this.jiabanshichangTotal) || 0
				const fallbackCishu = Number(this.jiabancishuTotal) || 0
				const finalShichang = allJiabanshichang > 0 ? allJiabanshichang : fallbackCishu
				return Number(finalShichang.toFixed(2))
			},
			qingjiaKoukuanDisplay() {
				const qjts = Number(this.ruleForm.qingjiatianshu) || 0
				return Number((qjts * 100).toFixed(2))
			},



		},
		components: {
		},
		created() {
			this.ruleForm.dengjiriqi = this.getCurDate()
			this.ruleForm.gangweibutie = 0
			this.ruleForm.koukuanjine = 0
			this.ruleForm.koukuanyuanyin = '无扣款'
		},
		methods: {
			round2(value) {
				return Number((Number(value) || 0).toFixed(2))
			},
			initGangweibutieType() {
				const value = Number(this.ruleForm.gangweibutie)
				if (!this.ruleForm.gangweibutie && value !== 0) {
					this.gangweibutieType = '无补贴'
					this.ruleForm.gangweibutie = 0
					return
				}
				if (value === 500) {
					this.gangweibutieType = '一等补贴'
				} else if (value === 300) {
					this.gangweibutieType = '二等补贴'
				} else if (value === 100) {
					this.gangweibutieType = '三等补贴'
				} else if (value === 0) {
					this.gangweibutieType = '无补贴'
				} else {
					this.gangweibutieType = '自定义'
					this.gangweibutieCustomName = ''
				}
			},
			handleGangweibutieTypeChange(value) {
				const map = {
					'一等补贴': 500,
					'二等补贴': 300,
					'三等补贴': 100,
					'无补贴': 0,
				}
				if (value === '自定义') {
					this.gangweibutieCustomName = ''
					this.ruleForm.gangweibutie = ''
				} else {
					this.gangweibutieCustomName = ''
					this.ruleForm.gangweibutie = map[value]
				}
				this.calcShifagongzi()
			},
			getGangweibutieTypeByZhiwei(zhiwei) {
				const position = String(zhiwei || '').trim()
				const firstLevel = ['总经理', '副总经理', '财务总监', '技术总监', '人力资源总监', '销售总监']
				const secondLevel = ['客户服务经理', '行政主管', '高级软件工程师', '高级会计师', '市场部经理', '项目经理', '销售区域经理', '财务会计主管', '生产车间班组长']
				const thirdLevel = ['测试工程师', '技术支持工程师', '设计部UI设计师', '运营数据分析员', '人力资源专员', '市场推广专员', '销售代表', '客服专员']
				if (firstLevel.includes(position)) {
					return '一等补贴'
				}
				if (secondLevel.includes(position)) {
					return '二等补贴'
				}
				if (thirdLevel.includes(position)) {
					return '三等补贴'
				}
				return '无补贴'
			},
			applyGangweibutieByZhiwei(zhiwei) {
				const type = this.getGangweibutieTypeByZhiwei(zhiwei)
				this.gangweibutieType = type
				this.gangweibutieCustomName = ''
				this.handleGangweibutieTypeChange(type)
			},
			initKoukuanType() {
				const reason = this.ruleForm.koukuanyuanyin
				const amount = Number(this.ruleForm.koukuanjine) || 0
				if (reason === '社保公积金') {
					this.koukuanType = '社保公积金'
					this.applyKoukuanPolicy(false)
				} else if (reason === '无扣款' || (!reason && amount === 0)) {
					this.koukuanType = '无扣款'
					this.applyKoukuanPolicy(false)
				} else {
					this.koukuanType = '其他扣款'
				}
			},
			handleKoukuanTypeChange(value) {
				if (value === '其他扣款') {
					this.ruleForm.koukuanyuanyin = ''
					this.ruleForm.koukuanjine = ''
					this.calcShifagongzi()
				} else {
					this.applyKoukuanPolicy()
				}
			},
			applyKoukuanPolicy(recalc = true) {
				if (this.koukuanType === '社保公积金') {
					this.ruleForm.koukuanyuanyin = '社保公积金'
					this.ruleForm.koukuanjine = this.round2((Number(this.ruleForm.jibengongzi) || 0) * 0.175)
				} else if (this.koukuanType === '无扣款') {
					this.ruleForm.koukuanyuanyin = '无扣款'
					this.ruleForm.koukuanjine = 0
				}
				if (recalc) {
					this.calcShifagongzi()
				}
			},
			calcShifagongzi() {
				const c = this.ruleForm
				c.jixiaojiangjin = 0
				const jb = Number(c.jibengongzi) || 0
				const jb2 = Number(c.jiabangongzi) || 0
				const gw = Number(c.gangweibutie) || 0
				const kk = Number(c.koukuanjine) || 0
				const wqdkx = Number(c.weiqiandaokouxin) || 0
				const jqkx = Number(c.jiaqikouxin) || 0
				const a = jb + jb2 + gw + wqdkx + jqkx - kk
				this.ruleForm.shifagongzi = Number(a.toFixed(2))
			},
			calcJiabangongzi() {
				const jibengongzi = Number(this.ruleForm.jibengongzi) || 0
				const allJiabanshichang = Number(this.jiabanshichangTotal) || 0
				const fallbackCishu = Number(this.jiabancishuTotal) || 0
				const finalShichang = allJiabanshichang > 0 ? allJiabanshichang : fallbackCishu
				const hourly = jibengongzi / 116
				const jiabangongzi = finalShichang * hourly
				this.ruleForm.jiabangongzi = Number(jiabangongzi.toFixed(2))
			},
			handleJibengongziChange() {
				this.applyKoukuanPolicy()
				this.loadSalaryAssistant()
			},
			getMonthRangeByDengjiriqi() {
				const base = this.ruleForm.dengjiriqi ? new Date(this.ruleForm.dengjiriqi) : new Date()
				if (Number.isNaN(base.getTime())) {
					return {
						startDate: '',
						endDate: '',
						startDateTime: '',
						endDateTime: '',
					}
				}
				const year = base.getFullYear()
				const month = base.getMonth()
				const start = new Date(year, month, 1)
				const end = new Date(year, month + 1, 0)
				const pad = (n) => String(n).padStart(2, '0')
				const startDate = `${start.getFullYear()}-${pad(start.getMonth() + 1)}-${pad(start.getDate())}`
				const endDate = `${end.getFullYear()}-${pad(end.getMonth() + 1)}-${pad(end.getDate())}`
				return {
					startDate,
					endDate,
					startDateTime: `${startDate} 00:00:00`,
					endDateTime: `${endDate} 23:59:59`,
				}
			},
			async loadSalaryAssistant() {
				if (!this.ruleForm.gonghao || !this.ruleForm.dengjiriqi) {
					this.ruleForm.qingjiatianshu = 0
					this.ruleForm.weiqiandaotianshu = 0
					this.ruleForm.weiqiandaokouxin = 0
					this.ruleForm.jiaqikouxin = 0
					this.jiabancishuTotal = 0
					this.jiabanshichangTotal = 0
					this.ruleForm.jiabangongzi = 0
					this.calcShifagongzi()
					return
				}
				const jb = this.ruleForm.jibengongzi
				const params = {
					gonghao: this.ruleForm.gonghao,
					dengjiriqi: this.ruleForm.dengjiriqi,
				}
				if (jb !== '' && jb !== null && jb !== undefined) {
					params.jibengongzi = jb
				}
				await this.$http({
					url: 'yuangongxinzi/salaryAssistant',
					method: 'get',
					params
				}).then(({ data }) => {
					if (data && data.code === 0 && data.data) {
						const d = data.data
						this.ruleForm.qingjiatianshu = d.qingjiatianshu != null ? d.qingjiatianshu : 0
						this.ruleForm.weiqiandaotianshu = d.weiqiandaotianshu != null ? d.weiqiandaotianshu : 0
						this.ruleForm.weiqiandaokouxin = d.weiqiandaokouxin != null ? d.weiqiandaokouxin : 0
						this.ruleForm.jiaqikouxin = d.jiaqikouxin != null ? d.jiaqikouxin : 0
						this.jiabanshichangTotal = Number(d.jiabanshichang) || 0
						this.jiabancishuTotal = Number(d.jiabancishu) || 0
						this.ruleForm.jiabangongzi = d.jiabangongzi != null ? Number(d.jiabangongzi) : 0
					} else {
						this.$message.error((data && data.msg) ? data.msg : '薪资辅助计算失败')
						this.calcJiabangongzi()
					}
					this.calcShifagongzi()
				}).catch(() => {
					this.$message.error('薪资辅助计算请求失败')
					this.calcJiabangongzi()
					this.calcShifagongzi()
				})
			},
			async syncKaoqinAndQingjiaData() {
				await this.loadSalaryAssistant()
			},
			imgPreView(url){
				this.$parent.imgPreView(url)
			},
			// 下载
			download(file){
				window.open(`${file}`)
			},
			// 初始化
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
						if(o=='qingjiatianshu'){
							this.ruleForm.qingjiatianshu = obj[o];
							this.ro.qingjiatianshu = true;
							continue;
						}
						if(o=='weiqiandaotianshu'){
							this.ruleForm.weiqiandaotianshu = obj[o];
							this.ro.weiqiandaotianshu = true;
							continue;
						}
						if(o=='weiqiandaokouxin'){
							this.ruleForm.weiqiandaokouxin = obj[o];
							this.ro.weiqiandaokouxin = true;
							continue;
						}
						if(o=='jibengongzi'){
							this.ruleForm.jibengongzi = obj[o];
							this.ro.jibengongzi = true;
							continue;
						}
						if(o=='jiabangongzi'){
							this.ruleForm.jiabangongzi = obj[o];
							this.ro.jiabangongzi = true;
							continue;
						}
						if(o=='jixiaojiangjin'){
							this.ruleForm.jixiaojiangjin = 0;
							this.ro.jixiaojiangjin = true;
							continue;
						}
						if(o=='gangweibutie'){
							this.ruleForm.gangweibutie = obj[o];
							this.ro.gangweibutie = true;
							continue;
						}
						if(o=='koukuanjine'){
							this.ruleForm.koukuanjine = obj[o];
							this.ro.koukuanjine = true;
							continue;
						}
						if(o=='jiaqikouxin'){
							this.ruleForm.jiaqikouxin = obj[o];
							this.ro.jiaqikouxin = true;
							continue;
						}
						if(o=='shifagongzi'){
							this.ruleForm.shifagongzi = obj[o];
							this.ro.shifagongzi = true;
							continue;
						}
						if(o=='koukuanyuanyin'){
							this.ruleForm.koukuanyuanyin = obj[o];
							this.ro.koukuanyuanyin = true;
							continue;
						}
						if(o=='dengjiriqi'){
							this.ruleForm.dengjiriqi = obj[o];
							this.ro.dengjiriqi = true;
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
				}
				// 获取用户信息
				this.$http({
					url: `${this.sessionTable}/session`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						var json = data.data;
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
				this.initGangweibutieType()
				this.initKoukuanType()
				if (this.ruleForm.gonghao) {
					this.syncKaoqinAndQingjiaData()
				}
			
			},
			// 下二随
			xingmingChange () {
				this.$http({
					url: `follow/yuangong/xingming?columnValue=`+ encodeURIComponent(this.ruleForm.xingming),
					method: "get"
				}).then(async ({ data }) => {
					if (data && data.code === 0) {
						if(data.data.gonghao){
							this.ruleForm.gonghao = data.data.gonghao
						}
						this.jiabanshichangTotal = Number(data.data.jiabanshichang) || 0
						this.yuangongBaseJiabancishu = Number(data.data.jiabantianshu || data.data.jiabancishu) || 0
						this.yuangongBaseQingjiatianshu = Number(data.data.qingjiatianshu) || 0
						this.applyGangweibutieByZhiwei(data.data.zhiwei)
						await this.syncKaoqinAndQingjiaData()
					} else {
						this.$message.error(data.msg);
					}
				});
			},
			gonghaoChange () {
				if (!this.ruleForm.gonghao) {
					this.clearYuangongLinkage()
					return
				}
				this.ruleForm.gonghao = String(this.ruleForm.gonghao).trim()
				this.$http({
					url: `follow/yuangong/gonghao?columnValue=`+ encodeURIComponent(this.ruleForm.gonghao),
					method: "get"
				}).then(async ({ data }) => {
					if (data && data.code === 0 && data.data) {
						if(data.data.xingming){
							this.ruleForm.xingming = data.data.xingming
						}
						this.jiabanshichangTotal = Number(data.data.jiabanshichang) || 0
						this.yuangongBaseJiabancishu = Number(data.data.jiabantianshu || data.data.jiabancishu) || 0
						this.yuangongBaseQingjiatianshu = Number(data.data.qingjiatianshu) || 0
						this.applyGangweibutieByZhiwei(data.data.zhiwei)
						await this.syncKaoqinAndQingjiaData()
					} else {
						this.ruleForm.xingming = ''
						this.$message.error((data && data.msg) ? data.msg : '未找到该工号对应的员工')
					}
				});
			},
			clearYuangongLinkage () {
				this.ruleForm.xingming = ''
				this.ruleForm.gonghao = ''
				this.syncKaoqinAndQingjiaData()
			},
			// 多级联动参数

			async info(id) {
				await this.$http({
					url: `yuangongxinzi/info/${id}`,
					method: "get"
				}).then(async ({ data }) => {
					if (data && data.code === 0) {
						this.ruleForm = data.data;
						this.ruleForm.jixiaojiangjin = 0
						this.initGangweibutieType()
						this.initKoukuanType()
						this.jiabanshichangTotal = 0
						this.yuangongBaseJiabancishu = 0
						this.yuangongBaseQingjiatianshu = 0
						// 详情页显示数据库落库值，避免与列表页（同为落库值）出现不一致
						if (this.type !== 'info') {
							await this.syncKaoqinAndQingjiaData()
							this.calcShifagongzi()
						} else if (this.ruleForm.gonghao && this.ruleForm.dengjiriqi) {
							// 详情页单独加载加班时长用于展示
							this.$http({
								url: 'yuangongxinzi/salaryAssistant',
								method: 'get',
								params: { gonghao: this.ruleForm.gonghao, dengjiriqi: this.ruleForm.dengjiriqi }
							}).then(({ data }) => {
								if (data && data.code === 0 && data.data) {
									this.jiabanshichangTotal = Number(data.data.jiabanshichang) || 0
									this.jiabancishuTotal = Number(data.data.jiabancishu) || 0
								}
							})
						}
						//解决前台上传图片后台不显示的问题
						let reg=new RegExp('../../../upload','g')//g代表全部
					} else {
						this.$message.error(data.msg);
					}
				});
			},

			// 提交
			async onSubmit() {
					this.ruleForm.jixiaojiangjin = 0
					this.applyKoukuanPolicy(false)
					this.calcShifagongzi()
					if(!this.ruleForm.id) {
						this.ruleForm.ispay = '未支付'
					}
					if(this.ruleForm.shifagongzi==0){
						this.$message.error('实发工资不能为空')
						return false
					}
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
								url: `yuangongxinzi/${!this.ruleForm.id ? "save" : "update"}`,
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
											this.parent.yuangongxinziCrossAddOrUpdateFlag = false;
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
				this.parent.yuangongxinziCrossAddOrUpdateFlag = false;
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
	.add-update-preview .linkage-field .el-select,
	.add-update-preview .linkage-field .el-input,
	.add-update-preview .linkage-field .el-input-number {
		margin-bottom: 8px;
	}
	.add-update-preview .linkage-field .el-input:last-child,
	.add-update-preview .linkage-field .el-input-number:last-child {
		margin-bottom: 0;
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
