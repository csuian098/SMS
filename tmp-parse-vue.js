const fs=require('fs');
const compiler=require('./src/main/resources/admin/node_modules/vue-template-compiler');
const path='F:/springboot32e20828n/springboot32e20828n/src/main/resources/admin/src/views/modules/zhiweidiaodong/list.vue';
const src=fs.readFileSync(path,'utf8');
const sfc=compiler.parseComponent(src);
const r=compiler.compile(sfc.template.content,{outputSourceRange:true});
console.log('templateErrors',r.errors);
console.log('templateTips',r.tips);
