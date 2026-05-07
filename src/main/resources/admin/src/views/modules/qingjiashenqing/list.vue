<template>
  <div class="main-content" :style='{"padding":"50px 30px 30px"}'>
    <template v-if="showFlag">
      <el-form class="center-form-pv" :style='{"width":"100%","padding":"0","margin":"0 0 20px"}' :inline="true" :model="searchForm">
        <el-row :style='{"padding":"10px 20px 0","boxShadow":"0 3px 3px 0px #095dac","borderRadius":"0px","flexWrap":"wrap","background":"#fff","display":"flex","width":"100%","position":"relative"}'>
          <div class="query-item">
            <label class="item-label">请假时间</label>
            <el-date-picker
              class="qingjia-search-daterange"
              v-model="searchForm.qingjiashijianDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              clearable
            />
          </div>
          <div class="query-item">
            <label class="item-label">姓名</label>
            <el-select
              v-model="searchForm.xingming"
              placeholder="请选择/输入姓名"
              filterable
              allow-create
              clearable
              default-first-option
              style="width: 200px;"
            >
              <el-option
                v-for="(name, idx) in xingmingOptions"
                :key="idx"
                :label="name"
                :value="name"
              />
            </el-select>
          </div>
          <div class="query-item">
            <label class="item-label">审核状态</label>
            <el-select v-model="searchForm.sfsh" placeholder="请选择审核状态" clearable style="width: 160px;">
              <el-option label="待审核" value="待审核" />
              <el-option label="通过" value="是" />
              <el-option label="未通过" value="否" />
            </el-select>
          </div>
          <el-button class="search" type="success" @click="search">
            <span class="icon iconfont icon-fangdajing01"></span>
            查询
          </el-button>
        </el-row>

        <el-row class="actions" :style='{"width":"calc(100% + 8px)","margin":"20px 0 20px -4px","flexWrap":"wrap","justifyContent":"flex-end","display":"flex"}'>
          <el-button class="add" v-if="hasAuth('新增', '鏂板')" type="success" @click="addOrUpdateHandler()">
            <span class="icon iconfont icon-tianjia16"></span>
            请假
          </el-button>
          <el-button class="btn18" v-if="hasAuth('审核', '瀹℃牳')" :disabled="!dataListSelections.length" type="success" @click="shBatchDialog">
            <span class="icon iconfont icon-shenhe9"></span>
            审核
          </el-button>
        </el-row>
      </el-form>

      <div :style='{"border":"1px solid #e9eafc","width":"100%","padding":"0px 15px 20px","boxShadow":"none","borderRadius":"10px","background":"#fff"}'>
        <el-table
          class="tables"
          :stripe="false"
          :style='{"padding":"0","borderColor":"#e7e8fc","borderRadius":"10px","borderWidth":"0px 0 0 0px","background":"#fff","width":"100%","borderStyle":"solid"}'
          :border="false"
          v-if="hasAuth('查看', '鏌ョ湅')"
          :data="dataList"
          v-loading="dataListLoading"
          @selection-change="selectionChangeHandler"
        >
          <el-table-column :resizable="true" type="selection" align="center" width="50" />
          <el-table-column :resizable="true" :sortable="true" label="序号" type="index" width="60" />
          <el-table-column :resizable="true" :sortable="true" prop="gonghao" label="工号" width="90" />
          <el-table-column :resizable="true" :sortable="true" prop="xingming" label="姓名" width="90" />
          <el-table-column :resizable="true" :sortable="true" prop="qingjialeixing" label="请假类型" width="100" />
          <el-table-column :resizable="true" :sortable="true" prop="qingjiashijian" label="请假时间" width="165" />
          <el-table-column :resizable="true" :sortable="true" prop="jieshushijian" label="结束时间" width="165" />
          <el-table-column :resizable="true" :sortable="true" prop="qingjiatianshu" label="请假天数" width="95" />
          <el-table-column :resizable="true" :sortable="true" prop="qingjiayuanyin" label="请假原因" show-overflow-tooltip min-width="180" />
          <el-table-column :resizable="true" :sortable="true" prop="guanlixingming" label="管理姓名" width="100" />
          <el-table-column :resizable="true" :sortable="true" prop="guanlizhanghao" label="管理账号" width="100" />
          <el-table-column :resizable="true" :sortable="true" prop="sfsh" label="审核状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="sfshTagType(scope.row.sfsh)">{{ sfshText(scope.row.sfsh) }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pageIndex"
        background
        :page-sizes="[5]"
        :page-size="pageSize"
        :layout="layouts.join()"
        :total="totalPage"
        prev-text="< "
        next-text="> "
        :hide-on-single-page="false"
        :style='{"padding":"0","margin":"20px 0 0","whiteSpace":"nowrap","color":"#333","display":"flex","width":"100%","fontWeight":"500","justifyContent":"center"}'
      />
    </template>

    <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate" />

    <el-dialog :title="batchIds.length > 1 ? '批量审核' : '审核'" :visible.sync="sfshBatchVisiable" width="50%">
      <el-form ref="shBatchForm" :model="shBatchForm" :rules="shRules" label-width="90px">
        <el-form-item label="审核状态" prop="sfsh">
          <el-select v-model="shBatchForm.sfsh" placeholder="请选择审核状态">
            <el-option label="通过" value="是" />
            <el-option label="不通过" value="否" />
            <el-option label="待审核" value="待审核" />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="sfshBatchVisiable = false">取消</el-button>
        <el-button type="primary" @click="shBatchHandler">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import AddOrUpdate from './add-or-update'

