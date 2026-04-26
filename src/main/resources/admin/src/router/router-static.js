	import Vue from 'vue';
//配置路由
	import VueRouter from 'vue-router'
	Vue.use(VueRouter);
//1.创建组件
	import Index from '@/views/index'
	import Home from '@/views/home'
	import Login from '@/views/login'
	import NotFound from '@/views/404'
	import UpdatePassword from '@/views/update-password'
	import pay from '@/views/pay'
	import register from '@/views/register'
	import center from '@/views/center'
	import renshiguanliyuan from '@/views/modules/renshiguanliyuan/list'
	import yuangongxinzi from '@/views/modules/yuangongxinzi/list'
	import yuangongqiandao from '@/views/modules/yuangongqiandao/list'
	// import yuangongkaoqin from '@/views/modules/yuangongkaoqin/list' // 员工考勤模块已整体注释
	import zhiweixinxi from '@/views/modules/zhiweixinxi/list'
	import zhiweidiaodong from '@/views/modules/zhiweidiaodong/list'
import zhiweishensu from '@/views/modules/zhiweishensu/list'
	import tongyixinxi from '@/views/modules/tongyixinxi/list'
	import jixiaokaohe from '@/views/modules/jixiaokaohe/list'
	import qingjiashenqing from '@/views/modules/qingjiashenqing/list'
	import syslog from '@/views/modules/syslog/list'
	import users from '@/views/modules/users/list'
	import menu from '@/views/modules/menu/list'
import config from '@/views/modules/config/list'

//2.配置路由   注意：名字
export const routes = [{
	path: '/',
	name: '系统首页',
	component: Index,
	children: [{
		// 这里不设置值，是把main作为默认页面
		path: '/',
		name: '系统首页',
		component: Home,
		meta: {icon:'', title:'center', affix: true}
	}, {
		path: '/updatePassword',
		name: '修改密码',
		component: UpdatePassword,
		meta: {icon:'', title:'updatePassword'}
	}, {
		path: '/pay',
		name: '支付',
		component: pay,
		meta: {icon:'', title:'pay'}
	}, {
		path: '/center',
		name: '个人信息',
		component: center,
		meta: {icon:'', title:'center'}
	}
	,{
		path: '/renshiguanliyuan',
		name: '人事管理员',
		component: renshiguanliyuan
	}
	,{
		path: '/renshiguanliyuanstat',
		name: '人事管理员统计',
		component: renshiguanliyuan
	}
	,{
		path: '/yuangongxinzi',
		name: '员工薪资',
		component: yuangongxinzi
	}
	,{
		path: '/yuangongxinzistat',
		name: '员工薪资统计',
		component: yuangongxinzi
	}
	,{
		path: '/yuangongqiandao',
		name: '员工签到',
		component: yuangongqiandao
	}
	// ,{
	// 	path: '/yuangongkaoqin',
	// 	name: '员工考勤',
	// 	component: yuangongkaoqin
	// }
	,{
		path: '/zhiweixinxi',
		name: '职位信息',
		component: zhiweixinxi
	}
	,{
		path: '/zhiweidiaodong',
		name: '职位调动',
		component: zhiweidiaodong
	}
	,{
		path: '/zhiweishensu',
		name: '申诉管理',
		component: zhiweishensu
	}
	,{
		path: '/tongyixinxi',
		name: '同意信息',
		component: tongyixinxi
	}
	,{
		path: '/jixiaokaohe',
		name: '绩效考核',
		component: jixiaokaohe
	}
	,{
		path: '/qingjiashenqing',
		name: '请假申请',
		component: qingjiashenqing
	}
	,{
		path: '/syslog',
		name: '系统日志',
		component: syslog
	}
	,{
		path: '/users',
		name: '管理员',
		component: users
	}
	,{
		path: '/menu',
		name: '菜单',
		component: menu
	}
	,{
		path: '/config/:type',
		name: '配置管理',
		component: config
	}
	]
	},
	{
		path: '/login',
		name: 'login',
		component: Login,
		meta: {icon:'', title:'login'}
	},
	{
		path: '/register',
		name: 'register',
		component: register,
		meta: {icon:'', title:'register'}
	},
	{
		path: '*',
		component: NotFound
	}
]
//3.实例化VueRouter  注意：名字
const router = new VueRouter({
	mode: 'hash',
	/*hash模式改为history*/
	routes // （缩写）相当于 routes: routes
})
const originalPush = VueRouter.prototype.push
//修改原型对象中的push方法
VueRouter.prototype.push = function push(location) {
	return originalPush.call(this, location).catch(err => err)
}
export default router;
