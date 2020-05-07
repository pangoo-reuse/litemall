<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.aftersaleSn"
        clearable
        class="filter-item"
        style="width: 200px;"
        placeholder="请输入售后编号"
      />
      <el-input v-model="listQuery.orderId" clearable class="filter-item" style="width: 200px;" placeholder="请输入订单ID" />
      <el-button
        v-permission="['GET /admin/aftersale/list']"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >查找
      </el-button>
      <el-button
        :loading="downloadLoading"
        class="filter-item"
        type="primary"
        icon="el-icon-download"
        @click="handleDownload"
      >导出
      </el-button>
    </div>

    <div class="operator-container">
      <el-button
        v-permission="['GET /admin/aftersale/batch-recept']"
        class="filter-item"
        type="success"
        icon="el-icon-info"
        @click="handleBatchRecept"
      >批量通过
      </el-button>
      <el-button
        v-permission="['GET /admin/aftersale/batch-reject']"
        class="filter-item"
        type="danger"
        icon="el-icon-delete"
        @click="handleBatchReject"
      >批量拒绝
      </el-button>
    </div>

    <el-tabs v-model="tab" @tab-click="handleClick">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待审核" name="uncheck" />
      <el-tab-pane label="待退款" name="unrefund" />
    </el-tabs>

    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="正在查询中。。。"
      fit
      highlight-current-row
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />

      <el-table-column align="center" label="售后编号" prop="aftersaleSn" />
      <el-table-column align="center" label="订单ID" prop="orderId" />
      <el-table-column align="center" label="用户ID" prop="userId" />
      <el-table-column align="center" label="售后类型" prop="type">
        <template slot-scope="scope">
          <el-tag :type="typeTag[scope.row.type]">{{ typeDesc[scope.row.type] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="退款原因" prop="reason">
        <template slot-scope="scope">
          <div>{{ scope.row.reason| ellipsis }}</div>
        </template>

      </el-table-column>
      <el-table-column align="center" label="退款价格" prop="amount" />
      <el-table-column align="center" label="申请时间" prop="addTime" />

      <el-table-column align="center" label="操作" min-width="120" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-permission="['POST /admin/aftersale/detail']"
            type="primary"
            size="mini"
            @click="handleDetail(scope.row)"
          >详情
          </el-button>

          <el-button
            v-if="scope.row.status === 1"
            v-permission="['POST /admin/aftersale/recept']"
            type="success"
            size="mini"
            @click="handleRecept(scope.row)"
          >通过
          </el-button>
          <el-button
            v-if="scope.row.status === 1"
            v-permission="['POST /admin/aftersale/reject']"
            type="danger"
            size="mini"
            @click="handleReject(scope.row)"
          >拒绝
          </el-button>
          <el-button
            v-if="scope.row.status === 2"
            v-permission="['POST /admin/aftersale/refund']"
            type="warning"
            size="mini"
            @click="handleRefund(scope.row)"
          >退款
          </el-button>
          <el-tag v-if="scope.row.status === 3" :type="success">已退款</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dataForm.name" :visible.sync="contentDialogVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="dataForm"
        status-icon
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="售后编号" prop="aftersaleSn">
          <div>{{ dataForm.aftersaleSn }}</div>
        </el-form-item>
        <el-form-item label="订单ID" prop="orderId">
          <div>{{ dataForm.orderId }}</div>
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <div>{{ dataForm.userId }}</div>
        </el-form-item>
        <el-form-item label="售后类型" prop="type">
          <el-tag :type="typeTag[dataForm.type]">{{ typeDesc[dataForm.type] }}</el-tag>
        </el-form-item>

        <el-form-item label="退款价格" prop="amount">
          <div>¥{{ dataForm.amount }}元</div>
        </el-form-item>
        <el-form-item label="申请时间" prop="addTime">
          <div>{{ dataForm.addTime }}</div>
        </el-form-item>
        <el-form-item label="退款原因" prop="reason">
          <div>{{ dataForm.reason }}</div>
        </el-form-item>

        <el-form-item v-if="dataForm.type != 3" label="退款凭证" prop="pictures">

          <div class="t-ctn">
            <div class="s-ctn">
              <div v-for="url in dataForm.pictures" v-if="dataForm.pictures">
                <img :src="url" class="avatar">
              </div>
            </div>
          </div>
        </el-form-item>
      </el-form>

    </el-dialog>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibility-height="100" />
    </el-tooltip>

  </div>
</template>
<style>
  .t-ctn {
    width: 100%;
    overflow-x: auto;
  }

  .t-ctn .s-ctn {
    height: 150px;
    white-space: nowrap;
    font-size: 0;
  }

  .t-ctn .s-ctn div {
    box-sizing: border-box;
    white-space: normal;
    word-wrap: break-word;
    word-break: break-all;
    overflow: hidden;
    display: inline-block;
    width: auto;
    height: 100%;

  }
  .t-ctn .s-ctn div img{
    width: auto;
    height: 150px;
    max-width: 100%;
    max-height: 100%;
  }
</style>
<script>
import {
  listAftersale,
  receptAftersale,
  batchReceptAftersale,
  rejectAftersale,
  batchRejectAftersale,
  refundAftersale
} from '@/api/aftersale'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import _ from 'lodash'

export default {
  name: 'Aftersale',
  filters: {
    ellipsis(value) {
      if (!value) return ''
      if (value.length > 12) {
        return value.slice(0, 12) + '...'
      }
      return value
    }
  },
  components: { BackToTop, Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      tab: 'all',
      listQuery: {
        page: 1,
        limit: 20,
        aftersaleSn: undefined,
        orderId: undefined,
        status: '',
        sort: 'add_time',
        order: 'desc'
      },
      typeTag: [
        '',
        'success',
        'warning'
      ],
      typeDesc: [
        '未收货退款',
        '不退货退款',
        '退货退款',
        '活动返现'
      ],
      dataForm: {},
      multipleSelection: [],
      contentDetail: '',
      contentDialogVisible: false,
      downloadLoading: false,
      rules: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {

    getList() {
      this.listLoading = true
      listAftersale(this.listQuery)
        .then(response => {
          this.list = response.data.data.list
          this.total = response.data.data.total
          this.listLoading = false
        })
        .catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
    },

    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleClick() {
      if (this.tab === 'all') {
        this.listQuery.status = ''
      } else if (this.tab === 'uncheck') {
        this.listQuery.status = '1'
      } else if (this.tab === 'unrefund') {
        this.listQuery.status = '2'
      }
      this.getList()
    },
    handleDetail(row) {
      this.dataForm = Object.assign({}, row)
      console.log(this.dataForm.pictures)
      this.contentDialogVisible = true
    },
    handleRecept(row) {
      receptAftersale(row)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '审核通过操作成功'
          })
          this.getList()
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    handleBatchRecept() {
      if (this.multipleSelection.length === 0) {
        this.$message.error('请选择至少一条记录')
        return
      }
      const ids = []
      _.forEach(this.multipleSelection, function(item) {
        ids.push(item.id)
      })
      batchReceptAftersale({ ids: ids })
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '批量通过操作成功'
          })
          this.getList()
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    handleReject(row) {
      rejectAftersale(row)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '审核拒绝操作成功'
          })
          this.getList()
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    handleBatchReject() {
      if (this.multipleSelection.length === 0) {
        this.$message.error('请选择至少一条记录')
        return
      }
      const ids = []
      _.forEach(this.multipleSelection, function(item) {
        ids.push(item.id)
      })
      batchRejectAftersale({ ids: ids })
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '批量拒绝操作成功'
          })
          this.getList()
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    handleRefund(row) {
      refundAftersale(row)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '退款操作成功'
          })
          this.getList()
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    handleDownload() {
      this.downloadLoading = true
                import('@/vendor/Export2Excel').then(excel => {
                  const tHeader = [
                    '售后编号',
                    '订单ID',
                    '用户ID',
                    '售后类型',
                    '退款原因',
                    '退款价格',
                    '申请时间'
                  ]
                  const filterVal = [
                    'aftersaleSn',
                    'orderId',
                    'userId',
                    'type',
                    'reason',
                    'amount',
                    'addTime'
                  ]
                  excel.export_json_to_excel2(tHeader, this.list, filterVal, '售后信息')
                  this.downloadLoading = false
                })
    }
  }
}
</script>