export default {
  data() {
    return {
      searchForm: {
        key: '',
        qingjiashijianDateRange: null,
        xingming: '',
        sfsh: ''
      },
      xingmingOptions: [],
      dataList: [],
      pageIndex: 1,
      pageSize: 5,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      sfshBatchVisiable: false,
      shBatchForm: { sfsh: '' },
      shRules: {
        sfsh: [{ required: true, message: '审核状态不能为空', trigger: 'change' }]
      },
      batchIds: [],
      shList: [],
      addOrUpdateFlag: false,
      layouts: ['total', 'sizes', 'prev', 'pager', 'next']
    }
  },
  created() {
    this.getDataList()
    this.loadXingmingOptions()
  },
  components: {
    AddOrUpdate
  },
  methods: {
    hasAuth(action, legacyAction) {
      return this.isAuth('qingjiashenqing', action) || (legacyAction && this.isAuth('qingjiashenqing', legacyAction))
    },
    search() {
      this.pageIndex = 1
      this.getDataList()
    },
    getDataList() {
      this.dataListLoading = true
      const params = {
        page: this.pageIndex,
        limit: this.pageSize,
        sort: 'id',
        order: 'desc'
      }
      const range = this.searchForm.qingjiashijianDateRange
      if (range && range.length === 2 && range[0] && range[1]) {
        params.qingjiashijian_start = range[0] + ' 00:00:00'
        params.qingjiashijian_end = range[1] + ' 23:59:59'
      }
      if (this.searchForm.xingming && this.searchForm.xingming.trim()) {
        params.xingming = '%' + this.searchForm.xingming.trim() + '%'
      }
      if (this.searchForm.sfsh) {
        params.sfsh = this.searchForm.sfsh
      }
      this.$http({
        url: 'qingjiashenqing/page',
        method: 'get',
        params
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list || []
          this.totalPage = data.data.total || 0
        } else {
          this.dataList = []
          this.totalPage = 0
          if (data && data.msg) this.$message.error(data.msg)
        }
      }).catch(() => {
        this.dataList = []
        this.totalPage = 0
        this.$message.error('加载列表失败，请稍后重试')
      }).finally(() => {
        this.dataListLoading = false
      })
    },
    loadXingmingOptions() {
      this.$http({
        url: 'yuangong/page',
        method: 'get',
        params: { page: 1, limit: 1000, sort: 'gonghao', order: 'asc' }
      }).then(({ data }) => {
        if (data && data.code === 0 && data.data && Array.isArray(data.data.list)) {
          const names = new Set()
          data.data.list.forEach(item => {
            if (item && item.xingming) names.add(item.xingming)
          })
          this.xingmingOptions = Array.from(names)
        }
      })
    },
    sfshText(value) {
      if (value === '是') return '通过'
      if (value === '否') return '未通过'
      return value || '待审核'
    },
    sfshTagType(value) {
      if (value === '是') return 'success'
      if (value === '否') return 'danger'
      return 'warning'
    },
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    selectionChangeHandler(val) {
      this.dataListSelections = val
    },
    addOrUpdateHandler(id, type) {
      this.showFlag = false
      this.addOrUpdateFlag = true
      if (type !== 'info' && type !== 'msg') type = 'else'
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id, type)
      })
    },
    shBatchDialog() {
      this.batchIds = []
      for (let i = 0; i < this.dataListSelections.length; i++) {
        const row = this.dataListSelections[i]
        if (row.sfsh && row.sfsh !== '待审核') {
          this.$message.error('选中记录中存在已审核请假，不能重复审核')
          return false
        }
        this.batchIds.push(row.id)
      }
      this.shList = this.dataListSelections
      this.shBatchForm.sfsh = ''
      this.sfshBatchVisiable = true
    },
    shBatchHandler() {
      this.$refs.shBatchForm.validate(valid => {
        if (!valid) return
        this.$confirm(`确认${this.batchIds.length > 1 ? '批量审核' : '审核'}选中的请假申请吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          if (this.shBatchForm.sfsh === '是') {
            for (let i = 0; i < this.shList.length; i++) {
              await this.$http.post('updateColumn/yuangong/2', {
                csuUpdateColumn: 'qingjiatianshu',
                csuUpdateColumnValue: '+' + this.shList[i].qingjiatianshu,
                csuConditionColumn: 'gonghao',
                csuConditionColumnValue: this.shList[i].gonghao
              })
            }
          }
          this.$http({
            url: 'qingjiashenqing/shBatch?sfsh=' + this.shBatchForm.sfsh + '&shhf=',
            method: 'post',
            data: this.batchIds
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.sfshBatchVisiable = false
                  this.batchIds = []
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg || '操作失败')
            }
          })
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.query-item {
  margin: 0 1% 10px 0;
  display: flex;
  align-items: center;
}
.item-label {
  margin: 0 10px 0 0;
  white-space: nowrap;
  color: #666;
  display: inline-block;
  line-height: 40px;
  font-size: 16px;
  font-weight: 500;
  height: 40px;
}
.center-form-pv .el-input,
.center-form-pv .el-select {
  width: 100%;
}
.center-form-pv .el-input /deep/ .el-input__inner,
.center-form-pv .el-select /deep/ .el-input__inner,
.center-form-pv .el-date-editor /deep/ .el-input__inner {
  border: 1px solid #b2d3ee;
  border-radius: 4px;
  box-shadow: none;
  outline: none;
  color: #333;
  width: 100%;
  font-size: 16px;
  height: 36px;
}
.center-form-pv .qingjia-search-daterange.el-date-editor.el-range-editor.el-input__inner {
  width: 450px;
  min-width: 450px;
  max-width: 100%;
  box-sizing: border-box;
  display: inline-flex;
  align-items: center;
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
.center-form-pv .actions .add,
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
  color: #333;
  font-weight: bold;
  display: flex;
  font-size: 15px;
  line-height: 24px;
  justify-content: flex-start;
  align-items: center;
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
.el-table /deep/ .cell {
  padding: 0 5px;
  white-space: normal;
  font-size: 14px;
  line-height: 24px;
}
</style>
